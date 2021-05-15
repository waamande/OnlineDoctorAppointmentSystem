package ca.sheridancollege.waamande.database;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.waamande.beans.Doctor;
import ca.sheridancollege.waamande.beans.Patient;
import ca.sheridancollege.waamande.beans.PatientAppointment;

@Repository
public class DatabaseAccess {
	
	@Autowired
	public NamedParameterJdbcTemplate jdbc;
	
	public ArrayList<Doctor> getAllDoctors() {
		String query = "SELECT * FROM doctor";
		ArrayList<Doctor> doctors = (ArrayList<Doctor>) jdbc.query(query, new BeanPropertyRowMapper<Doctor>(Doctor.class));
		return doctors;
	}

	public Doctor getDoctorById(int id) {
		String query = "SELECT * FROM doctor WHERE doctorId=:id";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		ArrayList<Doctor> doctor = (ArrayList<Doctor>) jdbc.query(query, map, new BeanPropertyRowMapper<Doctor>(Doctor.class));
		if(doctor.size() > 0) {
			return doctor.get(0);
		}
		return null;
	}
	
	public void addDoctor(Doctor doctor) {
		String query = "INSERT INTO doctor (doctorId, username, hospitalName, specialty, monday, mondayOpening, mondayClosing, tuesday, tuesdayOpening, tuesdayClosing, wednesday, wednesdayOpening, wednesdayClosing, thursday, thursdayOpening, thursdayClosing, friday, fridayOpening, fridayClosing, saturday, saturdayOpening, saturdayClosing, sunday, sundayOpening, sundayClosing, address, fee, emailId, phoneNumber) VALUES (:doctorId, :username, :hospitalName, :specialty, :monday, :mondayOpening, :mondayClosing, :tuesday, :tuesdayOpening, :tuesdayClosing, :wednesday, :wednesdayOpening, :wednesdayClosing, :thursday, :thursdayOpening, :thursdayClosing, :friday, :fridayOpening, :fridayClosing, :saturday, :saturdayOpening, :saturdayClosing, :sunday, :sundayOpening, :sundayClosing, :address, :fee, :emailId, :phoneNumber)";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("doctorId", doctor.getDoctorId());
		map.put("username", doctor.getUsername());
		map.put("hospitalName", doctor.getHospitalName());
		map.put("specialty", doctor.getSpecialty());
		map.put("monday", doctor.getMonday());
		map.put("mondayOpening", doctor.getMondayOpening());
		map.put("mondayClosing", doctor.getMondayClosing());
		map.put("tuesday", doctor.getTuesday());
		map.put("tuesdayOpening", doctor.getTuesdayOpening());
		map.put("tuesdayClosing", doctor.getTuesdayClosing());
		map.put("wednesday", doctor.getWednesday());
		map.put("wednesdayOpening", doctor.getWednesdayOpening());
		map.put("wednesdayClosing", doctor.getWednesdayClosing());
		map.put("thursday", doctor.getThursday());
		map.put("thursdayOpening", doctor.getThursdayOpening());
		map.put("thursdayClosing", doctor.getThursdayClosing());
		map.put("friday", doctor.getFriday());
		map.put("fridayOpening", doctor.getFridayOpening());
		map.put("fridayClosing", doctor.getFridayClosing());
		map.put("saturday", doctor.getSaturday());
		map.put("saturdayOpening", doctor.getSaturdayOpening());
		map.put("saturdayClosing", doctor.getSaturdayClosing());
		map.put("sunday", doctor.getSunday());
		map.put("sundayOpening", doctor.getSundayOpening());
		map.put("sundayClosing", doctor.getSundayClosing());
		map.put("address", doctor.getAddress());
		map.put("fee", doctor.getFee());
		map.put("emailId", doctor.getEmailId());
		map.put("phoneNumber", doctor.getPhoneNumber());
		jdbc.update(query, map);
	}
	
	public void deleteDoctorById(int id) {
		String query = "DELETE FROM doctor WHERE doctorId=:id";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		jdbc.update(query, map);
	}
	
	public ArrayList<Patient> getAllPatients() {
		String query = "SELECT * FROM patient";
		ArrayList<Patient> patients = (ArrayList<Patient>) jdbc.query(query, new BeanPropertyRowMapper<Patient>(Patient.class));
		return patients;
	}

	public Patient getPatientById(int id) {
		String query = "SELECT * FROM patient WHERE patientId=:id";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		ArrayList<Patient> patient = (ArrayList<Patient>) jdbc.query(query, map, new BeanPropertyRowMapper<Patient>(Patient.class));
		if(patient.size() > 0) {
			return patient.get(0);
		}
		return null;
	}
	
