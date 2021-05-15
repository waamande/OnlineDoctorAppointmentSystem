package ca.sheridancollege.waamande.beans;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient implements Serializable {

	private static final long serialVersionUID = 1L;
	private long patientId;
	private String name;
	private String username;
	private String password;
	private String emailId;
	private long phoneNumber;
	
}
