package com.company;

        import java.awt.AWTException;
        import java.awt.FlowLayout;
        import java.awt.Rectangle;
        import java.awt.Robot;
        import java.awt.Toolkit;
        import java.awt.image.BufferedImage;
        import java.io.File;
        import java.io.IOException;

        import javax.imageio.ImageIO;
        import javax.swing.JFrame;
        import javax.swing.JLabel;

/**
 * This program demonstrates how to capture a screenshot (full screen) as an
 * image which will be saved into a file.
 *
 */
public class FullScreenCaptureExample extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        FullScreenCaptureExample f = new FullScreenCaptureExample();
        try {
            /*
             * Let the program wait for 5 seconds to allow you to open the
             * window whose screenshot has to be captured
             */
            Thread.sleep(5000);
            Robot robot = new Robot();
            String fileName = "D://FullScreenshot.jpg";

            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit()
                    .getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            ImageIO.write(screenFullImage, "jpg", new File(fileName));

            f.setLocation(500, 500);
            JLabel text = new JLabel("A full screenshot saved!");
            f.add(text);
            f.setSize(200, 100);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.getContentPane().setLayout(new FlowLayout());
            f.setVisible(true);
        } catch (AWTException | IOException | InterruptedException ex) {
            System.err.println(ex);
        }
    }
}