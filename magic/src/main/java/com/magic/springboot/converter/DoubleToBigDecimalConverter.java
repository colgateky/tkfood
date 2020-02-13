package com.magic.springboot.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by xm on 2017/11/22.
 */
@Component
public class DoubleToBigDecimalConverter implements Converter<Double, BigDecimal> {

    @Override
    public BigDecimal convert(Double source) {
        if (source == null) {
            return null;
        }

        BigDecimal ret = new BigDecimal(source).setScale(5, BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros();
        ret = new BigDecimal(ret.toPlainString());
        return ret;
    }
}