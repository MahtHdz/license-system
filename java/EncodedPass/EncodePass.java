import java.io.File;
import java.util.Base64;
import java.util.Scanner;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class EncodePass{
    
    public static String FILE_NAME = "ENCODED_PASS.txt";
    public static void main(String[] args) {
        String pass = null;
        CreateFile();
        try {
            // create a scanner so we can read the command-line input
            Scanner scanner = new Scanner(System.in);
            // prompt for the user's name
            System.out.print("Enter the password: ");
            // get their input as a String
            pass = scanner.next();    
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        WriteEncodedPass(pass);
    }
    
    public static void WriteEncodedPass(String pass){
        try {
            FileWriter myWriter = new FileWriter(FILE_NAME);
            myWriter.write(Base64.getEncoder().encodeToString(pass.getBytes()));
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }  
    }
    
    public static void CreateFile(){
        try {
            File file = new File(FILE_NAME);
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}