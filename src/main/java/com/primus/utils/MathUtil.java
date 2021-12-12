package com.primus.utils;

import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MathUtil {

    public static Double round(Double dblValue)
    {
        MathContext mc = new MathContext(3, RoundingMode.CEILING);
        BigDecimal bigDecimal = new BigDecimal(dblValue).round(mc);
        return  bigDecimal.doubleValue();

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


}
