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
            boolean run = true;
            for (int j = i + 1; j < nOfLines; j++) {
                //check how many corners of i lie in j
                List<Cord> innerpoints = new ArrayList<>();
                Cord cj_min = input.get(j).originalCorners.get(0);
                Cord cj_max = input.get(j).originalCorners.get(7);
                for(Cord ci : input.get(i).adjustedCorners) {
                    //min_x <= ci.x <= max_x and min_y <= ci.y <= max_y and min_z <= ci.z <= max_z
                    if (cj_min.x < ci.x && ci.x < cj_max.x
                        && cj_min.y < ci.y && ci.y < cj_max.y
                        && cj_min.z < ci.z && ci.z < cj_max.z) {
                        innerpoints.add(ci);
                    }
                }
                if (innerpoints.size() == input.get(i).adjustedCorners.size()) {
                    run = false;
                } else if (!innerpoints.isEmpty()) {
                    List<Cord> cordList = input.get(i).adjustedCorners;
                    //Find edges of i which intersect with faces of j
                    List<Edge> x = new ArrayList<>();
                    List<Edge> y = new ArrayList<>();
                    List<Edge> z = new ArrayList<>();
                    for(Cord cl : cordList) {
                        if(!innerpoints.contains(cl)) {
                            for(Cord cip : innerpoints) {
                                if(cl.x == cip.x && cl.y == cip.y) {
                                    z.add(new Edge(cl.z, cl.z > cip.z, cip));
                                } else if (cl.x == cip.x && cl.z == cip.z) {
                                    y.add(new Edge(cl.y, cl.y > cip.y, cip));
                                } else if (cl.y == cip.y && cl.z == cip.z) {
                                    x.add(new Edge(cl.x, cl.x > cip.x, cip));
                                }
                            }
                        }
                    }
                    //remove old corners and add new ones
                    for(Cord c : innerpoints) {
                        cordList.remove(c);
                    }
                    for(Edge e : x) {
                        int thirdCord;
                        if (e.positiveDirection) {
                            thirdCord = cj_max.x;
                        } else {
                            thirdCord = cj_min.x;
                        }
                        cordList.add(new Cord(thirdCord, e.cord.y, e.cord.z));
                    }
                    for(Edge e : y) {
                        int thirdCord;
                        if (e.positiveDirection) {
                            thirdCord = cj_max.y;
                        } else {
                            thirdCord = cj_min.y;
                        }
                        cordList.add(new Cord(e.cord.x, thirdCord, e.cord.z));
                    }
                    for(Edge e : z) {
                        int thirdCord ;
                        if (e.positiveDirection) {
                            thirdCord = cj_max.z;
                        } else {
                            thirdCord = cj_min.z;
                        }
                        cordList.add(new Cord(e.cord.x, e.cord.y, thirdCord));
                    }
                }
                if (!run) {
                    break;
                }

            }
            if (run) {
                count += 1;
            }
        }
        System.out.println(count);
    }

    class Edge {
        int number;
        boolean positiveDirection;
        Cord cord;

        protected Edge(int n, boolean b, Cord c) {
            this.cord = c;
            this.number = n;
            this.positiveDirection = b;
        }

    }
}