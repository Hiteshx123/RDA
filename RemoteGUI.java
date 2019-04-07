package com.company;

import javax.swing.*;
import java.awt.*;

public class RemoteGUI {
    public JFrame frame;

    public RemoteGUI() {
        frame = new JFrame("HT RDA");
        frame.setSize(720,480);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setBackground(Color.gray);
    }

    private void run() {
        JButton host = Place_Jbuton("Host");
        JButton client = Place_Jbuton("Client");
    }

    private JButton Place_Jbuton(String input){
        JButton buton = new JButton(input);
        frame.add(buton);
        frame.repaint();
        return buton;
    }

    public static void main(String[]args){
        RemoteGUI a = new RemoteGUI();
        a.run();
    }
}
