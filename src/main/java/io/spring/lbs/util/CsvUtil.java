package io.spring.lbs.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CsvUtil {

    static long startTime;
    static long endTime;
    static long timeDiff;
    static double transactionTime;


    // CSV 파일을 생성한다.
    public static void writeCsv() throws IOException {
        startTime = System.currentTimeMillis();
        List<String[]> data = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= 5000000; i++) {
            String[] row = {String.valueOf(i), "0101234" + (random.nextInt(9000) + 1000), String.valueOf(random.nextInt(90000) + 10000)};
            data.add(row);
        }
        System.out.println("data (0) = " + Arrays.toString(data.get(0)));

        String csvFilePath = "C:\\Users\\nb18-03hp\\IdeaProjects\\test\\user_data.csv";
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            writer.writeAll(data);
        }
        endTime = System.currentTimeMillis();
        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("csv write file size = " + data.size());
        System.out.println("========== CSV WRITE TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println();
    }


    // CSV 파일에서 데이터를 읽어 List<String[]>로 만든다.
    public static List<String[]> readCsv(File file) throws IOException, CsvValidationException {
        startTime = System.currentTimeMillis();
        List<String[]> records = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                String[] paddedLine = Arrays.copyOf(line, 5);
                records.add(paddedLine);
            }
        }
        endTime = System.currentTimeMillis();
        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("csv read file size = " + records.size() + ", file(0) = " + Arrays.toString(records.get(0)));
        System.out.println("readCsv() DONE");
        System.out.println("========== CSV READ TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println();

        return records;
    }

}
