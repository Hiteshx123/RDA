package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ScreenCapture {
    private BufferedImage screenFullImage = null;
    private ObjectOutputStream out = null;
    private ObjectOutputStream outSound = null;
    private Socket source = null;
    int port = 0;
    String host;
    private byte[] ImageBytes = null;

    public static void main(String[] args){
        ScreenCapture t = new ScreenCapture();
        t.FullScreenStream(24);
        System.exit(0);
    }


    public void FullScreenStream(int FrameRate){
        ConnectServer();
        long start = System.currentTimeMillis();
        Thread a = new CaputureScreen();
        a.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException E){
            System.out.println(E);
        }
        KeyboardClient keyboard = new KeyboardClient(port,host);
        MouseKeyClient mouse = new MouseKeyClient(port,host);
        try{
            Thread.sleep(2000);
        }catch (InterruptedException E){

        }
        while(true){
            try {

                out.writeObject(CompressionUtils.compress(toByteArrayAutoClosable( ( (CaputureScreen)a).pic, "jpeg")));

            } catch (IOException E){
                System.out.println(E);
            }
        }
        //FullStreamDebug(start, FrameRate, i);

    }

    public void FullStreamDebug(long start, int FrameRate, int totalFrames){
        long end = System.currentTimeMillis();
        double totalTime = (double)(end - start)/1000;
        double EffectiveFrameRate = (totalFrames/totalTime);
        System.out.println("Start: " + start);
        System.out.println("End: " + end);
        System.out.println("Total Time: " + totalTime);
        System.out.println("Total Frames: " + totalFrames);
        System.out.println("The effective frame-rate: " + EffectiveFrameRate);
        System.out.println("Frame-rate desired: " + FrameRate);
        System.out.println("Frame-Division Loss: " + (1000 - ((1000/FrameRate))));

    }

    public void ConnectServer() {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter server Name: ");
        host = in.nextLine();
        System.out.println("Please enter server Port: ");
        port = in.nextInt();
        try {
            source = new Socket(host, port);
            out = new ObjectOutputStream(source.getOutputStream());
        } catch (IOException E) {
            System.out.println(E);
        }
    }

    private byte[] toByteArrayAutoClosable(BufferedImage image, String type) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            ImageIO.write(image, type, out);
            return out.toByteArray();
        }
    }

    public class CaputureScreen extends Thread {
        protected BufferedImage pic;
        private Rectangle screenRect = new Rectangle(1920,1080);
        public void run() {
            while(true) {
                pic = JNAScreenShot.getScreenshot(screenRect);
            }
        }
    }

}
