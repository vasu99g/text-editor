package test;

import texteditor.UnrolledTextList;

public class UnrolledTextListTest {
    public static void main(String[] args) {
        UnrolledTextList document = new UnrolledTextList(4);

        // tests
        boolean testPassed = true;

        // paste will be tested automatically in copy and cut
        testPassed = testInsert(document) && testCopy(document) && testCut(document);

        if (testPassed) {
            System.out.println("All tests passed!");
        } else {
            System.out.println("All tests didn't pass :(");
        }
    }

    private static boolean testInsert(UnrolledTextList document) {
        document.insertText("Hello World! ");
        document.insertText("Today is a good day");

        return document.toString().equals("Hello World! Today is a good day");
    }

    private static boolean testCopy(UnrolledTextList document) {
        document.pasteText(31, document.copyText(11, 12));

        return document.toString().equals("Hello World! Today is a good day!");
    }

    private static boolean testCut(UnrolledTextList document) {
        document.pasteText(19, document.cutText(0, 13));
        document.pasteText(20, document.cutText(32, 33));

        return document.toString().equals("Today is a good day! Hello World!");
    }
}
