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
public class PatientAppointment implements Serializable {

	private static final long serialVersionUID = 1L;
	private long appointmentId;
	private long patientId;
	private long doctorId;
	private String name;
	private String emailId;
	private long phoneNumber;
	private String appointmentTime;
	private String specialty;
	private String patientAddressLine;
	private String patientCity;
	private String patientProvince;
	private String patientCountry;
	private String patientZipCode;
	private boolean request;
	private String status;

}
