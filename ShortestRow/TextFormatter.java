import java.util.Arrays;
import java.util.List;

public class TextFormatter {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Please provide the maximum line length and a sentence as arguments.");
            return;
        }

        int len;
        try {
            len = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for the maximum line length. Please enter a valid number.");
            return;
        }

        String sentence = args[1];
        List<String> words = Arrays.asList(sentence.split(" "));

        int numberOfRows = calculateMinimalNumberOfRows(words, len);
        System.out.println("The minimal number of rows is: " + numberOfRows);
    }

    public static int calculateMinimalNumberOfRows(List<String> words, int len) {
        int currentLength = 0;
        int numberOfRows = 1;

        for (String word : words) {
            int space;
            if (currentLength == 0) {
                space = 0; // No space needed before the first word in a row
            } else {
                space = 1; // A space is needed between subsequent words in the same row
            }

            if (currentLength + word.length() + space <= len) {
                currentLength += word.length() + space;
            } else {
                numberOfRows++;
                currentLength = word.length();
            }
        }

        return numberOfRows;
    }
}
