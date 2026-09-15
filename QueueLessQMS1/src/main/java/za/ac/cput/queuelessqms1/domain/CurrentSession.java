package za.ac.cput.queuelessqms1.domain;

public final class CurrentSession {

    private static Staff loggedInStaff;

    private CurrentSession() {
        
    }

    public static void setLoggedInStaff(Staff staff) {
        loggedInStaff = staff;
    }

    public static Staff getLoggedInStaff() {
        return loggedInStaff;
    }

    public static boolean isLoggedIn() {
        return loggedInStaff != null;
    }

    public static void clear() {
        loggedInStaff = null;
    }
}
