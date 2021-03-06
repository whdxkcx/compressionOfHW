package LossyCompression.Implements;

import LossyCompression.Interface.LossyCompression;
import Model.Abstract.Spot;
import Model.ImplementsPack.*;
import Tools.InterfacePack.SpotTools;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class RealTimeCompression implements LossyCompression {

    private static int columnCount = 0;//The number of quotas.
    private static boolean isFlag = true;
    public static float p = 0.10f;
    public TypeOfFile typeCollection;
    public SpotTools spt;
    //统计压缩后压缩率变大的列数。
    public int comCount = 0;
    ArrayList<Integer> indexList = new ArrayList<>();//保存每个对象新的列索引
    boolean isFirstObject = true;//判断是否为第一更新列索引的对象

    public void setSpt(SpotTools spt) {
        this.spt = spt;
    }

    public void setTypeCollection(TypeOfFile typeCollection) {
        this.typeCollection = typeCollection;
    }

    @Override
    public void revolvingDoorCompression(String originalPath, String compressionPath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(originalPath));
        String line = null;
        String lineSplit[] = null;
        String objectId = "";
        AfterObject afo = null;
        int num = 0;
        ArrayList<AObject> allList = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            lineSplit = line.split(",");
            if (lineSplit[0].equals("startTime"))
                continue;
            if (isFlag) columnCount = lineSplit.length - 3;
            AObject tempObject = new AObject();
//            ArrayList<String> tempList = new ArrayList<>();
            tempObject.setStartTime(lineSplit[0]);
            tempObject.setEndTime(lineSplit[1]);
            tempObject.setObjId(lineSplit[2]);
            if (objectId.equals(""))
                objectId = lineSplit[2];
            else if (!objectId.equals(lineSplit[2])) {
                afo = compressionBaseObject(allList);
                printAfterObject(compressionPath, afo, num);
                allList.clear();
                objectId = lineSplit[2];
                num = 0;
            }
            for (int i = 3; i < lineSplit.length; i++) {
                tempObject.addVal(lineSplit[i]);
            }
            num++;
            allList.add(tempObject);
        }
        afo = compressionBaseObject(allList);
        printAfterObject(compressionPath, afo, num);
        System.out.println(comCount);
    }

    @Override
    public void bestCurveFittingCompression(String originalPath, String compressionPath) throws IOException {

    }


    //输出到文件
    public void printAfterObject(String filePath, AfterObject afo, int num) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
//        out.write(num+",");
        out.write(afo.getStartTime() + ",");
        out.write(afo.getEndTime() + ",");
        out.write(afo.getObjId() + ",");
