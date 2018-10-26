package Model.ImplementsPack;

import java.util.HashMap;
import java.util.Map;

public class utf8EncodeImpl {
    private static final char a[]= {'0','1','2','3','4','5','6','7','8','9','.',':',',','-',';'};
    private static final int  b[]= {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    private static Map<Character,Integer> localMap=new HashMap();
    static{
        for(int i=0;i<a.length;i++){
            localMap.put(a[i],b[i]);
        }
    }
    public static int getInt(char c) {
        return localMap.getOrDefault(c,-1);
    }
}
