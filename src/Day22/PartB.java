package Day22;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PartB {
    private final int nOfLines = 420;
    private int regionX = 0;
    private int shiftX = 0;
    private int regionY = 0;
    private int shiftY = 0;
    private int regionZ = 0;
    private int shiftZ = 0;
    private String[] array = new String[nOfLines];
    private boolean[][][] cubes;

    public static void main(String[] args) {
        PartB part = new PartB();
        part.setup();
        part.solution();
    }

    public void setup() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/Day22/input.txt"));
            int minX = 0;
            int minY = 0;
            int minZ = 0;
            int maxX = 0;
            int maxY = 0;
            int maxZ = 0;
            for (int i = 0; i < nOfLines; i++) {
                array[i] = reader.readLine();
                /*
                [1] => min x
                [3] => max x
                [5] => min y
                [7] => max y
                [9] => min z
                [11] => max z
             */
                String[] split = array[i].split("[,=.]");
                if (Integer.parseInt(split[1]) < minX) {
                    minX = Integer.parseInt(split[1]);
                } else if (Integer.parseInt(split[3]) > maxX) {
                    maxX = Integer.parseInt(split[3]);
                }
                if (Integer.parseInt(split[5]) < minY) {
                    minY = Integer.parseInt(split[5]);
                } else if (Integer.parseInt(split[7]) > maxY) {
                    maxY = Integer.parseInt(split[7]);
                }
                if (Integer.parseInt(split[9]) < minZ) {
                    minZ = Integer.parseInt(split[9]);
                } else if (Integer.parseInt(split[11]) > maxZ) {
                    maxZ = Integer.parseInt(split[11]);
                }
            }
            reader.close();
            shiftX = -minX;
            regionX = shiftX + maxX + 1;
            shiftY = -minY;
            regionY = shiftY + maxY + 1;
            shiftZ = -minZ;
            regionZ = shiftZ + maxZ + 1;
            cubes = new boolean[regionX][regionY][regionZ];
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void solution() {
        for (int i = 0; i < nOfLines; i++) {
            /*
                [1] => min x
                [3] => max x
                [5] => min y
                [7] => max y
                [9] => min z
                [11] => max z
             */
            String[] split = array[i].split("[,=.]");
            for (int x = Integer.parseInt(split[1]); x <= Integer.parseInt(split[3]); x++) {
                for (int y = Integer.parseInt(split[5]); y <= Integer.parseInt(split[7]); y++) {
                    for (int z = Integer.parseInt(split[9]); z <= Integer.parseInt(split[11]); z++) {
                        cubes[x + shiftX][y + shiftY][z + shiftZ] = split[0].contains("on");
                    }
                }
            }

        }

        int counter = 0;
        for (int x = 0; x < regionX; x++) {
            for (int y = 0; y < regionY; y++) {
                for (int z = 0; z < regionZ; z++) {
                    if (cubes[x][y][z]) {
                        counter++;
                    }
                }
            }
        }

        System.out.println(counter);
    }
}
