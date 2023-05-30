package view;

import javax.swing.*;
import java.awt.*;

public class LoadFrame extends JFrame {
    Thread thread;
    private final int WIDTH;
    private final int HEIGHT;
    private int process;
    private JLabel label;
    private LoadFrame frame;

    public LoadFrame(){
        thread = new Thread();
        WIDTH = 300;
        HEIGHT = 150;
        process = 0;
        frame = this;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setLayout(null);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);

        label = new JLabel("Waiting.");
        label.setLocation(45, 15);
        label.setSize(100, 40);
        label.setFont(new Font("Rockwell", Font.BOLD, 14));
        add(label);

        setVisible(true);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (process = 0; process < 5; process++) {
                    try {
                        thread.sleep(300);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    String text = "Waiting..";
                    for (int i = 0; i < process; i++) {
                        text += ".";
                    }
                    label.setText(text);

                    repaint();
                }

                try {
                    thread.sleep(500);
                } catch (Exception e){
                    e.printStackTrace();
                }

                //frame.setVisible(false);
                frame.hide();
            }
        });
        thread.start();


    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(new Color(0, 0, 0));
        g.drawRect(50, 77, 201, 15);

        g.setColor(new Color(0, 175, 0));
        g.fill3DRect(51, 78, 40 * process, 14, true);
    }

//    public static void main(String[] args) {
//        new LoadFrame();
//    }
}
