package LossyCompression.Implements;

import LossyCompression.Interface.LossyCompression;
import Model.Abstract.Spot;
import Model.ImplementsPack.*;
import Tools.Inplements.ByteConverterImpl;
import Tools.InterfacePack.SpotTools;
import Tools.InterfacePack.byteConverter;

import java.io.*;
import java.util.ArrayList;

public class BestCurveFittingCompression implements LossyCompression {
    private static int columnCount = 0;//The number of quotas.
    private static boolean isFlag = true;
    public static float p=0.05f;
    public TypeOfFile typeCollection;
    public SpotTools spt;
    ByteConverterImpl  bc=new ByteConverterImpl();


    public static void main(String[] args) throws  IOException{
        BestCurveFittingCompression bcfc=new BestCurveFittingCompression();
        String inputFilePath="E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\input\\UCELLnew.csv";
        String outputFilePath="E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\curveOutpout\\output1204.csv";
//        BufferedReader reader=new BufferedReader(new FileReader(inputFilePath));
//        String line = null;
//        ArrayList<String> inputList=new ArrayList<>();
//        while ((line=reader.readLine())!=null)
//            inputList.add(line);
//        Bit bit=bcfc.curveFittingCompressionBaseLine(inputList);
//        bcfc.printBit(outputFilePath,bit);
        long startTime = System.currentTimeMillis();
        bcfc.bestCurveFittingCompression(inputFilePath,outputFilePath);
        long endTime = System.currentTimeMillis();
        double time = endTime - startTime;
        System.out.println("压缩时间为：" + time/1000 + "s");
    }

    @Override
    public void bestCurveFittingCompression(String originalPath, String compressionPath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(originalPath));
        String line = null;
        String lineSplit[] = null;//每一行按逗号分隔后的列数组
        String objectId = "";//对象id
        CurveObject cb=null;//用于存放一个对象压缩完之后的数据.
        int num=0;//记录每个对象的行数
        ArrayList<AObject> allList = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            lineSplit = line.split(",");
            if (lineSplit[0].equals("startTime"))//剔除列名
                continue;
            if (isFlag) {//统计指标个数
                columnCount = lineSplit.length - 3;
                isFlag=false;
            }

            //新建一个AObject来存储每一行的值
            AObject tempObject = new AObject();
            //添加该行的起始时间和对象id
            ArrayList<String> tempList = new ArrayList<>();
            tempObject.setStartTime(lineSplit[0]);
            tempObject.setEndTime(lineSplit[1]);
            tempObject.setObjId(lineSplit[2]);

            //通过对象id来判断一个对象的数据是否结束
            if (objectId.equals(""))
                objectId = lineSplit[2];
            else if (!objectId.equals(lineSplit[2])) {//如果当前对象id与上一个对象id不一样,说明上一个对象的数据已经读取完了,对其进行压缩.
                cb = compressionBaseObject(allList);
                cb.setRowCount(num);//设置行数
                printObject(compressionPath, cb);
                allList.clear();//清空对象的列表
                objectId = lineSplit[2];
                num=1;
            }
            //添加该行的指标
            for (int i = 3; i < lineSplit.length; i++) {
                tempList.add(lineSplit[i]);
            }
            tempObject.setQuotaList(tempList);

