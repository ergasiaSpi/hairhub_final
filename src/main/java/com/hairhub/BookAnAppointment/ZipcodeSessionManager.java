package com.hairhub.BookAnAppointment;

public class ZipcodeSessionManager {
    private static boolean isZipcodeSet = false;
    private static String userZipcode = "";

   
    public static void setUserZipcode(String zipcode) {
        isZipcodeSet = true;
        userZipcode = zipcode;
        System.out.println("User's zipcode set to: " + zipcode);
    }

    
    public static void clearUserZipcode() {
        isZipcodeSet = false;
        userZipcode = "";
        System.out.println("User's zipcode cleared.");
    }

   
    public static boolean isZipcodeSet() {
        return isZipcodeSet;
    }

    
    public static String getUserZipcode() {
        if (isZipcodeSet) {
            return userZipcode;
        } else {
            throw new IllegalStateException("User's zipcode is not set.");
        }
    }
}
