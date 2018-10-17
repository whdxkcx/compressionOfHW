package DeviationCalculation.InterfacePack;

import java.io.IOException;
import java.util.ArrayList;

public interface RelativeDeviation {
    //求两个文件的最大偏差值（基于列）
    public ArrayList<Double> maxRelativeBiasBaseLine(String originalPath,String rebuildPath) throws IOException;

    //求两个文件的平均偏差值（基于列）
    public ArrayList<Double> averageDeviationBaseLine(String originalPath,String rebuildPath) throws IOException;

    //求两个文件的平均偏差值比（基于列）
    public ArrayList<Double> averageDeviationRadioBaseLine(String originalPath,String rebuildPath) throws IOException;

    //求两个文件的相对均方误差（按列）
    public ArrayList<Double> relativeMeanSquareDevitationBaseLine(String originalPath, String rebuildPath) throws IOException ;
}
