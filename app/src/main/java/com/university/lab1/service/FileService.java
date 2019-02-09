package com.university.lab1.service;

import com.university.lab1.dto.Word;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileService {
    public void exportToFile(String path, String filename, List<Word> words) throws Exception {
        File file = new File(path, filename);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (Word word : words) {
                bufferedWriter.write(word.toFileForm() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
