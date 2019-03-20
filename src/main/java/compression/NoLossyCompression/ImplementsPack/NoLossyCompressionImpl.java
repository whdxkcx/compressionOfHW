package NoLossyCompression.ImplementsPack;

import Model.ImplementsPack.utf8EncodeImpl;
import NoLossyCompression.InterfacePack.NoLossyCompressionInterface;

import java.io.*;

public class NoLossyCompressionImpl implements NoLossyCompressionInterface {

    static NoLossyCompressionInterface nlcp = new NoLossyCompressionImpl();

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        nlcp.compressionByEncode("E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\middleOutput\\UCELLnew(change20181221.102new).csv",
                "E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\output\\UCELLnew(change20181221.1022new).csv");
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        System.out.println("压缩时间为：" + time/1000 + "秒");
    }

    @Override
    public void compressionByEncode(String inputPath, String outputPath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputPath));
        BufferedWriter out = new BufferedWriter(new FileWriter(outputPath, true));
        String line = null;
        while ((line = reader.readLine()) != null) {
            //获取第四列的起始索引
            int index=0;
            for(int i=0;i<3;i++){
                index=line.indexOf(",",index+1);
            }
            String str=line.substring(index+1);
            out.write(line.substring(0,index+1)+new String(lineToByteArray(str), "utf-8"));
            out.newLine();
        }
        out.flush();
        out.close();
    }


    public byte[] lineToByteArray(String line) {
        //新建byte数组用于存储转换字符串之后的数据
        int len = line.length();
        byte ba[] = null;
        if (len % 2 == 0)
            ba = new byte[len / 2];
        else
            ba = new byte[len / 2 + 1];

        //遍历每个字符
        int j = 0;
        for (int i = 0; i < len - 1; i += 2) {
            ba[j] = charToByte(line.charAt(i), line.charAt(i + 1));
            j++;
        }
        if (j == len - 1)
            ba[j] = (byte) (charToInt(line.charAt(j)) << 4);
        return ba;
    }
    public byte charToByte(char a, char b) {
        return (byte) ((charToInt(a) << 4) | charToInt(b));
    }

    public int charToInt(char a) {
        return utf8EncodeImpl.getInt(a);
    }
}
