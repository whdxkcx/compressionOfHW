package DeviationCalculation.Implements;

import DeviationCalculation.InterfacePack.RelativeDeviation;

import javax.xml.crypto.dom.DOMCryptoContext;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RelativeDeviationImpl implements RelativeDeviation {

    public static void main(String[] args) throws IOException {
        RelativeDeviation rd = new RelativeDeviationImpl();
//        System.out.println(rd.maxRelativeBiasBaseLine("E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\input\\UCELLnew.csv", "E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\DeOutput\\UCELLnew(change20De).csv"));
//          System.out.println(rd.averageDeviationRadioBaseLine("E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\input\\UCELLnew.csv", "E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\DeOutput\\UCELLnew(change20De).csv"));
        System.out.println(rd.averageDeviationBaseLine("E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\input\\UCELLnew.csv", "E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\DeOutput\\UCELLnew(change20De).csv"));
        System.out.println(rd.relativeMeanSquareDevitationBaseLine("E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\input\\UCELLnew.csv", "E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\DeOutput\\UCELLnew(change20De).csv"));

    }

    //求最大偏差：（原文件的最大峰值-恢复后文件的最大峰值）/原文件的最大峰值
    @Override
    public ArrayList<Double> maxRelativeBiasBaseLine(String originalPath, String rebuildPath) throws IOException {
        BufferedReader readerOriginal = new BufferedReader(new FileReader(originalPath));
        BufferedReader readerRebuild = new BufferedReader(new FileReader(rebuildPath));
        String lineOriginal = null;
        String lineRebuild = null;
        //存原始数据的最大值
        ArrayList<Double> originalMaxList = new ArrayList<>();
        //存还原后数据的最大值
        ArrayList<Double> rebuildMaxList = new ArrayList<>();
        //存储每一列的最大偏差值
        ArrayList<Double> resList = new ArrayList<>();

        //原文件的行字符数组
        String[] originalAr = null;
        //恢复后文件的字符数组
        String[] rebuildAr = null;
        long lineCount = 0;
        boolean firstFlag = true;



        while ((lineOriginal = readerOriginal.readLine()) != null && (lineRebuild = readerRebuild.readLine()) != null) {
            originalAr = lineOriginal.split(",");
            rebuildAr = lineRebuild.split(",");
            //求列数和对偏差值初始化
            if(firstFlag){
                lineCount=originalAr.length;
                firstFlag=false;
                //对偏差值数组进行初始化
                for(int i=0;i<lineCount-3;i++){
                    originalMaxList.add(Double.MIN_VALUE);
                    rebuildMaxList.add(Double.MIN_VALUE);
                    resList.add(Double.MIN_VALUE);
                }
            }
            //首先要去除表头
            if (originalAr[0].equals("startTime")) {
                lineOriginal = readerOriginal.readLine();
                if (lineOriginal != null)
                    originalAr = lineOriginal.split(",");
            }
            if (rebuildAr[0].equals("startTime")) {
                lineRebuild = readerRebuild.readLine();
                if (lineRebuild != null)
                    rebuildAr = lineRebuild.split(",");
            }

            //然后依次获取最大值
            for(int i=3;i<lineCount;i++){
                 double originalTemp=originalAr[i].equals("NA")?0:Double.parseDouble(originalAr[i]);
                 double rebuildTemp=rebuildAr[i].equals("NA")?0:Double.parseDouble(rebuildAr[i]);
                 if(originalTemp>originalMaxList.get(i-3)) originalMaxList.set(i-3,originalTemp);
                 if(rebuildTemp>rebuildMaxList.get(i-3)) rebuildMaxList.set(i-3,rebuildTemp);
            }
        }
           //求每一列的最大偏差
           for(int i=0;i<lineCount-3;i++){
               resList.set(i,Math.abs(originalMaxList.get(i)-rebuildMaxList.get(i))/originalMaxList.get(i));
           }
           return resList;
    }

    //求两个文件的平均偏差值比（基于列）
    @Override
    public ArrayList<Double> averageDeviationRadioBaseLine(String originalPath, String rebuildPath) throws IOException {
        BufferedReader readerOriginal = new BufferedReader(new FileReader(originalPath));
        BufferedReader readerRebuild = new BufferedReader(new FileReader(rebuildPath));
        String lineOriginal = null;
        String lineRebuild = null;

        long startTime=System.currentTimeMillis();
        String originalAr[]=readerOriginal.readLine().split(",");
        String rebuildAr[]=readerRebuild.readLine().split(",");

        int N=0;//定义N来记录总的行数
        long lineCount = 0;
        boolean firstFlag = true;
        //定义数组用来存储每一列的偏差方的和
        ArrayList<Double>  averageList=new ArrayList<>();
        //用来 存储一列得原始值的和
        ArrayList<Double> originalSumList=new ArrayList<>();
        while((lineOriginal=readerOriginal.readLine())!=null&&(lineRebuild=readerRebuild.readLine())!=null){
            originalAr=lineOriginal.split(",");
            rebuildAr=lineRebuild.split(",");
            double newData=0;
            double oldData=0;
            if(firstFlag){
                lineCount=originalAr.length;
                firstFlag=false;
                //对偏差值数组进行初始化
                for(int i=0;i<lineCount-3;i++){
                    averageList.add(0.0);
                    originalSumList.add(0.0);
                }
            }
            //首先要去除表头
            if (originalAr[0].equals("startTime")) {
                lineOriginal = readerOriginal.readLine();
                if (lineOriginal != null)
                    originalAr = lineOriginal.split(",");
            }
            if (rebuildAr[0].equals("startTime")) {
                lineRebuild = readerRebuild.readLine();
                if (lineRebuild != null)
                    rebuildAr = lineRebuild.split(",");
            }
            for(int i=3;i<lineCount;i++){
                //存在NA就转换成为0;
                oldData=originalAr[i].contains("NA")?0:Double.parseDouble(originalAr[i]);
                newData=rebuildAr[i].contains("NA")?0:Double.parseDouble(rebuildAr[i]);
                originalSumList.set(i-3,originalSumList.get(i-3)+oldData*oldData);
                averageList.set(i-3,averageList.get(i-3)+(newData-oldData)*(newData-oldData));
            }
            N++;
        }
        System.out.println(originalSumList);
        System.out.println(averageList);
        for(int i=0;i<lineCount-3;i++){
            if(i==199) {
                System.out.println("deviation:" + averageList.get(i) + "   original:" + originalSumList.get(i));
                break;
            }
            if(originalSumList.get(i)==0)
                averageList.set(i,0.0);
            else
              averageList.set(i,averageList.get(i)/originalSumList.get(i));
        }
        long endTime=System.currentTimeMillis();
        System.out.println(endTime-startTime);
        return averageList;
    }

    //求两个文件的平均偏差值
    @Override
    public ArrayList<Double> averageDeviationBaseLine(String originalPath, String rebuildPath) throws IOException {
        BufferedReader readerOriginal = new BufferedReader(new FileReader(originalPath));
        BufferedReader readerRebuild = new BufferedReader(new FileReader(rebuildPath));
        String lineOriginal = null;
        String lineRebuild = null;

        long startTime=System.currentTimeMillis();
        String originalAr[]=readerOriginal.readLine().split(",");
        String rebuildAr[]=readerRebuild.readLine().split(",");

        int N=0;//定义N来记录总的行数
        long lineCount = 0;
        boolean firstFlag = true;
        //定义数组用来存储每一列的偏差方的和
        ArrayList<Double>  averageList=new ArrayList<>();
        while((lineOriginal=readerOriginal.readLine())!=null&&(lineRebuild=readerRebuild.readLine())!=null){
            originalAr=lineOriginal.split(",");
            rebuildAr=lineRebuild.split(",");
            double newData;
            double oldData;


            if(firstFlag){
                lineCount=originalAr.length;
                firstFlag=false;
                //对偏差值数组进行初始化
                for(int i=0;i<lineCount-3;i++){
                    averageList.add(0.0);
                }
            }
            //首先要去除表头
            if (originalAr[0].equals("startTime")) {
                lineOriginal = readerOriginal.readLine();
                if (lineOriginal != null)
                    originalAr = lineOriginal.split(",");
            }
            if (rebuildAr[0].equals("startTime")) {
                lineRebuild = readerRebuild.readLine();
                if (lineRebuild != null)
                    rebuildAr = lineRebuild.split(",");
            }
            for(int i=3;i<lineCount;i++){
                //存在NA就转换成为0;
                oldData=originalAr[i].contains("NA")?0:Double.parseDouble(originalAr[i]);
                newData=rebuildAr[i].contains("NA")?0:Double.parseDouble(rebuildAr[i]);
                averageList.set(i-3,averageList.get(i-3)+Math.pow(newData-oldData, 2));
            }
            N++;
        }
        for(int i=0;i<lineCount-3;i++){
            averageList.set(i,Math.sqrt(averageList.get(i)/N));
        }
        long endTime=System.currentTimeMillis();
        System.out.println(endTime-startTime);
        return averageList;
    }

    //求两个文件的相对均方误差（按列）
    public ArrayList<Double> relativeMeanSquareDevitationBaseLine(String originalPath, String rebuildPath) throws IOException {
        BufferedReader readerOriginal = new BufferedReader(new FileReader(originalPath));
        BufferedReader readerRebuild = new BufferedReader(new FileReader(rebuildPath));
        String lineOriginal = null;
        String lineRebuild = null;

        long startTime=System.currentTimeMillis();
        String originalAr[]=readerOriginal.readLine().split(",");
        String rebuildAr[]=readerRebuild.readLine().split(",");

        int N=0;//定义N来记录总的行数
        long lineCount = 0;
        boolean firstFlag = true;
        //定义数组用来存储每一列的偏差方的和
        ArrayList<Double>  averageList=new ArrayList<>();
        while((lineOriginal=readerOriginal.readLine())!=null&&(lineRebuild=readerRebuild.readLine())!=null){
            originalAr=lineOriginal.split(",");
            rebuildAr=lineRebuild.split(",");
            double newData;
            double oldData;


            if(firstFlag){
                lineCount=originalAr.length;
                firstFlag=false;
                //对偏差值数组进行初始化
                for(int i=0;i<lineCount-3;i++){
                    averageList.add(0.0);
                }
            }
            //首先要去除表头
            if (originalAr[0].equals("startTime")) {
                lineOriginal = readerOriginal.readLine();
                if (lineOriginal != null)
                    originalAr = lineOriginal.split(",");
            }
            if (rebuildAr[0].equals("startTime")) {
                lineRebuild = readerRebuild.readLine();
                if (lineRebuild != null)
                    rebuildAr = lineRebuild.split(",");
            }
            for(int i=3;i<lineCount;i++){
                //存在NA就转换成为0;
                oldData=originalAr[i].contains("NA")?0:Double.parseDouble(originalAr[i]);
                newData=rebuildAr[i].contains("NA")?0:Double.parseDouble(rebuildAr[i]);
                averageList.set(i-3,averageList.get(i-3)+(oldData==0?0:Math.pow((newData-oldData)/oldData, 2)));
            }
            N++;
        }

        System.out.println(averageList);
        for(int i=0;i<lineCount-3;i++){
            averageList.set(i,Math.sqrt(averageList.get(i)/N));
        }
        long endTime=System.currentTimeMillis();
        System.out.println(endTime-startTime);
        return averageList;
    }

}
