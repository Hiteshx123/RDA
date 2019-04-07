package com.company;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;

public class KeyboardServer extends Thread{
    ServerSocket b;
    ObjectOutputStream out;
    Socket client;
    String KeyCode;
    public KeyboardServer(int port){
        try {
            b = new ServerSocket(port + 6);
            client = b.accept();
            System.out.println("KEYBOARD CONNECTED");
            out = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException E){
            System.out.println(E + "KeyBoard");
        }
    }
    KeyListener listener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
            KeyCode = "1" + e.getKeyCode();
            System.out.println(KeyCode);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            KeyCode = "2" + e.getKeyCode();
            System.out.println(KeyCode);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            KeyCode = "2" + e.getKeyCode();
            System.out.println(KeyCode);
        }
    };

    @Override
    public void run() {
        while(true){
            try {
                if(KeyCode != null) {
                    System.out.println(KeyCode);
                    out.writeObject(KeyCode);
                    System.out.println("sent keycode to server");
                    KeyCode = null;
                }
            } catch (IOException E){
                System.out.println();
            }
        }
    }
}
