package app_main;

import parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Main {

    public static void main(String[] args) throws InterruptedException, UnsupportedEncodingException {
        System.out.print("Enter key phrase:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        System.out.print("Please enter user name : ");
        String keyphrase = null;
        try {
            keyphrase = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("You entered : " + keyphrase);
        Parser parser = new Parser();
        parser.readPage(keyphrase);
    }
}
