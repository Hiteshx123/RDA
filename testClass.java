package com.company;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

public class testClass {

    public static void main(String[] args) {

        BufferedImage bi = null, bj = null;
        Rectangle rect = new Rectangle(0, 0, 810, 384);
        long startTime, finishTime;

        startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            bi = JNAScreenShot.getScreenshot(rect);
        }
        finishTime = System.currentTimeMillis();

        System.out.println("With JNA Library: " + (finishTime - startTime)/1000);

        Robot robo = null;

        startTime = System.currentTimeMillis();
        try {
            robo = new Robot();
        } catch (AWTException a) {
        }
        for (int i = 0; i < 10; i++) {
            bj = robo.createScreenCapture(rect);
        }
        finishTime = System.currentTimeMillis();
        System.out.println("With Robot Class " + (finishTime - startTime)/1000);
    }
}