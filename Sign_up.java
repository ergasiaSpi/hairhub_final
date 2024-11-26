package ergasia_spineli;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Sign_up {

    private static Scanner input = new Scanner(System.in);

    private static String Get_Username() {
        Boolean C1 = true;
        Boolean C4 = true;
        String Username = " ";
        do {
            System.out.println("Username: ");
            Username = input.nextLine();
            C1 = Username_length_Constrictions(Username);
            C4 = Username_composition_constrictions(Username);
        } while ((C1 && C4) == false);
        return Username;
    }  
    private static String Get_Password() {
        String Password = " ";
        Boolean C3 = true;
        Boolean C2 = true;
        do {
            System.out.println("Password: ");
            Password = input.nextLine();
            C2 = Password_lenght_Constrictions(Password);
            C3 = Password_composition_constrictions(Password);
        } while ((C2 && C3) == false);
        input.close();
        return Password;    
    }
        
    
    //Ελεγχος μεγεθους του Username//
    private static boolean Username_length_Constrictions(String Username) {
        if (Username.length() < 5) {
            System.out.println("Too short Username. The Username must contain 5-20 characters.");
            return false;
        } else if (Username.length() > 20) {
            System.out.println("Too large Username. The Username must contain 5-20 characters.");
            return false;
        } else {
            return true;
        }
    }

    //Eλεγχος μεγεθους κωδικου//
    private static boolean Password_lenght_Constrictions(String Password) {
        if (Password.length() < 8) {
            System.out.println("Too short Password. The Password must contain 8-15 characters.");
            return false;   
        } else if (Password.length() > 15) {
            System.out.println("Too large Password. The Password must contain 8-15 characters.");
            return false; 
        } else {
            return true;
        }
    }

    //Ελεγχος συστασης του Username//
    private static boolean Username_composition_constrictions(String Username) {
        Boolean Var1 = true;
        Boolean Var2 = true;

        //Eλεγχω οτι υπαρχει τουλ. ενα πεζο γραμμα//
        Pattern lowerCasePattern = Pattern.compile("[a-z]");
        Matcher LowerCase = lowerCasePattern.matcher(Username);
        if (LowerCase.find() == false) {
            Var1 = false;
        }   

        //Ελεγχω οτι υπαρχει τουλ. ενα κεφαλαιο γραμμα//
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Matcher UpperCase = upperCasePattern.matcher(Username);
        if (UpperCase.find() == false) {
            Var2 = false;
        }

        //Ελεγχω αμα υπαρχει καποιο κενο//
        if (Username.contains(" ")) {
            System.out.println("The Username must not contain spaces");
        }

        if ((Var1 || Var2) == false) {
            System.out.println("The Username must contain at least one letter");
        }

        return (Var1 || Var2) && !Username.contains(" ");
    }

    
    //Ελεγχος συστασης κωδικου//
    private static boolean Password_composition_constrictions(String Password) {
        Boolean Var1 = true;
        Boolean Var2 = true;
        Boolean Var3 = true;
        Boolean Var4 = true;

        // Έλεγχος για τουλάχιστον έναν αριθμό//
        Pattern digitPattern = Pattern.compile("[0-9]");
        Matcher Digit = digitPattern.matcher(Password);
        if (Digit.find() == false) {
            System.out.println("The password must contain at least one digit");
            Var1 = false;

        }
   
        Pattern lowerCasePattern = Pattern.compile("[a-z]");
        Matcher LowerCase = lowerCasePattern.matcher(Password);
        if (LowerCase.find() == false) {
            System.out.println("The password must contain at least one lower case letter");
            Var2 = false;
        }   

        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Matcher UpperCase = upperCasePattern.matcher(Password);
        if (UpperCase.find() == false) {
            System.out.println("The password must contain at least one upper case letter");
            Var3 = false;
        }  

        // Έλεγχος για τουλάχιστον έναν ειδικό χαρακτήρα
        Pattern specialCharPattern = Pattern.compile("[!@#$%&^*]");
        Matcher SpecialChar = specialCharPattern.matcher(Password);
        if (SpecialChar.find() == false) {
            System.out.println("The password must contain at least one special Character");
            Var4 = false;
        }

        if (Password.contains(" ")) {
            System.out.println("The password must not contain spaces");
        }

        return Var1 && Var2 && Var3 && Var4 && !Password.contains(" "); 
            
    }

    public static void main(String[] args) {
       SQL_CON.SQL_INSERT(Get_Username(), Get_Password());
    }
}
