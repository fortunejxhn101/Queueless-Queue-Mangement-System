package za.ac.cput.queuelessqms1.dao;

import za.ac.cput.queuelessqms1.connection.QueueLessDBConnection;
import java.time.LocalDate;
import java.sql.*;
import za.ac.cput.queuelessqms1.domain.Patient;

/**
 * @author admin Fortune
 */
public class PatientDAO {

    public PatientDAO() {

        String createTableStmt = "CREATE TABLE Patient("
                + "identification_number VARCHAR(20) PRIMARY KEY, "
                + "first_name VARCHAR(50) NOT NULL, "
                + "last_name VARCHAR(50) NOT NULL, "
                + "phone_number VARCHAR(15) NOT NULL, "
                + "date_of_birth DATE NOT NULL, "
                + "gender VARCHAR(6) NOT NULL)";

        try (Connection con = QueueLessDBConnection.sqlConnection()) {

            DatabaseMetaData meta = con.getMetaData();
            try (ResultSet tables = meta.getTables(null, null, "PATIENT", null)) {
                if (tables.next()) {
                    return;
                }
            }

            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate(createTableStmt);
            }
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
        }
    }

    public boolean insertPatientRecord(Patient patientRecord) {

        String insertStmt = "INSERT INTO Patient VALUES (?,?,?,?,?,?)";
        try (Connection con = QueueLessDBConnection.sqlConnection();
                PreparedStatement pstmt = con.prepareStatement(insertStmt)) {

            pstmt.setString(1, patientRecord.getIdNumber());
            pstmt.setString(2, patientRecord.getFirstName());
            pstmt.setString(3, patientRecord.getLastName());
            pstmt.setString(4, patientRecord.getPhoneNumber());
            pstmt.setDate(5, java.sql.Date.valueOf(patientRecord.getDateOfBirth()));
            pstmt.setString(6, patientRecord.getGender());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
            return false;
        }
    }

    public boolean updatePatientRecord(Patient patientRecord) {

        String updateStmt = "UPDATE Patient SET first_name = ?, last_name = ?, phone_number = ?, "
                + "date_of_birth = ?, gender = ? WHERE identification_number = (?)";
        try (Connection con = QueueLessDBConnection.sqlConnection();
                PreparedStatement pstmt = con.prepareStatement(updateStmt)) {

            pstmt.setString(1, patientRecord.getFirstName());
            pstmt.setString(2, patientRecord.getLastName());
            pstmt.setString(3, patientRecord.getPhoneNumber());
            pstmt.setDate(4, java.sql.Date.valueOf(patientRecord.getDateOfBirth()));
            pstmt.setString(5, patientRecord.getGender());
            pstmt.setString(6, patientRecord.getIdNumber());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
            return false;
        }
    }

    public Patient findPatientByIdNumber(String idNumber) {

        String searchPatient = "SELECT * FROM Patient WHERE identification_number = (?)";
        try (Connection con = QueueLessDBConnection.sqlConnection();
                PreparedStatement pstmt = con.prepareStatement(searchPatient)) {

            pstmt.setString(1, idNumber);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    String identificationNumber = rs.getString("identification_number");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String phoneNumber = rs.getString("phone_number");
                    LocalDate date = rs.getDate("date_of_birth").toLocalDate();
                    String gender = rs.getString("gender");

                    return new Patient(identificationNumber, firstName, lastName, phoneNumber, date, gender);
                }
            }
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
        }
        return null;
    }

    public boolean isIdNumberTaken(String idNumber) throws SQLException {

        String select1Sql = "SELECT 1 FROM Patient WHERE identification_number = (?)";
        try (Connection con = QueueLessDBConnection.sqlConnection();
                PreparedStatement pstmt = con.prepareStatement(select1Sql)) {
            pstmt.setString(1, idNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}