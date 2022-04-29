package Day24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Solution {
    private int nOfLines = 252;
    private int[] array = new int[nOfLines];
    private List<String> monad;

    public static void main(String[] args) {
        Solution part = new Solution();
        part.setup();
    }

    public void setup() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/Day24/input.txt"));
            for (int i = 0; i < nOfLines; i++) {
                String[] line = reader.readLine().split(" ");
                if(line.length > 2) {
                    try {
                        array[i] = Integer.parseInt(line[2]);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }
            reader.close();
            monad = new ArrayList<>();
            solution("", 0, nOfLines - 1);
            int higestMonad = 0;
            for(String s : monad) {
                higestMonad = Math.max(higestMonad, Integer.parseInt(s));
            }
            System.out.println(higestMonad);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    /*
    This part (18 lines) is repeated 14 times:
    inp w
    mul x 0
    add x z
    mod x 26
    div z {a}
    add x {b}
    eql x w
    eql x 0
    mul y 0
    add y 25
    mul y x
    add y 1
    mul z y
    mul y 0
    add y w
    add y {c}
    mul y x
    add z y

    w is a digit in the input (the monad number we are testing)

    From where we can find:
    x = (z%26+{b}) != w
    y = 25*x+1
    z = z/{a}
    z = z * y
    y = (w + {c}) * x
    z = z + y

    becomes:
    z[now] = z[previous]/{a} * (25*((z[previous]%26+{b}) != w)+1) + (w + {c}) * (z[previous]%26+{b}) != w)

    Valid number if 0 is not in the number and when after running z=0

    {a} is located at line%18=4
    {b} is located at line%18=5
    {c} is located at line%18=15
     */
    public void solution(String s, int z, int n) {
        System.out.println(s.length() + ": " + s);
        int w = 9;
        int a = array[n - 13];
        int b = array[n - 12];
        int c = array[n - 2];
        while(w > 0) {
            for (int i = 25; i >= 0; i--) {
                int t = ((i % 26 + b) == w) ? 0 : 1;
                if (((i / a) * (25 * t + 1) + t * (w + c)) == z) {
                    if (s.length() == 13) {
                        monad.add(w + s);
                    } else {
                        solution(w + s, i, n - 18);
                    }
                }
            }

            w--;
        }
    }
}
