package Test;

import LossyCompression.Implements.RealTimeCompression;
import Model.Abstract.Spot;
import Model.ImplementsPack.ISubSpot;
import Model.ImplementsPack.fSubSpot;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.print.attribute.standard.Compression;
import java.io.IOException;
import java.util.ArrayList;

public class test {
    public static void main(String[] args) throws IOException{
//        ApplicationContext apc=getApplicationContext();
//        RealTimeCompression rtc=(RealTimeCompression)apc.getBean("realtimeCompression");
//        ArrayList<String>  list=new ArrayList<>();
//
//        for(int i=0;i<100;i++)
//
//            list.add(Math.random()*100+"");
//

////        System.out.println(rtc.revolvingDoorBaseLine(list));

        long startTime = System.currentTimeMillis();
        compressionTest("E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\input\\UCELLnew.csv","E:\\实验室学习\\项目\\数据压缩\\UCELL.csv\\middleOutput\\UCELLnew(change2010240.05new).csv");
        long endTime = System.currentTimeMillis();
        double time = endTime - startTime;
        System.out.println("压缩时间为：" + time/1000 + "s");
    }
    public static void compressionTest(String originalPath,String compressionPath) throws IOException {
        ApplicationContext apc=getApplicationContext();
        RealTimeCompression rtc=(RealTimeCompression)apc.getBean("realtimeCompression");
        rtc.revolvingDoorCompression(originalPath,compressionPath);
    }

    public  static ApplicationContext getApplicationContext(){
        return new ClassPathXmlApplicationContext("spring/spring-context.xml");
    }
}
