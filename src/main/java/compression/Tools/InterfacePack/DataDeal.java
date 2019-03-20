package Tools.InterfacePack;

/**
 * @创建人 kcx
 * @创建时间 2018/12/21
 * @描述
 */
public class DataDeal {
    /*
     * 数据处理：
     * 对于整数而言，保存从第一个非零数开始，到第三位；后面的数全变成0，如123456截断后变成123000；
     * 对于浮点数而言，保存从第一个非零的数开始，到第三位；若该浮点数大于100，将该浮点数变成整数，然后按照整数的方式处理；
     * 如123.4423->123;1234.32342->1230;
     * 若该浮点数在1~100之间，则从第一位开始，到第四位（中间包含一个小数点）；然后对后序的数据进行截断，即最多仅保留4位；
     * 如12.34343->12.3;2.3430->2.34;
     * 若该浮点数小于1，则从第一个非零的数开始，到第三位，三位以后的数截断；
     * 如0.123232->0.123;0.000231231->0.000231;
     *
     * */

        static final float BIAS=0.10f;
        /*
         * 简化处理数据：
         * 	将数据按照BIAS偏差要求，求出数据的上下界，然后在上下界当中求出一个较短的值来代替原值；
         * */
        public static String DataSimplify(String str){
            String flag=str;
            if(str.contains(".")){
                //小数处理
                double d=Double.parseDouble(str);
                double d1=d*(1+BIAS);
                double d2=d*(1-BIAS);
                long l1=(long)d1;
                long l2=(long)d2;
                StringBuffer sb=new StringBuffer();
                String st1=String.valueOf(l1);
                String st2=String.valueOf(l2);

                if(d>0){
                    if(l1==l2){
                        sb.append(SingleDouble(d1, d2));
                    }else{
                        l2+=1;
                        if(st1.length()!=st2.length()){
                            int i=0;
                            while(i<st2.length()){
                                sb.append(9);
                                i++;
                            }
                        }else{
                            if(l1==l2){
                                sb.append(l2);
                            }else{
                                int i=0;
                                while(i<st2.length()&&st1.charAt(i)==st2.charAt(i)){
                                    sb.append(st1.charAt(i));
                                    i++;
                                }
                                if((st1.charAt(i)-'0')-(st2.charAt(i)-'0')==1){
                                    sb.append(st1.charAt(i));
                                }else{
                                    sb.append(((st1.charAt(i)-'0')+(st2.charAt(i)-'0'))/2);
                                }
                                i++;
                                while(i<st1.length()){
                                    sb.append(0);
                                    i++;
                                }
                            }
                        }
                    }
                }else if(d<0){
                    if(l1==l2){
                        sb.append(SingleDouble(d1, d2));
                    }else{
                        l2+=-1;
                        if(st1.length()!=st2.length()){
                            int i=0;
                            while(i<st2.length()){
                                sb.append(9);
                                i++;
                            }
                        }else{
                            if(l1==l2){
                                sb.append(l2);
                            }else{
                                int i=0;
                                while(i<st2.length()&&st1.charAt(i)==st2.charAt(i)){
                                    sb.append(st1.charAt(i));
                                    i++;
                                }
                                if((st1.charAt(i)-'0')-(st2.charAt(i)-'0')==1){
                                    sb.append(st1.charAt(i));
                                }else{
                                    sb.append(((st1.charAt(i)-'0')+(st2.charAt(i)-'0'))/2);
                                }
                                i++;
                                while(i<st1.length()){
                                    sb.append(0);
                                    i++;
                                }
                            }
                        }
                    }
                }else{
                    sb.append(0);
                }
                flag=sb.toString();
            }else{
                //整数处理
                long l=Long.parseLong(str);

                flag=SingleLong(l);
            }
            return flag;
        }

        //将整数在上下界之间取10的倍数截断（如果值在条件范围内）
        public static String SingleLong(long l){
            StringBuffer sb=new StringBuffer();
            double d1=l*(1+BIAS);
            double d2=l*(1-BIAS);
            long l1=(long)d1;
            long l2=(long)d2;
            if(l>0){
                l2+=1;
                String st1=String.valueOf(l1);
                String st2=String.valueOf(l2);
                if(st1.length()!=st2.length()){
                    for(int i=0;i<st2.length();i++){
                        sb.append('9');
                    }
                }else{
                    if(l1==l2){
                        sb.append(l1);
                    }else{
                        int i=0;
                        while(i<st1.length()&&st1.charAt(i)==st2.charAt(i)){
                            sb.append(st1.charAt(i));
                            i++;
                        }
                        if((st1.charAt(i)-'0')-(st2.charAt(i)-'0')==1){
                            sb.append(st1.charAt(i));
                            i++;
                        }else{
                            sb.append((st1.charAt(i)-'0'+st2.charAt(i)-'0')/2);
                            i++;
                        }
                        while(i<st1.length()){
                            sb.append('0');
                            i++;
                        }
                    }
                }
            }else if(l==0){
                sb.append('0');
            }else{
                l2+=-1;
                String st1=String.valueOf(l1);
                String st2=String.valueOf(l2);
                if(st1.length()!=st2.length()){
                    sb.append('-');
                    for(int i=1;i<st2.length();i++){
                        sb.append('9');
                    }
                }else{
                    if(l1==l2){
                        sb.append(l1);
                    }else{
                        int i=0;
                        while(i<st1.length()&&st1.charAt(i)==st2.charAt(i)){
                            sb.append(st1.charAt(i));
                            i++;
                        }
                        if((st1.charAt(i)-'0')-(st2.charAt(i)-'0')==1){
                            sb.append(st1.charAt(i));
                            i++;
                        }else{
                            sb.append((st1.charAt(i)-'0'+st2.charAt(i)-'0')/2);
                            i++;
                        }
                        while(i<st1.length()){
                            sb.append('0');
                            i++;
                        }
                    }
                }
            }
            return sb.toString();
        }


        //将浮点数在上下界之间取最短值进行截断
        public static String SingleDouble(double d1,double d2){
            //|d1|>|d2|
//		System.out.println(d1+">>>>>"+d2);

            String st1=String.valueOf(d1);
            String st2=String.valueOf(d2);
            StringBuffer sb=new StringBuffer();
            int k=Math.min(st1.length(), st2.length());
            int i=0;
            while(i<k&&st1.charAt(i)==st2.charAt(i)){
                sb.append(st1.charAt(i));
                i++;
            }
            if((st1.charAt(i)-'0')-(st2.charAt(i)-'0')==1){
                sb.append(st1.charAt(i));
            }else{
                int c=Math.round((st1.charAt(i)-'0'+st2.charAt(i)-'0')/2);
                sb.append(c);
            }
            return sb.toString();
        }


}
