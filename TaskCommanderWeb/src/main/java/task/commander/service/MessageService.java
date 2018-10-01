package task.commander.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import task.commander.dto.MessageDTO;
import task.commander.exceptions.NotFoundException;
import task.commander.model.Message;
import task.commander.model.TaskGroup;
import task.commander.model.User;
import task.commander.repository.MessageRepository;
import task.commander.repository.TaskGroupRepository;
import task.commander.repository.UserRepository;

@Service
public class MessageService {
	
	@Autowired
	MessageRepository messageRepository;
	
	@Autowired
	TaskGroupRepository taskGroupRepository;
	
	@Autowired
	UserRepository userRepository;
	
	public Message create(MessageDTO messageDTO, Long group_id){
		
		User user = userRepository.findByEmail(messageDTO.getSender()).orElseThrow(NotFoundException::new);
		
		Message message = new Message(messageDTO.getText(), user.getEmail());
		message.setTimestamp(new Timestamp(System.currentTimeMillis()));
		message = messageRepository.save(message);
		
		TaskGroup taskGroup = taskGroupRepository.findById(group_id).orElseThrow(NotFoundException::new);
		taskGroup.getMessages().add(message);
		taskGroup = taskGroupRepository.save(taskGroup);
		return message; 
	}
	
}
