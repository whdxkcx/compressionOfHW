package Test;

import LossyCompression.Implements.RealTimeCompression;
import Model.Abstract.Spot;
import Model.ImplementsPack.ISubSpot;
import Model.ImplementsPack.fSubSpot;
import Tools.Inplements.ByteConverterImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.print.attribute.standard.Compression;
import java.io.*;
import java.util.ArrayList;

public class test {
    public static void main(String[] args) throws IOException{


        long startTime = System.currentTimeMillis();
        byte b=-8;
        byte h=(byte)(b|(2<<6));
        System.out.println((b&(3<<6))>>6);
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
