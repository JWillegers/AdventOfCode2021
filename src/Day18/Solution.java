package Day18;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Solution {
    private int nOfLines = 100;
    private String[] array = new String[nOfLines];

    public static void main(String[] args) {
        Solution part = new Solution();
        System.exit(0);
        part.setup();
        part.solution(part.array[0]);
    }

    public void setup() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/Day18/input.txt"));
            for (int i = 0; i < nOfLines; i++) {
                array[i] = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void solution(String line) {
        boolean run = true;
        do {
            int bracketCounter = 0;
            for (int j = 0; j < line.length(); j++) {
                char symbol = line.charAt(j);
                if (symbol == '[') {
                    bracketCounter++;
                } else if (symbol == ']') {
                    bracketCounter--;
                }

                if (bracketCounter == 5) {
                    line = explode(line, j);
                    break;
                }
            }

        } while (run);

        //TODO addition of 2 lines

        System.out.println();
    }

    public String explode(String line, int pos) {
        int i = pos;
        boolean run = true;
        while (run && i >= 0) {
            if (Character.isDigit(line.charAt(i))) {
                run = false;
            } else {
                i--;
            }
        }

        int j = pos + 4; //jump to closing bracket
        run = true;
        while (run && j < line.length()) {
            if (Character.isDigit(line.charAt(j))) {
                run = false;
            } else {
                j++;
            }
        }
        String newLine = "";
        if (i > 0) {
            newLine = line.substring(0, i);
            int number = Integer.parseInt(String.valueOf(line.charAt(i))) + Integer.parseInt(String.valueOf(line.charAt(pos+1)));
            newLine += number;
            if (line.charAt(pos - 1) == ',') {
                newLine += ",0]";
            } else {
                newLine += ",[0,";
            }
        } else {
            newLine = line.substring(0, pos);
            newLine += "0,";
        }

        newLine += line.substring(pos + 6, j);

        if (j + 1 < line.length()) {
            int number = Integer.parseInt(String.valueOf(line.charAt(j))) + Integer.parseInt(String.valueOf(line.charAt(pos+3)));
            newLine += number;
            newLine += line.substring(j + 1);
        }

        return newLine;
    }
}
