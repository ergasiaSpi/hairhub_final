package com.hairhub.sign_in_up;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Constrictions {

    
    // Ελέγχει το Username
    public static boolean Name_Constrictions(String Username) {
        if (Username.length() < 5 || Username.length() > 20) {
            System.out.println("The input must be between 5 and 20 characters.");
            return false;
        }
        
        if (!Username.matches(".*[a-zA-Z].*")) {
            System.out.println("The input must contain at least one letter.");
            return false;
        }
        
        if (Username.contains(" ")) {
            System.out.println("The input must not contain spaces.");
            return false;
        }
        
        return true;
    }

    // Ελέγχει το Password
    public static boolean Password_Constrictions(String Password) {
        if (Password.length() < 8 || Password.length() > 15) {
            System.out.println("The password must be between 8 and 15 characters.");
            return false;
        }

        if (!Password.matches(".*[0-9].*")) {
            System.out.println("The password must contain at least one digit.");
            return false;
        }

        if (!Password.matches(".*[a-z].*")) {
            System.out.println("The password must contain at least one lowercase letter.");
            return false;
        }

        if (!Password.matches(".*[A-Z].*")) {
            System.out.println("The password must contain at least one uppercase letter.");
            return false;
        }

        if (!Password.matches(".*[!@#$%&^*].*")) {
            System.out.println("The password must contain at least one special character.");
            return false;
        }

        if (Password.contains(" ")) {
            System.out.println("The password must not contain spaces.");
            return false;
        }

        return true;
    }

    // Ελέγχει το τηλέφωνο (π.χ., +XXX-XXX-XXXX)
    public static boolean Phone_Constrictions(String phone) {
        String regex = "^[+][0-9]{1,3}[-][0-9]{1,4}[-][0-9]{4,10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        if (!matcher.matches()) {
            System.out.println("Invalid phone number format. Please use +XXX-XXX-XXXX.");
            return false;
        }
        return true;
    }

    // Ελέγχει το email
    public static boolean Email_Constrictions(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            System.out.println("Invalid email format. Please ensure the email is in the correct format (example@example.com).");
            return false;
        }
        return true;
    }

    // Ελέγχει τον ταχυδρομικό κώδικα (π.χ., 5 ψηφία)
    public static boolean Postcode_Constrictions(String postcode) {
        if (!postcode.matches("[0-9]{5}")) {
            System.out.println("Invalid postal code. It must be exactly 5 digits.");
            return false;
        }
        return true;
    }

    // Ελέγχει τον ρόλο (πελάτης ή διαχειριστής)
    public static boolean Role_Constrictions(String role) {
        if (role.equalsIgnoreCase("Customer") || role.equalsIgnoreCase("Admin")) {
            return true;
        } else {
            System.out.println("Invalid role. It must be either 'Customer' or 'Admin'.");
            return false;
        }
    }

    public static boolean isValidTime(String time) {
        // Regular Expression for HH:MM:SS
        String timeRegex = "([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)";
        return time.matches(timeRegex);
    }

   
}

