package Test;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

/**
 * @创建人 kcx
 * @创建时间 2019/4/14
 * @描述
 */
public class Main {

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        String row="";
        ArrayList<ArrayList<Integer>> matrixList=new ArrayList<>();
        while(!(row=in.nextLine()).equals("")){
                String[] rowArr=row.split(" ");
                ArrayList<Integer> tempList=new ArrayList<>();
                for(String val:rowArr)
                    tempList.add(Integer.parseInt(val));
                matrixList.add(tempList);
        }
        int rowNumber=matrixList.size();
        int line=0;
        if(rowNumber!=0) line=matrixList.get(0).size();
        int[][] grid=new int[rowNumber][line];System.out.println(rowNumber+","+line);
        System.out.println(minMinutes(matrixList,grid));
    }


    public  static int minMinutes(ArrayList<ArrayList<Integer>> matrixList,int[][] grid){
        int count=0;
        int row=matrixList.size();
        int line=0;
        if(row!=0) line=matrixList.get(0).size();
        else return 0;
        for(int i=0;i<row;i++){
            for(int j=0;j<line;j++){
                if(grid[i][j]==1) continue;
                if(matrixList.get(i).get(j)==2)  count+=(helper(i+1,j,row,line,grid,matrixList)+
                        helper(i-1,j,row,line,grid,matrixList)+
                        helper(i,j-1,row,line,grid,matrixList)+
                        helper(i+1,j,row,line,grid,matrixList));
            }
        }
        for(int i=0;i<row;i++)
            for (int j = 0; j < line; j++)
                if(matrixList.get(i).get(j)==1)
                    return -1;
        return count;

    }
    public static int helper(int i,int j,int row,int line,int[][] grid,ArrayList<ArrayList<Integer>> matrix){
        if(i>=row||i<0||j>=line||j<0||grid[i][j]==1) return 0;
        grid[i][j]=1;
        if(matrix.get(i).get(j)==0) return 0;
        if(matrix.get(i).get(j)==1) matrix.get(i).set(j,2);
        return helper(i+1,j,row,line,grid,matrix)+
                helper(i-1,j,row,line,grid,matrix)+
                helper(i,j-1,row,line,grid,matrix)+
                helper(i+1,j,row,line,grid,matrix)+1;
    }
}
