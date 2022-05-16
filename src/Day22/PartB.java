package Day22;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PartB {
    private final int nOfLines = 420;
    private List<RebootStep> input;
    private boolean innerrun;

    public static void main(String[] args) {
        PartB part = new PartB();
        part.setup();
        part.solution();
    }

    public void setup() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/Day22/input.txt"));
            input = new ArrayList<>();
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
                RebootStep r = new RebootStep();
                r.on = split[0].contains("on");
                /*
                Order of originalCorners:
                min, min, min
                min, min, max
                min, max, min
                min, max, max
                max, min, min
                max, min, max
                max, max, min
                max, max, max
                 */
                for(int x = 1; x < 4; x+= 2) {
                    for(int y = 5; y < 8; y += 2) {
                        for(int z = 9; z < 12; z+= 2) {
                            Cord c = new Cord(Integer.parseInt(split[x]), Integer.parseInt(split[y]), Integer.parseInt(split[z]));
                            r.originalCorners.add(c);
                            r.adjustedCorners.add(c);
                        }
                    }
                }
                input.add(r);
            }
            reader.close();
            innerrun = false;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void solution() {
        long count = 0;
        for (int i = nOfLines - 2; i >= 0; i--) {
            Cord i_min = input.get(i).originalCorners.get(0);
            Cord i_max = input.get(i).originalCorners.get(7);
            long volume = Math.abs((i_max.x-i_min.x)*(i_max.y-i_min.y)*(i_max.z-i_min.z));
            List<Cord> allCornersOfI = input.get(i).adjustedCorners;
            boolean run = true;
            for (int j = i + 1; j < nOfLines; j++) {
                Cord cj_min = input.get(j).originalCorners.get(0);
                Cord cj_max = input.get(j).originalCorners.get(7);
                List<Cord> pointsOfIinJ = getPointsIinJ(allCornersOfI, cj_min, cj_max);
                if (pointsOfIinJ.size() == allCornersOfI.size()) {
                    run = false;
                } else if (!pointsOfIinJ.isEmpty()) {
                    checkEdgesForPointsIinJ(pointsOfIinJ);
                    checkEdgesForPointsIoutsideJ(pointsOfIinJ, allCornersOfI);
                    List<Cord> toAdd = addNewCornersAndRemoveOldOnes(pointsOfIinJ, allCornersOfI, cj_min, cj_max);
                    int[][] oldAlreadyPlacesCorners = initOldAlreadyPlacedCorers(pointsOfIinJ, toAdd);
                    innerrun = true;
                    while(innerrun) {
                        List<Cord> edges = findCornersWithLessThanThreeEdges(allCornersOfI);
                        innerrun = false;
                        if (innerrun) {
                            int[][] alreadyPlacedCorners = newAlreadyPlacedCorners(edges.size(), oldAlreadyPlacesCorners);
                            int counter = oldAlreadyPlacesCorners.length;
                            for(int a = 0; a < edges.size(); a++) {
                                Cord ca = edges.get(a);
                                for(int b = 0; b < edges.size(); b++) {
                                    if (a != b) {
                                        TwoCords t = defineC0andC1(edges, b, ca);
                                        if (t.c1 != null) {
                                            defineBooleans(t, counter, alreadyPlacedCorners);
                                            checkIfCornerCanBeAdded(t, counter, toAdd, allCornersOfI, alreadyPlacedCorners, cj_min, cj_max);
                                        }
                                    }
                                }
                            }
                            oldAlreadyPlacesCorners = alreadyPlacedCorners;
                        }
                    }
                }
                if (!run) {
                    break;
                }
            }
            if (run) {
                count += volume;
            }
        }
        System.out.println(count);
    }

    public List<Cord> getPointsIinJ(List<Cord> allCornersOfI, Cord cj_min, Cord cj_max) {
        List<Cord> pointsOfIinJ = new ArrayList<>();
        for(Cord ci : allCornersOfI) {
            //min_x <= ci.x <= max_x and min_y <= ci.y <= max_y and min_z <= ci.z <= max_z
            if (cj_min.x < ci.x && ci.x < cj_max.x
                    && cj_min.y < ci.y && ci.y < cj_max.y
                    && cj_min.z < ci.z && ci.z < cj_max.z) {
                pointsOfIinJ.add(ci);
            }
        }
        return pointsOfIinJ;
    }

    public void checkEdgesForPointsIinJ(List<Cord> pointsOfIinJ) {
        //Check for edges to other point i in j
        for (int a = 0; a < pointsOfIinJ.size(); a++) {
            Cord pointA = pointsOfIinJ.get(a);
            for (int b = a + 1; b < pointsOfIinJ.size(); b++) {
                Cord pointB = pointsOfIinJ.get(b);
                if (pointA.y == pointB.y && pointA.z == pointB.z) {
                    pointA.xLine = 0;
                    pointB.xLine = 0;
                }
                if (pointA.x == pointB.x && pointA.z == pointB.z) {
                    pointA.yLine = 0;
                    pointB.yLine = 0;
                }
                if (pointA.x == pointB.x && pointA.y == pointB.y) {
                    pointA.zLine = 0;
                    pointB.zLine = 0;
                }
            }
        }
    }

    public void checkEdgesForPointsIoutsideJ(List<Cord> pointsOfIinJ, List<Cord> allCornersOfI) {
        for (Cord corner : allCornersOfI) {
            if (!pointsOfIinJ.contains(corner)) {
                for (Cord c : pointsOfIinJ) {
                    if (corner.x == c.x && corner.y == c.y && c.zLine != 0) {
                        c.zLine = corner.z < c.z ? -1 : 1;
                    } else if (c.xLine != 0 && corner.y == c.y && corner.z == c.z) {
                        c.xLine = corner.x < c.x ? -1 : 1;
                    } else if (corner.x == c.x && c.yLine != 0 && corner.z == c.z) {
                        c.yLine = corner.y < c.y ? -1 : 1;
                    }
                }
            }
        }
    }

    public List<Cord> addNewCornersAndRemoveOldOnes(List<Cord> pointsOfIinJ, List<Cord> allCornersOfI, Cord cj_min, Cord cj_max) {
        List<Cord> toAdd = new ArrayList<>();
        for(Cord c : pointsOfIinJ) {
            allCornersOfI.remove(c);
            if (c.xLine == -1) {
                toAdd.add(new Cord(cj_min.x, c.y, c.z));
            } else if (c.xLine == 1) {
                toAdd.add(new Cord(cj_max.x, c.y, c.z));
            }
            if (c.yLine == -1) {
                toAdd.add(new Cord(c.x, cj_min.y, c.z));
            } else if (c.yLine == 1) {
                toAdd.add(new Cord(c.x, cj_max.y, c.z));
            }
            if (c.zLine == -1) {
                toAdd.add(new Cord(c.x, c.y, cj_min.z));
            } else if (c.zLine == 1) {
                toAdd.add(new Cord(c.x, c.y, cj_max.z));
            }
        }
        allCornersOfI.addAll(toAdd);
        return toAdd;
    }

    public int[][] initOldAlreadyPlacedCorers(List<Cord> pointsOfIinJ, List<Cord> toAdd) {
        int[][] oldAlreadyPlacedCorners = new int[pointsOfIinJ.size()+toAdd.size()][3];
        for (int a = 0; a < pointsOfIinJ.size(); a++) { //preventing deleted corners from being added again
            Cord c = pointsOfIinJ.get(a);
            oldAlreadyPlacedCorners[a][0] = c.x;
            oldAlreadyPlacedCorners[a][1] = c.y;
            oldAlreadyPlacedCorners[a][2] = c.z;
        }
        for (int a = pointsOfIinJ.size(); a < toAdd.size() + pointsOfIinJ.size(); a++) { //preventing already added corners from being added again
            Cord c = toAdd.get(a - pointsOfIinJ.size());
            oldAlreadyPlacedCorners[a][0] = c.x;
            oldAlreadyPlacedCorners[a][1] = c.y;
            oldAlreadyPlacedCorners[a][2] = c.z;
        }
        return oldAlreadyPlacedCorners;
    }

    public List<Cord> findCornersWithLessThanThreeEdges(List<Cord> allCornersOfI) {
        List<Cord> edges = new ArrayList<>();
        for(int a = 0; a < allCornersOfI.size(); a++) {
            Cord ca = allCornersOfI.get(a);
            int counter = 0;
            for(int b = 0; b < allCornersOfI.size(); b++) {
                if (a != b) {
                    Cord cb = allCornersOfI.get(b);
                    if ((ca.x == cb.x && ca.y == cb.y) || (ca.y == cb.y && ca.z == cb.z) || (ca.z == cb.z && ca.x == cb.x)) {
                        counter++;
                    }
                }
            }
            if (counter < 3) {
                edges.add(ca);
                innerrun = true;
            }
        }
        return edges;
    }

    public int[][] newAlreadyPlacedCorners(int edgesSize, int[][] oldAlreadyPlacesCorners) {
        int[][] alreadyPlacedCorners = new int[2*edgesSize+oldAlreadyPlacesCorners.length][3];
        for(int q = 0; q < oldAlreadyPlacesCorners.length; q++) {
            alreadyPlacedCorners[q][0] = oldAlreadyPlacesCorners[q][0];
            alreadyPlacedCorners[q][1] = oldAlreadyPlacesCorners[q][1];
            alreadyPlacedCorners[q][2] = oldAlreadyPlacesCorners[q][2];
        }
        return alreadyPlacedCorners;
    }

    public TwoCords defineC0andC1(List<Cord> edges, int b, Cord ca) {
        Cord cb = edges.get(b);
        TwoCords t = new TwoCords();
        if (ca.x == cb.x && ca.y != cb.y && ca.z != cb.z) {
            t.c0 = new Cord(ca.x, ca.y, cb.z);
            t.c1 = new Cord(ca.x, cb.y, ca.z);
        } else if (ca.x != cb.x && ca.y == cb.y && ca.z != cb.z) {
            t.c0 = new Cord(ca.x, ca.y, cb.z);
            t.c1 = new Cord(cb.x, ca.y, ca.z);
        } else if (ca.x != cb.x && ca.y != cb.y && ca.z == cb.z) {
            t.c0 = new Cord(ca.x, cb.y, ca.z);
            t.c1 = new Cord(cb.x, ca.y, ca.z);
        }
        return t;
    }

    public void defineBooleans(TwoCords t, int counter, int[][] alreadyPlacedCorners) {
        for (int c = 0; c <= counter; c++) {
            if (t.c0.x == alreadyPlacedCorners[c][0] &&
                    t.c0.y == alreadyPlacedCorners[c][1] &&
                    t.c0.z == alreadyPlacedCorners[c][2]) {
                t.c0b = false;
            } else if (t.c1.x == alreadyPlacedCorners[c][0] &&
                    t.c1.y == alreadyPlacedCorners[c][1] &&
                    t.c1.z == alreadyPlacedCorners[c][2]) {
                t.c1b = false;
            }
        }
    }

    public void checkIfCornerCanBeAdded(TwoCords t, int counter, List<Cord> toAdd, List<Cord> allCornersOfI, int[][] alreadyPlacedCorners, Cord cj_min, Cord cj_max) {
        for (int i = 0; i < 2; i++) {
            Cord c = t.c0;
            Boolean b = t.c0b;
            if (i == 1) {
                c = t.c1;
                b = t.c1b;
            }
            if (b && ((c.x <= cj_min.x && c.y <= cj_min.y) ||
                    (c.z <= cj_min.z && c.y <= cj_min.y) ||
                    (c.x <= cj_min.x && c.z <= cj_min.z) ||
                    (c.x >= cj_max.x && c.y >= cj_max.y) ||
                    (c.z >= cj_max.z && c.y >= cj_max.y) ||
                    (c.x >= cj_max.x && c.z >= cj_max.z))) {
                toAdd.add(c);
                allCornersOfI.add(c);
                alreadyPlacedCorners[counter][0] = c.x;
                alreadyPlacedCorners[counter][1] = c.y;
                alreadyPlacedCorners[counter][2] = c.z;
                counter++;
            }
        }


    }

    class TwoCords {
        Cord c0;
        Cord c1;
        boolean c0b;
        boolean c1b;

        public TwoCords() {
            c0 = null;
            c1 = null;
            c0b = true;
            c1b = true;
        }

    }
}