package task.commander.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import task.commander.model.Message;
import task.commander.model.Task;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long>{
	public Message save(Message message);
	public Optional<Message> findById(Long id);
	
}