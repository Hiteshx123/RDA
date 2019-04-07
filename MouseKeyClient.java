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
        try {
            mouse = new Robot();
        } catch (AWTException E){
            System.out.println(E);
        }
        pos = new MousePosition(1001, host);
        click = new MouseClick(1002, host);
        scroll = new MouseScroll(1003, host);
        pos.start();
        click.start();
        scroll.start();
    }

    public class MousePosition extends Thread {
        Socket a;
        ObjectInputStream in;
        public MousePosition(int port, String host){
            try {
                a = new Socket(host,port);
                in = new ObjectInputStream(a.getInputStream());
            } catch (IOException E){
                System.out.println(E + "MousePosition");
            }
        }
        public void run() {
            mouse.delay(5000);
            while(true) {
                try {
                    Point a = (Point) in.readObject();
                    mouse.mouseMove(a.x,a.y);
                    System.out.println(a.x + ", " + a.y);
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
                b = new Socket(host, port);
                in = new ObjectInputStream(b.getInputStream());
            } catch (IOException E){
                System.out.println(E + "MouseClick");
            }
        }
        public void run() {
            mouse.delay(5000);
            int button = 0;
            while(true){
                try {
                    currentMove = (String) in.readObject();
                    System.out.print(currentMove);
                    button = currentMove.charAt(0);
                    currentMove = currentMove.replaceFirst("" + button, "");
                    System.out.println(currentMove);
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
                c = new Socket(host,port);
                in = new ObjectInputStream(c.getInputStream());
            } catch (IOException E){
                System.out.println(E + "MouseScroll");
            }
        }

        public void run(){
            mouse.delay(5000);
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
