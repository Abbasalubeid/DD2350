import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Concordance {

    private String rawIndexFilePath = "rawindex.txt";
    private String firstIndexFilePath = "firstIndex.txt";
    private String secondIndexFilePath = "secondIndex.txt";

    public static int latMansHash(String word) {
        int hash;
        word = word.toLowerCase();
        if (word.length() == 1) {
            hash = word.charAt(0) * 997;
        } else if (word.length() == 2) {
            hash = word.charAt(0) * 997 + word.charAt(1) * 89; 
        } else {
            hash = word.charAt(0) * 997 + word.charAt(1) * 89 + word.charAt(2) * 7;
        }
        return hash;
    } 


    public void buildConcordance() throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(rawIndexFilePath), StandardCharsets.ISO_8859_1));
            BufferedWriter secondIndexFileWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(secondIndexFilePath), StandardCharsets.ISO_8859_1))) {
    
            String line;
            int hash = -1;
            int count = 0;
            String currentWord = null;
            long firstOccurrenceBytePosition = 0;
            long currentBytePosition = 0;
    
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String word = parts[0];
    
                if (hash != latMansHash(word) || !word.equals(currentWord)) {
                    if (currentWord != null) { // if there was a previous word
                        secondIndexFileWriter.write(currentWord + " " + hash + " " + firstOccurrenceBytePosition + " " + count + "\n");
                    }
                    hash = latMansHash(word);
                    count = 0; 
                    firstOccurrenceBytePosition = currentBytePosition; 
                    currentWord = word;
                }
    
                count++;
                currentBytePosition += line.getBytes(StandardCharsets.ISO_8859_1).length + 1;
            }
    
            // Write the last word after loop
            if (currentWord != null) {
                secondIndexFileWriter.write(currentWord + " " + hash + " " + firstOccurrenceBytePosition + " " + count + "\n");
            }
        }

        buildFirstFile();
    }

    public void buildFirstFile() throws IOException {
        try (BufferedReader second = new BufferedReader(new InputStreamReader(
                new FileInputStream(secondIndexFilePath), StandardCharsets.ISO_8859_1));
            BufferedWriter first = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(firstIndexFilePath), StandardCharsets.ISO_8859_1))) {

            String line;
            int hash = -1;
            long currentBytePosition = 0;
            while ((line = second.readLine()) != null) {
                String[] parts = line.split(" ");
                String word = parts[0];
                if (hash != latMansHash(word)) {
                    hash = latMansHash(word);
                    first.write(word + " " + hash + " " + currentBytePosition + "\n");
                }
                currentBytePosition += line.getBytes(StandardCharsets.ISO_8859_1).length + 1;
            }
        }
    }
    

    public List<long[]> getStartandNextPosition(String word) throws IOException {
        int targetHash = latMansHash(word);
        List<long[]> positionPairs = new ArrayList<>();
        long firstPosition = -1;
        long lastPosition = new File(firstIndexFilePath).length(); // default to end of file
    
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(firstIndexFilePath), StandardCharsets.ISO_8859_1))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                int hash = Integer.parseInt(parts[1]);
                long position = Long.parseLong(parts[2]);

                lastPosition = position; 
    
                if (hash == targetHash) {
                    if (firstPosition == -1) { // If it's the first occurrence of this hash
                        firstPosition = position;
                    } 
                } else if (firstPosition != -1) {
                    // If the current hash is not the target and we have found a previous occurrence
                    positionPairs.add(new long[] {firstPosition, position});
                    firstPosition = -1; // Reset for the next possible pair
                }
            }
            if (firstPosition != -1) { // If the last entry has the target hash
                positionPairs.add(new long[] {firstPosition, lastPosition});
            }
        }
        return positionPairs;
    }
    
    public void binarySearchForWord(String word, List<long[]> positionPairs) throws IOException {
        boolean wordFound = false;
    
        for (long[] pair : positionPairs) {
            long startPos = pair[0];
            long endPos = pair[1];
            
            List<String> linesBetween = getLinesBetweenPositions(startPos, endPos);
            int index = binarySearchInList(word, linesBetween);
    
            if (index != -1) {
                String foundLine = linesBetween.get(index);
                String[] parts = foundLine.split(" ");
                
                long rawIndexStartByte = Long.parseLong(parts[2]);
                int numberOfOccurrences = Integer.parseInt(parts[3]);
                
                System.out.println("\n** There are " + numberOfOccurrences + " occurrences of the word " + parts[0]
                           + " in korpus ** \n");
                
                // Now seek to the rawIndexFile to print occurrences
                try (RandomAccessFile rawRaf = new RandomAccessFile(rawIndexFilePath, "r")) {
                    rawRaf.seek(rawIndexStartByte);
                    for (int i = 0; i < numberOfOccurrences; i++) {
                        if (i == 25) {
                            // After printing 25 occurrences, ask the user if they'd like to continue
                            if (!promptUserForDisplay(numberOfOccurrences - i)) {
                                break; // Stop printing and exit loop if user says no
                            }
                        }
                
                        String rawLine = rawRaf.readLine();
                        String[] rawParts = rawLine.split(" ");
                        long korpusPosition = Long.parseLong(rawParts[1]);
                        System.out.println(getLineWithContext(korpusPosition, "korpus.txt", word));
                    }
                }
                wordFound = true;
                break;
            }
        }
    
        if (!wordFound) {
            System.out.println("\n Word not found.");
        }
    }
    
    private List<String> getLinesBetweenPositions(long startPos, long endPos) throws IOException {
        List<String> lines = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(secondIndexFilePath, "r")) {
            raf.seek(startPos);
            long currentPos = startPos;
            while (currentPos <= endPos) {
                String line = raf.readLine();
                if (line == null) break; // End of file reached
                lines.add(line);
                currentPos = raf.getFilePointer();
            }
        }
        return lines;
    }
    
    private int binarySearchInList(String word, List<String> list) {
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;

            String[] parts = list.get(mid).split(" ");
            int comparison = word.compareTo(parts[0]);
    
            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
    
        return -1; // Word not found
    }
    
    

    public String getLineAtBytePosition(long bytePosition, String fileName) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "r")) {
            raf.seek(bytePosition);
            return raf.readLine();
        }
    }

    public static String getLineWithContext(long bytePosition, String fileName, String searchTerm) throws IOException {
        int contextChars = 30;
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "r")) {
            
            long startPos = Math.max(bytePosition - contextChars, 0); 
            long endPos = bytePosition + searchTerm.length() + contextChars; 
            
            raf.seek(startPos);
            
            StringBuilder context = new StringBuilder();
            
            while (raf.getFilePointer() < endPos) {
                context.append((char) raf.read());
            }
    
            // Replace newlines with spaces and return the trimmed string
            return context.toString().replace('\n', ' ');
        }
    }

    private static boolean promptUserForDisplay(int occurrences) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("There are " + occurrences + " occurrences left, do you want them displayed on screen? (y/n)");
        String response = scanner.nextLine();
        scanner.close();
        return response.trim().equalsIgnoreCase("y");
    }
    

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Usage:");
            System.out.println("  --build : To build the concordance.");
            System.out.println("  --search <word> : To search for occurrences of <word>.");
            return;
        }
    
        Concordance concordance = new Concordance();
    
        String command = args[0];
    
        try {
            switch (command) {
                case "--build":
                    System.out.println("Building the concordance...");
                    concordance.buildConcordance();
                    System.out.println("Concordance built successfully!");
                break;

                case "--firstAndNext":
                    List<long[]> positionPairs = concordance.getStartandNextPosition(args[1].toLowerCase());
                    for(long[] pair : positionPairs) {
                        System.out.println("first: " + pair[0] + " next: " + pair[1]);
                        System.out.println("Line at first position in secondFile: " + concordance.getLineAtBytePosition(pair[0], concordance.secondIndexFilePath));
                        System.out.println("Line at last position in secondFile: " + concordance.getLineAtBytePosition(pair[1], concordance.secondIndexFilePath));

                        long rawAdress1 = Long.parseLong(concordance.getLineAtBytePosition(pair[0], concordance.secondIndexFilePath).split(" ")[2]);

                        long rawAdress2 = Long.parseLong(concordance.getLineAtBytePosition(pair[1], concordance.secondIndexFilePath).split(" ")[2]);


                        System.out.println("\nLine at " + rawAdress1 + " in rawindex file " + concordance.getLineAtBytePosition(rawAdress1, concordance.rawIndexFilePath));
                        System.out.println("Line at " + rawAdress2 + " in rawindex file " + concordance.getLineAtBytePosition(rawAdress2, concordance.rawIndexFilePath));
                    }
                break;
            
    
                case "--search":
                    if (args.length < 2) {
                        System.out.println("Usage: --search <word>");
                        return;
                    }
    
                    String targetWord = args[1].toLowerCase();
                    List<long[]> positions = concordance.getStartandNextPosition(targetWord);
    
                    // Benchmarks
                    long startTimeLinear = System.nanoTime();
                    concordance.binarySearchForWord(targetWord, positions);
                    long endTimeLinear = System.nanoTime();

                    // Convert to milliseconds
                    long durationInMs = (endTimeLinear - startTimeLinear) / 1_000_000;  

                    System.out.println("\nIt took " + durationInMs + " ms to find " + targetWord);
                    
                break;
    
                default:
                    System.out.println("Invalid command. Use --build to build the concordance or --search <word> to search for a word.");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}