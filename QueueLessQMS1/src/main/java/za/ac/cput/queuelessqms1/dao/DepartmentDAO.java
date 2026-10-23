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
        
        System.out.println("Added EMG: " + dao.addDepartment(new Department("EMG", "Emergency")));
        System.out.println("Added OPD: " + dao.addDepartment(new Department("OPD", "Outpatient")));
        System.out.println("Added PHR: " + dao.addDepartment(new Department("PHR", "Pharmacy")));
        System.out.println("Added RAD: " + dao.addDepartment(new Department("RAD", "Radiology")));
        System.out.println("Added LAB: " + dao.addDepartment(new Department("LAB", "Blood Lab")));
        
        Department found = dao.getDepartment("OPD");
        System.out.println("Found: " + found);
        
        List<Department> all = dao.getAllDepartments();
        System.out.println("Total departments: " + all.size());
        
         System.out.println("Updated: " + dao.updateDepartment(new Department("EMG", "Emergency Unit")));
         
         System.out.println("Delete: " + dao.deleteDepartment("LAB"));




    }
}
