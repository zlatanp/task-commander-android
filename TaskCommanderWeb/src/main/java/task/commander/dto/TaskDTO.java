package task.commander.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class TaskDTO {
	
	private String name;
	private String description;
	private Long group_id;
	private Timestamp deadline;
	private String assigneeMail;
	
	public TaskDTO(String name, String description, Long group_id, String assignee) {
		super();
		this.name = name;
		this.description = description;
		this.group_id = group_id;
		this.assigneeMail = assignee;
	}
	public TaskDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
