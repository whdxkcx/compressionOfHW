package Tools.InterfacePack;

import Model.Abstract.Spot;
import Model.ImplementsPack.ISubSpot;
import Model.ImplementsPack.fSubSpot;

public interface SpotTools {
    public float slopCalculation(fSubSpot start, fSubSpot end);
    public float slopCalculation(fSubSpot start, ISubSpot end);
}
