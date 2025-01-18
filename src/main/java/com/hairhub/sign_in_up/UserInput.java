package com.hairhub.sign_in_up;

import java.util.Scanner;

public class UserInput {

    private static Scanner input = new Scanner(System.in);

    public static String Get_Username(boolean UserOrAdmin) {
        String Username = " ";
        boolean valid = false;
        do {
            if (UserOrAdmin) {
                System.out.println("Username: ");
            } else {
                System.out.println("Salon name: ");
            }
            Username = input.nextLine();
            valid = Constrictions.Name_Constrictions(Username);
            if (!valid) {
                System.out.println("Invalid username. Please try again.");
            }
        } while (!valid);
        return Username;
    }  

    public static String Get_Password() {
        String Password = " ";
        boolean valid = false;
        do {
            System.out.println("Password: ");
            Password = input.nextLine();
            valid = Constrictions.Password_Constrictions(Password);
            if (!valid) {
                System.out.println("Invalid password. Please try again.");
            }
        } while (!valid);
        return Password;    
    }

    public static String Get_Phone() {
        String phone = " ";
        boolean valid = false;
        do {
            System.out.println("Phone Number (format: +XXX-XXX-XXXX): ");
            phone = input.nextLine();
            valid = Constrictions.Phone_Constrictions(phone);
            if (!valid) {
                System.out.println("Invalid phone number. Please try again.");
            }
        } while (!valid);
        return phone;
    }

    public static String Get_Email() {
        String email = " ";
        boolean valid = false;
        do {
            System.out.println("Email: ");
            email = input.nextLine();
            valid = Constrictions.Email_Constrictions(email);
            if (!valid) {
                System.out.println("Invalid phone number. Please try again.");
            }
        } while (!valid);
        return email;
    }

    public static String Get_Postcode() {
        String postcode = " ";
        boolean valid = false;
        do {
            System.out.println("Postal Code (5 digits): ");
            postcode = input.nextLine();
            valid = Constrictions.Postcode_Constrictions(postcode);
            if (!valid) {
                System.out.println("Invalid postal code. Please try again.");
            }
        } while (!valid);
        return postcode;
    }

    public static String Get_Role() {
        String role = " ";
        boolean valid = false;
        do {
            System.out.println("Role (Customer/Admin): ");
            role = input.nextLine();
            role.toLowerCase();
            valid = Constrictions.Role_Constrictions(role);
            if (!valid) {
                System.out.println("Invalid role. Please try again.");
            }
        } while (!valid);
        return role;
    }

}
