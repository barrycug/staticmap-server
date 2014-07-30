package org.gradoservice.mapRender.utils;

import com.google.common.collect.ImmutableMap;
import org.gradoservice.mapRender.layers.Layer;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 17:40
 */
public class Util {


    public static double wrapNum(double x, double[] range, boolean includeMax) {
        double max = range[1];
        double min = range[0];
        double d = max - min;
        return x == max && includeMax ? x : ((x - min) % d + d) % d + min;
    }

    public static String template(final String template, ImmutableMap<String,String> params) {
        String result = template;
        for (Map.Entry<String,String> entry : params.entrySet()) {
            result = result.replaceAll("\\{"+entry.getKey()+"\\}", entry.getValue());
        }
        return result;
    }

    public static String doubleToIntString(final double value) {
       return intToString(new Double(value).intValue());
    }

    public static String intToString(final int value) {
        return Integer.toString(value);
    }

    public static Double formatNum(double num, int digits) {
        double pow = Math.pow(10, digits);
        return Math.round(num * pow) / pow;
    }

    private static final String[] letters = "0123456789ABCDEF".split("");

    public static String randomColor() {
        StringBuilder colorBuilder = new StringBuilder();
        colorBuilder.append('#');
        for (int i = 0; i < 6; i++ ) {
            Double position = Math.floor(Math.random() * 16);
            colorBuilder.append(letters[position.intValue()]);
        }
        return colorBuilder.toString();
    }
}
