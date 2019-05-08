package LossyCompression.Implements;

import Model.ImplementsPack.*;
import Tools.Inplements.ByteConverterImpl;
import Tools.InterfacePack.SpotTools;
import Tools.InterfacePack.TheMinDiffVal;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @创建人 kcx
 * @创建时间 2019/4/9
 * @描述 针对一维数组的原始SZ压缩算法
 */
public class OrignalSZ {
    private static int columnCount = 4749;//The number of quotas.
    private static boolean isFlag = true;
    public static float p = 0.1f;
    public static int k = 20;
    public static int count1 = 0;
    public static int count2 = 0;
    public static int count3 = 0;
    public static HashMap<String, Integer> radioMap = new HashMap<>();
    public TypeOfFile typeCollection;
    public SpotTools spt;
    public static SZCompression sz = new SZCompression();
    static ByteConverterImpl bc = new ByteConverterImpl();

    public static void main(String args[]) throws IOException {
        String inputFilePath = "E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\3.csv\\3.csv";
        String outputFilePath = "E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\originalSZoutput\\originalSZoutputdata2（1).csv";
        long startTime = System.currentTimeMillis();
        orignalSZCompression(inputFilePath, outputFilePath);
        long endTime = System.currentTimeMillis();
        double time = endTime - startTime;
        System.out.println("压缩时间为：" + time / 1000 + "s");
    }

