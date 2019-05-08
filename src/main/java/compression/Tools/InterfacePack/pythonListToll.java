package Tools.InterfacePack;

import java.util.ArrayList;

/**
 * @创建人 kcx
 * @创建时间 2019/3/20
 * @描述 用于处理输出的列表
 */
public class pythonListToll {
    /**

     *@描述  把一个用中括号包裹的列表转为一个ArrayList<String>

     *@参数  [line]

     *@返回值  java.util.ArrayList<java.lang.String>

     *@创建人  kcx

     *@创建时间  2019/3/20

     *@修改人和其它信息

     */
    public static ArrayList<String> getList(String line){
        String[] lineArr=line.split(",");
        ArrayList<String> columnList=new ArrayList<>();
        for(int i=0;i<lineArr.length;i++) {
            if (lineArr[i].contains("["))
                columnList.add(lineArr[i].substring(1));
            else if (lineArr[i].contains("]"))
                columnList.add(lineArr[i].substring(0, lineArr[i].length() - 1));
            else
                columnList.add(lineArr[i]);
        }
        return columnList;
    }
}
