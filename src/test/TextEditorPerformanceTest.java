/*
 * Copyright 2020 <Copyright Vasu Gupta>
 * Vasu Gupta
 * Email: vasu99g@gmail.com
 */

package test;

import texteditor.TextEditor;

import java.io.*;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

public class TextEditorPerformanceTest {
    private static final String FILE_NAME = "sample.txt"; // sample.txt contains 2,162,041 characters
    private static final int NUMBER_OF_OPERATIONS = 1000;

    public static void main(String[] args) throws IOException {
        URL path = TextEditorPerformanceTest.class.getResource(FILE_NAME);
        File file = new File(path.getFile());
        BufferedReader br = new BufferedReader(new FileReader(file));

        String buffer;
        StringBuilder document = new StringBuilder();

        while ((buffer = br.readLine()) != null) {
            document.append(buffer);
        }

        int length = document.length();
        TextEditor editor = new TextEditor(document.toString());

        cutPaste(length, editor);
        retrieveText(editor);
        getMisspellings(editor);
        copyPaste(length, editor);
    }

    private static void cutPaste(int length, TextEditor editor) {
        long startTime = System.nanoTime();

        for (int i = 0; i < NUMBER_OF_OPERATIONS; i++) {
            int start = random(0, length);
            int end = random(start, length);
            editor.cut(start, end);
            editor.paste(random(0, length - (end - start)));
        }

        long endTime = System.nanoTime();

        System.out.print("Time to perform " + NUMBER_OF_OPERATIONS + " cut paste operations = ");
        System.out.println(endTime - startTime);
    }

    private static void copyPaste(int length, TextEditor editor) {
        long startTime = System.nanoTime();

        for (int i = 0; i < NUMBER_OF_OPERATIONS; i++) {
            int start = random(0, length);
            int end = random(start, length);

            editor.copy(start, end);
            editor.paste(random(0, length - (end - start)));
        }

        long endTime = System.nanoTime();

        System.out.print("Time to perform " + NUMBER_OF_OPERATIONS + " copy paste operations = ");
        System.out.println(endTime - startTime);
    }

    private static void retrieveText(TextEditor editor) {
        long startTime = System.nanoTime();

        for (int i = 0; i < NUMBER_OF_OPERATIONS; i++) {
            editor.getText();
        }

        long endTime = System.nanoTime();

        System.out.print("Time to perform " + NUMBER_OF_OPERATIONS + " text retrieval operations = ");
        System.out.println(endTime - startTime);
    }

    private static void getMisspellings(TextEditor editor) {
        long startTime = System.nanoTime();

        for (int i = 0; i < NUMBER_OF_OPERATIONS; i++) {
            editor.misspellings();
        }

        long endTime = System.nanoTime();

        System.out.print("Time to perform " + NUMBER_OF_OPERATIONS + " misspelling operations = ");
        System.out.println(endTime - startTime);
    }

    public static int random(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}