	public Patient getPatientByUsername(String username) {
		String query = "SELECT * FROM patient WHERE username=:username";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
		ArrayList<Patient> patient = (ArrayList<Patient>) jdbc.query(query, map, new BeanPropertyRowMapper<Patient>(Patient.class));
		if(patient.size() > 0) {
			return patient.get(0);
		}
		return null;
	}
	
	public void addPatient(Patient patient) {
		String query = "INSERT INTO patient (name, username, emailId, phoneNumber) VALUES (:name, :username, :emailId, :phoneNumber)";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", patient.getName());
		map.put("username", patient.getUsername());
		map.put("emailId", patient.getEmailId());
		map.put("phoneNumber", patient.getPhoneNumber());
		jdbc.update(query, map);
	}
	
	public void modifyPatient(Patient patient) {
		String query = "UPDATE patient SET name=:name, username=:username, emailId=:emailId, phoneNumber=:phoneNumber WHERE patientId=:id";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", patient.getName());
		map.put("username", patient.getUsername());
		map.put("emailId", patient.getEmailId());
		map.put("phoneNumber", patient.getPhoneNumber());
		map.put("id", patient.getPatientId());
		jdbc.update(query, map);
	}
	
	public void deletePatientById(int id) {
		String query = "DELETE FROM patient WHERE patientId=:id";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		jdbc.update(query, map);
	}
	
	public void addPatientAppointment(PatientAppointment patientAppointment) {
		String query = "INSERT INTO patientAppointment (patientId, doctorId, name, emailId, phoneNumber, appointmentTime, specialty, patientAddressLine, patientCity, patientProvince, patientCountry, patientZipCode, request, status) VALUES (:patientId, :doctorId, :name, :emailId, :phoneNumber, :appointmentTime, :specialty, :patientAddressLine, :patientCity, :patientProvince, :patientCountry, :patientZipCode, false, 'Requested')";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("patientId", patientAppointment.getPatientId());
		map.put("doctorId", patientAppointment.getDoctorId());
		map.put("name", patientAppointment.getName());
		map.put("emailId", patientAppointment.getEmailId());
		map.put("phoneNumber", patientAppointment.getPhoneNumber());
		map.put("appointmentTime", patientAppointment.getAppointmentTime());
		map.put("specialty", patientAppointment.getSpecialty());
		map.put("patientAddressLine", patientAppointment.getPatientAddressLine());
		map.put("patientCity", patientAppointment.getPatientCity());
		map.put("patientProvince", patientAppointment.getPatientProvince());
		map.put("patientCountry", patientAppointment.getPatientCountry());
		map.put("patientZipCode", patientAppointment.getPatientZipCode());
		jdbc.update(query, map);
	}
	
	public Doctor getDoctorByUsername(String username) {
		String query = "SELECT * FROM doctor WHERE username=:username";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
		ArrayList<Doctor> doctor = (ArrayList<Doctor>) jdbc.query(query, map, new BeanPropertyRowMapper<Doctor>(Doctor.class));
		if(doctor.size() > 0) {
			return doctor.get(0);
		}
		return null;
	}
	
	public ArrayList<PatientAppointment> getPatientAppointmentsByDoctorId(int doctorId) {
		String query = "SELECT * FROM patientAppointment WHERE doctorId=:doctorId";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("doctorId", doctorId);
		ArrayList<PatientAppointment> patientAppointments = (ArrayList<PatientAppointment>) jdbc.query(query, map, new BeanPropertyRowMapper<PatientAppointment>(PatientAppointment.class));
		return patientAppointments;
	}
	
	public ArrayList<PatientAppointment> getPatientAppointmentsByPatientId(int patientId) {
		String query = "SELECT * FROM patientAppointment WHERE patientId=:patientId";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("patientId", patientId);
		ArrayList<PatientAppointment> patientAppointments = (ArrayList<PatientAppointment>) jdbc.query(query, map, new BeanPropertyRowMapper<PatientAppointment>(PatientAppointment.class));
		return patientAppointments;
	}
	
	public PatientAppointment getPatientAppointmentById(int appointmentId) {
		String query = "SELECT * FROM patientAppointment WHERE appointmentId=:appointmentId";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("appointmentId", appointmentId);
		ArrayList<PatientAppointment> patientAppointments = (ArrayList<PatientAppointment>) jdbc.query(query, map, new BeanPropertyRowMapper<PatientAppointment>(PatientAppointment.class));
		if(patientAppointments.size()>0)
			return patientAppointments.get(0);
		return null;
	}
	
