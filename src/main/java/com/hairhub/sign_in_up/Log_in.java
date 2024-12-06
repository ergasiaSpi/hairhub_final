package com.Hairhub.sign_in_up;

import java.util.Scanner;

public class Log_in {

    private static Scanner input = new Scanner(System.in);

    private static void Get_Input() {
        String Username = " ";
        String Password = " ";
        do{
            System.out.println("Username: ");
            Username = input.nextLine();
            System.out.println("Password: ");
            Password = input.nextLine();
        } while (SQL_CON.SQL_SELECT(Username, Password)==false);
    }
    
    public static void main(String[] args) {
        Get_Input();
    }
}