            //把当前行添加到对象中
            allList.add(tempObject);
        }
        //对最后一个对象进行压缩
        cb = compressionBaseObject(allList);
        cb.setRowCount(num);//设置行数
        printObject(compressionPath, cb);
    }

    /**

     *@描述 对一个对象的数据进行压缩.

     *@参数  [objectList]

     *@返回值  Model.ImplementsPack.CurveObject

     *@创建人  kcx

     *@创建时间  2018/10/31

     *@修改人和其它信息

     */
    public CurveObject compressionBaseObject(ArrayList<AObject> objectList) {
        //初始化存储压缩结果的对象
        CurveObject cb = new CurveObject();
        cb.setStartTime(objectList.get(0).getStartTime());
        cb.setEndTime(objectList.get(objectList.size() - 1).getEndTime());
        cb.setObjId(objectList.get(0).getObjId());

        @SuppressWarnings("unchecked")
        ArrayList<String>[] allLineList = new ArrayList[columnCount];//用于存放每一列的原始数据.
        for (int i = 0; i < columnCount; i++) {
            ArrayList<String> tempList = new ArrayList<>();
            allLineList[i] = tempList;
        }
        //新建一个代表每一列是否为全零列的数组，并初始化为true
        ArrayList<Boolean> isZero=new ArrayList<>();
        for(int i=0;i<columnCount;i++)
            isZero.add(true);

        for (AObject ao : objectList) {
            for (int i = 0; i < columnCount; i++) {
                String str=ao.getQuotaList().get(i).equals("NA") ? "0" : ao.getQuotaList().get(i);
                allLineList[i].add(str);
                if(isZero.get(i))
                    isZero.set(i,Float.parseFloat(str)==0f?true:false);
            }
        }
        for(int i=0;i<columnCount;i++)
            cb.addBit(isZero.get(i)?"1":"0");
        //对每一列的数据进行压缩,并存储压缩结果至cb对象中
        for (int i=0;i<columnCount;i++) {
            cb.getBitList().add(curveFittingCompressionBaseLine(allLineList[i],isZero.get(i)));
        }
        return cb;
    }


    /**

     *@描述 以二进制的形式输出每个对象压缩后的结果.

     *@参数  [outputFilePath, cb]

     *@返回值  void

     *@创建人  kcx

     *@创建时间  2018/10/31

     *@修改人和其它信息

     */
    public void  printObject(String outputFilePath,CurveObject cb) throws  IOException{
        BufferedOutputStream  br=new BufferedOutputStream(new FileOutputStream(outputFilePath,true));
        byte  bytes[]=null;//用于存放将要输出的byte数组
        //第1步,输出该对象的行数
        bytes=ByteConverterImpl.getBytes(cb.getRowCount());
        br.write(bytes);
        //第2步,输出时间和对象id.
        bytes=(cb.getStartTime()+cb.getEndTime()+cb.getObjId()).getBytes();
        br.write(bytes);
        //输出每一列的类型
        bytes=new byte[cb.b.size()];
        for(int i=0;i<bytes.length;i++){
            bytes[i]=cb.b.get(i);
        }
        br.write(bytes);
        //第3步,输出每一列
        for(Bit bit:cb.getBitList()){
            //如果是全零列，则不用输出
            if(bit.isZero()) continue;
            //第3.1步,输出该列的曲线类型.
            bytes=new byte[bit.b.size()];
            for(int i=0;i<bytes.length;i++){
                bytes[i]=bit.b.get(i);
            }
            br.write(bytes);
            //第3.2步输出该列的浮点型数组.（并在这一步进行进行浮点数截断）
            float[] f=new float[bit.valList.size()];
            int k=0;
            //浮点数截断1：所有的值先减去中值得到一个新的原值数组（更接近于零），称为标准化数组
            float middleVal=bit.getMiddleVal();
            for(float val:bit.valList) f[k++]=val-middleVal;

            //浮点数截断2：求出数据半径在误差范围内表示所需尾数的最小位数，因为它是标准化数组的最大值，所以可以把这个位数当作是所有尾数的位数表示。
            float radius=bit.getRadiusVal();
            int count=getMinNumberOfMantissaBits(radius,p);//获取该列浮点数表示的最小字节数。
            //第3.2.1步,输出数组长度
            br.write(f.length);
            //输出middleVal，用于把标准化数组变成原数组。
            br.write(bc.floatToByteArray(middleVal));
            //输出每一个浮点数表示的字节数
            br.write(count);
            //第3.2.2步,截断标准化数组.
            bytes=bc.floatArrayToByteArray(f,count);
            //第3.2.3步，对截断后的数组进行前导零的压缩
            LeadingZero lz=leadingZeroCompression(bytes,count);
            //第3.3.4步，输出前导零压缩后的数组
              //输出每一个数值前导零的个数
            bytes=new byte[lz.b.size()];
            for(int i=0;i<bytes.length;i++){
                bytes[i]=lz.b.get(i);
            }
            br.write(bytes);
              //输出压缩的字节数组
            bytes=new byte[lz.valList.size()];
            for(int i=0;i<bytes.length;i++){
                bytes[i]=lz.valList.get(i);
            }
            br.write(bytes);
        }
        br.flush();
        br.close();
    }


    /**

     *@描述  前导零压缩，把截断后的数值数组再做一次前导零压缩。

     *@参数  [bytes, count]

     *@返回值  Model.ImplementsPack.LeadingZero

     *@创建人  kcx

     *@创建时间  2018/11/30

     *@修改人和其它信息

     */
    public LeadingZero leadingZeroCompression(byte[] bytes,int count){

        int len=bytes.length/count;
        LeadingZero lz=new LeadingZero();
        lz.addBit("00");
        //添加第一个数
        for(int i=0;i<count;i++){
            lz.valList.add(bytes[i]);
        }

        int num=0;//表示前导零的字节数
        for(int i=1;i<len;i++){
            num=0;
            for(int j=0;j<count;j++){
                if((bytes[(i-1)*count+j]^bytes[i*count+j])==0) num++;
                else break;
            }
            if(num==0)  lz.addBit("00");
            else if(num==1) lz.addBit("01");
            else  if(num==2) lz.addBit("10");
            else  if(num==3) lz.addBit("11");

            //添加数值
            for(int k=num;k<count;k++){
                lz.valList.add(bytes[i*count+k]);
            }
        }
        return lz;

    }





    /**

     *@描述   在确定误差的情况下，获取一个浮点数表示的最小的字节数。

     *@参数  [radius]

     *@返回值  int

     *@创建人  kcx

     *@创建时间  2018/11/27

     *@修改人和其它信息

     */
    public int getMinNumberOfMantissaBits(float radius,float e){
        int diff=Exp(radius)-Exp(e);
        int RQ_MBits=0;
        if(diff<0) RQ_MBits=0;
        else if(diff>23) RQ_MBits=23;
        else RQ_MBits=diff;
        int count=(1+8+RQ_MBits)%8==0?(1+8+RQ_MBits)/8:((1+8+RQ_MBits)/8+1);
        if(count>4)
            System.out.println(RQ_MBits+":"+count);
        return count;
    }
    /**

     *@描述  获取一个float数值的指数的值

     *@参数  [f]

     *@返回值  int

     *@创建人  kcx

     *@创建时间  2018/11/27

     *@修改人和其它信息

     */
    public int Exp(float f){
        int intBits=Float.floatToIntBits(f);
        return ((intBits>>>23)&255)-127;
    }

    /**

     *@描述  把一个列的压缩结果输出到一个文件中

     *@参数  [filePath, bit]

     *@返回值  void

     *@创建人  kcx

     *@创建时间  2018/10/31

     *@修改人和其它信息

     */
    public void printBit(String filePath,Bit bit) throws  IOException{
        BufferedOutputStream  br=new BufferedOutputStream(new FileOutputStream(filePath));
        byte  bt[]=new byte[bit.b.size()];
        for(int i=0;i<bt.length;i++){
            bt[i]=bit.b.get(i);
        }
        br.write(bt);
        br.write(";".getBytes());
        float[] f=new float[bit.valList.size()];
        int k=0;
        for(float val:bit.valList) f[k++]=val;
        br.write(bc.floatArrayToByteArray(f));
        br.flush();
        br.close();
    }


    /**

     *@描述  对一列数值进行曲线拟合压缩

     *@参数  [columnList]

     *@返回值  Model.ImplementsPack.Bit

     *@创建人  kcx

     *@创建时间  2018/10/31

     *@修改人和其它信息

     */
    public Bit curveFittingCompressionBaseLine(ArrayList<String>  columnList,boolean isZero){
        Bit res=new Bit();
        res.setZero(isZero);
        if(isZero) return  res;
        //定义变量存储当前值
        float currentVal=Float.parseFloat(columnList.get(0));
        //把第一个值存入原值数组中
        res.putVal(0,currentVal);
        //因为存的是原值，所以存00.
        res.addBit("00");
        float PNFVal=0;//第一种曲线，前面相邻的值
        float LCFVal=0;//第二种曲线，线性拟合的值
        float QCFVal=0;//第三种曲线，二次曲线拟合的值。

        //用一个数组存储解压后的值。
        ArrayList<Float>  deComList=new ArrayList<>();
        deComList.add(currentVal);

        //阈值为当前值的p倍
        float e=0;
        //记录当前误差最小的预测值
        float predictiveVal=0;
        //用于记录误差最小值的类型
        String curveType="";

        for(int i=1;i<columnList.size();i++){//从第二个值开始判断
            currentVal=Float.parseFloat(columnList.get(i));
            //阈值为当前值的p倍
             e=Math.abs(currentVal*p);
             if(i==1) {
                 predictiveVal=deComList.get(0);//如果是第二个值，这只需要计算PNF得预测值。
                 curveType="01";
             }
             else if(i==2){//如果是第三个值，则只需要计算PNF和LCF的值
                 PNFVal=deComList.get(1);
                 LCFVal=2*PNFVal-deComList.get(0);
                 if(Math.abs(LCFVal-currentVal)<Math.abs(PNFVal-currentVal)) {
                     predictiveVal = LCFVal;
                     curveType="10";
                 }
                 else {
                     predictiveVal = PNFVal;
                     curveType="01";
                 }
             }
             else{//否则要计算前三个预测值
                 PNFVal=deComList.get(i-1);
                 LCFVal=2*PNFVal-deComList.get(i-2);
                 QCFVal=3*PNFVal-3*deComList.get(i-2)+deComList.get(i-3);
                 if(Math.abs(QCFVal-currentVal)<Math.max(Math.abs(PNFVal-currentVal),Math.abs(LCFVal-currentVal))) {
                     predictiveVal = QCFVal;
                     curveType="11";
                 }
                 else  if(Math.abs(LCFVal-currentVal)<Math.abs(PNFVal-currentVal)) {
                     predictiveVal = LCFVal;
                     curveType="10";
                 }
                 else {
                     predictiveVal = PNFVal;
                     curveType="01";
                 }
             }
             if(Math.abs(predictiveVal-currentVal)<=e){//如果预测值与原值的误差在阈值范围之内，那么就存储预测的值，否则存储原值
                 res.addBit(curveType);//添加当前存的类型。
                 deComList.add(predictiveVal);
             }else{//存储原值
                 res.putVal(i,currentVal);
                 deComList.add(currentVal);
             }
        }
        return res;
    }




    @Override
    public void revolvingDoorCompression(String originalPath, String compressionPath) throws IOException {

    }
}
