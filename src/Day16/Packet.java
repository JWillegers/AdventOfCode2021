package Day16;

import java.util.List;

public class Packet {
    protected int version;
    protected int typeID;
    protected int value;
    protected int operator;
    protected List<Packet> innerPackets;
}
