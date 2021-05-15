package ca.sheridancollege.waamande.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.sheridancollege.waamande.beans.Doctor;
import ca.sheridancollege.waamande.beans.Patient;
import ca.sheridancollege.waamande.beans.PatientAppointment;
import ca.sheridancollege.waamande.database.DatabaseAccess;
import ca.sheridancollege.waamande.geocoder.Geocoder;

@Controller
public class HomeController {
	
	@Autowired
	private JdbcUserDetailsManager jdbc;
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	@Autowired
	private DatabaseAccess da;
	@Autowired
	private Geocoder geocoder;
	
	int count = -1;
	int doctorId = 1;
	int k = 1;
	
	public enum Opening {
		A(6,00), B(6,30), C(7,00), D(7,30), E(8,00), F(8,30), G(9,00), 
		H(9,30), I(10,00), J(10,30), K(11,00), L(11,30), M(12,00);
		
		private final int hr;
		private final int min;
		
		private Opening(int hr, int min) {
			this.hr = hr;
			this.min = min;
		}
		
		public int getHr() {
			return hr;
		}
		
		public int getMin() {
			return min;
		}
	}
	
	public enum Closing {
		A(20,00), B(20,30), C(21,00), D(21,30), E(22,00), F(22,30), G(23,00), 
		H(23,30);
		
		private final int hr;
		private final int min;
		
		private Closing(int hr, int min) {
			this.hr = hr;
			this.min = min;
		}
		
		public int getHr() {
			return hr;
		}
		
		public int getMin() {
			return min;
		}
	}
	
	public enum Monday {
		A("MONDAY"), B("null");
		
		private final String display;
		
		private Monday(String display) {
			this.display = display;
		}
		
		@Override
		public String toString() {
			return display;
		}
	}
	
	public enum Tuesday {
		A("TUESDAY"), B("null");
		
		private final String display;
		
		private Tuesday(String display) {
			this.display = display;
		}
		
		@Override
		public String toString() {
			return display;
		}
	}
	
	public enum Wednesday {
		A("WEDNESDAY"), B("null");
		
		private final String display;
		
		private Wednesday(String display) {
			this.display = display;
		}
		
		@Override
		public String toString() {
			return display;
		}
	}
	
	public enum Thursday {
		A("THURSDAY"), B("null");
		
		private final String display;
		
		private Thursday(String display) {
			this.display = display;
		}
		
		@Override
		public String toString() {
			return display;
		}
	}
	
	public enum Friday {
		A("FRIDAY"), B("null");
		
		private final String display;
		
		private Friday(String display) {
			this.display = display;
		}
		
		@Override
		public String toString() {
			return display;
		}
	}
	
	public enum Saturday {
		A("SATURDAY"), B("null");
		
		private final String display;
		
		private Saturday(String display) {
			this.display = display;
		}
		
		@Override
		public String toString() {
			return display;
		}
	}
	
	public enum Sunday {
		A("SUNDAY"), B("null");
		
		private final String display;
		
		private Sunday(String display) {
			this.display = display;
		}
		
		@Override
		public String toString() {
			return display;
		}
	}
	