	public void deletePatientAppointmentById(int appointmentId) {
		String q = "DELETE FROM patientAppointment WHERE appointmentId=:appointmentId";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("appointmentId", appointmentId);
		jdbc.update(q, map);
	}
	
	public void updatePatientAppointment(PatientAppointment patientAppointment) {
		String q = "UPDATE patientAppointment SET name=:name, emailId=:emailId, phoneNumber=:phoneNumber, appointmentTime=:appointmentTime, specialty=:specialty, patientAddressLine=:patientAddressLine, patientCity=:patientCity, patientProvince=:patientProvince, patientCountry=:patientCountry, patientZipCode=:patientZipCode, request=:request, status=:status WHERE appointmentId=:appointmentId";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", patientAppointment.getName());
		map.put("emailId", patientAppointment.getEmailId());
		map.put("phoneNumber", patientAppointment.getPhoneNumber());
		map.put("appointmentTime", patientAppointment.getAppointmentTime());
		map.put("specialty", patientAppointment.getSpecialty());
		map.put("patientAddressLine", patientAppointment.getPatientAddressLine());
		map.put("patientCity", patientAppointment.getPatientCity());
		map.put("patientProvince", patientAppointment.getPatientProvince());
		map.put("patientCountry", patientAppointment.getPatientCountry());
		map.put("patientZipCode", patientAppointment.getPatientZipCode());
		map.put("request", patientAppointment.isRequest());
		map.put("status", patientAppointment.getStatus());
		map.put("appointmentId", patientAppointment.getAppointmentId());
		jdbc.update(q, map);
	}
	
	public ArrayList<PatientAppointment> getPatientAppoinmentBySpecialty(String specialty) {
		String q = "SELECT * FROM patientAppointment WHERE LOWER(specialty) LIKE LOWER('%" + specialty + "%')";
		ArrayList<PatientAppointment> patientAppointments = (ArrayList<PatientAppointment>) jdbc.query(q,
				new BeanPropertyRowMapper<PatientAppointment>(PatientAppointment.class));
	    return patientAppointments;
	}
	
	public ArrayList<PatientAppointment> getPatientAppoinmentByAppointmentTime(String appointmentTime) {
		String q = "SELECT * FROM patientAppointment WHERE LOWER(appointmentTime) LIKE LOWER('%" + appointmentTime + "%')";
		ArrayList<PatientAppointment> patientAppointments = (ArrayList<PatientAppointment>) jdbc.query(q,
				new BeanPropertyRowMapper<PatientAppointment>(PatientAppointment.class));
	    return patientAppointments;
	}
	
	public ArrayList<PatientAppointment> getPatientAppoinmentByStatus(String status) {
		String q = "SELECT * FROM patientAppointment WHERE LOWER(status) LIKE LOWER('%" + status + "%')";
		ArrayList<PatientAppointment> patientAppointments = (ArrayList<PatientAppointment>) jdbc.query(q,
				new BeanPropertyRowMapper<PatientAppointment>(PatientAppointment.class));
	    return patientAppointments;
	}
	
	public ArrayList<PatientAppointment> getPatientAppoinmentByName(String name) {
		String q = "SELECT * FROM patientAppointment WHERE LOWER(name) LIKE LOWER('%" + name + "%')";
		ArrayList<PatientAppointment> patientAppointments = (ArrayList<PatientAppointment>) jdbc.query(q,
				new BeanPropertyRowMapper<PatientAppointment>(PatientAppointment.class));
	    return patientAppointments;
	}

	public ArrayList<PatientAppointment> getPatientAppoinmentByEmailId(String emailId) {
		String q = "SELECT * FROM patientAppointment WHERE LOWER(emailId) LIKE LOWER('%" + emailId + "%')";
		ArrayList<PatientAppointment> patientAppointments = (ArrayList<PatientAppointment>) jdbc.query(q,
				new BeanPropertyRowMapper<PatientAppointment>(PatientAppointment.class));
	    return patientAppointments;
	}

	public ArrayList<PatientAppointment> getPatientAppoinmentByPhoneNumber(String phoneNumber) {
		String q = "SELECT * FROM patientAppointment WHERE LOWER(phoneNumber) LIKE LOWER('%" + phoneNumber + "%')";
		ArrayList<PatientAppointment> patientAppointments = (ArrayList<PatientAppointment>) jdbc.query(q,
				new BeanPropertyRowMapper<PatientAppointment>(PatientAppointment.class));
	    return patientAppointments;
	}

}
