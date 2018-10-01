package task.commander.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import task.commander.model.TaskGroup;
import task.commander.model.User;

@Repository
public interface TaskGroupRepository  extends CrudRepository<TaskGroup, Long>{
	public TaskGroup save(TaskGroup group);
	public Optional<TaskGroup> findById(Long id);
	public Optional<TaskGroup> findByName(String name);
	
}