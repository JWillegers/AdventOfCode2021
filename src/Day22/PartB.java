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

    public static void main(String[] args) throws IncorrectSizeException, XORException {
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
    public void solution() throws IncorrectSizeException, XORException {
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
        //In between answer: 644660943211971 [base]
        //In between answer: 656724885359221 [4 completed]
        //In between answer: 698287205358124 [4 completed and 2 xLine completed]

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

    public void processCube(Cube cube) throws IncorrectSizeException, XORException {
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
                    caseTwo(cube, listOfCorners);
                    break;
                case 4:
                    caseFour(cube, listOfCorners);
                    break;
                case 8:
                    //fully inside another cube
                    break;
                default:
                    System.out.println("ERROR: unexpected amount (" + cube.cordsToBeRemoved.size() + ") of corners need to be removed");
                    System.exit(1248);
            }
        }
    }

    /**
     * If there are two points to be removed, find the 2 new cubes that need to be processed
     * @param cube
     * @param listOfCorners
     * @throws IncorrectSizeException
     * @throws XORException
     */
    public void caseTwo(Cube cube, List<Cord> listOfCorners) throws IncorrectSizeException, XORException {
        String methodname = new Object() {}.getClass().getEnclosingMethod().getName();
        if (cube.intersectionCords.size() == 4) {
            //the two cords that need to be removed lie on one line, so we need to find that line
            Cord r0 = cube.cordsToBeRemoved.get(0);
            Cord r1 = cube.cordsToBeRemoved.get(1);
            boolean xLine = r0.y == r1.y && r0.z == r1.z;
            boolean yLine = r0.x == r1.x && r0.z == r1.z;
            boolean zLine = r0.y == r1.y && r0.x == r1.x;
            if (!((xLine || yLine || zLine) && !((xLine && yLine) || (xLine && zLine) || (yLine && zLine)))) {
                throw new XORException(methodname, xLine, yLine, zLine);
            }
            //Now we need to find the 2 new cubes
            if (xLine) {
                caseTwoX(cube, listOfCorners, r0, r1);
            }
        } else {
            throw new IncorrectSizeException(methodname, 4, cube.intersectionCords.size());
        }
    }

    public void caseTwoX (Cube cube, List<Cord> listOfCorners, Cord r0, Cord r1) throws IncorrectSizeException, XORException {
        String methodname = new Object() {}.getClass().getEnclosingMethod().getName();
        listOfCorners.remove(r0);
        listOfCorners.remove(r1);
        List<Cord> secondCube = new ArrayList<>();
        for (Cord loc : listOfCorners) {
            if (loc.x == r0.x && loc.y == r0.y || loc.x == r1.x && loc.y == r1.y) {
                secondCube.add(loc);
            }
        }
        listOfCorners.removeAll(secondCube);
        if (secondCube.size() != 2) {
            throw new IncorrectSizeException(methodname, 2, secondCube.size());
        }
        int yLine = 0;
        for (Cord ic : cube.intersectionCords) {
            int offset = 1;
            if (ic.z == r0.z) { //x is the same as one of the two r, and z is the same as both
                if (r0.y == cube.max.y) {
                    offset = -1;
                }
                yLine = ic.y;
                listOfCorners.add(new Cord(ic.x, yLine + offset, ic.z));
                listOfCorners.add(new Cord(ic.x ,yLine + offset, secondCube.get(0).z));
            } else {
                if (r0.z == cube.max.z) {
                    offset = -1;
                }
                secondCube.add(new Cord(ic.x, ic.y, ic.z + offset));
            }
        }
        List<Cord> toAdd = new ArrayList<>();
        for (Cord c : secondCube) {
            toAdd.add(new Cord(c.x, yLine, c.z));
        }
        secondCube.addAll(toAdd);

        if (listOfCorners.size() != 8) {
            throw new IncorrectSizeException(methodname, 8, listOfCorners.size());
        }
        if (secondCube.size() != 8) {
            throw new IncorrectSizeException(methodname, 8, secondCube.size());
        }

        //find minmax of both cubes and process the cubes
        List<Cord> minmax1 = findMinMax(listOfCorners);
        processCube(new Cube(minmax1.get(0), minmax1.get(1), cube.on));
        List<Cord> minmax2 = findMinMax(secondCube);
        processCube(new Cube(minmax2.get(0), minmax2.get(1), cube.on));
    }


    /**
     * Adjust cube in case there are four points that need to be removed
     * @param cube
     * @param listOfCorners
     */
    public void caseFour(Cube cube, List<Cord> listOfCorners) throws IncorrectSizeException, XORException {
        String methodname = new Object() {}.getClass().getEnclosingMethod().getName();
        if (cube.intersectionCords.size() == 4) {
            //since there are 4 intersection points, they are on a plane. So first we need to find that plane
            Cord c0 = cube.intersectionCords.get(0);
            Cord c1 = cube.intersectionCords.get(1);
            boolean xPlane = c0.x == c1.x;
            boolean yPlane = c0.y == c1.y;
            boolean zPlane = c0.z == c1.z;
            for (int i = 2; i <= 3; i++) {
                if (xPlane && yPlane || xPlane && zPlane || yPlane && zPlane) {
                    Cord c2 = cube.intersectionCords.get(i);
                    xPlane = xPlane && c0.x == c2.x;
                    yPlane = yPlane && c0.y == c2.y;
                    zPlane = zPlane && c0.z == c2.z;
                }
            }
            //check that we found only one plane
            if (!((xPlane || yPlane || zPlane) && !(xPlane && yPlane || xPlane && zPlane || yPlane && zPlane))) {
                throw new XORException(methodname, xPlane, yPlane, zPlane);
            }
            //because the adjusted cube should not overlap with the cube it intersects with, we need to find the correct offset with the intersection points
            int edge = 0;
            if (xPlane) {
                if (Math.abs(cube.cordsToBeRemoved.get(0).x - c0.x - 1) < Math.abs(cube.cordsToBeRemoved.get(0).x - c0.x + 1)) {
                    edge = c0.x - 1;
                } else {
                    edge = c0.x + 1;
                }
                for (Cord c : cube.cordsToBeRemoved) {
                    listOfCorners.remove(c);
                    listOfCorners.add(new Cord(edge, c.y, c.z));
                }
            } else if (yPlane) {
                if (Math.abs(cube.cordsToBeRemoved.get(0).y - c0.y - 1) < Math.abs(cube.cordsToBeRemoved.get(0).y - c0.y + 1)) {
                    edge = c0.y - 1;
                } else {
                    edge = c0.y + 1;
                }
                for (Cord c : cube.cordsToBeRemoved) {
                    listOfCorners.remove(c);
                    listOfCorners.add(new Cord(c.x, edge, c.z));
                }
            } else { //zPlane
                if (Math.abs(cube.cordsToBeRemoved.get(0).z - c0.z - 1) < Math.abs(cube.cordsToBeRemoved.get(0).z - c0.z + 1)) {
                    edge = c0.z - 1;
                } else {
                    edge = c0.z + 1;
                }
                for (Cord c : cube.cordsToBeRemoved) {
                    listOfCorners.remove(c);
                    listOfCorners.add(new Cord(c.x, c.y, edge));
                }
            }
            List<Cord> minmax = findMinMax(listOfCorners);
            processCube(new Cube(minmax.get(0), minmax.get(1), cube.on));
        } else {
            throw new IncorrectSizeException(methodname, 4, cube.intersectionCords.size());
        }
    }

    /**
     * From a list of corners, find the cords with all the lowest values and the cords with all the highest values
     * @param listOfCorners
     * @return <min, max>
     */
    public List<Cord> findMinMax(List<Cord> listOfCorners) {
        Cord c = listOfCorners.get(0);
        Cord min = new Cord(c.x, c.y, c.z);
        Cord max = new Cord(c.x, c.y, c.z);
        for(int i = 1; i < listOfCorners.size(); i++) {
            c = listOfCorners.get(i);
            min.x = Math.min(min.x, c.x);
            min.y = Math.min(min.y, c.y);
            min.z = Math.min(min.z, c.z);
            max.x = Math.max(max.x, c.x);
            max.y = Math.max(max.y, c.y);
            max.z = Math.max(max.z, c.z);
        }
        List<Cord> returnList = new ArrayList<>();
        returnList.add(min);
        returnList.add(max);
        return returnList;
    }

    /**
     * method used for debugging purposes
     * @param loc
     */
    public void printCube(List<Cord> loc) {
        System.out.println("=======================================");
        for (Cord c : loc) {
            System.out.println(c.x + ", " + c.y + ", " + c.z);
        }
        System.out.println("=======================================");
    }

}