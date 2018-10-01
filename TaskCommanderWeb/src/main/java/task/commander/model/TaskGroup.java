package task.commander.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name = "task_group")
public class TaskGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "task_group_id")
	private Long id;

	@Column(unique = true, nullable = false)
	@Size(max = 40)
	@NotNull
	private String name;

	@ManyToMany(mappedBy = "task_groups")
	private Collection<User> members;

	@OneToMany(cascade = { CascadeType.ALL }, fetch=FetchType.EAGER)
	@JoinTable(name = "task_groups_tasks", joinColumns = { @JoinColumn(name = "task_group_id") }, inverseJoinColumns = {
			@JoinColumn(name = "task_id") })
	private Collection<Task> tasks;
	
	@OneToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "group_messages", joinColumns = { @JoinColumn(name = "task_group_id") }, inverseJoinColumns = {
			@JoinColumn(name = "message_id") })
	private Collection<Message> messages = new ArrayList<Message>();

	public TaskGroup() {
		super();
		this.members = new ArrayList<>();
		this.tasks = new ArrayList<>();
		
	}

	public TaskGroup(String name, Collection<User> members) {
		super();
		this.name = name;
		this.members = members;
		
	}

	@JsonIgnoreProperties({ "task_groups" })
	public Collection<User> getMembers() {
		return members;
	}

	
	public void addMember(User user){
		this.members.add(user);
	}

	public void removeMember(User user) {
		this.members.remove(user);
	}
	
}
