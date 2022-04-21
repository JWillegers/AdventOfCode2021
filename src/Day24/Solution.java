package Day24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


//https://github.com/dphilipson/advent-of-code-2021/blob/master/src/days/day24.rs
public class Solution {
    private int nOfLines = 252;
    private String[] array = new String[nOfLines];

    public static void main(String[] args) {
        Solution part = new Solution();
        part.setup();
        part.solution();
    }

    public void setup() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/Day24/input.txt"));
            for (int i = 0; i < nOfLines; i++) {
                array[i] = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void solution() {
        HashMap<String, Long> variables = new HashMap<>();
        long monad = 9999999 + (long) 9999999 * 10000000;
        boolean run = true;
        while (run && monad >= 1111111 + (long) 1111111 * 10000000) {
            variables.put("w", (long) 0);
            variables.put("x", (long) 0);
            variables.put("y", (long) 0);
            variables.put("z", (long) 0);
            if (!String.valueOf(monad).contains("0")) {
                int pointer = 0;
                for (int i = 0; i < nOfLines; i++) {
                    String[] line = array[i].split(" ");
                    String register = line[1];
                    long value1= variables.get(register);
                    long value2 = -1;
                    try {
                        value2 += Integer.parseInt(line[2]);
                    } catch (NumberFormatException e) {
                        value2 += variables.get(line[2]);
                    } catch (IndexOutOfBoundsException ignore) {}
                    switch (line[0]) {
                        case "inp":
                            long number = Long.parseLong(String.valueOf(String.valueOf(monad).charAt(pointer)));
                            variables.put(register, number);
                            pointer++;
                            break;
                        case "add":
                            variables.put(register, value1 + value2);
                            break;
                        case "mul":
                            variables.put(register, value1 * value2);
                            break;
                        case "div":
                            if (value2 != 0) {
                                variables.put(register, (long) Math.floor((double) value1 / value2));
                            }
                            break;
                        case "mod":
                            if (value1 > 0 && value2 > 0) {
                                variables.put(register, value1 % value2);
                            }
                            break;
                        case "eql":
                            variables.put(register, value1 == value2 ? (long) 1 : (long) 0);
                            break;
                        default:
                            System.exit(1);
                    }
                }

                if (variables.get("z") == (long) 0) {
                    run = false;
                } else {
                    monad--;
                }
            } else {
                String number = String.valueOf(monad);
                String newNumber = "";
                boolean zeroFound = false;
                for (int i = 0; i < number.length(); i++) {
                    if (i == 0) {
                        newNumber += number.charAt(0);
                    } else if (zeroFound) {
                        newNumber += "9";
                    } else if (number.charAt(i) == '0') {
                        newNumber = newNumber.substring(0, newNumber.length() - 1);
                        newNumber += Integer.parseInt(String.valueOf(number.charAt(i - 1))) - 1;
                        newNumber += "9";
                        zeroFound = true;
                    } else {
                        newNumber += Integer.parseInt(String.valueOf(number.charAt(i)));
                    }
                }
                monad = Long.parseLong(newNumber);
            }
        }
        System.out.println(monad);
    }
}
