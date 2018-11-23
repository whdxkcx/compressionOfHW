package Tools.Inplements;

import Tools.InterfacePack.byteConverter;

/**
 * @创建人 kcx
 * @创建时间 2018/10/30
 * @描述
 */
public class ByteConverterImpl implements byteConverter {
    /**

     *@描述  把float数组转换为byte数组

     *@参数  [f]

     *@返回值  byte[]

     *@创建人  kcx

     *@创建时间  2018/10/30

     *@修改人和其它信息

     */
    @Override
    public byte[] floatArrayToByteArray(float[] data) {
        int len=data.length;
        byte bytes[]=new byte[4*len];
        for(int i=0;i<len;i++){
            byte[] bs=floatToByteArray(data[i]);
            for(int j=i*4,k=0;k<4;j++,k++)
                bytes[j]=bs[k];
        }
        return bytes;
    }

    /**

     *@描述 把一个float类型的字符转换为字节数组。

     *@参数  [f]

     *@返回值  byte[]

     *@创建人  kcx

     *@创建时间  2018/10/30

     *@修改人和其它信息

     */
    public byte[] floatToByteArray(float data){
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }


    /**

     *@描述  把整数转化为字节数组

     *@参数  [data]

     *@返回值  byte[]

     *@创建人  kcx

     *@创建时间  2018/10/30

     *@修改人和其它信息

     */
    public static byte[] getBytes(int data){
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }


    /**

     *@描述 把byte型数组转换为float数组

     *@参数  [b]

     *@返回值  float[]

     *@创建人  kcx

     *@创建时间  2018/10/30

     *@修改人和其它信息

     */
    @Override
    public float[] byteArrayToFloatArray(byte[] b) {

        return new float[0];
    }

}
