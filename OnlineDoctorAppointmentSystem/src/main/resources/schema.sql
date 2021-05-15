DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS patientAppointment;
DROP TABLE IF EXISTS doctor;
DROP TABLE IF EXISTS patient;

CREATE TABLE users (
	username VARCHAR(50) NOT NULL PRIMARY KEY,
	password VARCHAR(120) NOT NULL,
	enabled BOOLEAN NOT NULL
);

CREATE TABLE authorities (
	username VARCHAR(50) NOT NULL,
	authority VARCHAR(50) NOT NULL,
	FOREIGN KEY (username) REFERENCES users (username)
);

CREATE TABLE doctor (
	doctorId LONG PRIMARY KEY NOT NULL,
	hospitalName VARCHAR(70) NOT NULL,
	username VARCHAR(50) NOT NULL,
	specialty VARCHAR(80) NOT NULL,
	monday VARCHAR(15),
	mondayOpening TIME,
	mondayClosing TIME,
	tuesday VARCHAR(15),
	tuesdayOpening TIME,
	tuesdayClosing TIME,
	wednesday VARCHAR(15),
	wednesdayOpening TIME,
	wednesdayClosing TIME,
	thursday VARCHAR(15),
	thursdayOpening TIME,
	thursdayClosing TIME,
	friday VARCHAR(15),
	fridayOpening TIME,
	fridayClosing TIME,
	saturday VARCHAR(15),
	saturdayOpening TIME,
	saturdayClosing TIME,
	sunday VARCHAR(15),
	sundayOpening TIME,
	sundayClosing TIME,
	address VARCHAR(80) NOT NULL,
	fee NUMBER NOT NULL,
	emailId VARCHAR(80) NOT NULL,
	phoneNumber NUMBER NOT NULL
);

CREATE TABLE patient (
	patientId LONG PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	username VARCHAR(50) NOT NULL,
	emailId VARCHAR(30) NOT NULL,
	phoneNumber NUMBER NOT NULL
);

CREATE TABLE patientAppointment (
	AppointmentId LONG PRIMARY KEY AUTO_INCREMENT,
	patientId LONG NOT NULL,
	doctorId LONG NOT NULL,
	name VARCHAR(50) NOT NULL,
	emailId VARCHAR(30) NOT NULL,
	phoneNumber NUMBER NOT NULL,
	appointmentTime VARCHAR(60) NOT NULL,
	specialty VARCHAR(80) NOT NULL,
	patientAddressLine VARCHAR(50) NOT NULL,
	patientCity VARCHAR(20) NOT NULL,
	patientProvince VARCHAR(20) NOT NULL,
	patientCountry VARCHAR(20) NOT NULL,
	patientZipCode VARCHAR(10) NOT NULL,
	request BOOLEAN NOT NULL, 
	status VARCHAR(25) NOT NULL,
	FOREIGN KEY (patientId) REFERENCES patient (patientId),
	FOREIGN KEY (doctorId) REFERENCES doctor (doctorId) 
);
