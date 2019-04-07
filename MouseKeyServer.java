package com.company;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MouseKeyServer {
    Thread pos;
    Thread click;
    Thread scroll;

    public MouseKeyServer(int port){
        pos = new MousePosition(1001);
        click = new MouseClick(1002);
        scroll = new MouseScroll(1003);
        pos.start();
        System.out.println("ITFUCKINGWPORKS");
        //click.start();
        System.out.println("ITFUCKINGWPORKS");
        //scroll.start();
        System.out.println("ITFUCKINGWPORKS");
    }

    public MouseWheelListener getScroll(){
        MouseScroll a = (MouseScroll) scroll;
        return a.wheelListener;
    }

    public MouseListener getClick(){
        MouseClick a = (MouseClick) click;
        return a.listener;
    }

    public class MousePosition extends Thread {
        ServerSocket a;
        ObjectOutputStream out;
        Socket client;
        public MousePosition(int port){
            System.out.println("Constructed call");

            try {
                a = new ServerSocket(port);
                client = a.accept();
                System.out.println("Accepted Client: " + port);
                out = new ObjectOutputStream(client.getOutputStream());
            } catch (IOException E){
                System.out.println(E + "MouseScroll");
            }
        }
        public void run() {
                while(true) {
                    Point mp = GetMousePos();
                    System.out.println(mp.getX() + ", " + mp.getY());
                    try {
                        out.writeObject(mp);
                    } catch (IOException E) {
                        System.out.println(E);
                    }
                }
        }
        public Point GetMousePos(){
            return MouseInfo.getPointerInfo().getLocation();
        }
    }

    public class MouseClick extends Thread {
        String currentMove;
        ServerSocket b;
        ObjectOutputStream out;
        Socket client;
        public MouseClick(int port){
            try {

                b = new ServerSocket(port);
                client = b.accept();
                out = new ObjectOutputStream(client.getOutputStream());
            } catch (IOException E){
                System.out.println(E + "MouseClick");
            }
        }
        public MouseListener listener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentMove = e.getButton() + "Click";
                System.out.println("Click");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                currentMove = e.getButton() + "Press";
                System.out.println("Press");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentMove = e.getButton() + "Release";
                System.out.println("Release");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                currentMove = e.getButton() + "Enter";
                System.out.println("Enter");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                currentMove = e.getButton() + "Exit";
                System.out.println("Exit");
            }
        };
        public void run() {
            while(true){
                try {
                    if(currentMove != null) {
                        System.out.println(currentMove);
                        out.writeObject(currentMove);
                    }
                    currentMove = null;

                } catch (IOException E){

                }
            }
        }
    }

    public class MouseScroll extends Thread {
        ServerSocket c;
        ObjectOutputStream out;
        Socket client;
        int scrollUnits = 0;
        MouseWheelListener wheelListener = new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                scrollUnits = e.getUnitsToScroll();
            }
        };

        public MouseScroll(int port){
            try {

                c = new ServerSocket(port);
                client = c.accept();
                out = new ObjectOutputStream(client.getOutputStream());

            } catch (IOException E){
                System.out.println(E + "MouseScroll");
            }
        }

        public void run(){
            while(true){
                try {
                   // if(scrollUnits != 0) {
                        out.writeObject(scrollUnits);
                  //  }
                  //  scrollUnits = 0;
                } catch (IOException E){

                }
            }
        }
    }
}
