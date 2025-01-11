package com.hairhub.sign_in_up;

import java.util.Scanner;

public class UserInput {

    private static Scanner input = new Scanner(System.in);

    public static String Get_Username() {
        String Username = " ";
        boolean valid = false;
        do {
            System.out.println("Username: ");
            Username = input.nextLine();
            valid = Constrictions.Username_Constrictions(Username);
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
        } while (!valid);
        return role;
    }

}
