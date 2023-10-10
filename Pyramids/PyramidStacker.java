import java.util.Arrays;

class Pyramid implements Comparable<Pyramid> {
    int s; // side length
    int h; // height

    public Pyramid(int s, int h) {
        this.s = s;
        this.h = h;
    }

    public boolean canFitInside(Pyramid other) {
        return this.s < other.s && this.h < other.h;
    }

    @Override
    public String toString() {
        return "Pyramid{s=" + s + ", h=" + h + "}";
    }

    @Override
    public int compareTo(Pyramid other) {
        if (this.s != other.s) {
            return this.s - other.s;
        }
        return this.h - other.h;
    }
}

public class PyramidStacker {

    public static int maxStackedPyramids(Pyramid[] pyramids) {
        int n = pyramids.length;
        int[] memo = new int[n];
    
        Arrays.fill(memo, 1);
    
        // Sort pyramids based on s and then h.
        Arrays.sort(pyramids);
    
        System.out.println("Sorted Pyramids: " + Arrays.toString(pyramids));
    
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (pyramids[j].canFitInside(pyramids[i])) {
                    memo[i] = Math.max(memo[i], memo[j] + 1);
                }
            }
        }
    
        System.out.println("memo Results: " + Arrays.toString(memo));
    
        int maxVal = memo[0];
        for (int i = 1; i < n; i++) {
            if (memo[i] > maxVal) {
                maxVal = memo[i];
            }
        }
    
        return maxVal;
    }
    

    public static void main(String[] args) {
        Pyramid[] pyramids = {
            new Pyramid(2, 3),
            new Pyramid(3, 5),
            new Pyramid(1, 1),
            new Pyramid(4, 6),
            new Pyramid(5, 2)
        };

        System.out.println("Input Pyramids: " + Arrays.toString(pyramids));
        int result = maxStackedPyramids(pyramids);
        System.out.println("Maximum number of pyramids that can be stacked: " + result);
    }
}
