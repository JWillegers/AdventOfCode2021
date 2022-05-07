package Day22;

import java.util.ArrayList;
import java.util.List;

public class RebootStep {
    protected boolean on;
    protected List<Cord> originalCorners;
    protected List<Cord> adjustedCorners;

    public RebootStep() {
        originalCorners = new ArrayList<>();
        adjustedCorners = new ArrayList<>();
    }
}
