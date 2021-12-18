package Day18;

import Day16.Packet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Solution {
    private int nOfLines = 10;
    private boolean somethingHappend;
    private boolean lastActionWasSplit;
    private String[] array = new String[nOfLines];

    public static void main(String[] args) {
        Solution part = new Solution();
/*
        //testing
        System.out.println("[[[[[9,8],1],2],3],4] -> " + part.explode("[[[[[9,8],1],2],3],4]", 4));
        System.out.println("[7,[6,[5,[4,[3,2]]]]] -> " + part.explode("[7,[6,[5,[4,[3,2]]]]]", 12));
        System.out.println("[[6,[5,[4,[3,2]]]],1] -> " + part.explode("[[6,[5,[4,[3,2]]]],1]", 10));
        System.out.println("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]] ->" + part.explode("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]", 10));
*/
        part.solution("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]", 9);
        System.out.println("-----------------------------------------");
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
        lastActionWasSplit = false;
        do {
            int bracketCounter = 0;
            somethingHappend = false;
            if (!lastActionWasSplit) {
                line = searchForSplit(line);
            }

            if (!somethingHappend) {
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
                                lastActionWasSplit = false;
                                innerrun = false;
                            } else if (symbol == ']') {
                                line = explode(line, j);
                                lastActionWasSplit = false;
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
                    line = searchForSplit(line);
                }
            }
            run = somethingHappend;
            System.out.println("TEST: " + line);
        } while (run);

        if (lineNumber + 1 != nOfLines) {
            String newLine = "[" + line + "," + array[lineNumber + 1] + "]";
            System.out.println("\nNEW ADDITION: " + newLine);
            solution(newLine,lineNumber + 1);
        } else {
            System.out.println(line);
        }


    }

    public String searchForSplit(String line) {
        for (int j = 0; j < line.length(); j++) {
            if (Character.isDigit(line.charAt(j)) && Character.isDigit(line.charAt(j + 1))) {
                line = split(line, j);
                somethingHappend = true;
                lastActionWasSplit = true;
                break;
            }
        }
        return line;
    }

    public String split(String line, int pos) {
        String newLine = line.substring(0, pos) + "[";
        double number = Double.parseDouble(String.valueOf(line.charAt(pos)) + line.charAt(pos + 1));
        newLine += (int) Math.floor(number / 2) + "," + (int) Math.ceil(number / 2);
        newLine += "]" + line.substring(pos + 2);
        return newLine;
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
                newLine += line.substring(i + 1, pos - 1);
                newLine += "[0,";
            }
        } else {
            newLine = line.substring(0, pos);
            newLine += "0,";
        }

        //System.out.println(line);
        //System.out.println(newLine);
        if (pos + 6 < j) {
            newLine += line.substring(pos + 6, j);
        }

        if (j + 1 < line.length()) {
            int number = Integer.parseInt(String.valueOf(line.charAt(j))) + Integer.parseInt(String.valueOf(line.charAt(pos+3)));
            newLine += number;
            newLine += line.substring(j + 1);
        }

        return newLine;
    }
}
