package task.commander.dto;

import java.util.ArrayList;

import lombok.Data;

@Data
public class GroupDTO {
	private Long uid;
	private String name;
	private ArrayList<String> members ;
	
	public GroupDTO(String name, ArrayList<String> members) {
		super();
		this.name = name;
		this.members = members;
		
	}

	public GroupDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
