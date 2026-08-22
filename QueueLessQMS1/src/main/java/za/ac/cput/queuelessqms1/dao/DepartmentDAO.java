package za.ac.cput.queuelessqms1.dao;

import za.ac.cput.queuelessqms1.domain.Department;
import za.ac.cput.queuelessqms1.connection.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author yanto
 */
public class DepartmentDAO {
     public boolean addDepartment(Department department) {
        String sql = "INSERT INTO department (department_code, department_name) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, department.getDepartmentCode());
            stmt.setString(2, department.getDepartmentName());
            int rows = stmt.executeUpdate();
            return rows > 0;
        }catch(SQLException e){
             System.out.println("Error adding department: " + e.getMessage());
            return false;
        }
        }
     
     public Department getDepartment(String departmentCode) {
        String sql = "SELECT * FROM department WHERE department_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, departmentCode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Department(
                    rs.getString("department_code"),
                    rs.getString("department_name")
                );
            }
        }catch (SQLException e) {
            System.out.println("Error getting department: " + e.getMessage());
        }
        return null;
     }
     
     public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM department";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, departmentCode);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting department: " + e.getMessage());
            return false;
        }
    }
     
     public static void main(String[] args) {
        DepartmentDAO dao = new DepartmentDAO();
        
        Department dept = new Department("EMG", "Emergency");
        boolean added = dao.addDepartment(dept);
        System.out.println("Added: " + added);
        
        Department found = dao.getDepartment("EMG");
        System.out.println("Found: " + found);

        List<Department> all = dao.getAllDepartments();
        System.out.println("All departments: " + all.size());
        
        dept.setDepartmentName("Emergency Department");
        boolean updated = dao.updateDepartment(dept);
        System.out.println("Updated: " + updated);
        
        boolean deleted = dao.deleteDepartment("EMG");
        System.out.println("Deleted: " + deleted);
    }


}
