package Decompression.ImplementsPack;

import LossyCompression.Interface.LossyCompression;

import java.io.IOException;

/**
 * @创建人 kcx
 * @创建时间 2018/10/30
 * @描述  曲线拟合压缩的解压
 */
public class CurveFittingDecompression  implements LossyCompression {

    public static void main(String[] args){
        byte b[]=new byte[1];
        b[0]=(byte)129;
        System.out.println(b[0]);
    }
    @Override
    public void revolvingDoorCompression(String originalPath, String compressionPath) throws IOException {

    }

    /**

     *@描述  最佳曲线拟合算法

     *@参数  [originalPath, compressionPath]

     *@返回值  void

     *@创建人  kcx

     *@创建时间  2018/10/30

     *@修改人和其它信息

     */
    @Override
    public void bestCurveFittingCompression(String originalPath, String compressionPath) throws IOException {

    }
}
