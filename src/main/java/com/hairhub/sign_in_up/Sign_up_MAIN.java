package com.hairhub.sign_in_up;

public class Sign_up_MAIN {
    public static void main(String[] args) {
        String username = UserInput.Get_Username();
        String password = UserInput.Get_Password();
        String phone = UserInput.Get_Phone();
        String email = UserInput.Get_Email();
        String postcode = UserInput.Get_Postcode();
        String role = UserInput.Get_Role();

        SQL_CON.SQL_INSERT(username, password, email, phone, postcode, role);
    }
}
