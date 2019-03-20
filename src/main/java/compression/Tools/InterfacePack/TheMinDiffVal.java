package Tools.InterfacePack;

import java.util.ArrayList;

/**
 * @创建人 kcx
 * @创建时间 2019/3/20
 * @描述
 */
public class TheMinDiffVal {
    public static String getMinDiffVal(float PNFVal,float LCFVal,float QCFVal,float currentVal){
        String curveType="";//记录数据类型
        if (Math.abs(QCFVal - currentVal) < Math.max(Math.abs(PNFVal - currentVal), Math.abs(LCFVal - currentVal))) {
            curveType = "10";
        } else if (Math.abs(LCFVal - currentVal) < Math.abs(PNFVal - currentVal)) {
            curveType = "01";
        } else {
            curveType = "00";
        }
        return  curveType;
    }


}
