package ca.sheridancollege.waamande.beans;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor implements Serializable {

	private static final long serialVersionUID = 1L;
	private long doctorId;
	private String hospitalName;
	private String username;
	private String password;
	private String specialty;
	private String monday;
	private @DateTimeFormat (iso=DateTimeFormat.ISO.TIME) LocalTime mondayOpening; 
	private @DateTimeFormat (iso=DateTimeFormat.ISO.TIME) LocalTime mondayClosing; 
	private String tuesday;
	private @DateTimeFormat (iso=DateTimeFormat.ISO.TIME) LocalTime tuesdayOpening; 
	private @DateTimeFormat (iso=DateTimeFormat.ISO.TIME) LocalTime tuesdayClosing; 
	private String wednesday;
	private @DateTimeFormat (iso=DateTimeFormat.ISO.TIME) LocalTime wednesdayOpening; 
	private @DateTimeFormat (iso=DateTimeFormat.ISO.TIME) LocalTime wednesdayClosing; 
	private String thursday;
	private @DateTimeFormat (iso=DateTimeFormat.ISO.TIME) LocalTime thursdayOpening; 
	private @DateTimeFormat (iso=DateTimeFormat.ISO.TIME) LocalTime thursdayClosing; 
	private String friday;
	private @DateTimeFormat (iso=DateTimeFormat.ISO.TIME) LocalTime fridayOpening; 
	private @DateTimeFormat (iso=DateTimeFormat.ISO.TIME) LocalTime fridayClosing; 
	private String saturday;
	private @DateTimeFormat (iso=DateTimeFormat.ISO.TIME) LocalTime saturdayOpening; 
	private @DateTimeFormat (iso=DateTimeFormat.ISO.TIME) LocalTime saturdayClosing; 
	private String sunday;
	private @DateTimeFormat (iso=DateTimeFormat.ISO.TIME) LocalTime sundayOpening; 
	private @DateTimeFormat (iso=DateTimeFormat.ISO.TIME) LocalTime sundayClosing;
	private String address;
	private double fee; 
	private String emailId;
	private long phoneNumber;
	private ArrayList<String> timings = new ArrayList<String>();
	
}
