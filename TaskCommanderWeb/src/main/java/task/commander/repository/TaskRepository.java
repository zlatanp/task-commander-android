package task.commander.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import task.commander.model.Task;

@Repository
public interface TaskRepository  extends CrudRepository<Task, Long>{
	public Task save(Task task);
	public Optional<Task> findById(Long id);
	public Optional<Task> findByName(String name);
	
	@Query("select t from Task t where t in (select t1 from Task t1 where t1.assignee.email like %?1% )")
	public Collection<Task> findByAssignee(String email);
	
}