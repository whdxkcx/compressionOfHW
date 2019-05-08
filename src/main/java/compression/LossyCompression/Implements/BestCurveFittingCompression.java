package LossyCompression.Implements;

import LossyCompression.Interface.LossyCompression;
import Model.Abstract.Spot;
import Model.ImplementsPack.*;
import Tools.Inplements.ByteConverterImpl;
import Tools.Inplements.SimplyData;
import Tools.InterfacePack.SpotTools;
import Tools.InterfacePack.TheMinDiffVal;
import Tools.InterfacePack.byteConverter;
import Tools.InterfacePack.pythonListToll;
import com.sun.xml.internal.ws.api.pipe.SyncStartForAsyncFeature;
import org.apache.commons.collections.ArrayStack;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BestCurveFittingCompression implements LossyCompression {
    private static int columnCount = 4749;//The number of quotas.
    private static boolean isFlag = true;
    private static boolean isFistLine = true;
    public static float p = 0.10f;
    public static int k = 20;
    public static int count1 = 0;
    public static int count2 = 0;
    public static int count3 = 0;
    public static HashMap<String, Integer> radioMap = new HashMap<>();
    public TypeOfFile typeCollection;
    public SpotTools spt;
    public SZCompression sz = new SZCompression();
    ByteConverterImpl bc = new ByteConverterImpl();
    static String inputFilePath = "E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\clustering\\test.csv";
    static String outputFilePath = "E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\NoModelChangeWithLossLess\\test（c1).csv";
    public static void main(String[] args) throws IOException {
        BestCurveFittingCompression bcfc = new BestCurveFittingCompression();

        radioMap.put("00", 0);
        radioMap.put("01", 0);
        radioMap.put("10", 0);
        radioMap.put("11", 0);
        radioMap.put("0", 0);
        long startTime = System.currentTimeMillis();
        bcfc.regressionBasedcurveFitingCompression(inputFilePath, outputFilePath);
//        System.out.println(radioMap.get("00") + ":" + radioMap.get("01") + ":" + radioMap.get("10") + ":" + radioMap.get("11") + ":" + radioMap.get("0"));
        long endTime = System.currentTimeMillis();
        double time = endTime - startTime;
        System.out.println("压缩时间为：" + time / 1000 + "s");
    }

    @Override
    public void bestCurveFittingCompression(String originalPath, String compressionPath) throws IOException {

    }

    /**
     * @描述 读取聚类过后的文件, 然后进行压缩
     * @参数 [inputFilePath, outputFilePath]
     * @返回值 void
     * @创建人 kcx
     * @创建时间 2019/3/20
     * @修改人和其它信息
     */
    public void regressionBasedcurveFitingCompression(String inputFilePath, String outputFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
        BufferedOutputStream br = new BufferedOutputStream(new FileOutputStream(outputFilePath, true));//true代表追加输出
        //第一步读取文件中的分组索引
        String indexLine = reader.readLine();
        //第二步，读取每一个分组的线性回归系数
        ArrayList<ArrayList<String>> regressionWsArr = new ArrayList<>();
        String line = "";
        while (!(line = reader.readLine()).equals("#")) {
            ArrayList<String> oneGroupRegressionWs = pythonListToll.getList(line);
            regressionWsArr.add(oneGroupRegressionWs);
        }

        //第三步读取各分组的数据
        ArrayList<String> oneGroupData = new ArrayList<>();
        ArrayList<Bit> oneGroupRes = null;
        int i = 0;
        while ((line = reader.readLine()) != null) {
            if (!line.equals("#")) {
                if(isFistLine){
                    isFistLine=false;
                    br.write(ByteConverterImpl.getBytes(line.split(",").length));
                    System.out.println(line.split(",").length);
                    br.flush();
                }
                oneGroupData.add(line);
            }
            else {
                oneGroupRes = groupBasedCompression(oneGroupData, regressionWsArr.get(i));
                i++;
                printOneGroupDataAfterCompression(oneGroupRes, outputFilePath);
                oneGroupRes.clear();
                oneGroupData = new ArrayList<>();
            }
        }
        oneGroupRes = groupBasedCompression(oneGroupData, regressionWsArr.get(i));
        printOneGroupDataAfterCompression(oneGroupRes, outputFilePath);
    }


    /**
     * @描述 输出一个组压缩的数据，先输出预测标志，然后输出非预测值。
     * @参数 [oneGroupRes, outputFileName]
     * @返回值 void
     * @创建人 kcx
     * @创建时间 2019/3/25
     * @修改人和其它信息
     */
    public void printOneGroupDataAfterCompression(ArrayList<Bit> oneGroupRes, String outputFileName) throws IOException {
        BufferedOutputStream br = new BufferedOutputStream(new FileOutputStream(outputFileName, true));//true代表追加输出
        //逐行输出
        for (int i = 0; i < oneGroupRes.size(); i++) {
            Bit bit = oneGroupRes.get(i);
//            ModelChangeSZ.printBitAfterCompression(outputFileName, bit);////仅改变模型的输出
            NomdelSZWIthLossless.printBitAfterCompression(outputFileName, bit);//最终改进压缩算法
        }
    }


    /**
     * @描述 压缩一个组的数据
     * @参数 []
     * @返回值 void
     * @创建人 kcx
     * @创建时间 2019/3/20
     * @修改人和其它信息
     */
    public ArrayList<Bit> groupBasedCompression(ArrayList<String> oneGroupData, ArrayList<String> ws) {
        int groupSize = oneGroupData.size();
        ArrayList<Bit> oneGroupRes = new ArrayList<>();
        ArrayList<Float> wsFloat = new ArrayList<>();
        ArrayList<Float> lastdeCompressionList = null;
        int count = 0;
        for (String str : ws)
            wsFloat.add(Float.parseFloat(str));
        for (int i = 0; i < groupSize; i++) {
            ArrayList<String> columnList = pythonListToll.getList(oneGroupData.get(i));
            Bit currentBit = null;
            currentBit = NomdelSZWIthLossless.finalSZCompression(columnList, ws);//最终改进压缩算法
            oneGroupRes.add(currentBit);
        }
        return oneGroupRes;
    }


    /**
     * @描述 读取文件的第一行，也就是分组索引信息。
     * @参数 [indexLine]
     * @返回值 java.util.ArrayList<java.util.ArrayList   <   java.lang.String>>
     * @创建人 kcx
     * @创建时间 2019/3/20
     * @修改人和其它信息
     */
    public ArrayList<ArrayList<String>> readIndexLine(String indexLine) {
        ArrayList<ArrayList<String>> resList = new ArrayList<>();
        String[] indexLineArr = indexLine.split("]");
        int len = resList.size();
        for (int i = 0; i < len; i++) {
            ArrayList<String> oneGroupIndex = new ArrayList<>();
            String curGroupStr = indexLineArr[i];
            String[] curGroupArr = curGroupStr.split(",");
            int lenCurGroup = curGroupArr.length;
            for (int j = 0; j < lenCurGroup; j++) {
                if (curGroupArr[j].contains("["))
                    oneGroupIndex.add(curGroupArr[j].substring(1));
                else
                    oneGroupIndex.add(curGroupArr[j]);
            }
            resList.add(oneGroupIndex);
        }
        return resList;
    }


    @Override
    public void revolvingDoorCompression(String originalPath, String compressionPath) throws IOException {

    }
}
