package Day15;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Solution {
    private int nOfLines = 100;
    private String[] array = new String[nOfLines];
    private final String polymer = "HHKONSOSONSVOFCSCNBC";
    private Map<String, Integer> occurence = new HashMap<>();
    //private final String polymer = "NNCB";

    public static void main(String[] args) {
        Solution part = new Solution();
        part.setup();
        part.solution(part.polymer, 1);
    }

    public void setup() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/Day15/input.txt"));
            for (int i = 0; i < nOfLines; i++) {
                array[i] = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void solution(String input, int counter) {
        String newLine = "";
        for (int i = 0; i < input.length() - 1; i++) {
            String check = input.charAt(i) + String.valueOf(input.charAt(i + 1));
            String letter = "";
            for (int j = 0; j < nOfLines; j++) {
                if (array[j].contains(check)) {
                    letter = array[j].split(" -> ")[1];
                }
            }
            newLine += input.charAt(i);
            if (!letter.equals("")) {
                newLine += letter;
            }

            if (i == input.length() - 2) {
                newLine += input.charAt(i + 1);
            }
        }
        if (counter == 10 || counter == 40) {
            Map<Character, Integer> frequency = new HashMap<>();
            for (char letter = 'A'; letter <= 'Z'; letter++) {
                int frequencyCounter = 0;
                for (int j = 0; j < newLine.length(); j++) {
                    if (letter == newLine.charAt(j)) {
                        frequencyCounter++;
                    }
                }
                System.out.println(letter + ": " + frequencyCounter);
                frequency.put(letter, frequencyCounter);
            }

            int mostCommon = -1;
            int leastCommon = -1;
            for (char key : frequency.keySet()) {
                int number = frequency.get(key);
                if (number != 0) {
                    if (mostCommon == -1) {
                        mostCommon = number;
                        leastCommon = number;
                    } else if (number > mostCommon) {
                        mostCommon = number;
                    } else if (number < leastCommon) {
                        leastCommon = number;
                    }
                }
            }

            System.out.println(mostCommon - leastCommon);
            /*if (counter == 10) {
                solution(newLine, 11);
            }*/
        } else {
            solution(newLine, counter + 1);
        }
    }
}
