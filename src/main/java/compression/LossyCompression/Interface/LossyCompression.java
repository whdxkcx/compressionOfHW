package LossyCompression.Interface;

import java.io.IOException;

public interface LossyCompression {
    //旋转门压缩算法
    public void revolvingDoorCompression(String originalPath,String compressionPath)  throws IOException;

    //线性拟合压缩算法
    public void bestCurveFittingCompression(String originalPath,String compressionPath)  throws IOException;

}
