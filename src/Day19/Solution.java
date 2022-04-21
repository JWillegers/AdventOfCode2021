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

                    myScanners.add(scanner);
                } else if (line.contains("scanner")) {
                    scanner.calculateRelativePositions();
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
        System.out.println(myScanners.get(1).cord.x);
        System.out.println(myScanners.get(1).cord.y);
        System.out.println(myScanners.get(1).cord.z);
    }

    public void createMap() {
        Scanner scannerZero = myScanners.get(0);
        scannerZero.matched = true;
        scannerZero.cord.x = 0;
        scannerZero.cord.y = 0;
        scannerZero.cord.z = 0;

        Scanner scannerOne = myScanners.get(1);
        for (int i = 0; i < scannerZero.beacons.size(); i++) {
            Beacon b0 = scannerZero.beacons.get(i);
            List<Cord> r0 = b0.relativePositionsOtherBeacons;
            for(int j = 0; j < scannerOne.beacons.size(); j++) {
                Beacon b1 = scannerZero.beacons.get(j);
                List<Cord> r1 = b1.relativePositionsOtherBeacons;
                int matches = 0;
                for (Cord c0 : r0) {
                    for(Cord c1 : r1) {
                        if(c0.x==c1.x && c0.y==c1.y && c0.z==c1.z) {
                            matches++;
                            if (matches == 12) {
                                scannerOne.cord.x = scannerZero.cord.x + b0.cord.x + b1.cord.x;
                                scannerOne.cord.y = scannerZero.cord.y + b0.cord.y + b1.cord.y;
                                scannerOne.cord.z = scannerZero.cord.z + b0.cord.z + b1.cord.z;
                                return;
                            }
                        }
                    }
                }
            }
        }

    }
}
