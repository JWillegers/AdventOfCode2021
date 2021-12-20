package Day19;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Solution {
    private int nOfScanners = 30;
    private Scanner[] array = new Scanner[nOfScanners];

    public static void main(String[] args) {
        Solution part = new Solution();
        part.setup();
        part.solution();
    }

    public void setup() {
        try {
            int nOfLines = 832;
            BufferedReader reader = new BufferedReader(new FileReader("src/Day19/input.txt"));
            List<Beacon> listOfBeacons = new ArrayList<>();
            int i = 0; //scanner
            int j = 0; //lines
            while (j <= nOfLines) {
                String line = "";
                if (j < nOfLines) {
                    line = reader.readLine();
                }
                if (j == nOfLines || line.isEmpty()) {
                    List<List<Beacon>> listOfListOfBeacons = new ArrayList<>();

                    //flipped axis
                    for (int a = -1; a <= 1; a+=2) {
                        for (int b = -1; b <= 1; b+=2) {
                            for (int c = -1; c <= 1; c+=2) {
                                List<Beacon> toAdd = new ArrayList<>();
                                for (Beacon beacon : listOfBeacons) {
                                    Beacon newBeacon = new Beacon(a * beacon.x,b * beacon.y, c * beacon.z);
                                    toAdd.add(newBeacon);
                                }
                                listOfListOfBeacons.add(toAdd);
                            }
                        }
                    }

                    //rotate 2 axis



                    Scanner scanner = new Scanner();
                    scanner.list = listOfListOfBeacons;
                    array[i] = scanner;
                    i++;
                    listOfBeacons.clear();
                } else if (!line.contains("scanner")) {
                    String[] split = line.split(",");
                    Beacon beacon = new Beacon(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                    listOfBeacons.add(beacon);
                }
                j++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void solution() {


        System.out.println();
    }
}
