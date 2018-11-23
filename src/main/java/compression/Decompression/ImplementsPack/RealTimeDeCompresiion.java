package Decompression.ImplementsPack;

import Decompression.InterfacePack.LossyDecompression;
import Model.ImplementsPack.AObject;
import Model.ImplementsPack.ISubSpot;
import Model.ImplementsPack.TypeOfFile;
import Model.ImplementsPack.fSubSpot;

import java.io.*;
import java.util.ArrayList;

public class RealTimeDeCompresiion implements LossyDecompression {


    public static void main(String[] args) throws IOException {
        LossyDecompression  ldc=new RealTimeDeCompresiion();
        long startTime = System.currentTimeMillis();
        ldc.revolvingDoorDeCompression("E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\middleOutput\\UCELLnew(change20).csv",
                "E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\DeOutput\\UCELLnew(change20De).csv");
        long endTime = System.currentTimeMillis();
        double time = endTime - startTime;
        System.out.println("解压时间为：" + time/1000 + "s");
    }


    @Override
    public void revolvingDoorDeCompression(String originalPath, String decompressionPath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(originalPath));
        String line = null;
        AObject tempObject = null;
        ArrayList<String> tempList = null;
        ArrayList<AObject> aoList = null;
        int num = 0;
        while ((line = reader.readLine()) != null) {
            tempObject = new AObject();
            tempList = new ArrayList<>();
            String[] lineSplit = line.split(",");
            if (lineSplit[0].equals("startTime"))
                continue;
            num = Integer.parseInt(lineSplit[0]);
            tempObject.setStartTime(lineSplit[1]);
            tempObject.setEndTime(lineSplit[2]);
            tempObject.setObjId(lineSplit[3]);
            for (int i = 4; i < lineSplit.length; i++) {
                tempList.add(lineSplit[i]);
            }
            tempObject.setQuotaList(tempList);
            aoList = deCompressionBaseObaject(tempObject, num);
            printAfterdeCompression(decompressionPath, aoList);
            aoList.clear();
        }
    }


    public void printAfterdeCompression(String outputFilepath, ArrayList<AObject> aoList) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(outputFilepath, true));
        for (AObject ao : aoList) {
            out.write(ao.getStartTime() + ",");
            out.write(ao.getEndTime() + ",");
            out.write(ao.getObjId() + ",");
            ArrayList<String> strList = ao.getQuotaList();
//            if(strList.size()!=4749) System.out.print(strList.size());
            for (int i = 0; i < strList.size(); i++) {
                if (i != strList.size() - 1)
                    out.write(strList.get(i) + ",");
                else
                    out.write(strList.get(i));
            }
            out.newLine();
        }
        out.flush();
        out.close();
    }


    boolean  rowFlag=true;
    public ArrayList<AObject> deCompressionBaseObaject(AObject baseAObject, int num) {
        if (baseAObject == null)
            return null;
        ArrayList<AObject> aoList = new ArrayList<>();
        ArrayList<String> rowList = baseAObject.getQuotaList();
        int nextIndex = 0;
        for (int i = 0; i < num; i++) {
            AObject tempAo = new AObject();
            tempAo.setObjId(baseAObject.getObjId());
            tempAo.setStartTime(Long.parseLong(baseAObject.getStartTime().trim()) + i * 1800000L + "");
            tempAo.setEndTime(Long.parseLong(tempAo.getStartTime().trim()) + 1800000L + "");
            tempAo.setQuotaList(new ArrayList<>());
            aoList.add(tempAo);
        }

//        System.out.print(rowList.size());

        for (int i = 0; i < rowList.size(); i++) {//对每一列解压
            if(rowList.get(i).contains(":")){
                String val=rowList.get(i).split(":")[0];
                for(int j=0;j<num;j++)//对num行分别遍历
                    aoList.get(j).getQuotaList().add(val);
                continue;
            }
            String lineAr[] = rowList.get(i).split(";");
            String spotAr[] = null;
            if (TypeOfFile.typeList[i] == 1) {
                fSubSpot fSpot = new fSubSpot();
                //对每一个点解压成多行。
                for (int j = 0; j < lineAr.length; j++) {
                    spotAr = lineAr[j].split("\\?");//点的坐标数组
                    if (j < lineAr.length - 1)
                        nextIndex = Integer.parseInt(lineAr[j + 1].split("\\?")[0]);//下一个点第一个索引。
                    else
                        nextIndex = num;
//                    fSpot = new fSubSpot(Integer.parseInt(spotAr[0]), Float.parseFloat(spotAr[1]));
                    fSpot.setX(Integer.parseInt(spotAr[0]));
                    fSpot.setY(Float.parseFloat(spotAr[1]));
                    if (spotAr.length == 3) {
                        fSpot.setZ(Float.parseFloat(spotAr[2]));
                        decompressionBaseSpot(fSpot, aoList, nextIndex);
                    } else {
                        aoList.get(fSpot.getX()).getQuotaList().add(fSpot.getY() + "");
                    }
                }
            } else {
                ISubSpot ISpot=new ISubSpot();
                for (int j = 0; j < lineAr.length; j++) {
                    spotAr = lineAr[j].split("\\?");
                    if (j < lineAr.length - 1)
                        nextIndex = Integer.parseInt(lineAr[j + 1].split("\\?")[0]);
                    else
                        nextIndex = num;
//                    ISpot= new ISubSpot(Integer.parseInt(spotAr[0]), Integer.parseInt(spotAr[1]));
                    ISpot.setX(Integer.parseInt(spotAr[0]));
                    ISpot.setY(Long.parseLong(spotAr[1]));
                    if (spotAr.length == 3) {
                        ISpot.setZ(Long.parseLong(spotAr[2]));
                        decompressionBaseSpot(ISpot, aoList, nextIndex);
                    } else {
                        aoList.get(ISpot.getX()).getQuotaList().add(ISpot.getY() + "");
                    }
                }
            }
        }
        return aoList;
    }


    public void decompressionBaseSpot(fSubSpot fSpot, ArrayList<AObject> aoList, int num) {
        int x1 = fSpot.getX();
        float y1 = fSpot.getY();
        int x2 = num-1;
        float y2 = fSpot.getZ();
        float[] a = functionCalculation(x1, y1, x2, y2);
        float k = a[0];
        float b = a[1];
        for (int i = x1; i <= x2; i++) {
            float y = k * i + b;
            aoList.get(i).getQuotaList().add(y + "");
        }
    }


    public void decompressionBaseSpot(ISubSpot ISpot, ArrayList<AObject> aoList, int num) {
        int x1 = ISpot.getX();
        long y1 = ISpot.getY();
        int x2 = num -1;
        long y2 = ISpot.getZ();
        float[] a = functionCalculation(x1, y1, x2, y2);
        float k = a[0];
        float b = a[1];
        for (int i = x1; i <= x2; i++) {
            int y = (int) (k * i + b);
            aoList.get(i).getQuotaList().add(y + "");
        }
    }

    @Override
    public void bestCurveFittingDeCompression(String compressiomPath, String deCompressionPath) throws IOException {

    }

    public float[] functionCalculation(int x1, float y1, int x2, float y2) {
        float[] a = new float[2];
        float k = (y2 - y1) / (x2 - x1);
        float b = y1 - k * x1;
        a[0] = k;
        a[1] = b;
        return a;
    }

}
