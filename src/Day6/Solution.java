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
        Map<Integer, Long> fishTimersOne = new HashMap<>();
        Map<Integer, Long> fishTimersTwo = new HashMap<>();

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
        fishTimersOne.put(1, one);
        fishTimersOne.put(2, two);
        fishTimersOne.put(3, three);
        fishTimersOne.put(4, four);
        fishTimersOne.put(5, five);

        for (int t = 1; t <= 256; t++) {
            if (t % 2 == 0) { //two -> one
                fishTimersOne.clear();
                for (int key : fishTimersTwo.keySet()) {
                    if (key == 0) {
                        if (fishTimersTwo.containsKey(7)) {
                            long keySum = fishTimersTwo.get(key) + fishTimersTwo.get(7);
                            fishTimersOne.put(6, keySum);
                        } else {
                            fishTimersOne.put(6, fishTimersTwo.get(key));
                        }
                        fishTimersOne.put(8, fishTimersTwo.get(key));
                    } else if (key != 7) {
                        int newKey = key - 1;
                        fishTimersOne.put(newKey, fishTimersTwo.get(key));
                    } else if (!fishTimersTwo.containsKey(0)) { //key is always 7 when reached
                        fishTimersOne.put(6, fishTimersTwo.get(key));
                    }
                }
                if (t == 80) {
                    int sum = 0;
                    for (int key : fishTimersOne.keySet()) {
                        sum += fishTimersOne.get(key);
                    }
                    System.out.println("Solution part 1: " + sum);
                }
            } else { //one -> two
                fishTimersTwo.clear();
                for (int key : fishTimersOne.keySet()) {
                    if (key == 0) {
                        if (fishTimersOne.containsKey(7)) {
                            long keySum = fishTimersOne.get(key) + fishTimersOne.get(7);
                            fishTimersTwo.put(6, keySum);
                        } else {
                            fishTimersTwo.put(6, fishTimersOne.get(key));
                        }
                        fishTimersTwo.put(8, fishTimersOne.get(key));
                    } else if (key != 7){
                        int newKey = key - 1;
                        fishTimersTwo.put(newKey, fishTimersOne.get(key));
                    } else if (!fishTimersOne.containsKey(0)) {//key is always 7 when reached
                        fishTimersTwo.put(6, fishTimersOne.get(key));
                    }
                }
            }

        }

        long sum = 0;
        for (int key : fishTimersOne.keySet()) {
            sum += fishTimersOne.get(key);
        }
        System.out.println("Solution part 2: " + sum);


    }
}