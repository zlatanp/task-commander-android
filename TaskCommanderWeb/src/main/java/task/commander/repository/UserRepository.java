package task.commander.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import task.commander.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	public User save(User user);
	public Optional<User> findById(Long id);
	public Optional<User> findByEmail(String email);
}
