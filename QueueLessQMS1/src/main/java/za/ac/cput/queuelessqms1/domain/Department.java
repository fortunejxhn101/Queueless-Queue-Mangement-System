package za.ac.cput.queuelessqms1.domain;

/**
 *
 * @author yantolo
 * 
 */
public class Department {
    private String departmentCode;
    private String departmentName;
    
    public Department(String departmentCode, String departmentName){
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
    }
    public String getDepartmentCode(){
        return departmentCode;
    }
    
    public String getDepartmentName(){
        return departmentName;
    }
    
    public void setDepartmentCode(String departmentCode){
        this.departmentCode = departmentCode;
    }
    
    public void setDepartmentName(String departmentName){
        this.departmentName = departmentName;
    }

    @Override
    public String toString(){
        return"Department{" +  " departmentCode='" + departmentCode + '\'' + ", departmentName='" + departmentName + '\'' + '}';
    }
}

