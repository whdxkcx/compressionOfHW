package LossyCompression.Interface;

import java.util.ArrayList;

/**
 * @创建人 kcx
 * @创建时间 2019/3/20
 * @描述
 */
public interface SZ {
    public byte[] floatCutComprression(ArrayList<Float> originalVal   ,float MiddleVal,float e);
    public float[] leadingZeroCompression(float[] afterFloatCutVal);
}
