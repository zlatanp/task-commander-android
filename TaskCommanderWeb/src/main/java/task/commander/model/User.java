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
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name="user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	
	@Column(unique = true, nullable = false)
	@NotNull
	@Email
	private String email;
	
	@Column(unique = true, nullable = false)
	@NotNull
	private String uid;
	
	@Column(name="task_groups")
	@ManyToMany(cascade = { CascadeType.ALL}, fetch = FetchType.EAGER )
	@JoinTable(name = "task_groups_members", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "task_group_id") })
	private Collection<TaskGroup> task_groups;
	
	public User() {
		super();
		task_groups = new ArrayList<>();
		// TODO Auto-generated constructor stub
	}

	public User(@NotNull @Email String email, Collection<TaskGroup> groups) {
		super();
		this.email = email;
		this.task_groups = groups;
	}

	public User(String email, String uid) {
		this.email = email;
		this.uid = uid;
		this.task_groups = new ArrayList<TaskGroup>();
	}	
	
	

}
