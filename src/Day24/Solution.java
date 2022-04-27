package Day24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Solution {
    private int nOfLines = 252;
    private int[] array = new int[nOfLines];

    public static void main(String[] args) {
        Solution part = new Solution();
        part.setup();
        part.solution();
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
    public void solution() {
        int z = 0;
        int n = nOfLines - 1;
        int mod = 26;
        List<Monad> options = new ArrayList<>();

        int w = 9;
        int a = array[n - 13];
        int b = array[n - 12];
        int c = array[n - 2];
        while(w > 0) {
            for (int i = mod - 1; i > 0; i--) {
                int t = ((i % 26 + b) != w) ? 1 : 0;
                if ((i / a * (25 * (t + 1)) + t * (w + c)) == z) {
                    options.add(new Monad(String.valueOf(w), i));
                }
            }
            w--;
        }

        while(options.get(0).s.length() < 14) {
            Monad m = options.remove(0);
            if(m.s.length() < 3) {
                System.out.println(m.s.length() + ": " + m.s + ", " + m.z);
            }
            a = array[n - 13 - 18 * m.s.length()];
            b = array[n - 12 - 18 * m.s.length()];
            c = array[n - 2 - 18 * m.s.length()];
            z = m.z;
            w = 9;
            while(w > 0) {
                for (int i = mod - 1; i >= 0; i--) {
                    int t = ((i % mod + b) != w) ? 1 : 0;
                    if ((i / a * (25 * (t + 1)) + t * (w + c)) == z) {
                        options.add(new Monad(w+m.s, i));
                    }
                }
                w--;
            }
        }

        int monad = 0;
        for(Monad m : options) {
            monad = Math.max(monad, Integer.parseInt(m.s));
        }
        System.out.println(monad);
    }

    private class Monad {
        String s;
        int z;

        public Monad(String s, int z) {
            this.s = s;
            this.z = z;
        }
    }
}
