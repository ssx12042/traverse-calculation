package com.jeesite.utils;

/**
 * @Author: ssx
 * @Date: 2022/4/6
 * @Description: 角度工具
 */
public class AngleUtils {

    /**
     * 角度转弧度
     * @param angleDeg 度
     * @param angleMin 分
     * @param angleSec 秒
     * @return 弧度
     */
    public static double angle2radian(int angleDeg, int angleMin, double angleSec) {
        double angle = angleDeg + (double) angleMin / 60 + angleSec / 3600;
        return Math.toRadians(angle);
    }

    /**
     * 弧度转角度
     * @param radian 弧度
     * @return 数组 [度, 分, 秒]
     */
    public static double[] radian2angle(double radian) {
        double angle = Math.toDegrees(radian);
        double angleDeg = Math.floor(angle);        // 度
        double angleMin_ = (angle - angleDeg) * 60;
        double angleMin = Math.floor(angleMin_);    // 分
        double angleSec = (angleMin_ - angleMin) * 60;
        // 对秒保留一位小数
        angleSec = Double.parseDouble(String.format("%.1f", angleSec));
        //进位
        if (angleSec == 60) {
            angleSec = 0.;
            angleMin += 1;
            if (angleMin == 60) {
                angleMin = 0;
                angleDeg += 1;
            }
        }
        return new double[]{angleDeg, angleMin, angleSec};
    }
}
