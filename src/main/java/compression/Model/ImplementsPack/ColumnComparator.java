package Model.ImplementsPack;

import java.util.Comparator;

/**
 * @创建人 kcx
 * @创建时间 2018/12/17
 * @描述
 */
public class ColumnComparator implements Comparator<Column> {
    @Override
    public int compare(Column o1, Column o2) {
        float sd1 = o1.getStandardDeviation();
        float sd2 = o2.getStandardDeviation();
        float md1=o1.getMiddle();
        float md2=o2.getMiddle();
        if (sd1 > sd2) return 1;
        else if (sd1 < sd2) return -1;
        else if(md1>md2) return 1;
        else if(md1<md2) return -1;
        return 0;
    }
}
