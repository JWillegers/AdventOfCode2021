package Day22;

import java.util.ArrayList;
import java.util.List;

public class CaseTwo {
    PartB pb;

    public CaseTwo(PartB pb) {
        this.pb = pb;
    }

    /**
     * If there are two points to be removed, find the 2 new cubes that need to be processed
     * @param cube
     * @param listOfCorners
     * @throws IncorrectSizeException
     * @throws XORException
     */
    public void main(Cube cube, List<Cord> listOfCorners) throws IncorrectSizeException, XORException {
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
                x(cube, listOfCorners, r0, r1);
            }
        } else {
            throw new IncorrectSizeException(methodname, 4, cube.intersectionCords.size());
        }
    }

    public void x(Cube cube, List<Cord> listOfCorners, Cord r0, Cord r1) throws IncorrectSizeException, XORException {
        List<List<Cord>> llc = createSecondCube(listOfCorners, r0, r1, "z");
        listOfCorners = llc.get(0);
        List<Cord> secondCube = llc.get(1);
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

        finalStep(listOfCorners, secondCube, cube);
    }

    /**
     * create the second cube
     * remove corners from listOfCorners
     * add the needed corners to secondCube
     * @param listOfCorners
     * @param r0
     * @param r1
     * @param ignore
     * @return
     * @throws IncorrectSizeException
     */
    public List<List<Cord>> createSecondCube(List<Cord> listOfCorners, Cord r0, Cord r1, String ignore) throws IncorrectSizeException {
        String methodname = new Object() {}.getClass().getEnclosingMethod().getName();
        listOfCorners.remove(r0);
        listOfCorners.remove(r1);
        List<Cord> secondCube = new ArrayList<>();
        for (Cord loc : listOfCorners) {
            if ((ignore.equals("z") && (loc.x == r0.x && loc.y == r0.y || loc.x == r1.x && loc.y == r1.y)) ||
                    (ignore.equals("y") && (loc.x == r0.x && loc.z == r0.z || loc.x == r1.x && loc.z == r1.z)) ||
                    (ignore.equals("x") && (loc.z == r0.z && loc.y == r0.y || loc.z == r1.z && loc.y == r1.y))) {
                secondCube.add(loc);
            }
        }
        listOfCorners.removeAll(secondCube);
        if (secondCube.size() != 2) {
            throw new IncorrectSizeException(methodname, 2, secondCube.size());
        }
        List<List<Cord>> returnObject = new ArrayList<>();
        returnObject.add(listOfCorners);
        returnObject.add(secondCube);
        return returnObject;
    }

    /**
     * Check cube sizes and make new cubes to process
     * @param cube1
     * @param cube2
     * @param originalCube
     * @throws IncorrectSizeException
     * @throws XORException
     */
    public void finalStep(List<Cord> cube1, List<Cord> cube2, Cube originalCube) throws IncorrectSizeException, XORException {
        String methodname = new Object() {}.getClass().getEnclosingMethod().getName();
        if (cube1.size() != 8) {
            throw new IncorrectSizeException(methodname, 8, cube1.size());
        }
        if (cube2.size() != 8) {
            throw new IncorrectSizeException(methodname, 8, cube2.size());
        }

        //find minmax of both cubes and process the cubes
        List<Cord> minmax1 = pb.findMinMax(cube1);
        pb.processCube(new Cube(minmax1.get(0), minmax1.get(1), originalCube.on));
        List<Cord> minmax2 = pb.findMinMax(cube2);
        pb.processCube(new Cube(minmax2.get(0), minmax2.get(1), originalCube.on));
    }
}
