package view;

import controller.Controller;
import model.Board;
import model.Difficulty;
import model.Timer;

import javax.swing.*;
import java.awt.*;

public class BeginFrame extends JFrame {
    GameFrame gameFrame;
    AIFrame aiFrame;

    private final int WIDTH;
    private final int HEIGHT;

    public BeginFrame() {
        setTitle("Jungle");
        this.WIDTH = 400;
        this.HEIGHT = 500;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        GameFrame gameFrame = new GameFrame(1100, 810);
        Controller controller = new Controller(gameFrame.getBoardView(), new Board());
        this.gameFrame = gameFrame;
        gameFrame.beginFrame = this;

        this.aiFrame = new AIFrame();
        aiFrame.beginFrame = this;

        addBeginButton();
        addAIButton();

        Image image = new ImageIcon("resource/background/bg.png").getImage();
        image = image.getScaledInstance(400, 500,Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        JLabel bg = new JLabel(icon);
        bg.setSize(400, 500);
        bg.setLocation(0, 0);
        add(bg);
    }

    private void addBeginButton() {
        JButton button = new JButton("Begin");
        button.addActionListener((e) -> {
            this.setVisible(false);
            Timer.time = 45;
            if (Controller.timer == null){
                Controller.timer = new Timer(gameFrame.getBoardView().controller);
                Controller.timer.start();
            }

            gameFrame.statusLabel.setLocation(770, 81);
            gameFrame.repaint();
            gameFrame.timeLabel.setVisible(true);
            gameFrame.getBoardView().controller.reset();
            gameFrame.getBoardView().controller.AIPlaying = false;
            gameFrame.getBoardView().controller.AIDiff = Difficulty.NONE;
            gameFrame.setVisible(true);
        });
        button.setLocation(100, 100);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addAIButton() {
        JButton button = new JButton("AI");
        button.addActionListener((e) -> {
            this.setVisible(false);
            gameFrame.getBoardView().controller.AIPlaying = true;
            aiFrame.setVisible(true);
        });
        button.setLocation(100, 300);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }
}
