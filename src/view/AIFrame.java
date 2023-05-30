package view;

import controller.Controller;
import model.Difficulty;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class AIFrame extends JFrame {
    public BeginFrame beginFrame;
    public boolean isLogin;
    public User user;
    public ArrayList<User> users;

    private final int WIDTH;
    private final int HEIGHT;

    public AIFrame(){
        setTitle("Jungle");
        this.WIDTH = 400;
        this.HEIGHT = 500;
        users = new ArrayList<>();

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        addBackButton();
        addEasyButton();
        addNormalButton();
        addHardButton();
        addUserButton();
        addRankButton();

        Image image = new ImageIcon("resource/background/bg.png").getImage();
        image = image.getScaledInstance(400, 500,Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        JLabel bg = new JLabel(icon);
        bg.setSize(400, 500);
        bg.setLocation(0, 0);
        add(bg);

        try {
            File file = new File("user\\users");

            String temp;
            InputStreamReader read = new InputStreamReader(new FileInputStream(file),"GBK");
            ArrayList<String> readList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(read);

            while(true){
                if (!((temp = reader.readLine()) != null && !"".equals(temp))) break;
                readList.add(temp);
                System.out.println(temp);
            }

            for (int i = 0; i < readList.size(); i++){
                String[] strArr = readList.get(i).split(" ");
                users.add(new User(strArr[0], strArr[1], Integer.parseInt(strArr[2])));
            }

        } catch (Exception e){}
    }

    private void addBackButton() {
        JButton button = new JButton("Back");
        button.addActionListener((e) -> {
            isLogin = false;
            user = null;
            this.setVisible(false);
            beginFrame.setVisible(true);
        });
        button.setLocation(20, 20);
        button.setSize(60, 30);
        button.setFont(new Font("Rockwell", Font.BOLD, 10));
        add(button);
    }

    private void addEasyButton() {
        JButton button = new JButton("Easy");
        button.addActionListener((e) -> {
            if (Controller.timer != null){
                Controller.timer.stop();
                Controller.timer = null;
            }

            beginFrame.gameFrame.getBoardView().controller.reset();
            this.setVisible(false);
            beginFrame.gameFrame.timeLabel.setVisible(false);
            beginFrame.gameFrame.statusLabel.setLocation(810, 81);
            beginFrame.gameFrame.repaint();
            beginFrame.gameFrame.getBoardView().controller.AIDiff = Difficulty.EASY;
            beginFrame.gameFrame.setVisible(true);
        });
        button.setLocation(100, 140);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addNormalButton() {
        JButton button = new JButton("Normal");
        button.addActionListener((e) -> {
            if (Controller.timer != null){
                Controller.timer.stop();
                Controller.timer = null;
            }

            beginFrame.gameFrame.getBoardView().controller.reset();
            this.setVisible(false);
            beginFrame.gameFrame.timeLabel.setVisible(false);
            beginFrame.gameFrame.statusLabel.setLocation(810, 81);
            beginFrame.gameFrame.repaint();
            beginFrame.gameFrame.getBoardView().controller.AIDiff = Difficulty.NORMAL;
            beginFrame.gameFrame.setVisible(true);

        });
        button.setLocation(100, 230);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addHardButton() {
        JButton button = new JButton("Hard");
        button.addActionListener((e) -> {
//            this.setVisible(false);
//            beginFrame.gameFrame.getBoardView().controller.AIDiff = Difficulty.HARD;
//            beginFrame.gameFrame.setVisible(true);
            JOptionPane.showMessageDialog(null, "Haven't been implemented yet");
        });
        button.setLocation(100, 320);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addUserButton(){
        JButton button = new JButton("User");
        button.addActionListener((e) -> {
            if (isLogin){
                String inf = "ID：" + user.name + "\n" + "Score：" + user.score;
                JOptionPane.showMessageDialog(null, inf, "Information",JOptionPane.INFORMATION_MESSAGE);
            } else {
                new LoginFrame(this);
            }
        });
        button.setLocation(240, 20);
        button.setSize(60, 30);
        button.setFont(new Font("Rockwell", Font.BOLD, 10));
        add(button);
    }

    private void addRankButton(){
        JButton button = new JButton("Rank");
        button.addActionListener((e) -> {
            Collections.sort(users);
            String rank = "Rank  ID (Score)\n";
            for (int i = 0; i < users.size(); i++){
                rank += "   " + (i + 1) + "      " + users.get(i).name + " (" + users.get(i).score + ")\n";
            }
            JOptionPane.showMessageDialog(null, rank, "Rank List",JOptionPane.INFORMATION_MESSAGE);
        });
        button.setLocation(310, 20);
        button.setSize(60, 30);
        button.setFont(new Font("Rockwell", Font.BOLD, 10));
        add(button);
    }
}
