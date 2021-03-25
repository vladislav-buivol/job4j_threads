package ru.job4j.io;

import java.io.*;
import java.util.function.Predicate;

public class ParseFile {
    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public String getContent(Predicate<Character> filter) throws IOException {
        InputStream i = new FileInputStream(file);
        StringBuilder output = new StringBuilder();
        int data;
        while ((data = i.read()) > 0) {
            if (filter.test((char) data)) {
                output.append((char) data);
            }
        }
        i.close();
        return output.toString();
    }

    private class Writer {

        public void saveContent(String content, File file) throws IOException {
            OutputStream o = new FileOutputStream(file);
            for (int i = 0; i < content.length(); i += 1) {
                o.write(content.charAt(i));
            }
            o.close();
        }
    }
}