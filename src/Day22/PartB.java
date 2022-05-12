package Day22;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PartB {
    private final int nOfLines = 420;
    private List<RebootStep> input;

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
            System.out.println("NEW LINE");
            for (int j = i + 1; j < nOfLines; j++) {
                //check how many corners of i lie in j
                List<Cord> pointsOfIinJ = new ArrayList<>();
                Cord cj_min = input.get(j).originalCorners.get(0);
                Cord cj_max = input.get(j).originalCorners.get(7);
                for(Cord ci : allCornersOfI) {
                    //min_x <= ci.x <= max_x and min_y <= ci.y <= max_y and min_z <= ci.z <= max_z
                    if (cj_min.x < ci.x && ci.x < cj_max.x
                        && cj_min.y < ci.y && ci.y < cj_max.y
                        && cj_min.z < ci.z && ci.z < cj_max.z) {
                        pointsOfIinJ.add(ci);
                    }
                }
                if (pointsOfIinJ.size() == allCornersOfI.size()) {
                    run = false;
                } else if (!pointsOfIinJ.isEmpty()) {
                    //Check for edges to other point i in j
                    for (int a = 0; a < pointsOfIinJ.size(); a++) {
                        Cord pointA = pointsOfIinJ.get(a);
                        for (int b = a + 1; b < pointsOfIinJ.size(); b++) {
                            Cord pointB = pointsOfIinJ.get(b);
                            if (pointA.x == pointB.x) {
                                pointA.xLine = 0;
                                pointB.xLine = 0;
                            }
                            if (pointA.y == pointB.y) {
                                pointA.yLine = 0;
                                pointB.yLine = 0;
                            }
                            if (pointA.z == pointB.z) {
                                pointA.zLine = 0;
                                pointB.zLine = 0;
                            }
                        }
                    }
                    //Check for direction of edge to points i outside of j
                    for(Cord corner : allCornersOfI) {
                        if(!pointsOfIinJ.contains(corner)) {
                            for(Cord c : pointsOfIinJ) {
                                if(corner.x == c.x && corner.y == c.y && c.zLine != 0) {
                                    c.zLine = corner.z < c.z ? -1 : 1;
                                } else if(c.xLine != 0 && corner.y == c.y && corner.z == c.z) {
                                    c.xLine = corner.x < c.x ? -1 : 1;
                                } else if(corner.x == c.x && c.yLine != 0 && corner.z == c.z) {
                                    c.yLine = corner.y < c.y ? -1 : 1;
                                }
                            }
                        }
                    }

                    //Decided initial new corners that have to be found, and remove all innerpoints
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

                    boolean innerrun = true;
                    while(innerrun) {
                        //check if every corner has some edge in x,y,z
                        innerrun = false;
                        List<Cord> edges = new ArrayList<>();
                        for(int a = 0; a < allCornersOfI.size(); a++) {
                            Cord ca = allCornersOfI.get(a);
                            int counter = 0;
                            for(int b = a + 1; b < allCornersOfI.size(); b++) {
                                Cord cb = allCornersOfI.get(b);
                                if (ca.x == cb.x || ca.y == cb.y || ca.z == cb.z) {
                                    counter++;
                                }
                            }
                            if (counter < 3) {
                                edges.add(ca);
                                innerrun = true;
                            } else if (counter > 3) {
                                //System.out.println("HELP");
                            }
                        }
                        if (innerrun) {
                            int[][] alreadyPlacesCorners = new int[edges.size()][3];
                            int counter = 0;
                            for(int a = 0; a < edges.size(); a++) {
                                Cord ca = edges.get(a);
                                for(int b = a + 1; b < edges.size(); b++) {
                                    Cord cb = edges.get(b);
                                    Cord c0 = null;
                                    Cord c1 = null;
                                    if(ca.x == cb.x && ca.y != cb.y && ca.z != cb.z) {
                                        c0 = new Cord(ca.x, ca.y, cb.z);
                                        c1 = new Cord(ca.x, cb.y, ca.z);
                                    } else if (ca.x != cb.x && ca.y == cb.y && ca.z != cb.z) {
                                        c0 = new Cord(ca.x, ca.y, cb.z);
                                        c1 = new Cord(cb.x, ca.y, ca.z);
                                    } else if (ca.x != cb.x && ca.y != cb.y && ca.z == cb.z) {
                                        c0 = new Cord(ca.x, cb.y, ca.z);
                                        c1 = new Cord(cb.x, ca.y, ca.z);
                                    }
                                    if (c1 != null) {
                                        Boolean c0b = true;
                                        Boolean c1b = true;
                                        for (int c = 0; c < edges.size(); c++) {
                                            if (c0.x == alreadyPlacesCorners[c][0] &&
                                                    c0.y == alreadyPlacesCorners[c][1] &&
                                                    c0.z == alreadyPlacesCorners[c][2]) {
                                                c0b = false;
                                            } else if (c1.x == alreadyPlacesCorners[c][0] &&
                                                    c1.y == alreadyPlacesCorners[c][1] &&
                                                    c1.z == alreadyPlacesCorners[c][2]) {
                                                c1b = false;
                                            }
                                        }
                                        if (c0b) {
                                            toAdd.add(c0);
                                            allCornersOfI.add(c0);
                                            alreadyPlacesCorners[counter][0] = c0.x;
                                            alreadyPlacesCorners[counter][1] = c0.y;
                                            alreadyPlacesCorners[counter][2] = c0.z;
                                            counter++;
                                        }
                                        if (c1b) {
                                            toAdd.add(c1);
                                            allCornersOfI.add(c1);
                                            alreadyPlacesCorners[counter][0] = c1.x;
                                            alreadyPlacesCorners[counter][1] = c1.y;
                                            alreadyPlacesCorners[counter][2] = c1.z;
                                            counter++;
                                        }
                                    }
                                }
                            }
                        }
                        innerrun = false;
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
}