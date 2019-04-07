package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.DataFormatException;

public class Server extends JFrame {
    private ServerSocket server = null;
    private ObjectInputStream in = null;
    public  JFrame frame = null;
    private int port = 1234;
    public static void main(String[]args) {
        Server show = new Server();
        show.StartStream();
    }

    public Server(){
        try {
            server = new ServerSocket(port);
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println("Host Name: " + ip.getHostName());
            System.out.println("Waiting for client on port " +
                    server.getLocalPort() + "...");
            Socket ser = server.accept();

            System.out.println("Just connected to " + ser.getRemoteSocketAddress());

            in = new ObjectInputStream(ser.getInputStream());
        } catch (IOException E){
            System.out.println(E);
        }
    }

    public void StartStream(){
        final Rectangle screenRect = new Rectangle(1920,1080);
        CreateJframe(screenRect);
        Image a = new BufferedImage(1,1,1);
        Icon  c = new ImageIcon(a);
        JLabel b = new JLabel(c);
        frame.getContentPane().add(b);
       /* KeyboardServer keyServer = new KeyboardServer(port);
        frame.addKeyListener(keyServer.listener);
        keyServer.start();
        MouseKeyServer mouse = new MouseKeyServer(port);
        frame.addMouseListener(mouse.getClick());
        frame.addMouseWheelListener(mouse.getScroll()); */
        while(true){
            try {
                ((ImageIcon) c).setImage(ImageResizer.resize(createImageFromBytes(CompressionUtils.decompress((byte[])in.readObject())), screenRect.width , screenRect.height));
                b.setIcon(c);
            } catch (IOException | ClassNotFoundException | DataFormatException E){
                System.out.println(E);
            }
            frame.repaint();
        }
    }

    public void CreateJframe(Rectangle screenRect) {
        frame = new JFrame();
        frame.setSize(screenRect.width, screenRect.height);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.setVisible(true);
    }

    private BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
