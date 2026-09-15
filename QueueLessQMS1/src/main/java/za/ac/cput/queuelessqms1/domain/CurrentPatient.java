package za.ac.cput.queuelessqms1.domain;

/**
 * 
 * @author admin Fortune
 */
public final class CurrentPatient {

    private static String identificationNumber;
    private static String department;
    private static String triageLevel;

    private CurrentPatient() {
        
    }

    public static void set(String idNumber, String dept, String triage) {
        identificationNumber = idNumber;
        department = dept;
        triageLevel = triage;
    }

    public static String getIdentificationNumber() {
        return identificationNumber;
    }

    public static String getDepartment() {
        return department;
    }

    public static String getTriageLevel() {
        return triageLevel;
    }

    public static boolean hasPatient() {
        return identificationNumber != null;
    }

    public static void clear() {
        identificationNumber = null;
        department = null;
        triageLevel = null;
    }
}
