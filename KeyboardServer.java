package com.company;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class KeyboardServer extends Thread{
    ServerSocket b;
    ObjectOutputStream out;
    Socket client;
    public KeyboardServer(int port){
        try {
            b = new ServerSocket(port + 6);
            client = b.accept();
            out = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException E){
            System.out.println(E + "KeyBoard");
        }
    }
    String KeyCode = "";
    KeyListener listener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
            KeyCode = "1" + e.getKeyCode();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            KeyCode = "2" + e.getKeyCode();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            KeyCode = "2" + e.getKeyCode();
        }
    };

    @Override
    public void run() {
        while(true){
            try {
                out.writeObject(KeyCode);
            } catch (IOException E){
                System.out.println();
            }
        }
    }
}
