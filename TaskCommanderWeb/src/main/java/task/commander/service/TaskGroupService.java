package task.commander.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import task.commander.dto.GroupDTO;
import task.commander.exceptions.NotFoundException;
import task.commander.model.TaskGroup;
import task.commander.model.User;
import task.commander.repository.TaskGroupRepository;
import task.commander.repository.UserRepository;

@Service
public class TaskGroupService {

	@Autowired
	TaskGroupRepository taskGroupRepository;
	@Autowired
	UserRepository userRepository;

	public TaskGroup create(GroupDTO groupDTO) {
		TaskGroup taskGroup = new TaskGroup();
		taskGroup.setName(groupDTO.getName());

		Collection<String> members = groupDTO.getMembers();

		taskGroup = taskGroupRepository.save(taskGroup);
		
		User user = null;
		if(members!=null){
			for (String email : members) {
				try {
					user = userRepository.findByEmail(email).orElseThrow(NotFoundException::new);
					taskGroup.addMember(user);
					user.getTask_groups().add(taskGroup);
					userRepository.save(user);
				} catch (NotFoundException e) {
					continue;
				}
			}
		}
		
		return taskGroupRepository.save(taskGroup);
	}

	public TaskGroup removeUserFromTaskGroup(Long group_id, String user_email) {
		TaskGroup taskGroup = taskGroupRepository.findById(group_id).orElseThrow(NotFoundException::new);
		User user = userRepository.findByEmail(user_email).orElseThrow(NotFoundException::new);

		taskGroup.removeMember(user);
		user.getTask_groups().remove(taskGroup);
		taskGroup = taskGroupRepository.save(taskGroup);
		userRepository.save(user);
		return taskGroup;
	}

	public TaskGroup addMembersToGroup(Long group_id, String user_email) {
		TaskGroup taskGroup = taskGroupRepository.findById(group_id).orElseThrow(NotFoundException::new);
		User user = userRepository.findByEmail(user_email).orElseThrow(NotFoundException::new);

		taskGroup.getMembers().add(user);
		user.getTask_groups().add(taskGroup);
		taskGroup = taskGroupRepository.save(taskGroup);
		userRepository.save(user);
		return taskGroup;
	}

	public TaskGroup getGroup(Long group_id) {
		TaskGroup taskGroup = taskGroupRepository.findById(group_id).orElseThrow(NotFoundException::new);
		return taskGroup;
	}

}
