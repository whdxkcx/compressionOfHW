package DataAnlyze.ImplementsPack;

import DataAnlyze.InterfacePack.AnalyzationOfFile;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class AnalyzationImpl implements AnalyzationOfFile {
     public static void main(String[] args) throws IOException {
             AnalyzationOfFile af=new AnalyzationImpl();
             System.out.println(af.getStaadardDeviationBaseLine("E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\input\\UCELLnew.csv"));
//     System.out.println(af.getAmplitudeFromFile("E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\input\\UCELLnew.csv"));
//             System.out.println(af.dataTypeAnalyzation("C:\\Users\\90765\\Desktop\\压缩试验数据\\input\\UCELLnew.csv"));
//         int a[]=((AnalyzationImpl) af).conutOffloat(af.dataTypeAnalyzation("/data/compressionData/input/test2.csv"));
//         System.out.println("整数个数为"+a[0]);
//         System.out.println("浮点数数个数为"+a[1]);
//           ((AnalyzationImpl) af).setAllObjectFromFile("E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\UCELL.csv","E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\input\\UCELLnew.csv");
//         af.getOneObjectFromFile("0d0fd7c6e093f7b804fa0150b875b868","/home/data/compressionData/originData/UCELL.csv","/home/data/compressionData/input/test68.csv");
     }

    @Override
    public ArrayList<Integer> dataTypeAnalyzation(String filePath) throws IOException {
        BufferedReader reader=new BufferedReader(new FileReader(filePath));
        String line=null;
        String lineSplit[]=null;
        boolean flag=true;//The first line.
        ArrayList<Integer> typeList=new ArrayList<>();
        while((line=reader.readLine())!=null){
            lineSplit=line.split(",");
            if(lineSplit[0].equals("startTime"))
               continue;
            if(flag) {
                for (int i = 3; i < lineSplit.length; i++)
                    typeList.add(0);
                flag=false;
            }
            for (int i = 3; i < lineSplit.length; i++)
                if(lineSplit[i].contains("."))
                    typeList.set(i-3,1);
        }
        return typeList;
    }


    public int[] conutOffloat(ArrayList<Integer> list){
        int coubntf=0;
        int countI=0;
        for(int val:list){
            if(val==0) countI++;
            else coubntf++;
        }
        int a[]=new int[2];
        a[0]=countI;
        a[1]=coubntf;
        return a;
    }

    @Override
    public void getOneObjectFromFile(String objId, String filePath, String oneObjectPath) throws IOException {
         BufferedReader reader=new BufferedReader(new FileReader(filePath));
         BufferedWriter  out=new BufferedWriter(new FileWriter(oneObjectPath,true));
         String line=null;
         String lineSplit[]=null;
         while((line=reader.readLine())!=null){
             lineSplit=line.split(",");
             if(lineSplit[2].equals(objId)) {
                 out.write(line);
                 out.newLine();
             }
         }
         out.flush();
         out.close();
    }

    //从 文件中获取所有的对id
    public HashSet<String> getAllObjectIdFromFile(String filePath) throws IOException {
        HashSet<String>  hs=new HashSet<>();
        BufferedReader reader=new BufferedReader(new FileReader(filePath));
        String line=null;
        while((line=reader.readLine())!=null){
            if(line.split(",")[0].equals("startTime")) continue;
            hs.add(line.split(",")[2]);
        }
        return hs;
    }

    //根据对象重新排序一个文件，并输出到另一个文件中
    public void  setAllObjectFromFile(String  inputFilePath,String outputFilePath) throws IOException {
           HashSet<String>  hs=getAllObjectIdFromFile(inputFilePath);
           for(String objid:hs){
               getOneObjectFromFile(objid,inputFilePath,outputFilePath);
           }
    }

    //获取某一列的值
    @Override
    public void getOneLineOfFile(String objId, String originalPath, String rebuildPath) throws IOException {
        BufferedReader readerOriginal = new BufferedReader(new FileReader(originalPath));
        BufferedReader readerRebuild = new BufferedReader(new FileReader(rebuildPath));
        String lineOriginal = null;
        String lineRebuild = null;

        String originalAr[]=readerOriginal.readLine().split(",");
        String rebuildAr[]=readerRebuild.readLine().split(",");
        while((lineOriginal=readerOriginal.readLine())!=null&&(lineRebuild=readerRebuild.readLine())!=null){
            originalAr=lineOriginal.split(",");
            rebuildAr=lineRebuild.split(",");
            }
    }

    //计算每一列的标准差
    @Override
    public ArrayList<Double> getStaadardDeviationBaseLine(String filePath) throws IOException {
        BufferedReader reader= new BufferedReader(new FileReader(filePath));
        ArrayList<Double> resList=new ArrayList<>();
        ArrayList<Double> meanList=getMeanOfBaseLine(filePath);
        String line=null;
        String lineSplit[]=null;
        int columnCount=0;
        double rowCount=meanList.get(meanList.size()-1);
        boolean flag=true;
        while((line=reader.readLine())!=null) {
            lineSplit = line.split(",");
            if (flag){
                flag = false;
                columnCount = lineSplit.length;
                for (int i = 0; i < columnCount-3; i++)
                    resList.add(0.0);
            }
            if (lineSplit[0].equals("startTime"))
                continue;
            for(int i=3;i<columnCount;i++){
                double temp=Double.parseDouble(lineSplit[i].equals("NA")?"0":lineSplit[i]);
                double val=Math.pow(temp-meanList.get(i-3),2);
                resList.set(i-3,resList.get(i-3)+val/(rowCount-1));
            }
        }
        System.out.println(meanList);
        System.out.println(resList);
        for(int i=0;i<columnCount-3;i++){
            if(meanList.get(i)==0)
                resList.set(i,0.0);
            else
                resList.set(i,Math.sqrt(resList.get(i))/meanList.get(i));
        }
        System.out.println();
         return resList;
    }

    //计算每一列的平均值
    @Override
    public ArrayList<Double> getMeanOfBaseLine(String filePath) throws IOException {
        BufferedReader reader= new BufferedReader(new FileReader(filePath));
        ArrayList<Double> resList=new ArrayList<>();
        String line=null;
        String lineSplit[]=null;
        int columnCount=0;
        int rowCount=0;
        boolean flag=true;
        while((line=reader.readLine())!=null){
            lineSplit=line.split(",");
            if(flag){
                flag=false;
                columnCount=lineSplit.length;
                for(int i=0;i<columnCount-3;i++)
                     resList.add(0.0);
            }
            if(lineSplit[0].equals("startTime"))
                continue;
            rowCount++;
            for(int i=3;i<columnCount;i++){
                float temp=Float.parseFloat(lineSplit[i].equals("NA")?"0":lineSplit[i]);
                resList.set(i-3,resList.get(i-3)+temp);
            }
        }

        for(int i=0;i<columnCount-3;i++){
            resList.set(i,resList.get(i)/columnCount);
        }
        resList.add((double)rowCount);
        return resList;
    }

    @Override
    public ArrayList<Float>  getAmplitudeFromFile(String filePath) throws IOException {
        BufferedReader reader= new BufferedReader(new FileReader(filePath));
        String line=null;
        ArrayList<Float> resList=new ArrayList<>();
        String lineSplit[]=null;
        line=reader.readLine();
        lineSplit=line.split(",");
        int count=lineSplit.length-3;
        float minList[]=new float[count];
        float maxList[]=new float[count];
        //数组初始化
        for(int i=0;i<count;i++) {
            minList[i]=Integer.MAX_VALUE;
            maxList[i]=Integer.MIN_VALUE;
            resList.add(0f);
        }
        //如果第一行不是子段名
        if(!lineSplit[0].equals("StartTime")){
            for(int i=0;i<count;i++){
                minList[i]=Math.min(minList[i],lineSplit[i+3].equals("NA")?0f:Float.parseFloat(lineSplit[i+3]));
                maxList[i]=Math.max(maxList[i],lineSplit[i+3].equals("NA")?0f:Float.parseFloat(lineSplit[i+3]));
            }
        }
        while((line=reader.readLine())!=null){
            lineSplit=line.split(",");
            for(int i=0;i<count;i++){
                minList[i]=Math.min(minList[i],lineSplit[i+3].equals("NA")?0f:Float.parseFloat(lineSplit[i+3]));
                maxList[i]=Math.max(maxList[i],lineSplit[i+3].equals("NA")?0f:Float.parseFloat(lineSplit[i+3]));
                resList.set(i,maxList[i]-minList[i]);
            }
        }
        return resList;
    }
}
