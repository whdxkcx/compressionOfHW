package Decompression.InterfacePack;

import java.io.IOException;

public interface LossyDecompression {
    //旋转门有损压缩
    public void revolvingDoorDeCompression(String compressionPath, String deCompressionPath)  throws IOException;
    //最佳拟合曲线压缩
    public void bestCurveFittingDeCompression(String compressiomPath,String deCompressionPath) throws IOException;
}
