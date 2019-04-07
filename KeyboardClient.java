package com.company;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class KeyboardClient extends Thread {
    Socket keyboard;
    ObjectInputStream in;
    Robot keybard;
    public KeyboardClient(int port, String host){
        try {
            keyboard = new Socket(host, port + 6);
            in = new ObjectInputStream(keyboard.getInputStream());
            keybard = new Robot();
        }catch (IOException | AWTException E){
            System.out.println(E);
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                String i = (String)in.readObject();
                switch (i.charAt(0)){
                    case '1' : i = i.replaceFirst("1", "");
                        keybard.keyPress(Integer.parseInt(i));
                        keybard.delay(5);
                        keybard.keyRelease(Integer.parseInt(i));
                        break;
                    case '2' : i = i.replaceFirst("2", "");
                        keybard.keyPress(Integer.parseInt(i));
                        break;
                    case '3' : i = i.replaceFirst("3", "");
                        keybard.keyRelease(Integer.parseInt(i));
                        break;
                }
            } catch (IOException | ClassNotFoundException E){
                System.out.println(E);
            }
        }
    }
}
