package com.company;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class MouseKeyClient {
    MousePosition pos;
    MouseClick click;
    MouseScroll scroll;
    Robot mouse;

    public MouseKeyClient(int port, String host){
        pos = new MousePosition(port, host);
        click = new MouseClick(port, host);
        scroll = new MouseScroll(port, host);
        pos.start();
        click.start();
        scroll.start();
    }

    public class MousePosition extends Thread {
        Socket a;
        ObjectInputStream in;
        public MousePosition(int port, String host){
            try {
                a = new Socket(host,port + 1);
                in = new ObjectInputStream(a.getInputStream());
            } catch (IOException E){
                System.out.println(E + "MouseScroll");
            }
        }
        public void run() {
            while(true) {
                try {
                    Point a = (Point) in.readObject();
                    mouse.mouseMove(a.x,a.y);
                } catch (IOException | ClassNotFoundException E) {
                    System.out.println(E);
                }
            }
        }

    }

    public class MouseClick extends Thread {
        String currentMove;
        Socket b;
        ObjectInputStream in;
        public MouseClick(int port, String host){
            try {
                b = new Socket(host, port + 2);
                in = new ObjectInputStream(b.getInputStream());
            } catch (IOException E){
                System.out.println(E + "MouseClick");
            }
        }
        public void run() {
            int button = 0;
            while(true){
                try {
                    currentMove = (String) in.readObject();
                    button = currentMove.charAt(0);
                    currentMove = currentMove.replaceFirst("" + button, "");
                    switch (currentMove){
                        case "Click": mouse.mousePress(button);
                            mouse.delay(5);
                            mouse.mouseRelease(button);
                            break;
                        case "Press": mouse.mousePress(button);
                            break;
                        case  "Release": mouse.mouseRelease(button);
                            break;
                    }
                } catch (IOException | ClassNotFoundException E){
                    System.out.println(E);
                }
            }
        }
    }

    public class MouseScroll extends Thread {
        Socket c;
        ObjectInputStream in;
        public MouseScroll(int port, String host){
            try {
                c = new Socket(host,port + 3);
                in = new ObjectInputStream(c.getInputStream());
            } catch (IOException E){
                System.out.println(E + "MouseScroll");
            }
        }

        public void run(){
            while(true){
                try {
                    mouse.mouseWheel(in.read());
                } catch (IOException E){
                    System.out.println(E);
                }
            }
        }
    }
}
