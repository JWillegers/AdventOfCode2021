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
                List<Integer> innerpoints = new ArrayList<>();
                Cord cj_min = input.get(j).originalCorners.get(0);
                Cord cj_max = input.get(j).originalCorners.get(7);
                for(Cord ci : input.get(i).adjustedCorners) {
                    //min_x <= ci.x <= max_x and min_y <= ci.y <= max_y and min_z <= ci.z <= max_z
                    if (cj_min.x < ci.x && ci.x < cj_max.x
                        && cj_min.y < ci.y && ci.y < cj_max.y
                        && cj_min.z < ci.z && ci.z < cj_max.z) {
                        innerpoints.add(input.get(i).adjustedCorners.indexOf(ci));
                    }
                }
                //https://stackoverflow.com/questions/21037241/how-to-determine-a-point-is-inside-or-outside-a-cube#:~:text=Construct%20the%20direction%20vector%20from,is%20outside%20of%20the%20cube.
                switch (innerpoints.size()) {
                    case 0:
                        //2 disjoint cubes, do nothing
                        break;
                    case 1:
                        int posX = 0;
                        int posY = 0;
                        int posZ = 0;
                        List<Cord> cordListI = input.get(i).adjustedCorners;
                        Cord ci = cordListI.get(innerpoints.get(0));
                        for (int k = 0; k < input.get(i).adjustedCorners.size(); k++) {
                            if (k != innerpoints.get(0)) {
                                Cord ck = cordListI.get(k);
                                if(ck.x == ci.x && ck.y == ci.y) {
                                    posZ = ck.z < ci.z ? -1 : 1;
                                } else if (ck.z == ci.z && ck.y == ci.y) {
                                    posX = ck.x < ci.x ? -1 : 1;
                                } else if (ck.x == ci.x && ck.z == ci.z) {
                                    posY = ck.y < ci.y ? -1 : 1;
                                }
                            }
                        }

                        int x = 0;
                        int y = 0;
                        int z = 0;

                        List<Cord> cordListJ = input.get(j).originalCorners;
                        for(int k = 0; k < 8; k++) {
                            Cord ck = cordListJ.get(k);
                            if (posX == 1 && ck.x > ci.x) {
                                if (posY == 1 && ck.y > ci.y) {
                                    if (posZ == 1 && ck.z > ci.z) {
                                        x = ck.x;
                                        y = ck.y;
                                        z = ck.z;
                                    } else if (posZ == -1 && ck.z < ci.z) {
                                        x = ck.x;
                                        y = ck.y;
                                        z = ck.z;
                                    }
                                } else {
                                    if (posY == -1 && ck.y > ci.y) {
                                        if (posZ == 1 && ck.z > ci.z) {
                                            x = ck.x;
                                            y = ck.y;
                                            z = ck.z;
                                        } else if (posZ == -1 && ck.z < ci.z) {
                                            x = ck.x;
                                            y = ck.y;
                                            z = ck.z;
                                        }
                                    }
                                }
                            } else if (posX == -1 && ck.x < ci.x) {
                                if (posY == 1 && ck.y > ci.y) {
                                    if (posZ == 1 && ck.z > ci.z) {
                                        x = ck.x;
                                        y = ck.y;
                                        z = ck.z;
                                    } else if (posZ == -1 && ck.z < ci.z) {
                                        x = ck.x;
                                        y = ck.y;
                                        z = ck.z;
                                    }
                                } else {
                                    if (posY == -1 && ck.y > ci.y) {
                                        if (posZ == 1 && ck.z > ci.z) {
                                            x = ck.x;
                                            y = ck.y;
                                            z = ck.z;
                                        } else if (posZ == -1 && ck.z < ci.z) {
                                            x = ck.x;
                                            y = ck.y;
                                            z = ck.z;
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case 2:
                        //TODO
                        break;
                    case 4:
                        //TODO
                        break;
                    case 8:
                        run = false;
                        break;
                    default:
                        System.out.println("IMPLEMENT THIS: " + innerpoints.size());
                        System.exit(777);
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
}
