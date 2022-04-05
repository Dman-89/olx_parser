package app_main;

import parser.Parser;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, Charset.forName("CP1251"))); //Charset.forName("CP1251") StandardCharsets.UTF_8
//        System.out.print("Enter key phrase:");
//        String keyphrase = null;
//        try {
//            keyphrase = reader.readLine();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String keyphrase = null;
        BufferedReader br = Files.newBufferedReader(Paths.get("src/main/resources/input.txt"), Charset.forName("CP1251"));
        keyphrase = br.readLine();

        System.out.println("You entered : " + keyphrase);

        List<String> exclusions = null;
        try (Stream<String> stream = Files.lines(Paths.get("src/main/resources/exclude.txt"), Charset.forName("CP1251"))) {
            exclusions = stream.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parser parser = new Parser();
        parser.readPageAndSearchBy(new String(keyphrase.getBytes(Charset.forName("CP1251")), StandardCharsets.UTF_8), exclusions);
    }
}
