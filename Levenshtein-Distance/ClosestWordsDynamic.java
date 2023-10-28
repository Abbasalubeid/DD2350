import java.util.List;
import java.util.LinkedList;

public class ClosestWordsDynamic {
    LinkedList<String> closestWords = null;
    int closestDistance = -1;
    int[][] matrix = new int[40][40];

    int distance(String w1, String w2) {
        int len1 = w1.length();
        int len2 = w2.length();
        for (int i = 0; i <= len1; i++) matrix[i][0] = i;
        for (int j = 0; j <= len2; j++) matrix[0][j] = j;
        
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (w1.charAt(i - 1) == w2.charAt(j - 1)) ? 0 : 1;
                matrix[i][j] = Math.min(Math.min(matrix[i-1][j] + 1, matrix[i][j-1] + 1), matrix[i-1][j-1] + cost);
            }
        }
        return matrix[len1][len2];
    }

    public ClosestWordsDynamic(String w, List<String> wordList) {
        for (String s : wordList) {
            int dist = distance(w, s);
            if (dist < closestDistance || closestDistance == -1) {
                closestDistance = dist;
                closestWords = new LinkedList<String>();
                closestWords.add(s);
            } else if (dist == closestDistance) {
                closestWords.add(s);
            }
        }
    }

    int getMinDistance() {
        return closestDistance;
    }

    List<String> getClosestWords() {
        return closestWords;
    }
}