//        for (ArrayList<Spot> spotList : afo.getQuotaList()) {
        for (int i = 0; i < afo.getQuotaList().size(); i++) {
            ArrayList<Spot> spotList = afo.getQuotaList().get(i);
            if (TypeOfFile.typeList[indexList.get(i)] == 1) {
                //如果整列只有一个元素，并且整列为0，那么输出空字符串。
                if (spotList.size() == 1 && ((fSubSpot) spotList.get(0)).getY() == 0 && ((fSubSpot) spotList.get(0)).getZ() == 0) {
                    out.write("");
                } else
                    for (int j = 0; j < spotList.size(); j++) {
                        if (j == spotList.size() - 1)
                            out.write(spotList.get(j) + "");
                        else
                            out.write(spotList.get(j) + ";");
                    }
            } else {
                //如果整列只有一个元素，并且整列为0，那么输出空字符串。
                if (spotList.size() == 1 && ((ISubSpot) spotList.get(0)).getY() == 0 && ((ISubSpot) spotList.get(0)).getZ() == 0) {
                    out.write("");
                } else
                    for (int j = 0; j < spotList.size(); j++) {
                        if (j == spotList.size() - 1)
                            out.write(spotList.get(j) + "");
                        else
                            out.write(spotList.get(j) + ";");
                    }
            }
            out.write(",");
        }
        out.newLine();
        out.flush();
        out.close();
    }


    /**
     * @描述 对一个对象的所有数据进行压缩.
     * @参数 [objectList]
     * @返回值 Model.ImplementsPack.AfterObject
     * @创建人 kcx
     * @创建时间 2018/10/31
     * @修改人和其它信息
     */
    public AfterObject compressionBaseObject(ArrayList<AObject> objectList) {

        AfterObject afo = new AfterObject();
        afo.setQuotaList(new ArrayList<>());
        afo.setStartTime(objectList.get(0).getStartTime());
        afo.setEndTime(objectList.get(objectList.size() - 1).getEndTime());
        afo.setObjId(objectList.get(0).getObjId());

        //每一个元素是一列。
        @SuppressWarnings("unchecked")
        Column[] allLineList = new Column[columnCount];
        //数组初始化
        for (int i = 0; i < columnCount; i++) {
            allLineList[i] = new Column();
            allLineList[i].setIndex(i);
        }


        //遍历每个对象，行转列。
        for (AObject ao : objectList) {
            for (int i = 0; i < columnCount; i++)
                allLineList[i].addVal(ao.getVal(i));
        }

        //对列按照标准差和中间值排序
        Arrays.sort(allLineList, new ColumnComparator());

        //存储排序后的列索引。
        if (isFirstObject) {
            for (int i = 0; i < columnCount; i++)
                indexList.add(allLineList[i].getIndex());
            isFirstObject = false;
        } else
            for (int i = 0; i < columnCount; i++)
                indexList.set(i, allLineList[i].getIndex());


//        for (ArrayList<String> lineList : allLineList) {
        for (int i = 0; i < columnCount; i++) {
            ArrayList<Spot> lineResultList = null;
            if (TypeOfFile.typeList[allLineList[i].getIndex()] == 1)
                lineResultList = revolvingDoorBaseLine(allLineList[i].getValList(), TypeOfFile.thresholds[i]);
            else
                lineResultList = revolvingDoorBaseLineforInteger(allLineList[i].getValList(), TypeOfFile.thresholds[i]);
            afo.getQuotaList().add(lineResultList);
        }
        return afo;
    }


    /**
     * @描述 对一个浮点列进行旋转门压缩
     * @参数 [columnList, threshold]
     * @返回值 java.util.ArrayList<Model.Abstract.Spot>
     * @创建人 kcx
     * @创建时间 2018/10/31
     * @修改人和其它信息
     */
    //It's for floating point number.
    public ArrayList<Spot> revolvingDoorBaseLine(ArrayList<String> columnList, double thresjold) {
        if (columnList == null || columnList.size() == 0)
            return null;
        //使用振幅的百分之一
        float e = 0f;
        float newValue = Float.parseFloat(columnList.get(0));
        //使用动态阈值
        if (newValue == 0) e = 0.05f;
        else e = Math.abs(newValue) * p;
        int spotCount = 0;
        ArrayList<Spot> resultList = new ArrayList<>();
        fSubSpot upSpot = null;
        fSubSpot downSpot = null;
        fSubSpot nowSpot = null;
        fSubSpot preSpot = null;
        try {
            upSpot = new fSubSpot(0, Float.parseFloat(columnList.get(0)) + e);
            downSpot = new fSubSpot(0, Float.parseFloat(columnList.get(0)) - e);
            nowSpot = new fSubSpot(0, Float.parseFloat(columnList.get(0)));
        } catch (Exception ec) {
            System.out.println("What is it?:" + columnList.get(0));
            ec.printStackTrace();
            return null;
        }
        resultList.add(nowSpot);
        spotCount++;
        double upSlope = 0;
        double downSlope = 0;
        upSlope = -Float.MAX_VALUE;
        downSlope = Float.MAX_VALUE;

        //原始列的大小
        double oriLen = 0;
        //压缩后的列的大小
        double comLen = 0;

        oriLen = columnList.get(0).length();
        for (int i = 1; i < columnList.size(); i++) {
            oriLen += columnList.get(i).length();
            nowSpot = new fSubSpot(i, Float.parseFloat(columnList.get(i)));
            float tempUpSlope = spt.slopCalculation(upSpot, nowSpot);
            float tempDownSlope = spt.slopCalculation(downSpot, nowSpot);
            if (tempUpSlope > upSlope)
                upSlope = tempUpSlope;
            if (tempDownSlope < downSlope)
                downSlope = tempDownSlope;
            //最后一个值依然在斜率范围之内。
            if (i == columnList.size() - 1 && downSlope >= upSlope) {
                preSpot = (fSubSpot) resultList.get(resultList.size() - 1);
                spotCount++;
                preSpot.setX(spotCount);
                preSpot.setZ(Float.parseFloat(columnList.get(i)));//行结束点，也是一个段结束点
                comLen += (preSpot + "").length();//在段结束点统计压缩后的字符串长度。
                preSpot.flag = false;
            }


            //当下斜率小于上斜率时
            else if (downSlope < upSlope) {
                preSpot = (fSubSpot) resultList.get(resultList.size() - 1);
                //判断这是范围之外的点有没有超出个数。。
                if(preSpot.addVal(Float.parseFloat(columnList.get(i - 1))))
                    continue;
                //存储元素个数。
                preSpot.setX(spotCount);
                preSpot.setZ(Float.parseFloat(columnList.get(i - 1)));//段结束点2
                comLen += (preSpot + "").length();//在段结束点统计压缩后的字符串长度。
                //重新设置动态阈值
                newValue = Float.parseFloat(columnList.get(i));
                if (newValue == 0) e = 0.05f;
                else e = Math.abs(newValue) * p;
                //重新计数
                spotCount = 1;
                upSpot.setX(i);
                upSpot.setY(Float.parseFloat(columnList.get(i)) + e);
                downSpot.setX(i);
                downSpot.setY(Float.parseFloat(columnList.get(i)) - e);

                //初始值的元素个数为
                nowSpot = new fSubSpot(i, Float.parseFloat(columnList.get(i)));
                resultList.add(nowSpot);
                //如果是最后一个值的时候发生超出行为，那么把x设为元素个数1.
                if (columnList.size() - 1 == i) {
                    nowSpot.setX(spotCount);//段结束点3
                    comLen += (preSpot + "").length();//在段结束点统计压缩后的字符串长度。
                }
                //重新初始化一个斜率。
                upSlope = -Float.MAX_VALUE;
                downSlope = Float.MAX_VALUE;
            } else spotCount++;//否则计数加一
        }
        if (comLen / oriLen > 0.5)
            comCount++;
//        System.out.print(String.format("%.2f",comLen/oriLen)+",");
        return resultList;
    }

    /**
     * @描述 对一个整型列进行压缩.
     * @参数 [columnList, threshold]
     * @返回值 java.util.ArrayList<Model.Abstract.Spot>
     * @创建人 kcx
     * @创建时间 2018/10/31
     * @修改人和其它信息
     */
    //It's for Integer number
    public ArrayList<Spot> revolvingDoorBaseLineforInteger(ArrayList<String> columnList, double threshold) {

        if (columnList == null || columnList.size() == 0)
            return null;
        float e = (float) threshold / 20;
        float newValue = Float.parseFloat(columnList.get(0));
        if (newValue == 0) e = 0.05f;
        else e = Math.abs(newValue) * p;
        ArrayList<Spot> resultList = new ArrayList<>();
        fSubSpot upSpot = null;
        fSubSpot downSpot = null;
        ISubSpot nowSpot = null;
        ISubSpot preSpot = null;
        //统计当前阶段的数据的个数。
        int spotCount = 0;
        try {
            upSpot = new fSubSpot(0, Float.parseFloat(columnList.get(0)) + e);
            downSpot = new fSubSpot(0, Float.parseFloat(columnList.get(0)) - e);
            nowSpot = new ISubSpot(0, Long.parseLong(columnList.get(0)));
        } catch (Exception ec) {
            System.out.println("What is it?:" + columnList.get(0));
            ec.printStackTrace();
            return null;
        }
        resultList.add(nowSpot);
        spotCount++;
        double upSlope = Integer.MIN_VALUE;
        double downSlope = Integer.MAX_VALUE;
        upSlope = -Float.MAX_VALUE;
        downSlope = Float.MAX_VALUE;

        //原始列的大小
        double oriLen = 0;
        //压缩后的列的大小
        double comLen = 0;
        oriLen = columnList.get(0).length();
        for (int i = 1; i < columnList.size(); i++) {
            oriLen += columnList.get(i).length();
            nowSpot = new ISubSpot(i, (long) Float.parseFloat(columnList.get(i)));
            float tempUpSlope = spt.slopCalculation(upSpot, nowSpot);
            float tempDownSlope = spt.slopCalculation(downSpot, nowSpot);
            if (tempUpSlope > upSlope)
                upSlope = tempUpSlope;
            if (tempDownSlope < downSlope)
                downSlope = tempDownSlope;
            if (i == columnList.size() - 1 && downSlope >= upSlope) {//如果到达最后一个元素，并且没有超出阈值范围
                preSpot = (ISubSpot) resultList.get(resultList.size() - 1);
                spotCount++;
                preSpot.setZ(Long.parseLong(columnList.get(i)));//段结束点1
                comLen += (preSpot + "").length();//在段结束点统计压缩后的字符串长度。
                preSpot.setX(spotCount);
                preSpot.flag = false;
            } else if (downSlope < upSlope) {//数据超出阈值范围
                preSpot = (ISubSpot) resultList.get(resultList.size() - 1);
                //判断这是范围之外的点有没有超出个数。。
                if(preSpot.addVal(Long.parseLong(columnList.get(i - 1))))
                    continue;
                //存储元素个数。
                preSpot.setZ(Long.parseLong(columnList.get(i - 1)));
                preSpot.setX(spotCount);//段结束点2
                comLen += (preSpot + "").length();//在段结束点统计压缩后的字符串长度。
                //重新设置动态阈值
                newValue = Float.parseFloat(columnList.get(i));
                if (newValue == 0) e = 0.05f;
                else e = Math.abs(newValue) * p;
                //重新设置元素个数
                spotCount = 1;

                //重新设置上斜率点和下斜率点
                upSpot.setX(i);
                upSpot.setY(Long.parseLong(columnList.get(i)) + e);
                downSpot.setX(i);
                downSpot.setY(Long.parseLong(columnList.get(i)) - e);
                //重新设置起始点
                nowSpot = new ISubSpot(i, (long) Float.parseFloat(columnList.get(i)));
                resultList.add(nowSpot);
                //如果是最后一个值的时候发生超出行为，那么把x设为元素个数1.
                if (columnList.size() - 1 == i) {
                    nowSpot.setX(spotCount);//行结束点，也是段结束点3。
                    comLen += (preSpot + "").length();//在段结束点统计压缩后的字符串长度。
                }
                //重新给上下斜率初始化一个值。
                upSlope = -Float.MAX_VALUE;
                downSlope = Float.MAX_VALUE;
            } else spotCount++;//元素个数加1
        }
        if (comLen / oriLen > 0.5)
            comCount++;
//            System.out.print(String.format("%.2f",comLen/oriLen)+",");
        return resultList;
    }

    public static float thresholdGenerate() {
        return 100f;
    }
}
