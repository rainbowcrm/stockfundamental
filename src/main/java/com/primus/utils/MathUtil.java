package com.primus.utils;

import org.springframework.security.access.method.P;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class MathUtil {

    public static Double round(Double dblValue)
    {
        MathContext mc = new MathContext(3, RoundingMode.CEILING);
        BigDecimal bigDecimal = new BigDecimal(dblValue).round(mc);
        return  bigDecimal.doubleValue();

    }

    public static double perChange(Double value1, Double value2)
    {
       return round (((value2-value1)/value1)*100);
    }
    public static double getDoubleValue(Double doub)
    {
        return  doub==null?0.0:doub.doubleValue();
    }


    public static Double getMode (List<Double> valueList) {

        if (!CollectionUtils.isEmpty(valueList)) {
            List<Double> sortedValues = valueList.stream().sorted().collect(Collectors.toList());


            Double minValue = sortedValues.get(0);
            Double maxValue = sortedValues.get(valueList.size() - 1);

        }
        return 0d;
    }

    public static Double getStandardDeviation (List<Double> valueList) {

        Double avg = getMean(valueList);
        Double sum = 0.0;
        for (Double val : valueList)  {
            Double entry = (val-avg) * (val-avg);
            sum += entry;
        }
        Double variance =  (sum/valueList.size());
        Double sqrt = Math.sqrt(variance);
        return round(sqrt) ;
    }



    public static Double getMean(List<Double> valueList)
    {
        Double sum = 0.0;
        for (Double val : valueList) {
            sum += val ;
        }
        return round(sum/valueList.size());
    }



    public static Double getMedian(List<Double> valueList)
    {
        if (!CollectionUtils.isEmpty(valueList)) {
            List<Double> sortedValues = valueList.stream().sorted().collect(Collectors.toList());

            if (sortedValues.size() < 2)
                return sortedValues.get(0);
            else if (sortedValues.size() % 2 == 0) {
                Double secValue = sortedValues.get(sortedValues.size() / 2);
                Double firstValue = sortedValues.get((sortedValues.size() / 2) - 1);
                return (firstValue + secValue) / 2;
            } else {
                return sortedValues.get(sortedValues.size() / 2);
            }
        }else
            return 0d;
    }


    public static Map<Float,Integer> rateToPerc(Collection<Float> floats)
    {
        float max = 0;
        for ( Float fl : floats ) {
            if (Math.abs(fl) > max)
                max = Math.abs(fl);
        }
        float scaleDown = max/100;
        Map<Float,Integer> scaledDownValues= new HashMap<>();
        for (Float fl : floats) {
            scaledDownValues.put(fl, Math.round(fl/scaleDown) ) ;
        }
        return  scaledDownValues ;

    }
}
