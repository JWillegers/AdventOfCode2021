package Day6;

import java.util.ArrayList;
import java.util.List;

public class Solution {

    public static void main(String[] args) {
        Solution part = new Solution();
        part.solution();
    }


    public void solution() {
        String input = "5,1,5,3,2,2,3,1,1,4,2,4,1,2,1,4,1,1,5,3,5,1,5,3,1,2,4,4,1,1,3,1,1,3,1,1,5,1,5,4,5,4,5,1,3,2,4,3,5,3,5,4,3,1,4,3,1,1,1,4,5,1,1,1,2,1,2,1,1,4,1,4,1,1,3,3,2,2,4,2,1,1,5,3,1,3,1,1,4,3,3,3,1,5,2,3,1,3,1,5,2,2,1,2,1,1,1,3,4,1,1,1,5,4,1,1,1,4,4,2,1,5,4,3,1,2,5,1,1,1,1,2,1,5,5,1,1,1,1,3,1,4,1,3,1,5,1,1,1,5,5,1,4,5,4,5,4,3,3,1,3,1,1,5,5,5,5,1,2,5,4,1,1,1,2,2,1,3,1,1,2,4,2,2,2,1,1,2,2,1,5,2,1,1,2,1,3,1,3,2,2,4,3,1,2,4,5,2,1,4,5,4,2,1,1,1,5,4,1,1,4,1,4,3,1,2,5,2,4,1,1,5,1,5,4,1,1,4,1,1,5,5,1,5,4,2,5,2,5,4,1,1,4,1,2,4,1,2,2,2,1,1,1,5,5,1,2,5,1,3,4,1,1,1,1,5,3,4,1,1,2,1,1,3,5,5,2,3,5,1,1,1,5,4,3,4,2,2,1,3";
        List<Integer> fishTimers = new ArrayList<>();
        String[] split = input.split(",");
        for (int i = 0; i < split.length; i++) {
            fishTimers.add(Integer.parseInt(split[i]));
        }

        for (int t = 1; t <= 256; t++) {
            System.out.println(t);
            int toAdd = 0;
            for (int i = 0; i < fishTimers.size(); i++) {
                if (fishTimers.get(i) == 0) {
                    fishTimers.set(i, 6);
                    toAdd++;
                } else {
                    int number = fishTimers.get(i);
                    fishTimers.set(i, number - 1);
                }
            }
            for (int i = 0; i < toAdd; i++) {
                fishTimers.add(8);
            }
            if (t == 80) {
                System.out.println("solution part1: " + fishTimers.size());
            }
        }

        System.out.println("solution part2: " + fishTimers.size());
    }
}