	@GetMapping("/")
	public String getHome(Model model, PatientAppointment patientAppointment) {
		model.addAttribute("patientAppointment", patientAppointment);
		return "Home page";
	}
	
	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}
	
	@GetMapping("/registerDoctor")
	public String getRegisterDoctor(Model model, Doctor doctor) {		
		model.addAttribute("doctor", doctor);
		return "register doctor";
	}
	
	@PostMapping("/registerDoctor")
	public String postRegisterDoctor(@ModelAttribute Doctor doctor, @RequestParam double fee, @RequestParam long phoneNumber) {
		doctor.setFee(fee);
		doctor.setPhoneNumber(phoneNumber);
		List<GrantedAuthority> authDoctor = new ArrayList<GrantedAuthority>();
		authDoctor.add(new SimpleGrantedAuthority("ROLE_DOCTOR"));
		String encodedPassword = bcryptPasswordEncoder.encode(doctor.getPassword());
		User user = new User(doctor.getUsername(), encodedPassword, authDoctor);
		doctor.setDoctorId(doctorId);
		doctorId++;
		jdbc.createUser(user);
		da.addDoctor(doctor);
		return "redirect:/";
	}
	
	@GetMapping("/registerPatient")
	public String getRegisterPatient(Model model, Patient patient) {
		model.addAttribute("patient", patient);
		return "register patient";
	}

	
	@PostMapping("/registerPatient")
	public String postRegisterPatient(@ModelAttribute Patient patient, @RequestParam long phoneNumber) {
		patient.setPhoneNumber(phoneNumber);
		List<GrantedAuthority> authPatient = new ArrayList<GrantedAuthority>();
		authPatient.add(new SimpleGrantedAuthority("ROLE_PATIENT"));
		String encodedPassword = bcryptPasswordEncoder.encode(patient.getPassword());
		User user = new User(patient.getUsername(), encodedPassword, authPatient);
		jdbc.createUser(user);
		da.addPatient(patient);
		return "redirect:/";
	}
	
	@PostMapping("/searchDoctors")
	public String postSearchDoctors(@ModelAttribute PatientAppointment patientAppointment, Model model) throws IOException, InterruptedException {
		ObjectMapper mapper = new ObjectMapper();
		String response = geocoder.FindPlace(patientAppointment.getPatientAddressLine() + " " + patientAppointment.getPatientCity() + " " + patientAppointment.getPatientProvince() + " " + patientAppointment.getPatientCountry() + " " + patientAppointment.getPatientZipCode());
		JsonNode responseJsonNode = mapper.readTree(response);
		
		JsonNode items = responseJsonNode.get("candidates");
		String lat = "";
		String lng = "";
		
		for(JsonNode item: items) {
			JsonNode geometry = item.get("geometry");
			JsonNode location = geometry.get("location");
			lat = location.get("lat").asText();
			lng = location.get("lng").asText();
		}
		
		response = geocoder.NearbySearch(lat, lng, patientAppointment.getSpecialty());
		responseJsonNode = mapper.readTree(response);
		items = responseJsonNode.get("results");
		ArrayList<Doctor> doctors = new ArrayList<Doctor>();
		
		for(JsonNode item: items) {
			Doctor doctor = new Doctor();
			
			String name = item.get("name").asText();
			doctor.setHospitalName(name);
			
			double fee = (Math.random()*76)+25;
			fee = Math.ceil(fee);
			doctor.setFee(fee);
			
			JsonNode geometry = item.get("geometry");
			JsonNode location = geometry.get("location");
			lat = location.get("lat").asText();
			lng = location.get("lng").asText();
			
			String address = item.get("vicinity").asText();
			doctor.setAddress(address);
			
			long num1 = (long)(Math.random()* 90000000000L)+ 10000000000L;
			doctor.setPhoneNumber(num1);
			doctor.setSpecialty(patientAppointment.getSpecialty());
			
			String emailId = name.replaceAll(" ", "");
			emailId = emailId.replaceAll(",", "");
			emailId = emailId.replaceAll("\\.", "");
			emailId = emailId.replaceAll("-", "");
			emailId = emailId.replaceAll("\\(", "");
			emailId = emailId.replaceAll("\\)", "");
			emailId = emailId + "@gmail.com";
			doctor.setEmailId(emailId);
			
			int num2 = (int) Math.random()*2;
			Monday m = Monday.values()[num2];
			String monday = m.toString();
			doctor.setMonday(monday);
			
			num2 = (int) Math.random()*2;
			Tuesday t = Tuesday.values()[num2];
			String tuesday = t.toString();
			doctor.setTuesday(tuesday);
			
			num2 = (int) Math.random()*2;
			Wednesday w = Wednesday.values()[num2];
			String wednesday = w.toString();
			doctor.setWednesday(wednesday);
			
			num2 = (int) Math.random()*2;
			Thursday th = Thursday.values()[num2];
			String thursday = th.toString();
			doctor.setThursday(thursday);
			
			num2 = (int) Math.random()*2;
			Friday f = Friday.values()[num2];
			String friday = f.toString();
			doctor.setFriday(friday);
			
			num2 = (int) Math.random()*2;
			Saturday sa = Saturday.values()[num2];
			String saturday = sa.toString();
			doctor.setSaturday(saturday);
			
			num2 = (int) Math.random()*2;
			Sunday su = Sunday.values()[num2];
			String sunday = su.toString();
			doctor.setSunday(sunday);
			
			ArrayList<LocalDateTime> dateTime = new ArrayList<LocalDateTime>();
			LocalDate date = LocalDate.now();
			Month month = date.getMonth();
			int num = date.getDayOfMonth();
			int monLength = month.maxLength() + num;
			
			for(int i=num; i<monLength; i++)
			{
				switch (date.getDayOfWeek().getValue())
				{
					case 1:
						try {
							if(monday.equals("MONDAY"))
							{	
								ArrayList<LocalTime> times= new ArrayList<LocalTime>();
								
								num2 = (int) Math.random()*13;
								Opening op = Opening.values()[num2];
								LocalTime openingTime = LocalTime.of(op.getHr(), op.getMin());
								doctor.setMondayOpening(openingTime);
								
								int num3 = (int) Math.random()*8;
								Closing cl = Closing.values()[num3];
								LocalTime closingTime = LocalTime.of(cl.getHr(), cl.getMin());
								doctor.setMondayClosing(closingTime);
								
								while(openingTime.isBefore(closingTime))
								{
									times.add(openingTime);
									openingTime = openingTime.plusMinutes(30);
								}
								
								for(int j=0; j<times.size(); j++)
								{
									dateTime.add(times.get(j).atDate(date));
								}
							}
						} catch(NullPointerException e) {
							
						} finally {
							
						}
						
						date = date.plusDays(1);
						break;
					
					case 2:
						try {
							if(tuesday.equals("TUESDAY"))
							{
								ArrayList<LocalTime> times= new ArrayList<LocalTime>();
								
								num2 = (int) Math.random()*13;
								Opening op = Opening.values()[num2];
								LocalTime openingTime = LocalTime.of(op.getHr(), op.getMin());
								doctor.setTuesdayOpening(openingTime);
								
								int num3 = (int) Math.random()*8;
								Closing cl = Closing.values()[num3];
								LocalTime closingTime = LocalTime.of(cl.getHr(), cl.getMin());
								doctor.setTuesdayClosing(closingTime);
								
								while(openingTime.isBefore(closingTime))
								{
									times.add(openingTime);
									openingTime = openingTime.plusMinutes(30);
								}
								
								for(int j=0; j<times.size(); j++)
								{
									dateTime.add(times.get(j).atDate(date));
								}
							}
						} catch(NullPointerException e) {
							
						} finally {
							
						}
						
						date = date.plusDays(1);
						break;
						
					case 3:
						try {
							if(wednesday.equals("WEDNESDAY"))
							{
								ArrayList<LocalTime> times= new ArrayList<LocalTime>();
								
								num2 = (int) Math.random()*13;
								Opening op = Opening.values()[num2];
								LocalTime openingTime = LocalTime.of(op.getHr(), op.getMin());
								doctor.setWednesdayOpening(openingTime);
								
								int num3 = (int) Math.random()*8;
								Closing cl = Closing.values()[num3];
								LocalTime closingTime = LocalTime.of(cl.getHr(), cl.getMin());
								doctor.setWednesdayClosing(closingTime);
								
								while(openingTime.isBefore(closingTime))
								{
									times.add(openingTime);
									openingTime = openingTime.plusMinutes(30);
								}
								
								for(int j=0; j<times.size(); j++)
								{
									dateTime.add(times.get(j).atDate(date));
								}
							}
						} catch(NullPointerException e) {
							
						} finally {
							
						}
						
						date = date.plusDays(1);
						break;
						
					case 4:
						try {
							if(thursday.equals("THURSDAY"))
							{
								ArrayList<LocalTime> times= new ArrayList<LocalTime>();
								
								num2 = (int) Math.random()*13;
								Opening op = Opening.values()[num2];
								LocalTime openingTime = LocalTime.of(op.getHr(), op.getMin());
								doctor.setThursdayOpening(openingTime);
								
								int num3 = (int) Math.random()*8;
								Closing cl = Closing.values()[num3];
								LocalTime closingTime = LocalTime.of(cl.getHr(), cl.getMin());
								doctor.setThursdayClosing(closingTime);
								
								while(openingTime.isBefore(closingTime))
								{
									times.add(openingTime);
									openingTime = openingTime.plusMinutes(30);
								}
								
								for(int j=0; j<times.size(); j++)
								{
									dateTime.add(times.get(j).atDate(date));
								}
							}
						} catch(NullPointerException e) {
							
						} finally {
							
						}
						
						date = date.plusDays(1);
						break;
						
					case 5:
						try {
							if(friday.equals("FRIDAY"))
							{
								ArrayList<LocalTime> times= new ArrayList<LocalTime>();
								
								num2 = (int) Math.random()*13;
								Opening op = Opening.values()[num2];
								LocalTime openingTime = LocalTime.of(op.getHr(), op.getMin());
								doctor.setFridayOpening(openingTime);
								
								int num3 = (int) Math.random()*8;
								Closing cl = Closing.values()[num3];
								LocalTime closingTime = LocalTime.of(cl.getHr(), cl.getMin());
								doctor.setFridayClosing(closingTime);
								
								while(openingTime.isBefore(closingTime))
								{
									times.add(openingTime);
									openingTime = openingTime.plusMinutes(30);
								}
								
								for(int j=0; j<times.size(); j++)
								{
									dateTime.add(times.get(j).atDate(date));
								}
							}
						} catch(NullPointerException e) {
							
						} finally {
							
						}
						
						date = date.plusDays(1);
						break;
						
					case 6:
						try {
							if(saturday.equals("SATURDAY"))
							{
								ArrayList<LocalTime> times= new ArrayList<LocalTime>();
								
								num2 = (int) Math.random()*13;
								Opening op = Opening.values()[num2];
								LocalTime openingTime = LocalTime.of(op.getHr(), op.getMin());
								doctor.setSaturdayOpening(openingTime);
								
								int num3 = (int) Math.random()*8;
								Closing cl = Closing.values()[num3];
								LocalTime closingTime = LocalTime.of(cl.getHr(), cl.getMin());
								doctor.setSaturdayClosing(closingTime);
														
								while(openingTime.isBefore(closingTime))
								{
									times.add(openingTime);
									openingTime = openingTime.plusMinutes(30);
								}
								
								for(int j=0; j<times.size(); j++)
								{
									dateTime.add(times.get(j).atDate(date));
								}
							}
						} catch (NullPointerException e) {
							
						} finally {
							
						}
						
						date = date.plusDays(1);
						break;
						
					case 7:
						try {
							if(sunday.equals("SUNDAY"))
							{
								ArrayList<LocalTime> times= new ArrayList<LocalTime>();
								
								num2 = (int) Math.random()*13;
								Opening op = Opening.values()[num2];
								LocalTime openingTime = LocalTime.of(op.getHr(), op.getMin());
								doctor.setSundayOpening(openingTime);
								
								int num3 = (int) Math.random()*8;
								Closing cl = Closing.values()[num3];
								LocalTime closingTime = LocalTime.of(cl.getHr(), cl.getMin());
								doctor.setSundayClosing(closingTime);
								
								
								while(openingTime.isBefore(closingTime))
								{
									times.add(openingTime);
									openingTime = openingTime.plusMinutes(30);
								}
								
								for(int j=0; j<times.size(); j++)
								{
									dateTime.add(times.get(j).atDate(date));
								}
							}
						} catch (NullPointerException e) {
							
						} finally {
							
						}
						
						date = date.plusDays(1);
						break;
				}
			}
			
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			ArrayList<String> times = new ArrayList<String>();
			
			for(int j=0; j<dateTime.size(); j++)
				times.add(j, dateTime.get(j).format(myFormatObj));
			
			doctor.setTimings(times);
			doctor.setUsername("doctor" + k);
			k++;
			
			doctor.setDoctorId(doctorId);
			doctorId++;
			doctors.add(doctor);
		}
		
		List<GrantedAuthority> authDoctor = new ArrayList<GrantedAuthority>();
		authDoctor.add(new SimpleGrantedAuthority("ROLE_DOCTOR"));
		ArrayList<String> passwords = new ArrayList<String>(doctors.size());
		
		for(int j=0; j<doctors.size(); j++) {
			passwords.add("pass" + (j+1));
		}
		
		ArrayList<String> encodedPasswords = new ArrayList<String>(doctors.size());
		
		for(int j=0; j<doctors.size(); j++) {
			encodedPasswords.add(bcryptPasswordEncoder.encode(passwords.get(j)));
			User user = new User(doctors.get(j).getUsername(), encodedPasswords.get(j), authDoctor);
			jdbc.createUser(user);
			da.addDoctor(doctors.get(j));
		}

		
		model.addAttribute("patientAppointment", patientAppointment);
		model.addAttribute("doctors", doctors);
		return "result";
	}
	
	@PostMapping("/requestAppointment")
	public String requestAppointment(
			@RequestParam String appointmentTime,
			@RequestParam int doctorId,
			@RequestParam String patientAddressLine,
			@RequestParam String patientCity,
			@RequestParam String patientProvince,
			@RequestParam String patientCountry,
			@RequestParam String patientZipCode,
			@RequestParam String specialty
			) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Patient patient = da.getPatientByUsername(auth.getName());
		PatientAppointment patientAppointment = new PatientAppointment();
		patientAppointment.setPatientId(patient.getPatientId());
		patientAppointment.setDoctorId(doctorId);
		patientAppointment.setName(patient.getName());
		patientAppointment.setEmailId(patient.getEmailId());
		patientAppointment.setPhoneNumber(patient.getPhoneNumber());
		patientAppointment.setAppointmentTime(appointmentTime);
		patientAppointment.setSpecialty(specialty);
		patientAppointment.setPatientAddressLine(patientAddressLine);
		patientAppointment.setPatientCity(patientCity);
		patientAppointment.setPatientProvince(patientProvince);
		patientAppointment.setPatientCountry(patientCountry);
		patientAppointment.setPatientZipCode(patientZipCode);
		da.addPatientAppointment(patientAppointment);
		return "redirect:/patienttAppointments";
	}
	
	@GetMapping("/requests")
	public String getRequestsPage(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Doctor doctor = da.getDoctorByUsername(auth.getName());
		ArrayList<PatientAppointment> patientAppointments = da.getPatientAppointmentsByDoctorId((int)doctor.getDoctorId());
		if(count > 0) {
			PatientAppointment patientAppointment = da.getPatientAppointmentById(count);
			patientAppointment.setRequest(true);
			patientAppointment.setStatus("Accepted");
			da.updatePatientAppointment(patientAppointment);
			patientAppointments = da.getPatientAppointmentsByDoctorId((int)doctor.getDoctorId());
			count = -1;
		}
		model.addAttribute("patientAppointments", patientAppointments);
		return "Doctor Appointments";
	}
	
	@GetMapping("/accept/{id}")
	public String getAccept(@PathVariable int id) {
		count = id;
		return "redirect:/requests";
	}
	
	@GetMapping("/decline/{id}")
	public String getDecline(@PathVariable int id) {
		PatientAppointment patientAppointment = da.getPatientAppointmentById(id);
		patientAppointment.setRequest(true);
		patientAppointment.setStatus("Rejected");
		da.updatePatientAppointment(patientAppointment);
		count = -1;
		return "redirect:/requests";
	}
	
	@GetMapping("/patienttAppointments")
	public String getStudentAppointments(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Patient patient = da.getPatientByUsername(auth.getName());
		ArrayList<PatientAppointment> patientAppointments = da.getPatientAppointmentsByPatientId((int)patient.getPatientId());
		model.addAttribute("studentAppointments", patientAppointments);
		return "Patient Appointments";
	}
	
	@PostMapping("/describeDoctor")
	public String postDescribeDoctor(
			@RequestParam int doctorId,
			@RequestParam String appointmentTime,
			@RequestParam String status,
			Model model
			) {
		model.addAttribute("doctor", da.getDoctorById(doctorId));
		model.addAttribute("appointmentTime", appointmentTime);
		model.addAttribute("status", status);
		return "Describe Doctor";
	}
	
	@PostMapping("/describeDoctors")
	public String getDescribeDoctor(
			@RequestParam ArrayList<String> timings,
			@RequestParam int doctorId, 
			@RequestParam String patientAddressLine,
			@RequestParam String patientCity,
			@RequestParam String patientProvince,
			@RequestParam String patientCountry,
			@RequestParam String patientZipCode,
			@RequestParam String specialty,
			Model model) {
		
		Doctor doctor = da.getDoctorById(doctorId);
		doctor.setTimings(timings);
		timings.set(0, timings.get(0).substring(1));
		timings.set(timings.size()-1, timings.get(timings.size()-1).substring(0, (timings.get(timings.size()-1).length())-1));
		model.addAttribute("doctor", doctor);
		model.addAttribute("patientAddressLine", patientAddressLine);
		model.addAttribute("patientCity", patientCity);
		model.addAttribute("patientProvince", patientProvince);
		model.addAttribute("patientCountry", patientCountry);
		model.addAttribute("patientZipCode", patientZipCode);
		model.addAttribute("specialty", specialty);
		return "describeDoctor";
	}
	
	@PostMapping("/searchDoctor")
	public String postSearchDoctor(@RequestParam String searchValue, @RequestParam String type, Model model) {
		if(type.equals("specialty")) {
			model.addAttribute("studentAppointments", da.getPatientAppoinmentBySpecialty(searchValue));
		}
		else if(type.equals("appointmentTime")) {
			model.addAttribute("studentAppointments", da.getPatientAppoinmentByAppointmentTime(searchValue));
		}
		else if(type.equals("status")) {
			model.addAttribute("studentAppointments", da.getPatientAppoinmentByStatus(searchValue));
		}
		
		if(searchValue.equals("")) {
			return "redirect:/studentAppointments";
		}
		
		return "Patient Appointments";
	}
	
	@PostMapping("/searchPatient")
	public String name(@RequestParam String searchValue, @RequestParam String type, Model model) {
		if(type.equals("name")) {
			model.addAttribute("patientAppointments", da.getPatientAppoinmentByName(searchValue));
		}
		else if(type.equals("emailId")) {
			model.addAttribute("patientAppointments", da.getPatientAppoinmentByEmailId(searchValue));
		}
		else if(type.equals("phoneNumber")) {
			model.addAttribute("patientAppointments", da.getPatientAppoinmentByPhoneNumber(searchValue));
		} 
		else if(type.equals("appointmentTime")) {
			model.addAttribute("patientAppointments", da.getPatientAppoinmentByAppointmentTime(searchValue));
		}
		
		if(searchValue.equals("")) {
			return "redirect:/requests";
		}
		
		return "Doctor Appointments";
	}
	
}
