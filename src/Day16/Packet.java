package Day16;

import java.util.List;

public class Packet {
    protected int version;
    protected int typeID;
    protected long value;
    protected int operator;
    protected int label;
    protected int padding;
    protected List<Packet> innerPackets;
}
