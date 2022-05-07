package Day22;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PartB {
    private final int nOfLines = 420;
    private List<RebootStep> input;

    public static void main(String[] args) {
        PartB part = new PartB();
        part.setup();
        part.solution();
    }

    public void setup() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/Day22/input.txt"));
            input = new ArrayList<>();
            for (int i = 0; i < nOfLines; i++) {
                String line = reader.readLine();
                String[] split = line.split("[,=.]");
                /*
                        [0] => "on x" or "off x"
                        [1] => min x
                        [3] => max x
                        [5] => min y
                        [7] => max y
                        [9] => min z
                        [11] => max z
                */
                RebootStep r = new RebootStep();
                r.on = split[0].contains("on");
                for(int x = 1; x < 4; x+= 2) {
                    for(int y = 5; y < 8; y += 2) {
                        for(int z = 9; z < 10; z+= 2) {
                            Cord c = new Cord(Integer.parseInt(split[x]), Integer.parseInt(split[y]), Integer.parseInt(split[z]));
                            r.originalCorners.add(c);
                            r.adjustedCorners.add(c);
                        }
                    }
                }
                input.add(r);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void solution() {
        long count = 0;
        for (int i = nOfLines - 2; i >= 0; i--) {
            for (int j = i + 1; j < nOfLines; j++) {
                for(Cord c : input.get(j).originalCorners) {

                }
            }
        }



        System.out.println(count);
    }
}
