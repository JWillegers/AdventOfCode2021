package Day22;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PartB {
    private final int nOfLines = 420;
    private List<Cube> allCubes;
    private List<Cube> checkedCubes;
    private long volume;

    public static void main(String[] args) {
        PartB part = new PartB();
        part.setup();
        part.solution();
    }

    /**
    Read all lines from the input file and put all information in allCubes
     */
    public void setup() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/Day22/input.txt"));
            allCubes = new ArrayList<>();
            checkedCubes = new ArrayList<>();
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
                allCubes.add(new Cube(Integer.parseInt(split[1]), Integer.parseInt(split[5]), Integer.parseInt(split[9]),
                        Integer.parseInt(split[3]), Integer.parseInt(split[7]), Integer.parseInt(split[11]), split[0].contains("on")));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (NumberFormatException n) {
            n.printStackTrace();
            System.exit(2);
        }
    }

    /**
     * Main method for calculating the solution, from last cube in the list to the first one.
     * Since the last one is (one of the) largest
     */
    public void solution() {
        /*
         * Check the last cube
         */
        Cube currentCube = allCubes.get(allCubes.size() - 1);
        if (currentCube.on) {
            volume = calculateVolume(currentCube);
        } else {
            volume = 0;
        }
        /*
         * Loop through all other cubes
         */
        for (int i = allCubes.size() - 2; i >= 0; i--) {
            processCube(allCubes.get(i));
        }
        System.out.println("Answer: " + volume);
    }

    /**
     * Calculate volume of cube c
     * @param c
     * @return volume of a cube
     */
    public long calculateVolume(Cube c) {
        return (long) Math.abs(c.max.x - c.min.x) * Math.abs(c.max.y - c.min.y) * Math.abs(c.max.z - c.min.z);
    }

    /**
     * Find all the corners of cube c, given min and max
     * @param c
     * @return
     */
    public List<Cord> findAllCorners(Cube c) {
        List<Cord> returnList = new ArrayList<>();
        returnList.add(c.min);
        returnList.add(c.max);
        returnList.add(new Cord(c.min.x, c.min.y, c.max.z));
        returnList.add(new Cord(c.min.x, c.max.y, c.max.z));
        returnList.add(new Cord(c.max.x, c.min.y, c.max.z));
        returnList.add(new Cord(c.max.x, c.min.y, c.min.z));
        returnList.add(new Cord(c.max.x, c.max.y, c.min.z));
        returnList.add(new Cord(c.min.x, c.max.y, c.min.z));
        return returnList;
    }

    /**
     * Check which corners from param are inside a cube from checkedCubes
     * @param corners
     * @return
     */
    public List<Cord> checkIfCornersAreInsideOtherCubes(List<Cord> corners) {
        List<Cord> insideOtherCubes = new ArrayList<>();
        for (Cord c : corners) {
            for (int i = 0; i < checkedCubes.size(); i++) {
                Cord min = checkedCubes.get(i).min;
                Cord max = checkedCubes.get(i).max;
                if (c.x >= min.x && c.x <= max.x && c.y >= min.y && c.y <= max.y && c.z >= min.z && c.z <= max.z) {
                    insideOtherCubes.add(c);
                    break;
                }
            }
        }
        return insideOtherCubes;
    }

    public void processCube(Cube cube) {
        List<Cord> listOfCorners = findAllCorners(cube);
        List<Cord> cornersInCubes = checkIfCornersAreInsideOtherCubes(listOfCorners);
        if (cornersInCubes.isEmpty()) {
            checkedCubes.add(cube);
            if (cube.on) {
                volume += calculateVolume(cube);
            }
        } else {
            switch (cornersInCubes.size()) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    /*
                     * if cornersInCubes.size() == 8, its fully inside checked cubes, so it should be already fully counted for (WARNING: there might be cases that this is false)
                     */
                    break;
                default:
                    System.out.println("cornersInCubes.size() > 8");
                    System.exit(808);
            }
        }
    }
}