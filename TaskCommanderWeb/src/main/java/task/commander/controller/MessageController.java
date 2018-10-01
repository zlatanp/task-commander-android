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

import task.commander.dto.MessageDTO;
import task.commander.model.Message;
import task.commander.model.TaskGroup;
import task.commander.model.User;
import task.commander.service.AndroidPushNotificationsService;
import task.commander.service.MessageService;
import task.commander.service.TaskGroupService;

@Controller
@RequestMapping(value="/message")
public class MessageController {
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	TaskGroupService taskGroupService;
	
	@Autowired
	AndroidPushNotificationsService notificationService;
	
	@RequestMapping(
			value = "/create/{group_id}",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Message> createMessage(@PathVariable("group_id") Long group_id,
			@RequestBody MessageDTO msg , HttpServletRequest request) throws Exception {

		System.out.println("-----------------------------CUVAMO MSG------------------------");
		Message message = messageService.create(msg, group_id);
		TaskGroup group = taskGroupService.getGroup(group_id);
		for(User member : group.getMembers()) {
			pushNotificationHelper(member.getUid(), msg.getSender(), msg.getText());
		}
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/get_all/{group_id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Message>> getMessages(@PathVariable("group_id") Long group_id,
			HttpServletRequest request) throws Exception {
		System.out.println("SAMO ISPIS NESTOO");
		TaskGroup taskGroup = taskGroupService.getGroup(group_id);
		
		return new ResponseEntity<Collection<Message>>(taskGroup.getMessages(), HttpStatus.OK);
	}
	
	private int pushNotificationHelper(String user_uid, String sender, String text) {
		 
		JSONObject body = new JSONObject();
		body.put("to", "/topics/" + user_uid);
		body.put("priority", "high");
		
 
		JSONObject notification = new JSONObject();
		notification.put("title", "New message");
		notification.put("body", sender + " says: " + text);
		
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
