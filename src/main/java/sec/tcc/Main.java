package sec.tcc;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

public class Main {

    private static String[] ignoreList = new String[] {"import"};

    public static void main(String[] args) {
        String directoryPath = "";
        if (args.length != 0) {
            directoryPath = args[0];
        } else {
            System.out.println("The directoryPath and word parameters must be provided.");
            return;
        }

        File directory = new File(directoryPath);
        String[] allTargets = getAllTargets();

        if (directory.exists() && directory.isDirectory()) {
            searchFiles(directory, allTargets);
        } else {
            System.out.println("Directory not found or invalid.");
        }
    }

    public static void searchFiles(File directory, String[] allTargets) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    searchFiles(file, allTargets);
                } else if (file.isFile()) {
                    searchWordInFile(file, allTargets);
                }
            }
        }
    }

    public static void searchWordInFile(File file, String[] allTargets) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineCount = 1;

            while ((line = br.readLine()) != null) {
                if (Arrays.stream(allTargets).anyMatch(line.toLowerCase()::contains) &&
                        Arrays.stream(ignoreList).noneMatch(line.toLowerCase()::contains)) {
                    System.out.println(new Vulnerability(lineCount, file.getName(), line.trim()).report());
                }
                lineCount++;
            }
        } catch (IOException e) {
            System.out.printf("Error reading the file %s: %s", file.getName(), e.getMessage());
        }
    }

    public static String[] getAllTargets() {
        String packageName = "sec.tcc";
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage(packageName));
        Set<Class<? extends Target>> classes = reflections.getSubTypesOf(Target.class);
        List<String> targetedWords = new ArrayList<>();

        for (Class<? extends Target> clazz : classes) {
            try {
                Target instance = clazz.getDeclaredConstructor().newInstance();

                Method method = clazz.getMethod("getTargets");
                Collections.addAll(targetedWords, (String[]) method.invoke(instance));
            } catch (Exception e) {
                System.out.println("Error calling getTargets in " + clazz.getSimpleName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        return targetedWords.toArray(new String[0]);
    }

}