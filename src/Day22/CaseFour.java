package Day22;

import java.util.List;

public class CaseFour {

    /**
     * Adjust cube in case there are four points that need to be removed
     * @param cube
     * @param listOfCorners
     */
    public void caseFour(Cube cube, List<Cord> listOfCorners, PartB pb) throws IncorrectSizeException, XORException {
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
            List<Cord> minmax = pb.findMinMax(listOfCorners);
            pb.processCube(new Cube(minmax.get(0), minmax.get(1), cube.on));
        } else {
            throw new IncorrectSizeException(methodname, 4, cube.intersectionCords.size());
        }
    }
}
