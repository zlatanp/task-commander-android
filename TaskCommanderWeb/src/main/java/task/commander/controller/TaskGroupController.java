package task.commander.controller;

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

import task.commander.dto.GroupDTO;
import task.commander.model.TaskGroup;
import task.commander.model.User;
import task.commander.service.AndroidPushNotificationsService;
import task.commander.service.TaskGroupService;
import task.commander.service.UserService;

@Controller
@RequestMapping(value="/task_group")
public class TaskGroupController {
	
	@Autowired
	TaskGroupService taskGroupService;
	
	@Autowired
	AndroidPushNotificationsService notificationService;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(
			value = "/create",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TaskGroup> createTaskGroup(@RequestBody GroupDTO groupDTO , HttpServletRequest request) throws Exception {
		
		System.out.println("---------------------------PRAVIM GRUPU---------------------------");
		TaskGroup taskGroup = taskGroupService.create(groupDTO);
		for(String email : groupDTO.getMembers()){
			User user = userService.getUserByEmail(email);
			pushNotificationHelper(user.getUid());
		}
		return new ResponseEntity<TaskGroup>(taskGroup, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/leave/{group_id}/{user_email}",
			method = RequestMethod.PUT,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TaskGroup> leaveTaskGroup(@PathVariable("group_id") Long group_id, @PathVariable("user_email") String user_email , HttpServletRequest request) throws Exception {
		
		System.out.println("=======================NAPUSTAM GRUPU========================");
		
		TaskGroup taskGroup;
		try {
			taskGroup = taskGroupService.removeUserFromTaskGroup(group_id, user_email);
			return new ResponseEntity<TaskGroup>(taskGroup, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(
			value = "/addMember/{group_id}/{user_email}",
			method = RequestMethod.PUT,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TaskGroup> addMemberToTaskGroup(@PathVariable("group_id") Long group_id, @PathVariable("user_email") String user_email , HttpServletRequest request) throws Exception {
		
		TaskGroup taskGroup;
		try {
			taskGroup = taskGroupService.addMembersToGroup(group_id, user_email);
			User user = userService.getUserByEmail(user_email);
			pushNotificationHelper(user.getUid());
			
			return new ResponseEntity<TaskGroup>(taskGroup, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	private int pushNotificationHelper(String user_uid) {
		 
		JSONObject body = new JSONObject();
		body.put("to", "/topics/" + user_uid);
		body.put("priority", "high");
		
 
		JSONObject notification = new JSONObject();
		notification.put("title", "New group");
		notification.put("body", "You have been added to a new group!");
		
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
