package com.tiefan.cps;

import com.tiefan.fbs.fdg.util.date.DateUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by stevenzhou on 2018/2/6.
 */
public class Test {
    public static void main(String[] args) {
        try {
            java.text.SimpleDateFormat sdf1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
            Date smdate = sdf1.parse(sdf1.format(DateUtils.parseDate("2018-03-16")));
            Date bdate = sdf1.parse(sdf1.format(DateUtils.parseDate("2018-04-01")));
            Calendar aft = Calendar.getInstance();
            Calendar bft = Calendar.getInstance();
            bft.setTime(smdate);
            aft.setTime(bdate);
            long month = aft.get(java.util.Calendar.MONTH) - bft.get(java.util.Calendar.MONTH) + (aft.get(java.util.Calendar.YEAR) - bft.get(java.util.Calendar.YEAR)) * 12;
            System.out.println(new BigDecimal(String.valueOf(month)));


            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long between_days=(time2-time1)/(1000*3600*24);
            System.out.println(new BigDecimal(String.valueOf(between_days)));


             month = 0;
            smdate=sdf1.parse(sdf1.format(smdate));
            bdate=sdf1.parse(sdf1.format(bdate));
            Calendar c1= Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(smdate);
            c2.setTime(bdate);
            int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
            month = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH) + year * 12;
            if(month != 0) {
                int day = c2.get(Calendar.DAY_OF_MONTH) - c1.get(Calendar.DAY_OF_MONTH);
                if (day < 0) {
                    month = month - 1;
                }
            }
            System.out.println( new BigDecimal(month));




        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
