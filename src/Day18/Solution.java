package Day18;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//Big thanks to u/Pun-Master-General and u/Dataforce in this reddit thread https://www.reddit.com/r/adventofcode/comments/rj1p92/2021_day_18_part_1_if_i_encounter_a_pair_that/
public class Solution {
    private int nOfLines = 10;
    private String[] array = new String[nOfLines];

    public static void main(String[] args) {
        Solution part = new Solution();

        //testing
        System.out.println("[[[[[9,8],1],2],3],4] -> " + part.explode("[[[[[9,8],1],2],3],4]", 4));
        System.out.println("[7,[6,[5,[4,[3,2]]]]] -> " + part.explode("[7,[6,[5,[4,[3,2]]]]]", 12));
        System.out.println("[[6,[5,[4,[3,2]]]],1] -> " + part.explode("[[6,[5,[4,[3,2]]]],1]", 10));
        System.out.println("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]] -> " + part.explode("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]", 10));
        System.out.println("-----------------------------------------\n");
        part.solution("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]", 9);
        System.out.println("-----------------------------------------\n");
        part.setup();
        part.solution(part.array[0], 0);
    }

    public void setup() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/Day18/test.txt"));
            for (int i = 0; i < nOfLines; i++) {
                array[i] = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void solution(String line, int lineNumber) {
        boolean run;
        do {
            int bracketCounter = 0;
            boolean somethingHappend = false;
            for (int j = 0; j < line.length(); j++) {
                char symbol = line.charAt(j);
                if (symbol == '[') {
                    bracketCounter++;
                } else if (symbol == ']') {
                    bracketCounter--;
                }

                if (bracketCounter == 5) {
                    int k = j + 1;
                    boolean innerrun = true;
                    while (innerrun) {
                        symbol = line.charAt(k);
                        if (symbol == '[') {
                            line = explode(line, k);
                            System.out.println("after explode: " + line);
                            innerrun = false;
                        } else if (symbol == ']') {
                            line = explode(line, j);
                            System.out.println("after explode: " + line);
                            innerrun = false;
                        } else {
                            k++;
                        }
                    }
                    somethingHappend = true;
                    break;
                }

            }
            if (!somethingHappend) {
                for (int j = 0; j < line.length(); j++) {
                    if (Character.isDigit(line.charAt(j)) && Character.isDigit(line.charAt(j + 1))) {
                        line = split(line, j);
                        somethingHappend = true;
                        break;
                    }
                }
                System.out.println("after split: " + line);
            }
            run = somethingHappend;

        } while (run);

        if (lineNumber + 1 != nOfLines) {
            String newLine = "[" + line + "," + array[lineNumber + 1] + "]";
            System.out.println("\nNEW ADDITION: " + newLine);
            solution(newLine,lineNumber + 1);
            if (lineNumber == 1) {
            }
        } else {
            System.out.println(line);
        }


    }

    public String split(String line, int pos) {
        String newLine = line.substring(0, pos) + "[";
        double number = Double.parseDouble(String.valueOf(line.charAt(pos)) + line.charAt(pos + 1));
        newLine += (int) Math.floor(number / 2) + "," + (int) Math.ceil(number / 2);
        newLine += "]" + line.substring(pos + 2);
        return newLine;
    }

    public String explode(String line, int pos) {
        boolean doubleDigit = Character.isDigit(line.charAt(pos + 2));
        int pos2;
        if (doubleDigit) {
            pos2 = pos + 1;
        } else {
            pos2 = pos;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.exit(1);
        }
        int i = pos;
        boolean run = true;
        while (run && i >= 0) {
            if (Character.isDigit(line.charAt(i))) {
                run = false;
            } else {
                i--;
            }
        }

        int j;
        //jump to closing bracket
        if (doubleDigit) {
            j = pos + 5;
        } else {
            j = pos + 4;
        }
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
            int number1;
            int number2;
            if (Character.isDigit(line.charAt(i + 1))) {
                number1 = Integer.parseInt(String.valueOf(line.charAt(i)) + line.charAt(i + 1));
            } else {
                number1 = Integer.parseInt(String.valueOf(line.charAt(i)));
            }

            if (doubleDigit) {
                number2 = Integer.parseInt(String.valueOf(line.charAt(pos + 1)) + line.charAt(pos + 2));
            } else {
                number2 = Integer.parseInt(String.valueOf(line.charAt(pos + 1)));
            }
            newLine += (number1 + number2);
            if (line.charAt(pos - 1) == ',') {
                newLine += ",0]";
            } else {
                newLine += line.substring(i + 1, pos - 1);
                newLine += "[0,";
            }
        } else {
            newLine = line.substring(0, pos);
            newLine += "0,";
        }

        if (pos + 6 < j) {
            newLine += line.substring(pos + 6, j);
        }

        if (j + 1 < line.length()) {
            int number1;
            int number2;
            if (Character.isDigit(line.charAt(j + 1))) {
                number1 = Integer.parseInt(String.valueOf(line.charAt(j)) + line.charAt(j + 1));
                System.out.println("number1: " + number1);
            } else {
                number1 = Integer.parseInt(String.valueOf(line.charAt(j)));
            }

            if (Character.isDigit(line.charAt(pos2 + 4))) {
                number2 = Integer.parseInt(String.valueOf(line.charAt(pos2 + 3)) + line.charAt(pos2 + 4));
                System.out.println("number2: " + number2);
            } else {
                number2 = Integer.parseInt(String.valueOf(line.charAt(pos2 + 3)));
            }
            newLine += (number1 + number2);
            newLine += line.substring(j + 1);
        }

        return newLine;
    }
}
