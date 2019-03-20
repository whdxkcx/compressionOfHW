package LossyCompression.Implements;

import LossyCompression.Interface.SZ;

import java.util.ArrayList;

/**
 * @创建人 kcx
 * @创建时间 2019/3/20
 * @描述
 */
public class SZCompression implements SZ {
    @Override
    public float[] leadingZeroCompression(float[] afterFloatCutVal) {
        return new float[0];
    }

    @Override
    public float[] floatCutComprression(ArrayList<Float> originalVal) {
        return new float[0];
    }
}
