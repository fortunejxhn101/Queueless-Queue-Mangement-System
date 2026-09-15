package za.ac.cput.queuelessqms1.domain;
import java.time.LocalDate;

import java.time.LocalDate;

/**
 *
 * @author admin Fortune 
 */
public class Patient {
    protected String idNumber;
    protected String firstName;
    protected String lastName;
    protected String phoneNumber;
    protected LocalDate dateOfBirth;
    protected String gender;
   
    public Patient(String idNumber, String firstName, String lastName, String phoneNumber, LocalDate dateOfBirth, String gender) {
        this.idNumber = idNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setlastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
