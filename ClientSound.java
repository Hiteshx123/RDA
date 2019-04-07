package com.company;


import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Vector;

public class ClientSound {
    boolean stopCapture = false;
    AudioFormat audioFormat;
    TargetDataLine targetDataLine;
    ByteArrayOutputStream out = null;
    ObjectOutputStream out2 = null;
    BufferedInputStream in = null;
    Socket sock = null;
    public static void main(String[] args) {
        ClientSound tx = new ClientSound();
        tx.getFormats();
        tx.captureAudio();
    }
    private void captureAudio() {
        try {
            sock = new Socket("DESKTOP-SL4C3HK", 1234);
            out2 = new ObjectOutputStream(sock.getOutputStream());
            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
            audioFormat = getAudioFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(
                    TargetDataLine.class, audioFormat);
            Mixer mixer = AudioSystem.getMixer(mixerInfo[7]);
            mixer.open();
            targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();

            Thread captureThread = new CaptureThread();
            captureThread.start();

        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    public void getFormats() {
        try {
            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
            Mixer mixer = AudioSystem.getMixer(mixerInfo[7]); // default mixer
            mixer.open();

            System.out.printf("Supported SourceDataLines of default mixer (%s):\n\n", mixer.getMixerInfo().getName());
            for (Line.Info info : mixer.getSourceLineInfo()) {
                if (SourceDataLine.class.isAssignableFrom(info.getLineClass())) {
                    SourceDataLine.Info info2 = (SourceDataLine.Info) info;
                    System.out.println(info2);
                    System.out.printf("  max buffer size: \t%d\n", info2.getMaxBufferSize());
                    System.out.printf("  min buffer size: \t%d\n", info2.getMinBufferSize());
                    AudioFormat[] formats = info2.getFormats();
                    System.out.println("  Supported Audio formats: ");
                    for (AudioFormat format : formats) {
                        System.out.println("    " + format);
          System.out.printf("      encoding:           %s\n", format.getEncoding());
          System.out.printf("      channels:           %d\n", format.getChannels());
          System.out.printf(format.getFrameRate()==-1?"":"      frame rate [1/s]:   %s\n", format.getFrameRate());
          System.out.printf("      frame size [bytes]: %d\n", format.getFrameSize());
         System.out.printf(format.getSampleRate()==-1?"":"      sample rate [1/s]:  %s\n", format.getSampleRate());
         System.out.printf("      sample size [bit]:  %d\n", format.getSampleSizeInBits());
          System.out.printf("      big endian:         %b\n", format.isBigEndian());

          Map<String,Object> prop = format.properties();
          if(!prop.isEmpty()) {
              System.out.println("      Properties: ");
              for(Map.Entry<String, Object> entry : prop.entrySet()) {
                  System.out.printf("      %s: \t%s\n", entry.getKey(), entry.getValue());
              }
         }
                    }
                    System.out.println();
                } else {
                    System.out.println(info.toString());
                }
            }
        } catch (LineUnavailableException E){
            System.out.println(E);
        }
    }

    public AudioFormat getAudioFormat() {
        float sampleRate = 44100F;
        // You can try also 8000,11025,16000,22050,44100
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
                bigEndian);
    }

    class CaptureThread extends Thread {
        // temporary buffer
        byte tempBuffer[] = new byte[10000];
        public void run() {
            boolean running = true;
            try {
                while (running) {
                    out = new ByteArrayOutputStream();
                    int count =
                            targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                    if (count > 0) {
                        out.write(tempBuffer, 0, count);
                    }
                    out2.writeObject(out.toByteArray());
                }
                out.close();
            } catch (IOException e) {
                System.err.println("I/O problems: " + e);
                System.exit(-1);
            }
        }
    }
}
