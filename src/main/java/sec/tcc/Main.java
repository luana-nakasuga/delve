package sec.tcc;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final String[] ignoreList = new String[]{"import"};

    public static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.log.org.reflections", "off");

        String directoryPath = "";
        if (args.length != 0) {
            directoryPath = args[0];
        } else {
            System.out.println("The directoryPath parameter must be provided.");
            return;
        }

        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            searchFiles(directory);
        } else {
            System.out.println("Directory not found or invalid.");
        }
    }

    public static void searchFiles(File directory) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    searchFiles(file);
                } else if (file.isFile()) {
                    searchByTarget(file);
                }
            }
        }
    }

    public static <T extends Target> void searchWordsInFile(File file, String[] targetedWords, T target) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineCount = 1;

            while ((line = br.readLine()) != null) {
                String lowerLine = line.toLowerCase();

                if (Arrays.stream(targetedWords).anyMatch(lowerLine::contains) &&
                        Arrays.stream(ignoreList).noneMatch(lowerLine::contains) ) {
                    Pattern[] patterns = target.getCompiledVulRegex();
                    for (Pattern pattern : patterns) {
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            System.out.println(new Vulnerability(lineCount, file.getName(), line.trim()).report(target.getVulnerabilityName()));
                        }
                    }
                }

                lineCount++;
            }
        } catch (IOException e) {
            System.out.printf("Error reading the file %s: %s", file.getName(), e.getMessage());
        }
    }

    public static void searchByTarget(File file) {
        String packageName = "sec.tcc.target";
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage(packageName));
        Set<Class<? extends Target>> classes = reflections.getSubTypesOf(Target.class);

        for (Class<? extends Target> clazz : classes) {
            try {
                Target instance = clazz.getDeclaredConstructor().newInstance();

                Method method = clazz.getMethod("getTargets");
                String[] targetedWords = (String[]) method.invoke(instance);

                searchWordsInFile(file, targetedWords, instance);
            } catch (Exception e) {
                System.out.println("Error calling getTargets in " + clazz.getSimpleName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}