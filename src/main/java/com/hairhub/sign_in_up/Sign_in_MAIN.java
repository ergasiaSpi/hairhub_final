package com.hairhub.sign_in_up;

import java.util.Scanner;

public class Sign_in_MAIN {

    static Scanner input = new Scanner(System.in);

    public static void Get_Input() {
        String Username = " ";
        String Password = " ";
        do{
            System.out.println("Username: ");
            Username = input.nextLine();
            System.out.println("Password: ");
            Password = input.nextLine();
        } while (SQL_CON.SQL_SELECT(Username, Password)==false);
    }
    
}
