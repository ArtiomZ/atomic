package org.example;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static AtomicInteger countLength3 = new AtomicInteger();
    public static AtomicInteger countLength4 = new AtomicInteger();
    public static AtomicInteger countLength5 = new AtomicInteger();

    public static void main(String[] args) {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        new Thread(() -> {
            for (String name : texts) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(name);
                stringBuffer.reverse();
                String reverse = stringBuffer.toString();
                if (name.equals(reverse)) {
                    sort(name.length());
                }
            }
        }).start();


        new Thread(() -> {
            for (String name : texts) {
                Pattern pattern = Pattern.compile("(.)\\1+");
                Matcher matcher = pattern.matcher(name);
                while (matcher.find()) {
                    String sub = matcher.group();
                    sort(sub.length());
                }
            }
        }).start();

        new Thread(() -> {
            Compare com = new Compare();
            for (String name : texts) {
                char[] s = name.toCharArray();
                for (int i = 0; i + 1 < name.length(); i++) {
                    int j = com.compare(s[i], s[i + 1]);
                    if (j > 0) {
                        break;
                    } else if (i + 2 == s.length) {
                        sort(name.length());
                    }
                }
            }
        }).start();

        System.out.println("Красивых слов с длинной 3: " + countLength3 + " шт");
        System.out.println("Красивых слов с длинной 4: " + countLength4 + " шт");
        System.out.println("Красивых слов с длинной 5: " + countLength5 + " шт");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void sort(int amount) {
        switch (amount) {
            case 3:
                countLength3.incrementAndGet();
                break;

            case 4:
                countLength4.incrementAndGet();
                break;

            case 5:
                countLength5.incrementAndGet();
                break;
        }
    }
}