package org.pdfocrexport.com.pdfocrdesktopapp.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlipParser {
    private static final Logger logger = Logger.getLogger(SlipParser.class.getName());

    // Define the order of columns based on your requirement
    private static final String[] COLUMN_ORDER_FOR_T4 = {
            "Slip", "Box 12", "Year", "Employer Name", "Box 10", "Box 14",
            "Box 16", "Box 16 A", "Box 17", "Box 17 A", "Box 18", "Box 55",
            "Box 20", "Box 52", "Box 22", "Box 24", "Box 26", "Box 44",
            "Box 45", "Box 46", "Green Box"
    };

    private static final String[] COLUMN_ORDER_FOR_RL1 = {
            "Slip",
            "Year",
            "Numero d’assurance Sociale",
            "Nom et address de l’employeur",
            "Box A",
            "Box E",
            "Box J",
            "Box L"
    };


    public static void main(String[] args) {
        String pdfFilePath = "D:\\Personal\\OCR-Project\\20240516_153925-11.pdf";

        try {
            FileHandler fileHandler = new FileHandler("log.txt");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException e) {
            logger.warning("Failed to set up logging file.");
        }

        try (PDDocument document = PDDocument.load(new File(pdfFilePath))) {

            PDFTextStripper pdfStripper = new PDFTextStripper();

            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                pdfStripper.setStartPage(page + 1);
                pdfStripper.setEndPage(page + 1);
                String pageText = pdfStripper.getText(document);
                writeT4DataToCSV(generateT4Data(pageText), "D:\\Personal\\OCR-Project");
                writeRL1DataToCSV(generateRL1Data(pageText), "D:\\Personal\\OCR-Project");
            }
        } catch (IOException e) {
            logger.severe("An error occurred while processing the PDF file.");
            e.printStackTrace();
        }

    }

    public static int processPDF(File selectedPDFFile, String outputFolderPath) {
        try (PDDocument document = PDDocument.load(selectedPDFFile)) {

            PDFTextStripper pdfStripper = new PDFTextStripper();

            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                pdfStripper.setStartPage(page + 1);
                pdfStripper.setEndPage(page + 1);
                String pageText = pdfStripper.getText(document);
                writeRL1DataToCSV(generateRL1Data(pageText),outputFolderPath);
                writeT4DataToCSV(generateT4Data(pageText), outputFolderPath);
            }
            return 1;
        } catch (IOException e) {
            logger.severe("An error occurred while processing the PDF file.");
            return -1;
        }
    }

    public static List<Map<String, String>> generateT4Data(String pageText) {
        // Define regex pattern for T4 sections
        String regex = "(T4.*?)(?=T4|$)";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(pageText);

        // List to store all extracted T4 data
        List<Map<String, String>> dataList = new ArrayList<>();

        int slipNumber = 1;

        // Iterate over matched sections and extract data
        while (matcher.find()) {
            String sanitizedInputText = matcher.group().replace("�", "").replace("�", "");

            // Store each box value in the specified order
            Map<String, String> data = new HashMap<>();
            data.put("Slip", "T4;" + slipNumber++);
            data.put("Box 12", findBox12(sanitizedInputText));
            data.put("Year", findYearT4(sanitizedInputText));
            data.put("Employer Name", findEmployerName(sanitizedInputText));
            data.put("Box 10", findBox10(sanitizedInputText));
            data.put("Box 14", findBox14(sanitizedInputText));
            data.put("Box 16", findBox16(sanitizedInputText));
            data.put("Box 16 A", findBox16A(sanitizedInputText));
            data.put("Box 17", findBox17(sanitizedInputText));
            data.put("Box 17 A", findBox17A(sanitizedInputText));
            data.put("Box 18", findBox18(sanitizedInputText));
            data.put("Box 55", findBox55(sanitizedInputText));
            data.put("Box 20", findBox20(sanitizedInputText));
            data.put("Box 52", findBox52(sanitizedInputText));
            data.put("Box 22", findBox22(sanitizedInputText));
            data.put("Box 24", findBox24(sanitizedInputText));
            data.put("Box 26", findBox26(sanitizedInputText));
            data.put("Box 44", findBox44(sanitizedInputText));
            data.put("Box 45", findBox45(sanitizedInputText));
            data.put("Box 46", findBox46(sanitizedInputText));
            data.put("Green Box", findGreenBox(sanitizedInputText));

            dataList.add(data);
        }

        return dataList;
    }

    public static void writeT4DataToCSV(List<Map<String, String>> dataList, String outputDirectory) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

        for (int i = 0; i < dataList.size(); i++) {
            Map<String, String> data = dataList.get(i);
            if (data.get("Employer Name") != null && data.get("Year") != null) {
                String timestamp = dateFormat.format(new Date());
                String fileName = outputDirectory + "/T4Data_" + timestamp + "_" + (i + 1) + ".csv"; // Unique file name with timestamp

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                    // Write headers
                    writer.write("Box,Value\n");

                    // Write data based on COLUMN_ORDER
                    for (String column : COLUMN_ORDER_FOR_T4) {
                        String value = data.get(column);
                        if (value == null)
                            value = "";
                        writer.write(column + "," + value + "\n");
                    }

                    System.out.println("CSV file created: " + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String findGreenBox(String sanitizedInputText) {
        return null;
    }

    private static String findBox46(String sanitizedInputText) {
        return null;
    }

    private static String findBox44(String sanitizedInputText) {
        return null;
    }

    private static String findBox22(String sanitizedInputText) {
        return null;
    }

    private static String findBox55(String sanitizedInputText) {
        return null;
    }

    private static String findBox17A(String sanitizedInputText) {
        return null;
    }

    private static String findBox16A(String sanitizedInputText) {
        return null;
    }

    private static String findBox16(String sanitizedInputText) {
        return null;
    }

    private static String findBox12(String sanitizedInputText) {
        return null;
    }

    // Helper method to write box value to the CSV file
    private static void writeBoxValue(BufferedWriter writer, String boxName, String boxValue) throws IOException {
        writer.write(boxName + "," + boxValue + "\n");
    }

    public static String findEmployerName(String inputText) {
        String[] lines = inputText.split("\n");

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("T4")) {
                StringBuilder employerNameBuilder = new StringBuilder();
                for (int j = i + 1; j <= Math.min(i + 3, lines.length - 1); j++) {
                    String line = lines[j];
                    int endIndex = findFirstIndexOf(line, "Year", "Annee", "Employer");
                    if (endIndex != -1) {
                        line = line.substring(0, endIndex);
                    }
                    employerNameBuilder.append(line.trim()).append(" ");
                }
                return employerNameBuilder.toString().trim();
            }
        }
        return null;
    }

    private static int findFirstIndexOf(String line, String... substrings) {
        for (String substring : substrings) {
            int index = line.indexOf(substring);
            if (index != -1) {
                return index;
            }
        }
        return -1;
    }

    public static String findYearT4(String text) {
        Matcher matcherYear = Pattern.compile("Annee\\s+(\\d{3,4})").matcher(text);
        if (matcherYear.find()) {
            int year = Integer.parseInt(matcherYear.group(1).trim());
            if (year < 1000) {
                year += 2000;
            }
            return String.valueOf(year);
        }
        return null;

    }

    public static String findBox45(String inputText) {
        Pattern pattern = Pattern.compile("Numero de compte de.*?(\\d+)");
        Matcher matcher = pattern.matcher(inputText);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    public static String findBox10(String inputText) {
        Pattern pattern = Pattern.compile("Exemption Go\\\\ ([A-Z]{2})");
        Matcher matcher = pattern.matcher(inputText);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    public static String findBox14(String inputText) {
        return findNextLineAfterText(inputText, "us d'emplo");
    }

    public static String findBox18(String inputText) {
        return findNextLineAfterText(inputText, "a l'AE");
    }

    public static String findBox17(String inputText) {
        return findNextLineAfterText(inputText, "u RRQ");
    }

    public static String findBox24(String inputText) {
        return findNextLineAfterText(inputText, "Gains assurables");
    }

    public static String findBox26(String inputText) {
        return findNextLineAfterText(inputText, "RPCIRRQ");
    }

    public static String findBox20(String inputText) {
        // Adjusted pattern to check if "Cotisations à un RPA" exists, case insensitively
        Pattern pattern = Pattern.compile("(Cotisalions a un RPA)|(Cotisalions un RPA)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputText);

        if (matcher.find()) {
            // Match found, now look for the content on the next line
            int startIndex = matcher.end(); // End index of the match
            int nextLineIndex = inputText.indexOf("\n", startIndex); // Find next newline after the match

            if (nextLineIndex != -1) {
                // Extract content from the next line
                int contentStartIndex = nextLineIndex + 1; // Start of the content on the next line
                int contentEndIndex = inputText.indexOf("\n", contentStartIndex); // End of the content line
                if (contentEndIndex == -1) {
                    contentEndIndex = inputText.length(); // If no more newlines, capture until the end
                }
                String content = inputText.substring(contentStartIndex, contentEndIndex).trim();
                return content;
            }
        }

        return null; // Return null if pattern not found or content on next line not found
    }


    public static String findBox52(String inputText) {
        // Pattern to capture the content after "Facteur d'équivalence" or similar
        Pattern pattern = Pattern.compile("(Facleur d'equivalence)\\s*\\n([^\\n]*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputText);

        if (matcher.find()) {
            return matcher.group(2).trim(); // Return the content captured after the match, trimming whitespace
        }

        return null;
    }


    public static String findNextLineAfterText(String inputText, String searchText) {
        // Escape the search text to ensure it's treated as a literal in the regex
        String escapedSearchText = Pattern.quote(searchText);

        // Constructing the regex pattern
        Pattern pattern = Pattern.compile(".*?" + escapedSearchText + ".*?\\n(.*?)(?:\\n|\\z)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(inputText);

        if (matcher.find()) {
            // Group 1 captures the content of the next line
            return matcher.group(1).trim(); // Trim to remove any leading/trailing whitespace
        }

        return null;
    }

    public static Map<String, String> generateRL1Data(String pageText) {
        // Define regex pattern for T4 sections
        String regex = "(\\bRL[—-]1\\b)";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(pageText);

        // List to store all extracted RL-1 data
        Map<String, String> data = new HashMap<>();

        // Iterate over data
        if (matcher.find()) {
            String sanitizedInputText = pageText.replace("�", "").replace("�", "");

            data.put("Slip", "RL-1;1");
            data.put("Year", findYearForRL1(sanitizedInputText));
            data.put("Numero d’assurance Sociale", findAssuranceForRL1(sanitizedInputText));
            data.put("Nom et address de l’employeur", findAddressForRL1(sanitizedInputText));
            data.put("Box A", findBoxAForRL1(sanitizedInputText));
            data.put("Box E", findBoxEForRL1(sanitizedInputText));
            data.put("Box J", findBoxJForRL1(sanitizedInputText));
            data.put("Box L", findBoxLForRL1(sanitizedInputText));

        }

        return data;
    }

    public static void writeRL1DataToCSV(Map<String, String> dataList, String outputDirectory) {
        // Check if there is data against "Year"
        if (dataList.get("Year") != null && !dataList.get("Year").isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

            String timestamp = dateFormat.format(new Date());
            String fileName = outputDirectory + "/RL1Data_" + timestamp + ".csv"; // Unique file name with timestamp

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                // Write headers
                writer.write("Box,Value\n");

                // Write data based on COLUMN_ORDER_FOR_RL1
                for (String column : COLUMN_ORDER_FOR_RL1) {
                    String value = dataList.get(column);
                    if (value == null)
                        value = "";
                    writer.write(column + "," + value + "\n");
                }

                System.out.println("CSV file created: " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String findBoxLForRL1(String sanitizedInputText) {
        return "";
    }

    private static String findBoxJForRL1(String sanitizedInputText) {
        return "";
    }

    private static String findBoxEForRL1(String sanitizedInputText) {
        return "";
    }

    private static String findBoxAForRL1(String sanitizedInputText) {
        return "";
    }

    private static String findAddressForRL1(String sanitizedInputText) {
        return "";
    }

    private static String findAssuranceForRL1(String sanitizedInputText) {
        return "";
    }

    public static String findRL1(String text) {
        // Pattern to find "RL—1" or "RL-1"
        Pattern rlPattern = Pattern.compile("\\bRL[—-]1\\b");
        Matcher rlMatcher = rlPattern.matcher(text);
        if (rlMatcher.find()) {
            return rlMatcher.group();
        }
        return "";
    }

    public static String findYearForRL1(String text) {
        // Pattern to find "revenus divers" and the year before "R"
        Pattern yearPattern = Pattern.compile("revenus divers I (\\d{4}) IR");
        Matcher yearMatcher = yearPattern.matcher(text);
        if (yearMatcher.find()) {
            return yearMatcher.group(1);
        }
        return "";
    }


}
