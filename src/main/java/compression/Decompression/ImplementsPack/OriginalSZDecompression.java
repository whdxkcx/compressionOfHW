package Decompression.ImplementsPack;

import Tools.Inplements.ByteConverterImpl;

import java.io.*;
import java.util.ArrayList;

/**
 * @创建人 kcx
 * @创建时间 2019/5/6
 * @描述
 */
public class OriginalSZDecompression {
    public static  void main(String[] args)  throws IOException{
        String filePath="E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\NoModelChangeWithLossLess\\UCELL1(c).csv";
        String outputPath="E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\NoModelChangeWithLosslessDecompression\\UCELL1（cde).csv";
        deCompression(filePath,outputPath);
    }


    public static void deCompression(String compressionPath,String deCompressionPath) throws IOException{
        BufferedInputStream bi=new BufferedInputStream(new FileInputStream(compressionPath));
        BufferedWriter bw=new BufferedWriter(new FileWriter(deCompressionPath,true));
        byte bytes[]=new byte[4];
        bi.read(bytes);
        //第一步，先读取每一段的行数。
        int rowLength= ByteConverterImpl.getIntFrombytes(bytes,0);
        int byteSize=rowLength/8;//预测命中标志字节数组的大小
        if(rowLength%8!=0)  byteSize+=1;
        //定义三个数组，分别用于读取每一行的预测命中标志，预测类型和不可预测数组
        byte[] predictbleBytes=new byte[byteSize];
        byte[] modelCategories=null;
        byte[] unPredictableBytes=new byte[4];
        ArrayList<Float>  floatList=new ArrayList<>();
        int count=0;
        //第二步，读取预测命中标志
        while((count=bi.read(predictbleBytes))!=-1){
            int predictableLenth=0;//记录预测成功的数据个数
            for(int i=0;i<rowLength;i++){
                int flag=(predictbleBytes[i/8])&(1<<(i%8));
                if(flag!=0) predictableLenth++;
            }
            if(predictableLenth%4==0) predictableLenth/=4;
            else predictableLenth=predictableLenth/4+1;
            modelCategories=new byte[predictableLenth];
            bi.read(modelCategories);

            int j=0;
            //逐步遍历和输出字符串
            for(int i=0;i<rowLength;i++){
                float deVal=0;
                int flag=(predictbleBytes[i/8])&(1<<(i%8));
                if(flag==0){//如果是预测标志为0，说明未预测成功，则读取未预测值
                    bi.read(unPredictableBytes);
                    deVal=Float.intBitsToFloat(ByteConverterImpl.getIntFrombytes(unPredictableBytes,0));
                }
                else{//如果预测标志位不为，说明预测成功，则读取预测模型类型
                    int predictClass=((modelCategories[j/4])&(3<<((2*j)%8)))>>((2*j)%8);
                    j++;
                    if(i<1) System.out.println(i);
                    if(predictClass==0) deVal=floatList.get(i-1);
                    else if(predictClass==1) deVal=2*floatList.get(i-1)-floatList.get(i-2);
                    else if(predictClass==2) deVal=3*floatList.get(i-1)-3*floatList.get(i-2)+floatList.get(i-3);
                    //如果有其它的模型就可以继续添加。
                }
                floatList.add(deVal);//添加到数组中
                bw.write(Float.toString(deVal));//输出这个浮点数到解压文件中
                if(i!=rowLength-1) bw.write(",");
            }
            bw.write("\n");//输出换行符
            bw.flush();
            //重新初始化数组
            predictbleBytes=new byte[byteSize];
            floatList=new ArrayList<>();
        }
        bw.close();
    }
}
