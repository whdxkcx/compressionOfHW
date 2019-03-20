package Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

/**
 * @创建人 kcx
 * @创建时间 2018/12/17
 * @描述
 */
public class TestOther {
    public static void main(String[] args) {
        String jsonStr = "{'programmers':[['McLaughlin','aaaa','国农科技','13.636363636363635','1.2104','-21.5371'],['Mbsdlin','bbbb','世纪星源','27.27272727272727','-6.3493',null]]}";
        JSONObject jobj = JSON.parseObject(jsonStr);
        JSONArray jarray = jobj.getJSONArray("programmers");
        int size = jarray.size();
        String []test = new String[size];
        String []test1 = new String[size];
          String []test2 = new String[size];
        String []test3 = new String[size];
        try {
            for(int i = 0 ; i< size ; i++){
                int size1 = jarray.getJSONArray(i).size();
                for(int j = 0;j<size1;j++) {
                    if(jarray.getJSONArray(i).get(j)!=null) {
                        test[i] = jarray.getJSONArray(i).get(0).toString();
                        test1[i] = jarray.getJSONArray(i).get(2).toString();
                        test2[i] = jarray.getJSONArray(i).get(3).toString();
                        test3[i] = jarray.getJSONArray(i).get(5).toString();
                    }else {
                        test3[i] = "";

                    }
                }
                System.out.println(test[i]);
                System.out.println(test1[i]);
                System.out.println(test2[i]);
                System.out.println(test3[i]);
            }
        }catch(Exception e) {
            System.out.println("Error :" + e);
        }
    }

}
