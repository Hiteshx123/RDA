package com.company;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;

public class SoundRecorder {
    private boolean stopped = false;
    private TargetDataLine line;
    private AudioFormat format;
    private byte[] data = null;

    public static void main(String[]args){
        SoundRecorder a = new SoundRecorder();
        a.StartRecording();
    }

    public SoundRecorder(){

    }
    public void StartRecording(){
        int numBytesRead;
        data = new byte[line.getBufferSize() / 5];

// Begin audio capture.
        line.start();

// Here, stopped is a global boolean set by another thread.
        while (!stopped) {
            // Read the next chunk of data from the TargetDataLine.
            numBytesRead =  line.read(data, 0, data.length);
            // Save this chunk of data.
        }
    }

}
