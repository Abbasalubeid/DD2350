import java.util.List;
import java.util.LinkedList;

public class ClosestWordsRecursive {
    LinkedList<String> closestWords = null;
    int closestDistance = -1;

    int partDist(String w1, String w2, int w1len, int w2len) {
        if (w1len == 0) return w2len;
        if (w2len == 0) return w1len;
        
        int res = partDist(w1, w2, w1len - 1, w2len - 1) + (w1.charAt(w1len - 1) == w2.charAt(w2len - 1) ? 0 : 1);
        int addLetter = partDist(w1, w2, w1len - 1, w2len) + 1;
        int deleteLetter = partDist(w1, w2, w1len, w2len - 1) + 1;
        
        return Math.min(res, Math.min(addLetter, deleteLetter));
    }

    int distance(String w1, String w2) {
        return partDist(w1, w2, w1.length(), w2.length());
    }

    public ClosestWordsRecursive(String w, List<String> wordList) {
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
