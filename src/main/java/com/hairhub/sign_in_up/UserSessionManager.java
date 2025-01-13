package com.hairhub.sign_in_up;

public class UserSessionManager {
    private static boolean isUserSignedIn = false;
    private static int signedInUserId = -1;
    private static String signedInUserRole = null;

    // Method to sign in a user
    public static void signInUser(int userId) {
        isUserSignedIn = true;
        signedInUserId = userId;
        System.out.println("User with ID " + userId + " is signed in.");
    }

    // Method to sign out a user
    public static void signOutUser() {
        isUserSignedIn = false;
        signedInUserId = -1;
        System.out.println("User signed out.");
    }

    // Method to check if a user is signed in
    public static boolean isUserSignedIn() {
        return isUserSignedIn;
    }

    // Method to get the currently signed-in user's ID
    public static int getSignedInUserId() {
        if (isUserSignedIn) {
            return signedInUserId;
        } else {
            throw new IllegalStateException("No user is signed in.");
        }
    }

    public static void setUserRole(String role) {
        signedInUserRole = role;
    }

    public static String getSignedInUserRole() {
        if (isUserSignedIn) {
            return signedInUserRole;
        } else {
            throw new IllegalStateException("No user is signed in.");
        }
    }
}
