package LossyCompression.Implements;

import LossyCompression.Interface.SZ;
import Tools.Inplements.ByteConverterImpl;
import Tools.InterfacePack.byteConverter;

import java.util.ArrayList;

/**
 * @创建人 kcx
 * @创建时间 2019/3/20
 * @描述
 */
public class SZCompression  {
    public static ByteConverterImpl bc=new ByteConverterImpl();
    /**

     *@描述   输入一个数和它前面相邻的数，返回前导零的个数

     *@参数  [afterFloatTrunCatVal, preccedingVal]

     *@返回值  int

     *@创建人  kcx

     *@创建时间  2019/3/24

     *@修改人和其它信息

     */
    public static int leadingZeroCount(float afterFloatTrunCatVal,float preccedingVal) {
        byte[] afterBytes=bc.floatToByteArray(afterFloatTrunCatVal);
        byte[] preceedingbytes=bc.floatToByteArray(preccedingVal);
        int num=0;
        for(int i=0;i<3;i++)
            if((afterBytes[i] ^ preceedingbytes[i])==0) num++;
        return num;
    }


    /**

     *@描述  输入一个截断后的数，以及其截断后的字节个数和前导零的字节个数，返回最后保留的字节个数r't

     *@参数  [afterFloatTrunCatVal, truncatCount, leadingZreoCount]

     *@返回值  byte[]

     *@创建人  kcx

     *@创建时间  2019/3/24

     *@修改人和其它信息

     */
    public static byte[] leadingZeroCompression(float afterFloatTrunCatVal,int truncatCount,int leadingZreoCount) {
        byte[] bytes=new byte[truncatCount-leadingZreoCount];
        byte[] aftValBytes=bc.floatToByteArray(afterFloatTrunCatVal);
        for(int i=leadingZreoCount;i<truncatCount;i++)
            bytes[i]=aftValBytes[i];
        return bytes;
    }

    /**

     *@描述 输入一个浮点数，原值和中值和表示这个浮点数所需的最小字节数。返回截断后的字节数组

     *@参数  [originalVal, MiddleVal, count]

     *@返回值  byte[]

     *@创建人  kcx

     *@创建时间  2019/3/24

     *@修改人和其它信息

     */
    public static byte[] floatTruncatComprression(float originalVal,float MiddleVal,int count) {
        float NormalizedData=originalVal-MiddleVal;
        return bc.oneFloatTruncat(NormalizedData,count);
    }

    /**

     *@描述  根据一个数截断后的字符数组，补全0，转化成一个整数

     *@参数  [count, bytes]

     *@返回值  float

     *@创建人  kcx

     *@创建时间  2019/3/24

     *@修改人和其它信息

     */
    public static float truncatBytesToFloat(int count,byte[] bytes){
        return bc.bytesToFloat(bytes,count);
    }

    /**
     * @描述 在确定误差的情况下，获取一个浮点数表示的最小的字节数。
     * @参数 [radius]
     * @返回值 int
     * @创建人 kcx
     * @创建时间 2018/11/27
     * @修改人和其它信息
     */
    public static int getMinNumberOfMantissaBits(float radius, float e) {
        int diff = Exp(radius) - Exp(e);
        int RQ_MBits = 0;
        if (diff < 0) RQ_MBits = 0;
        else if (diff > 23) RQ_MBits = 23;
        else RQ_MBits = diff;
        int count = (1 + 8 + RQ_MBits) % 8 == 0 ? (1 + 8 + RQ_MBits) / 8 : ((1 + 8 + RQ_MBits) / 8 + 1);
        if (count > 4)
            System.out.println(RQ_MBits + ":" + count);
        return count;
    }

    /**
     * @描述 获取一个float数值的指数的值
     * @参数 [f]
     * @返回值 int
     * @创建人 kcx
     * @创建时间 2018/11/27
     * @修改人和其它信息
     */
    public static int Exp(float f) {
        int intBits = Float.floatToIntBits(f);
        return ((intBits >>> 23) & 255) - 127;
    }
}
