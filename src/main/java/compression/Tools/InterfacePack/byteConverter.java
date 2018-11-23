package Tools.InterfacePack;


//字节转换器，用于把其他类型转换为字节类型，和把字节类型转换为其他类型
public interface byteConverter {
       //float数组转换为byte数组
      public byte[] floatArrayToByteArray(float[] f);
      //byte数组转换为float数组
      public float[] byteArrayToFloatArray(byte[] b);
}