    public static void orignalSZCompression(String originalPath, String compressionPath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(originalPath));
        String line = null;
        String lineSplit[] = null;//每一行按逗号分隔后的列数组
        String objectId = "";//对象id
        CurveObject cb = null;//用于存放一个对象压缩完之后的数据.
        int num = 0;//记录每个对象的行数
        AObject tempObject = null;
        while ((line = reader.readLine()) != null) {
            lineSplit = line.split(",");
            if (lineSplit[0].equals("startTime")||lineSplit[0].equals("TIME"))//剔除列名
                continue;
            //新建一个AObject来存储每一行的值
            tempObject = new AObject();
            //添加该行的起始时间和对象id
            tempObject.setStartTime(lineSplit[0]);
            tempObject.setEndTime(lineSplit[1]);
            tempObject.setObjId(lineSplit[2]);
            //添加该行的指标
            for (int i = 3; i < lineSplit.length; i++) {
                tempObject.addVal(lineSplit[i]);
            }
            printBit(compressionPath,curveFittingCompressionBaseLine(tempObject.getQuotaList()));
            num++;
        }
        //对最后一个对象进行压缩
        printBit(compressionPath,curveFittingCompressionBaseLine(tempObject.getQuotaList()));
    }


    /**
     * @描述 压缩一列数据
     * @参数 [columList]
     * @返回值 Model.ImplementsPack.Bit
     * @创建人 kcx
     * @创建时间 2019/4/9
     * @修改人和其它信息
     */
    public static Bit curveFittingCompressionBaseLine(ArrayList<String> columnList) {
        Bit res = new Bit();
        //求出中值和数据半径。
        float[] coffecient = sz.getRelativeCoffecients(columnList);
        float middleVal = coffecient[0];
        int byteCount = (int) coffecient[1];
        //存储浮点数转换成的字节数组
        byte[] floatBytes = null;
        //定义变量存储当前值
        float currentVal = Float.parseFloat(columnList.get(0));
        //用一个数组存储解压后的值。
        ArrayList<Float> deComList = new ArrayList<>();
        byte[] trunCatBytes = null;
        int leadZreoCount = 0;
        if (byteCount < 4) {
            trunCatBytes = sz.floatTruncatComprression(currentVal, middleVal, byteCount);
            deComList.add(sz.truncatBytesToFloat(byteCount, trunCatBytes));
        } else {
            trunCatBytes = bc.floatToByteArray(currentVal);
            deComList.add(currentVal);
        }
        //把第一个值存入原值数组中
        for (byte val : trunCatBytes) res.byteList.add(val);
//        因为存的是原值，所以存00.
        res.addBit("11");
        float PNFVal = 0;//第一种曲线，前面相邻的值
        float LCFVal = 0;//第二种曲线，线性拟合的值
        float QCFVal = 0;//第三种曲线，二次曲线拟合的值。

        //阈值为当前值的p倍
        float e = 0;
        //记录当前误差最小的预测值
        float predictiveVal = 0;
        //用于记录误差最小值的类型
        String curveType = "";

        for (int i = 1; i < columnList.size(); i++) {//从第二个值开始判断
            currentVal = Float.parseFloat(columnList.get(i));
            //阈值为当前值的p倍
            e = Math.abs(currentVal * p);
            if (i == 1) {
                predictiveVal = deComList.get(0);//如果是第二个值，这只需要计算PNF得预测值。
                curveType = "00";
            } else if (i == 2) {//如果是第三个值，则只需要计算PNF和LCF的值
                PNFVal = deComList.get(1);
                LCFVal = 2 * PNFVal - deComList.get(0);
                if (Math.abs(LCFVal - currentVal) < Math.abs(PNFVal - currentVal)) {
                    predictiveVal = LCFVal;
                    curveType = "01";
                } else {
                    predictiveVal = PNFVal;
                    curveType = "00";
                }
            } else {//否则要计算前三个预测值
                PNFVal = deComList.get(i - 1);
                LCFVal = 2 * PNFVal - deComList.get(i - 2);
                QCFVal = 3 * PNFVal - 3 * deComList.get(i - 2) + deComList.get(i - 3);
                curveType = TheMinDiffVal.getMinDiffVal(PNFVal, LCFVal, QCFVal, currentVal);
                if (curveType.equals("00")) predictiveVal = PNFVal;
                else if (curveType.equals("01")) predictiveVal = LCFVal;
                else predictiveVal = QCFVal;
            }


            if (Math.abs(predictiveVal - currentVal) <= e) {//如果预测值与原值的误差在阈值范围之内，那么就存储预测的值，否则存储原值
                res.addBit(curveType);//添加当前存的类型。
                deComList.add(predictiveVal);
            } else {//存储原值
                res.addBit("11");
                if (byteCount < 4) {
                    trunCatBytes = sz.floatTruncatComprression(currentVal, middleVal, byteCount);
                    deComList.add(sz.truncatBytesToFloat(byteCount, trunCatBytes));
                } else {
                    trunCatBytes = bc.floatToByteArray(currentVal);
                    deComList.add(currentVal);
                }
                leadZreoCount = sz.leadingZeroCount(trunCatBytes, deComList.get(i - 1));
                if (leadZreoCount > 0)
                    trunCatBytes = sz.leadingZeroCompression(deComList.get(i), byteCount, leadZreoCount);
                for (byte val : trunCatBytes) res.byteList.add(val);
            }
        }
        return res;
    }


    /**
     * @描述 以二进制的形式输出每个对象压缩后的结果.
     * @参数 [outputFilePath, cb]
     * @返回值 void
     * @创建人 kcx
     * @创建时间 2018/10/31
     * @修改人和其它信息
     */
    public static  void printObject(String outputFilePath, CurveObject cb) throws IOException {
        //逐行输出
        for (int i = 0; i < cb.getBitList().size(); i++) {
            Bit bit = cb.getBitList().get(i);
            printBit(outputFilePath,bit);
        }
    }

    /**
     * @描述 以二进制的形式输出每列压缩后的结果.
     * @参数 [outputFilePath, cb]
     * @返回值 void
     * @创建人 kcx
     * @创建时间 2018/10/31
     * @修改人和其它信息
     */
    public static void printBit(String outputFilePath,Bit bit) throws IOException {
        BufferedOutputStream br = new BufferedOutputStream(new FileOutputStream(outputFilePath, true));//true代表追加输出
            //输出预测标志
            byte[] bytes = new byte[bit.b.size()];
            for (int j = 0; j < bit.b.size(); j++) {
                bytes[j] = bit.b.get(j);
            }
            br.write(bytes);
            //输出不可预测值
            for (byte val : bit.byteList) {
                br.write(val);
            }
            br.flush();
            br.close();
    }
}

