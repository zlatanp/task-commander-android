package task.commander.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import task.commander.dto.TaskDTO;
import task.commander.exceptions.NotFoundException;
import task.commander.model.Task;
import task.commander.model.TaskGroup;
import task.commander.model.User;
import task.commander.repository.TaskGroupRepository;
import task.commander.repository.TaskRepository;
import task.commander.repository.UserRepository;

@Service
public class TaskService {
	
	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
	TaskGroupRepository taskGroupRepository;
	
	@Autowired
	UserRepository userRepository;
	
	public Task create(TaskDTO taskDTO) {
		
		User user = userRepository.findByEmail(taskDTO.getAssigneeMail()).orElseThrow(NotFoundException::new);
		
		Task task = new Task(taskDTO.getName(), taskDTO.getDescription(), user, taskDTO.getDeadline());
		
		TaskGroup taskGroup = taskGroupRepository.findById(taskDTO.getGroup_id()).orElseThrow(NotFoundException::new);
		
		task = taskRepository.save(task);
		taskGroup.getTasks().add(task);
		taskGroupRepository.save(taskGroup);
		return task;
	}

	public Collection<Task> complete(Long task_id, String email) {
		Task task = taskRepository.findById(task_id).orElseThrow(NotFoundException::new);
		task.setCompleted(true);
		task.setActive(false);
		task = taskRepository.save(task);
		return taskRepository.findByAssignee(email);
	}
	
	public Task editLocation(Task task_1) {
		Task task = taskRepository.findById(task_1.getId()).orElseThrow(NotFoundException::new);
		task.setLatitude(task_1.getLatitude());
		task.setLongitude(task_1.getLongitude());
		task = taskRepository.save(task);
		return task;
	}
	
	public Collection<Task> getTasksByAssignee(String email){
		return taskRepository.findByAssignee(email);
	}

	public Collection<Task> getTasksByGroupId(Long id) {
		TaskGroup group = taskGroupRepository.findById(id).orElseThrow(NotFoundException::new);
		return group.getTasks();
	}

}
