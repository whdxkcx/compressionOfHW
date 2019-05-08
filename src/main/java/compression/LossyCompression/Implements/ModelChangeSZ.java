package LossyCompression.Implements;

import Model.ImplementsPack.Bit;
import Model.ImplementsPack.TypeOfFile;
import Tools.Inplements.ByteConverterImpl;
import Tools.InterfacePack.SpotTools;
import Tools.InterfacePack.TheMinDiffVal;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @创建人 kcx
 * @创建时间 2019/4/10
 * @描述
 */
public class ModelChangeSZ {
    private static int columnCount = 4749;//The number of quotas.
    private static boolean isFlag = true;
    public static float p = 0.10f;
    public static int k = 20;
    public static int count1 = 0;
    public static int count2 = 0;
    public static int count3 = 0;
    public static HashMap<String, Integer> radioMap = new HashMap<>();
    public TypeOfFile typeCollection;
    public SpotTools spt;
    public static SZCompression sz = new SZCompression();
    static ByteConverterImpl bc = new ByteConverterImpl();
    static{
        radioMap.put("00",0);radioMap.put("01",0);radioMap.put("10",0);radioMap.put("11",0);radioMap.put("0",0);
    }
    /**
     * @描述 四种模型和哈夫曼编码的压缩
     * @参数 [rowOfGroup, ws]
     * @返回值 void
     * @创建人 kcx
     * @创建时间 2019/3/20
     * @修改人和其它信息
     */
    public static Bit modelChangeSZCompression(ArrayList<String> columnList, ArrayList<String> ws) {
        Bit res = new Bit();
        //求出中值和数据半径。
        float[] coffecient = sz.getRelativeCoffecients(columnList);
        float middleVal = coffecient[0];
        int byteCount = (int) coffecient[1];
        //定义变量存储当前值
        float currentVal = Float.parseFloat(columnList.get(0));
        //用一个数组存储解压后的值。
        ArrayList<Float> deComList = new ArrayList<>();
        byte[] trunCatBytes = null;//存储截断后的数据
        int leadZreoCount = 0;
        if (byteCount < 4) {//如果表示所需的最小位数小于e
            trunCatBytes = sz.floatTruncatComprression(currentVal, middleVal, byteCount);
            deComList.add(sz.truncatBytesToFloat(byteCount, trunCatBytes));
        } else {
            trunCatBytes = bc.floatToByteArray(currentVal);
            deComList.add(currentVal);
        }
        //把第一个值存入原值数组中
        for (byte val : trunCatBytes) res.byteList.add(val);
//        因为存的是原值，所以存00.
        res.addPredictBit("0");
        float PNFVal = 0;//第一种曲线，前面相邻的值
        float LCFVal = 0;//第二种曲线，线性拟合的值
        float QCFVal = 0;//第三种曲线，二次曲线拟合的值。
        float RFLVal = 0;//第四种曲线，线性拟合的值。


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
            if (i > k) {//要线性回归预测的值。
                for (int j = 0; j < k; j++)
                    RFLVal += Float.parseFloat(ws.get(j)) * deComList.get(i - k + j);
                if (Math.abs(RFLVal - currentVal) < Math.abs(predictiveVal - currentVal)) {
                    predictiveVal = RFLVal;
                    curveType = "11";
                }
            }
            if (Math.abs(predictiveVal - currentVal) <= e) {//如果预测值与原值的误差在阈值范围之内，那么就存储预测的值，否则存储原值
                res.addBit(curveType);//添加当前存的类型。
                radioMap.put(curveType, radioMap.get(curveType) + 1);
                res.addPredictBit("1");
                deComList.add(predictiveVal);
            } else {//存储原值
                radioMap.put("0", radioMap.get("0") + 1);
                res.addPredictBit("0");//表示不可预测
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

    public static void printBitAfterCompression(String outFilePath, Bit bit) throws IOException {
        BufferedOutputStream br = new BufferedOutputStream(new FileOutputStream(outFilePath, true));//true代表追加输出
        //输出预测标志
        byte[] bytes = new byte[bit.predictB.size()];
        for (int j = 0; j < bit.predictB.size(); j++) {
            bytes[j] = bit.predictB.get(j);
        }
        br.write(bytes);
        //输出模型标志
        bytes=new byte[bit.b.size()];
        for(int j=0;j<bit.b.size();j++)
            bytes[j]=bit.b.get(j);
        br.write(bytes);
        //输出不可预测值
        for (byte val : bit.byteList) {
            br.write(val);
        }
    }
}
