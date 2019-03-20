package LossyCompression.Interface;

import java.util.ArrayList;

/**
 * @创建人 kcx
 * @创建时间 2019/3/20
 * @描述
 */
public interface SZ {
    public float[] floatCutComprression(ArrayList<Float> originalVal);
    public float[] leadingZeroCompression(float[] afterFloatCutVal);
}
