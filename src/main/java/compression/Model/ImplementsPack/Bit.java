package Model.ImplementsPack;

public class Bit {
    int[] b=new int[200];
    int index=0;
    public void add(String bit){
        int intIndex=index/16;
        int bitIndex=(index*2)%32;
        switch (bit){
            case "00":break;//不用改变
            case "01":b[intIndex]=b[intIndex]|(1<<bitIndex);//用01左移
                 break;
            case "10":b[intIndex]=b[intIndex]|(2<<bitIndex);//用10左移
                 break;
            case "11":b[intIndex]=b[intIndex]|(3<<bitIndex);//用11左移
                 break;
        }
        //元素个数加1；
        index++;
    }
    public String  get(int index){
        int intIndex=index/16;
        int bitIndex=(index*2)%32;
        int temp=(b[bitIndex]>>bitIndex)&3;
        switch (temp){
            case 0:return "00";
            case 1:return "01";
            case 2:return "10";
        }
        return "11";
    }
}
