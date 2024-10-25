package sec.tcc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String directory = "";
        String word = "";
        if (args.length != 1) {
            directory = args[0];
            word = "csrf";
        } else {
            System.out.println("The directory and word parameters must be provided.");
            return;
        }

        File pasta = new File(directory);

        if (pasta.exists() && pasta.isDirectory()) {
            searchFiles(pasta, word);
        } else {
            System.out.println("Directory not found or invalid.");
        }
    }

    public static void searchFiles(File directory, String word) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    searchFiles(file, word);
                } else if (file.isFile()) {
                    searchWordInFile(file, word);
                }
            }
        }
    }

    public static void searchWordInFile(File file, String word) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineCount = 1;

            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains(word)) {
                    System.out.println(new Vulnerability(lineCount, file.getName(), line.trim(), VulnerabilityLevel.HIGH).report());
                }
                lineCount++;
            }
        } catch (IOException e) {
            System.out.println("Error reading the file " + file.getName() + ": " + e.getMessage());
        }
    }

}