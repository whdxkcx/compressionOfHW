package Tools.Inplements;

import Model.Abstract.Spot;
import Model.ImplementsPack.ISubSpot;
import Model.ImplementsPack.fSubSpot;
import Tools.InterfacePack.SpotTools;

public class SpotToolsImpl implements SpotTools {
    @Override
    public float slopCalculation(fSubSpot start, fSubSpot end) {
        return (end.getY()-start.getY())/(end.getX()-start.getX());
    }

    @Override
    public float slopCalculation(fSubSpot start, ISubSpot end) {
        return  (end.getY()-start.getY())/(end.getX()-start.getX());
    }
}
