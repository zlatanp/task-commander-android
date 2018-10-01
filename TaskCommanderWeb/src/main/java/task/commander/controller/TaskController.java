package task.commander.controller;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import task.commander.dto.TaskDTO;
import task.commander.model.Task;
import task.commander.model.User;
import task.commander.service.AndroidPushNotificationsService;
import task.commander.service.TaskService;
import task.commander.service.UserService;

@Controller
@RequestMapping(value="/task")
public class TaskController {

	@Autowired
	TaskService taskService;

	@Autowired
	UserService userService;

	@Autowired
	AndroidPushNotificationsService notificationService;
	
	@RequestMapping(
			value = "/create",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> createTask(@RequestBody TaskDTO taskDTO , HttpServletRequest request) throws Exception {
		
		Task task;
		try {
			task = taskService.create(taskDTO);
			User user = userService.getUserByEmail(taskDTO.getAssigneeMail());
			pushNotificationHelper(user.getUid());
			return new ResponseEntity<Task>(task, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	@RequestMapping(
			value = "/complete/{task_id}/{email}",
			method = RequestMethod.PUT,
			
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Task>> createTask(@PathVariable("task_id") Long task_id, @PathVariable("email") String email, HttpServletRequest request) throws Exception {
		System.out.println("=====================HIT COMPLETE=========================");
		Task task;
		try {
			
			return new ResponseEntity<Collection<Task>>(taskService.complete(task_id, email), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	@RequestMapping(
			value = "/setLocation",
			method = RequestMethod.PUT,
			
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> setTaskLocation(@RequestBody Task task, HttpServletRequest request) throws Exception {
		System.out.println("=====================HIT SET LOCATION=========================");
		
		try {
			
			return new ResponseEntity<Task>(taskService.editLocation(task), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	@RequestMapping(
			value = "/find/{filter}/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Task>> find(@PathVariable("filter") String filter, @PathVariable("id") String id , HttpServletRequest request) throws Exception {
		
		System.out.println("===============================POGODJEN FIND===========================");
		
		if(filter.equals("assignee")){
			return new ResponseEntity<Collection<Task>>(taskService.getTasksByAssignee(id), HttpStatus.OK);
		}else if(filter.equals("group_id")){
			try {
				return new ResponseEntity<Collection<Task>>(taskService.getTasksByGroupId(Long.parseLong(id)), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}
		
		return null;
		
	}
	
	private int pushNotificationHelper(String user_uid) {
		 
		JSONObject body = new JSONObject();
		body.put("to", "/topics/" + user_uid);
		body.put("priority", "high");
		
 
		JSONObject notification = new JSONObject();
		notification.put("title", "New task");
		notification.put("body", "You have been assigned a new task!");
		
		JSONObject data = new JSONObject();
		data.put("Key-1", "JSA Data 1");
		data.put("Key-2", "JSA Data 2");
 
		body.put("notification", notification);
		body.put("data", data);
 
/**
		{
		   "notification": {
		      "title": "JSA Notification",
		      "body": "Happy Message!"
		   },
		   "data": {
		      "Key-1": "JSA Data 1",
		      "Key-2": "JSA Data 2"
		   },
		   "to": "/topics/JavaSampleApproach",
		   "priority": "high"
		}
*/
 
		HttpEntity<String> request = new HttpEntity<>(body.toString());
 
		CompletableFuture<String> pushNotification = notificationService.send(request);
		CompletableFuture.allOf(pushNotification).join();
 
		try {
			String firebaseResponse = pushNotification.get();
			return 0;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
 
		return 1;
	}
}
