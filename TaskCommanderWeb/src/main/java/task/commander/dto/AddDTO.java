package task.commander.dto;

import java.util.Collection;

public class AddDTO {
	
	private Long group_id;
	private Collection<Long> members;
	
	public AddDTO(Long group_id, Collection<Long> members) {
		super();
		this.group_id = group_id;
		this.members = members;
	}
	
	

}
