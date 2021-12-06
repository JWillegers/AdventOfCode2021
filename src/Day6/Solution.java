package Day6;
import java.util.HashMap;
import java.util.Map;

public class Solution {

    public static void main(String[] args) {
        Solution part = new Solution();
        part.solution();
    }


    public void solution() {
        //String input = "3,4,3,1,2"; //example
        String input = "5,1,5,3,2,2,3,1,1,4,2,4,1,2,1,4,1,1,5,3,5,1,5,3,1,2,4,4,1,1,3,1,1,3,1,1,5,1,5,4,5,4,5,1,3,2,4,3,5,3,5,4,3,1,4,3,1,1,1,4,5,1,1,1,2,1,2,1,1,4,1,4,1,1,3,3,2,2,4,2,1,1,5,3,1,3,1,1,4,3,3,3,1,5,2,3,1,3,1,5,2,2,1,2,1,1,1,3,4,1,1,1,5,4,1,1,1,4,4,2,1,5,4,3,1,2,5,1,1,1,1,2,1,5,5,1,1,1,1,3,1,4,1,3,1,5,1,1,1,5,5,1,4,5,4,5,4,3,3,1,3,1,1,5,5,5,5,1,2,5,4,1,1,1,2,2,1,3,1,1,2,4,2,2,2,1,1,2,2,1,5,2,1,1,2,1,3,1,3,2,2,4,3,1,2,4,5,2,1,4,5,4,2,1,1,1,5,4,1,1,4,1,4,3,1,2,5,2,4,1,1,5,1,5,4,1,1,4,1,1,5,5,1,5,4,2,5,2,5,4,1,1,4,1,2,4,1,2,2,2,1,1,1,5,5,1,2,5,1,3,4,1,1,1,1,5,3,4,1,1,2,1,1,3,5,5,2,3,5,1,1,1,5,4,3,4,2,2,1,3";
        Map<Integer, Long> fishTimers = new HashMap<>();

        //setup
        long one = 0;
        long two = 0;
        long three = 0;
        long four = 0;
        long five = 0;
        String[] split = input.split(",");
        for (int i = 0; i < split.length; i++) {
            switch (split[i]){
                case "1": one++; break;
                case "2": two++; break;
                case "3": three++; break;
                case "4": four++; break;
                case "5": five++; break;
                default: System.out.println("You need to add this number" + split[i]); System.exit(1);
            }
        }
        fishTimers.put(1, one);
        fishTimers.put(2, two);
        fishTimers.put(3, three);
        fishTimers.put(4, four);
        fishTimers.put(5, five);

        for (int t = 1; t <= 256; t++) {
            long[] numbers = new long[9];
            for (int key : fishTimers.keySet()) {
                numbers[key] = fishTimers.get(key);
            }
            fishTimers.clear();
            for (int i = 0; i < numbers.length; i++) {
                if (i == 0) {
                    fishTimers.put(6, numbers[i] + numbers[7]);
                    fishTimers.put(8, numbers[i]);
                } else if (i != 7) {
                    fishTimers.put(i - 1, numbers[i]);
                }
            }
            if (t == 80) {
                int sum = 0;
                for (int key : fishTimers.keySet()) {
                    sum += fishTimers.get(key);
                }
                System.out.println("Solution part 1: " + sum);
            }
        }

        long sum = 0;
        for (int key : fishTimers.keySet()) {
            sum += fishTimers.get(key);
        }
        System.out.println("Solution part 2: " + sum);


    }
}