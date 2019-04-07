package com.company;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.*;


public class SoundServer {
    ServerSocket MyService;
    Socket clientSocket = null;
    ObjectInputStream input;
    AudioFormat audioFormat;
    SourceDataLine sourceDataLine;
    ByteArrayOutputStream out = null;
    byte tempBuffer[] = new byte[10000];

    SoundServer() {

        try {
            MyService = new ServerSocket(1234);
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println("Host Name: " + ip.getHostName());
            System.out.println("Waiting for client on port " +
                    MyService.getLocalPort() + "...");
            Socket ser = MyService.accept();

            System.out.println("Just connected to " + ser.getRemoteSocketAddress());

            input = new ObjectInputStream(ser.getInputStream());
            while (true){
                playByteArray((byte[]) input.readObject());
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void playByteArray(byte[] bytes){
        try {
            System.out.println("Runs");
            Thread a = new play();
            ((play) a).clip = AudioSystem.getClip(); //generates a generic audio clip check API doc for more info
            ((play) a).clip.open(getAudioFormat(), bytes, 0, bytes.length);
            int start = Math.toIntExact((((play) a).clip.getMicrosecondLength()));
            System.out.println(bytes.length);
            a.start();
            delay(start);
            ((play) a).clip.close();
        }catch (LineUnavailableException E){
            System.out.println(E);
        }
    }

    public class play extends Thread {
        public Clip clip;

        public void run() {
            clip.start();
        }
    }


    private void playAudio() {
        try {
            byte audio[] = out.toByteArray();
            InputStream input =
                    new ByteArrayInputStream(audio);
            final AudioFormat format = getAudioFormat();
            final AudioInputStream ais =
                    new AudioInputStream(input, format,
                            audio.length / format.getFrameSize());
            DataLine.Info info = new DataLine.Info(
                    SourceDataLine.class, format);
            final SourceDataLine line = (SourceDataLine)
                    AudioSystem.getLine(info);
            line.open(format);
            line.start();

            Runnable runner = new Runnable() {
                int bufferSize = (int) format.getSampleRate()
                        * format.getFrameSize();
                byte buffer[] = new byte[bufferSize];

                public void run() {
                    try {
                        int count;
                        while ((count = ais.read(
                                buffer, 0, buffer.length)) != -1) {
                            if (count > 0) {
                                line.write(buffer, 0, count);
                            }
                        }
                        line.drain();
                        line.close();
                    } catch (IOException e) {
                        System.err.println("I/O problems: " + e);
                        System.exit(-3);
                    }
                }
            };
            Thread playThread = new Thread(runner);
            playThread.start();
        } catch (LineUnavailableException e) {
            System.err.println("Line unavailable: " + e);
            System.exit(-4);
        }
    }

    public void delay(int time){
        try{
            Thread.sleep(time);
        } catch (InterruptedException E){
            System.out.println(E);
        }
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 44100.0F;
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }

    public static void main(String [] args){
        SoundServer s2 = new SoundServer();
    }}