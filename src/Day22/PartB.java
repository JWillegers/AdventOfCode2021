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
     * Try to find a cube from checkedCubes which has an overlap with cube
     * @param corners
     * @param cube
     */
    public void checkIfCornersAreInsideOtherCubes(List<Cord> corners, Cube cube) {
        for (int i = 0; i < checkedCubes.size(); i++) {
            Cord min = checkedCubes.get(i).min;
            Cord max = checkedCubes.get(i).max;
            for (Cord c : corners) {
                if (c.x >= min.x && c.x <= max.x && c.y >= min.y && c.y <= max.y && c.z >= min.z && c.z <= max.z) {
                    cube.cordsToBeRemoved.add(c);
                }
            }
            if (!cube.cordsToBeRemoved.isEmpty()) {
                findIntersectionCords(cube, min, max, corners);
                return;
            }
        }
    }

    /**
     * Find the intersectionpoints between the 2 cubes found in findIntersectionCords
     * @param cube
     * @param min
     * @param max
     * @param corners
     */
    public void findIntersectionCords(Cube cube, Cord min, Cord max, List<Cord> corners) {
        for (Cord ctbr : cube.cordsToBeRemoved) {
            for (int i = 0; i < corners.size(); i++) {
                Cord cord = corners.get(i);
                if (!cube.cordsToBeRemoved.contains(cord)) {
                    if (ctbr.x == cord.x && ctbr.y == cord.y) {
                        if (cord.z < ctbr.z) {
                            cube.intersectionCords.add(new Cord(ctbr.x, ctbr.y, min.z));
                        } else {
                            cube.intersectionCords.add(new Cord(ctbr.x, ctbr.y, max.z));
                        }
                    } else if (ctbr.x == cord.x && ctbr.z == cord.z) {
                        if (cord.y < ctbr.y) {
                            cube.intersectionCords.add(new Cord(ctbr.x, min.y, ctbr.z));
                        } else {
                            cube.intersectionCords.add(new Cord(ctbr.x, max.y, ctbr.z));
                        }
                    } else if (ctbr.z == cord.z && ctbr.y == cord.y) {
                        if (cord.x < ctbr.x) {
                            cube.intersectionCords.add(new Cord(min.x, ctbr.y, ctbr.z));
                        } else {
                            cube.intersectionCords.add(new Cord(max.x, ctbr.y, ctbr.z));
                        }
                    }
                }
            }
        }
    }



    public void processCube(Cube cube) {
        List<Cord> listOfCorners = findAllCorners(cube);
        checkIfCornersAreInsideOtherCubes(listOfCorners, cube);
        if (cube.cordsToBeRemoved.isEmpty()) {
            checkedCubes.add(cube);
            if (cube.on) {
                volume += calculateVolume(cube);
            }
        } else {
            switch (cube.cordsToBeRemoved.size()) {
                case 1:
                    break;
                case 2:
                    break;
                case 4:
                    break;
                case 8:
                    return;
                default:
                    System.out.println("ERROR: unexpected amount (" + cube.cordsToBeRemoved.size() + ") of corners need to be removed");
                    System.exit(1);
            }
        }
    }
}