package task.commander.dto;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;
import task.commander.model.User;

@Data
public class MessageDTO {

	@NotNull
	private String text;
	
	
	@NotNull
	private String sender;


	public MessageDTO(@NotNull String text, @NotNull String sender) {
		super();
		this.text = text;
		this.sender = sender;
	}


	public MessageDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
