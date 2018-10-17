package DataAnlyze.InterfacePack;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public interface AnalyzationOfFile {
    //获取数据类型
    public ArrayList<Integer>  dataTypeAnalyzation(String filePath) throws IOException;
    //获取某一个对象的值
    public void  getOneObjectFromFile(String objId,String filePath,String oneObjectPath) throws IOException;
    //获取每一项指标的振幅
    public ArrayList<Float>  getAmplitudeFromFile(String filePath) throws IOException;
    //获取某一列的值用于分析
    public void getOneLineOfFile(String objId,String filePath,String oneObjectPath) throws IOException;

}
