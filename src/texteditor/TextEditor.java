/*
 * Copyright 2020 <Copyright Vasu Gupta>
 * Vasu Gupta
 * Email: vasu99g@gmail.com
 */

package texteditor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * <b>TextEditor</b> represents a class that can be used to process, manipulate, and analyze text.
 */
public final class TextEditor {
    private UnrolledTextList document; // the document to manipulate
    private UnrolledTextList clipboard; // String representation of the copied text
    private final Set<String> dictionary; // Set of all words in the dictionary
    // On windows, the dictionary can often be found at:
    //  C:/Users/{username}/AppData/Roaming/Microsoft/Spelling/en-US/default.dic
    private static final String FILE_PATH = "/usr/share/dict/words";
    private static final int NODE_SIZE = 1500; // number of characters per node

    /**
     * Constructs an instance of a TextEditor object initializing it with the provided document, and
     * initializes the dictionary of words.
     *
     * @param document the text file that needs to be loaded into the editor
     * @throws IllegalArgumentException if document is null
     */
    public TextEditor(String document) {
        if (document == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }

        this.document = new UnrolledTextList(NODE_SIZE);
        this.document.insertText(document);
        dictionary = new HashSet<>();

        populateDictionary();
    }

    /**
     * Populates the dictionary of words.
     */
    private void populateDictionary() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String word;

            while ((word = reader.readLine()) != null) {
                dictionary.add(word);
            }
        } catch (FileNotFoundException exception) {
            System.out.println(FILE_PATH + " does not exist");
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Cuts the text from ith character to jth character in the document (exclusive). If the ending index is
     * greater than the length of the document (in terms of characters), then the cut text ranges from
     * starting index to the end of the document.
     *
     * @param i the index of the starting character that needs to be copied
     * @param j the index of the ending character that needs to be copied
     * @throws IllegalArgumentException if j < i or i < 0 or i > document length (in characters)
     */
    public void cut(int i, int j) {
        clipboard = document.cutText(i, j);
    }

    /**
     * Copies the text from ith character to the jth character in the document (exclusive). If the ending index is
     * greater than the length of the document (in terms of characters), then the copied text ranges from
     * starting index to the end of the document.
     *
     * @param i the index of the starting character that needs to be copied
     * @param j the index of the ending character that needs to be copied
     * @throws IllegalArgumentException if j < i or i < 0 or i > document length (in characters)
     */
    public void copy(int i, int j) {
        clipboard = document.copyText(i, j);
    }

    /**
     * Pastes the text in the clipboard into the document starting from the ith character.
     *
     * @param i the index of the starting character in the document
     * @throws IllegalArgumentException if i > document length or clipboard is null
     */
    public void paste(int i) {
        document.pasteText(i, clipboard);
    }

    /**
     * Returns the string representation of the entire document.
     *
     * @return String representation of the text file
     */
    public String getText() {
        return document.toString();
    }

    /**
     * Counts and returns the number of misspelled words in the entire document.
     *
     * @return number of misspelled words in the text file
     */
    public int misspellings() {
        int misspelledWords = 0;

        String[] words = document.toString().split(" ");

        for (String word : words) {
            if (!dictionary.contains(word)) {
                misspelledWords++;
            }
        }

        return misspelledWords;
    }
}
