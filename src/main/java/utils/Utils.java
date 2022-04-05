package utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Utils {

    private static final Random random = new Random();

    public static void printCP1251ToUtf8(String str) {
        System.out.println(new String(str.getBytes(StandardCharsets.UTF_8), Charset.forName("CP1251")));
    }

    public static void randomPause() {
        try {
            Thread.sleep(random.nextInt(7000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
