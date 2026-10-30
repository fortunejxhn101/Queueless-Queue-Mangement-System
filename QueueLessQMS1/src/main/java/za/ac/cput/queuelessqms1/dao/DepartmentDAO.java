package za.ac.cput.queuelessqms1.dao;

import za.ac.cput.queuelessqms1.domain.Department;
import za.ac.cput.queuelessqms1.connection.QueueLessDBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yanto
 */
public class DepartmentDAO {

    public DepartmentDAO() {
        createDepartmentTable();
    }

    public static void createDepartmentTable() {
        String createTableStmt = "CREATE TABLE Department("
                + "department_code VARCHAR(10) PRIMARY KEY, "
                + "department_name VARCHAR(50) NOT NULL)";

        try ( Connection conn = QueueLessDBConnection.sqlConnection()) {

            DatabaseMetaData meta = conn.getMetaData();
            try ( ResultSet tables = meta.getTables(null, null, "DEPARTMENT", null)) {
                if (tables.next()) {
                    return;
                }
            }

            try ( Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createTableStmt);
                System.out.println("Department table created.");
            }

        } catch (SQLException e) {
            System.out.println("Error creating department table: " + e.getMessage());
        }
    }

    public boolean addDepartment(Department department) {
        String sql = "INSERT INTO department (department_code, department_name) VALUES (?, ?)";
        try ( Connection conn = QueueLessDBConnection.sqlConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, department.getDepartmentCode());
            stmt.setString(2, department.getDepartmentName());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error adding department: " + e.getMessage());
            return false;
        }
    }

    public Department getDepartment(String departmentCode) {
        String sql = "SELECT * FROM department WHERE department_code = ?";
        try ( Connection conn = QueueLessDBConnection.sqlConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, departmentCode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Department(
                        rs.getString("department_code"),
                        rs.getString("department_name")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getting department: " + e.getMessage());
        }
        return null;
    }

    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM department";
        try ( Connection conn = QueueLessDBConnection.sqlConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                departments.add(new Department(
                        rs.getString("department_code"),
                        rs.getString("department_name")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error getting all departments: " + e.getMessage());
        }
        return departments;
    }

    public boolean updateDepartment(Department department) {
        String sql = "UPDATE department SET department_name = ? WHERE department_code = ?";
        try ( Connection conn = QueueLessDBConnection.sqlConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, department.getDepartmentName());
            stmt.setString(2, department.getDepartmentCode());
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error updating department: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteDepartment(String departmentCode) {
        String sql = "DELETE FROM department WHERE department_code = ?";
        try ( Connection conn = QueueLessDBConnection.sqlConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, departmentCode);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting department: " + e.getMessage());
            return false;
        }
    }
}