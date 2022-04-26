package Day19;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Solution {
    private List<Scanner> myScanners;

    public static void main(String[] args) {
        Solution part = new Solution();
        part.setup();
        part.solution();
    }

    public void setup() {
        try {
            int nOfLines = 136;
            BufferedReader reader = new BufferedReader(new FileReader("src/Day19/example.txt"));
            int lineNumber = 0;
            myScanners = new ArrayList<>();
            Scanner scanner = new Scanner();
            while (lineNumber <= nOfLines) {
                String line = "";
                if (lineNumber < nOfLines) {
                    line = reader.readLine();
                }
                if (lineNumber == nOfLines || line.isEmpty()) {
                    scanner.calculateRelativePositions();
                    myScanners.add(scanner);
                } else if (line.contains("scanner")) {
                    scanner = new Scanner();
                }
                else {
                    String[] split = line.split(",");
                    scanner.beacons.add(new Beacon(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])));
                }
                lineNumber++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void solution() {
        createMap();

        for(int i = 0; i < 5; i++) {
            System.out.println("Scanner " + i + ": " + myScanners.get(i).cord.x + ", " + myScanners.get(i).cord.y + ", " + myScanners.get(i).cord.z);
        }
    }

    public void createMap() {
        Scanner scannerZero = myScanners.get(0);
        scannerZero.matched = true;
        scannerZero.cord.x = 0;
        scannerZero.cord.y = 0;
        scannerZero.cord.z = 0;

        boolean run = true;
        int counter0 = 0;
        int counter1 = 1;

        while (run) {
            Scanner s_a = myScanners.get(counter0);
            if(s_a.matched) {
                while (run && counter1 < myScanners.size()) {
                    boolean match = false;
                    Scanner s_b = myScanners.get(counter1);
                    int i = 0;
                    while (i < s_a.beacons.size()) {
                        if (!s_b.matched && counter0 != counter1) {
                            Beacon b0 = s_a.beacons.get(i);
                            List<Cord> r0 = b0.relativePositionsOtherBeacons;
                            int j = 0;
                            while (j < s_b.beacons.size() && !match) {
                                Beacon b1 = s_b.beacons.get(j);
                                List<Cord> r1 = b1.relativePositionsOtherBeacons;
                                for (int mx = -1; mx < 2; mx += 2) {
                                    for (int my = -1; my < 2; my += 2) {
                                        for (int mz = -1; mz < 2; mz += 2) {
                                            int face = 0;
                                            /*
                                            0: x,y,z
                                            1: x,z,y
                                            2: y,x,z
                                            3: y,z,x
                                            4: z,x,y
                                            5: z,y,x
                                             */
                                            while (face < 6 && !match) {
                                                int matches = 0;
                                                for (Cord c0 : r0) {
                                                    for (Cord c1 : r1) {
                                                        if (face == 0) {
                                                            if (c0.x == mx * c1.x && c0.y == my * c1.y && c0.z == mz * c1.z) {
                                                                matches++;
                                                            }
                                                        } else if (face == 1) {
                                                            if (c0.x == mx * c1.x && c0.z == my * c1.y && c0.y == mz * c1.z) {
                                                                matches++;
                                                            }
                                                        } else if (face == 2) {
                                                            if (c0.y == mx * c1.x && c0.x == my * c1.y && c0.z == mz * c1.z) {
                                                                matches++;
                                                            }
                                                        } else if (face == 3) {
                                                            if (c0.y == mx * c1.x && c0.z == my * c1.y && c0.x == mz * c1.z) {
                                                                matches++;
                                                            }
                                                        } else if (face == 4) {
                                                            if (c0.z == mx * c1.x && c0.x == my * c1.y && c0.y == mz * c1.z) {
                                                                matches++;
                                                            }
                                                        } else if (c0.z == mx * c1.x && c0.y == my * c1.y && c0.x == mz * c1.z) {
                                                            matches++;
                                                        }
                                                    }
                                                }
                                                if (matches >= 12) {
                                                    System.out.println(counter0 + ", " + counter1 + ", " + face);
                                                    switch (face) {
                                                        case 0:
                                                            s_b.cord.x = b0.cord.x - mx * b1.cord.x;
                                                            s_b.cord.y = b0.cord.y - my * b1.cord.y;
                                                            s_b.cord.z = b0.cord.z - mz * b1.cord.z;
                                                            break;
                                                        case 1:
                                                            s_b.cord.x = b0.cord.x - mx * b1.cord.x;
                                                            s_b.cord.y = b0.cord.y - mz * b1.cord.z;
                                                            s_b.cord.z = b0.cord.z - my * b1.cord.y;
                                                            break;
                                                        case 2:
                                                            s_b.cord.x = b0.cord.x - my * b1.cord.y;
                                                            s_b.cord.y = b0.cord.y - mx * b1.cord.x;
                                                            s_b.cord.z = b0.cord.z - mz * b1.cord.z;
                                                            break;
                                                        case 3:
                                                            s_b.cord.x = b0.cord.x - mz * b1.cord.z;
                                                            s_b.cord.y = b0.cord.y - mx * b1.cord.x;
                                                            s_b.cord.z = b0.cord.z - my * b1.cord.y;
                                                            break;
                                                        case 4:
                                                            s_b.cord.x = b0.cord.x - my * b1.cord.y;
                                                            s_b.cord.y = b0.cord.y - mz * b1.cord.z;
                                                            s_b.cord.z = b0.cord.z - mx * b1.cord.x;
                                                            break;
                                                        case 5:
                                                            s_b.cord.x = b0.cord.x - mz * b1.cord.z;
                                                            s_b.cord.y = b0.cord.y - my * b1.cord.y;
                                                            s_b.cord.z = b0.cord.z - mx * b1.cord.x;
                                                            break;
                                                        default:
                                                            System.exit(1);
                                                    }
                                                    s_b.alignBeacons(face, mx, my, mz);
                                                    s_b.matched = true;
                                                    match = true;
                                                }
                                                face++;
                                            }
                                        }
                                    }
                                }
                                j++;
                            }
                        } else {
                            i = s_a.beacons.size();
                        }
                        i++;
                    }
                    run = false;
                    for (int a = 1; a < myScanners.size(); a++) {
                        if (!myScanners.get(a).matched) {
                            run = true;
                            break;
                        }
                    }
                    counter1++;
                }
            }
            counter0++;
            counter0 = counter0 == myScanners.size() ? 0 : counter0;
            counter1 = 1;
        }
    }
}
