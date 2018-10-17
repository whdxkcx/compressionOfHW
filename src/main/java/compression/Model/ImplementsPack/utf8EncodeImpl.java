package Model.ImplementsPack;

public class utf8EncodeImpl {
    public static final char a[]= {'0','1','2','3','4','5','6','7','8','9','.','?',':'};
    public static final int  b[]= {1,2,3,4,5,6,7,8,9,10,11,12,13};

    public static int getInt(char c) {
        for(int i=0;i<a.length;i++) {
            if(a[i]==c) return b[i];
        }
        return 0;
    }
}
