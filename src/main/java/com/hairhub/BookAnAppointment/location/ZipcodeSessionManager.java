package com.hairhub.BookAnAppointment.location;

public class ZipcodeSessionManager {
    private static boolean isZipcodeSet = false;
    private static String userZipcode = "";

    // Method to set the user's zipcode
    public static void setUserZipcode(String zipcode) {
        isZipcodeSet = true;
        userZipcode = zipcode;
        System.out.println("User's zipcode set to: " + zipcode);
    }

    // Method to clear the user's zipcode
    public static void clearUserZipcode() {
        isZipcodeSet = false;
        userZipcode = "";
        System.out.println("User's zipcode cleared.");
    }

    // Method to check if the user's zipcode is set
    public static boolean isZipcodeSet() {
        return isZipcodeSet;
    }

    // Method to get the user's current zipcode
    public static String getUserZipcode() {
        if (isZipcodeSet) {
            return userZipcode;
        } else {
            throw new IllegalStateException("User's zipcode is not set.");
        }
    }
}
