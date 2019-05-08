package Tools.Inplements;

import Tools.InterfacePack.byteConverter;

import javax.swing.plaf.metal.MetalIconFactory;

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

     *@描述  根据传入的字节数来对浮点数进行截断，然后转换成字节数组

     *@参数  [data, num]

     *@返回值  byte[]

     *@创建人  kcx

     *@创建时间  2018/11/27

     *@修改人和其它信息

     */
    public byte[] floatArrayToByteArray(float[] data,int num) {
        if(num==4) return floatArrayToByteArray(data);
        int len=data.length;
        byte bytes[]=new byte[num*len];
        for(int i=0;i<len;i++){
            byte[] bs=floatToByteArray(data[i]);
            try {
                for (int j = i * num, k = 0; k < num; j++, k++)
                    bytes[j] = bs[k];
            }
            catch (Exception e){
                System.out.println(num);
            }
        }
        return bytes;
    }


    /**

     *@描述   把一个浮点数截断后返回截断后的字节数组

     *@参数  [originalVal, count]

     *@返回值  byte[]

     *@创建人  kcx

     *@创建时间  2019/3/24

     *@修改人和其它信息

     */
    public  byte[] oneFloatTruncat(float originalVal,int count){
        byte bytes[]=new byte[count];
        byte originalBytes[]=floatToByteArray(originalVal);
        for(int i=0;i<count;i++)
            bytes[i]=originalBytes[i];
        return bytes;
    }


    /**

     *@描述   把一个包含count字节的数组转化为一个浮点数

     *@参数  [bytes, count]

     *@返回值  float

     *@创建人  kcx

     *@创建时间  2019/3/24

     *@修改人和其它信息

     */
    public float bytesToFloat(byte[] bytes,int count){
        byte[] floatBytes=new byte[4];
        for(int i=0;i<count;i++)
            floatBytes[i]=bytes[i];
        for (int i=count;i<4;i++)
            floatBytes[i]=0;
        return Float.intBitsToFloat(getIntFrombytes(floatBytes,0));
    }


    /**

     *@描述  把一个bytes字节数组转化为一个整数

     *@参数  [bytes, index]

     *@返回值  int

     *@创建人  kcx

     *@创建时间  2019/3/24

     *@修改人和其它信息

     */
    public static int getIntFrombytes(byte[] bytes,int index){
        return 	(0xff000000 & (bytes[index+0] << 24))  |
                (0x00ff0000 & (bytes[index+1] << 16))  |
                (0x0000ff00 & (bytes[index+2] << 8))   |
                (0x000000ff &  bytes[index+3]);
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
        bytes[3] = (byte) (data & 0xff);
        bytes[2] = (byte) ((data & 0xff00) >> 8);
        bytes[1] = (byte) ((data & 0xff0000) >> 16);
        bytes[0] = (byte) ((data & 0xff000000) >> 24);
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
