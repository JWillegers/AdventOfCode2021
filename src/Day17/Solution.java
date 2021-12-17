package Day17;

public class Solution {
    //puzzle input
    private final int maxX = 251;
    private final int minX = 192;
    private final int maxY = -89;
    private final int minY = -59;
    private final int startX = 0;
    private final int startY = 0;


    public static void main(String[] args) {
        Solution part = new Solution();
        part.solution();
    }


    public void solution() {
        int maxYreached = 0;
        for (int x = 0; x < maxX; x++) {
            for (int y = -100; y < 100; y++) {
                boolean run = true;
                int currentX = startX;
                int currentY = startY;
                int forward = x;
                int upward = y;
                int currentMaxY = 0;
                do {
                    currentX += forward;
                    currentY += upward;
                    if (currentY > currentMaxY) {
                        currentMaxY = currentY;
                    }
                    if ((currentX >= minX && currentY <= minY) || currentX >= maxX || currentY <= maxY) {
                        run = false;
                        if (currentX <= maxX && currentX >= minX && currentY >= maxY && currentY <= minY) {
                            if (currentMaxY > maxYreached) {
                                maxYreached = currentMaxY;
                            }
                        }
                    } else {
                        if (forward > 0) {
                            forward--;
                        }
                        upward--;
                    }
                } while (run);

            }
        }



        System.out.println(maxYreached);
    }
}
