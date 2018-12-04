package Model.ImplementsPack;

import java.util.ArrayList;

/**
 * @创建人 kcx
 * @创建时间 2018/11/30
 * @描述  进行前导零压缩后存储的类，包括两部分数值的类型和数值。
 */
public class LeadingZero {
    //用于存比特位
    public ArrayList<Byte> b=new ArrayList<>();
    //用于存储值数组
    public ArrayList<Byte> valList=new ArrayList<>();
    //比特位的索引
    public int index=0;
    //存储两个比特位
    public void addBit(String bit){
        int intIndex=index/4;
        int bitIndex=(index*2)%8;
        if(b.size()<=intIndex) b.add((byte)0);
        switch (bit){
            case "00":break;//不用改变
            case "01":b.set(intIndex,(byte)(b.get(intIndex)|(1<<bitIndex)));//用01左移
                break;
            case "10":b.set(intIndex,(byte)(b.get(intIndex)|(2<<bitIndex)));//用10左移
                break;
            case "11":b.set(intIndex,(byte)(b.get(intIndex)|(3<<bitIndex)));//用11左移
                break;
        }
        //元素个数加1
        index++;
    }
}
