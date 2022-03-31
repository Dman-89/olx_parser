package app_main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        System.out.print("Enter key phrase:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Please enter user name : ");
        String keyphrase = null;
        try {
            keyphrase = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("You entered : " + keyphrase);

    }
}
