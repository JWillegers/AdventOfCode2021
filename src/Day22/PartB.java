package Day22;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PartB {
    private final int nOfLines = 420;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private int minZ;
    private int maxZ;
    private final int step = 1000;
    private int[][] array = new int[nOfLines][7];
    private boolean[][][] cubes;

    public static void main(String[] args) {
        PartB part = new PartB();
        part.setup();
        part.solution();
    }

    public void setup() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/Day22/input.txt"));
            minX = 0;
            maxX = 0;
            minY = 0;
            maxY = 0;
            minZ = 0;
            maxZ = 0;
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
                array[i][0] = split[0].contains("on") ? 0 : 1;
                array[i][1] = Integer.parseInt(split[1]);
                array[i][2] = Integer.parseInt(split[3]);
                array[i][3] = Integer.parseInt(split[5]);
                array[i][4] = Integer.parseInt(split[7]);
                array[i][5] = Integer.parseInt(split[9]);
                array[i][6] = Integer.parseInt(split[11]);
                if (array[i][1] < minX) {
                    minX = array[i][1];
                } else if (array[i][2] > maxX) {
                    maxX = array[i][2];
                }
                if (array[i][3] < minY) {
                    minY = array[i][3];
                } else if (array[i][4] > maxY) {
                    maxY = array[i][4];
                }
                if (array[i][5] < minZ) {
                    minZ = array[i][5];
                } else if (array[i][6] > maxZ) {
                    maxZ = array[i][6];
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void solution() {
        long counter = 0;
        cubes = new boolean[step][step][step];
        for (int direction1 = minX; direction1 <= maxX; direction1+= step) {
            for (int direction2 = minY; direction2 <= maxY; direction2 += step) {
                System.out.println("x, y: " + direction1 + ", " + direction2 + "; count: " + counter);
                for (int direction3 = minZ; direction3 <= maxZ; direction3 += step) {
                    boolean tryCount = false;
                    for (int i = 0; i < nOfLines; i++) {
                        if (array[i][0] == 1 || (array[i][0] == 0 && tryCount)) { //line start with "on", or line start with "off" and there are some lights on in the section
                            for (int x = Math.max(array[i][1], direction1); x < Math.min(array[i][2], direction1 + step); x++) {
                                for (int y = Math.max(array[i][3], direction2); y < Math.min(array[i][4], direction2 + step); y++) {
                                    for (int z = Math.max(array[i][5], direction3); z < Math.min(array[i][6], direction3 + step); z++) {
                                        cubes[x - direction1][y - direction2][z - direction3] = array[i][0] == 1;
                                        tryCount = true;
                                    }
                                }
                            }
                        }
                    }
                    if (tryCount) {
                        for (int x = 0; x < step; x++) {
                            for (int y = 0; y < step; y++) {
                                for (int z = 0; z < step; z++) {
                                    if (cubes[x][y][z]) {
                                        counter++;
                                        cubes[x][y][z] = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println(counter);
    }
}
