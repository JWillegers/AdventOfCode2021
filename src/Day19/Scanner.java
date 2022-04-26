package Day19;

import java.util.ArrayList;
import java.util.List;

public class Scanner{
    protected boolean matched;
    protected Cord cord;
    protected List<Beacon> beacons;

    public Scanner() {
        this.matched = false;
        this.beacons = new ArrayList<>();
        this.cord = new Cord();
    }

    public void calculateRelativePositions() {
        for(int i = 0; i < beacons.size(); i++) {
            Beacon b = beacons.get(i);
            int bx = b.cord.x;
            int by = b.cord.y;
            int bz = b.cord.z;
            for(int j = 0; j < beacons.size(); j++) {
                if (j == i) {
                    b.relativePositionsOtherBeacons.add(new Cord(0, 0, 0));
                } else {
                    Cord c = beacons.get(j).cord;
                    b.relativePositionsOtherBeacons.add(new Cord(c.x-bx, c.y-by, c.z-bz));
                }
            }
        }
    }

    /*
    Cords of beacons will be their position relative to scanner 0 (located at 0, 0, 0)
    Face meaning
    0: x,y,z
    1: x,z,y
    2: y,x,z
    3: y,z,x
    4: z,x,y
    5: z,y,x
     */
    public void alignBeacons(int face, int mx, int my, int mz) {
        for(Beacon b : beacons) {
            b.relativePositionsOtherBeacons.clear();
            int x = mx*b.cord.x + cord.x;
            int y = my*b.cord.y + cord.y;
            int z = mz*b.cord.z + cord.z;
            if (face == 0) {
                b.cord.x = x;
                b.cord.y = y;
                b.cord.z = z;
            } else if (face ==1) {
                b.cord.x = x;
                b.cord.y = z;
                b.cord.z = y;
            } else if (face == 2) {
                b.cord.x = y;
                b.cord.y = x;
                b.cord.z = z;
            } else if (face == 3) {
                b.cord.x = y;
                b.cord.y = z;
                b.cord.z = x;
            } else if (face == 4) {
                b.cord.x = z;
                b.cord.y = x;
                b.cord.z = y;
            } else {
                b.cord.x = z;
                b.cord.y = y;
                b.cord.z = x;
            }
        }

        calculateRelativePositions();
    }
}
