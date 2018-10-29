package LossyCompression.Implements;

import LossyCompression.Interface.LossyCompression;
import Model.Abstract.Spot;
import Model.ImplementsPack.AObject;
import Model.ImplementsPack.AfterObject;
import Model.ImplementsPack.Bit;
import Model.ImplementsPack.TypeOfFile;
import Tools.InterfacePack.SpotTools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BestCurveFittingCompression implements LossyCompression {
    private static int columnCount = 0;//The number of quotas.
    private static boolean isFlag = true;
    public static float p=0.05f;
    public TypeOfFile typeCollection;
    public SpotTools spt;
    @Override
    public void bestCurveFittingCompression(String originalPath, String compressionPath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(originalPath));
        String line = null;
        String lineSplit[] = null;
        String objectId = "";
        AfterObject afo = null;
        int num=0;
        ArrayList<AObject> allList = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            lineSplit = line.split(",");
            if (lineSplit[0].equals("startTime"))
                continue;
            if (isFlag) columnCount = lineSplit.length - 3;
            AObject tempObject = new AObject();
            ArrayList<String> tempList = new ArrayList<>();
            tempObject.setStartTime(lineSplit[0]);
            tempObject.setEndTime(lineSplit[1]);
            tempObject.setObjId(lineSplit[2]);
            if (objectId.equals(""))
                objectId = lineSplit[2];
            else if (!objectId.equals(lineSplit[2])) {
//                afo = compressionBaseObject(allList);
//                printAfterObject(compressionPath, afo,num);
                allList.clear();
                objectId = lineSplit[2];
                num=0;
            }
            for (int i = 3; i < lineSplit.length; i++) {
                tempList.add(lineSplit[i]);
            }
            tempObject.setQuotaList(tempList);
            allList.add(tempObject);
        }
//        afo = compressionBaseObject(allList);
//        printAfterObject(compressionPath, afo,num);

    }


    public AfterObject compressionBaseObject(ArrayList<AObject> objectList) {
        AfterObject afo = new AfterObject();
        afo.setQuotaList(new ArrayList<>());
        afo.setStartTime(objectList.get(0).getStartTime());
        afo.setEndTime(objectList.get(objectList.size() - 1).getEndTime());
        afo.setObjId(objectList.get(0).getObjId());

        @SuppressWarnings("unchecked")
        ArrayList<String>[] allLineList = new ArrayList[columnCount];
        for (int i = 0; i < columnCount; i++) {
            ArrayList<String> tempList = new ArrayList<>();
            allLineList[i] = tempList;
        }
        for (AObject ao : objectList) {
            for (int i = 0; i < columnCount; i++)
                allLineList[i].add(ao.getQuotaList().get(i).equals("NA") ? "0" : ao.getQuotaList().get(i));
        }


//        for (ArrayList<String> lineList : allLineList) {
        for (int i=0;i<columnCount;i++) {
            ArrayList<Spot> lineResultList=null;
            if(TypeOfFile.typeList[i]==1)
//                lineResultList= revolvingDoorBaseLine(allLineList[i],TypeOfFile.thresholds[i]);
//            else
//                lineResultList=revolvingDoorBaseLineforInteger(allLineList[i],TypeOfFile.thresholds[i]);
            afo.getQuotaList().add(lineResultList);
        }
        return afo;
    }

    public Bit curveFittingCompressionBaseLine(ArrayList<String>  arrayList){
        Bit res=new Bit();
        return res;
    }


    @Override
    public void revolvingDoorCompression(String originalPath, String compressionPath) throws IOException {

    }
}
