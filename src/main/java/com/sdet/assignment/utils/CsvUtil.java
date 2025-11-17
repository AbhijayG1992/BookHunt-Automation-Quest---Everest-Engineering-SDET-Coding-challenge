package com.sdet.assignment.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.sdet.assignment.model.Book;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {
    public static List<Book> readBooks(String filePath) {
        List<Book> books = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();
            // Skip header
            for (int i = 1; i < records.size(); i++) {
                String[] record = records.get(i);
                // Assumes CSV format: Book Name, Quantity
                String bookName = record[0];
                int quantity = Integer.parseInt(record[1].trim());
                
                books.add(new Book(bookName, quantity));
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return books;
    }
}
