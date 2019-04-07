package com.company;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MouseKeyServer {
    MousePosition pos;
    MouseClick click;
    MouseScroll scroll;

    public MouseKeyServer(int port){
        pos = new MousePosition(port + 1);
        click = new MouseClick(port + 2);
        scroll = new MouseScroll(port + 3);
        pos.start();
        click.start();
        scroll.start();
    }

    public MouseWheelListener getScroll(){
        return scroll.wheelListener;
    }

    public MouseListener getClick
            (){
        return click.listener;
    }

    public class MousePosition extends Thread {
        ServerSocket a;
        ObjectOutputStream out;
        Socket client;
        public MousePosition(int port){
            try {
                a = new ServerSocket(port);
                client = a.accept();
                out = new ObjectOutputStream(client.getOutputStream());
            } catch (IOException E){
                System.out.println(E + "MouseScroll");
            }
        }
        public void run() {
                while(true) {
                    try {
                        out.writeObject(GetMousePos());
                    } catch (IOException E) {

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
        MouseListener listener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentMove = e.getButton() + "Click";
            }

            @Override
            public void mousePressed(MouseEvent e) {
                currentMove = e.getButton() + "Press";
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentMove = e.getButton() + "Release";
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                currentMove = e.getButton() + "Enter";
            }

            @Override
            public void mouseExited(MouseEvent e) {
                currentMove = e.getButton() + "Exit";
            }
        };
        public void run() {
            while(true){
                try {
                        out.writeObject(currentMove);
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
                    out.writeObject(scrollUnits);
                } catch (IOException E){

                }
            }
        }
    }
}
