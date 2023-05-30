package view;

import javax.swing.*;
import java.awt.*;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class GameFrame extends JFrame {
    public BeginFrame beginFrame;

    private final int WIDTH;
    private final int HEIGHT;

    private final int ONE_CHESS_SIZE;

    private BoardView view;

    JLabel statusLabel;
    JLabel timeLabel;

    public boolean isSpring;
    JLabel background;
    public final JLabel springBG;
    public final JLabel autumnBG;

    public GameFrame(int width, int height) {
        setTitle("Jungle");
        this.WIDTH = width;
        this.HEIGHT = height;
        this.ONE_CHESS_SIZE = (HEIGHT * 4 / 5) / 9;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        isSpring = true;

        addStatusLabel();
        addTimeLabel();
        addChessboard();
        addResetButton();
        addSaveButton();
        addLoadButton();
        addRegretButton();
        addPlaybackButton();
        addChangeBoardButton();
        addChangeThemeButton();
        addBackButton();

        Image image = new ImageIcon("resource/background/spring.png").getImage();
        image = image.getScaledInstance(1100, 810,Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        springBG = new JLabel(icon);
        springBG.setSize(1100, 810);
        springBG.setLocation(0, 0);

        image = new ImageIcon("resource/background/autumn.png").getImage();
        image = image.getScaledInstance(1100, 810,Image.SCALE_DEFAULT);
        icon = new ImageIcon(image);
        autumnBG = new JLabel(icon);
        autumnBG.setSize(1100, 810);
        autumnBG.setLocation(0, 0);

        background = springBG;
        add(background);

    }

    public BoardView getBoardView() {
        return view;
    }

    public void setBoardView(BoardView boardView) {
        this.view = boardView;
    }

    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        view = new BoardView(ONE_CHESS_SIZE, statusLabel, timeLabel);
        view.setLocation(HEIGHT / 5, HEIGHT / 10);
        add(view);
    }

    /**
     * 在游戏面板中添加标签
     */
    private void addStatusLabel() {
        statusLabel = new JLabel("Turn 1: BLUE");
        statusLabel.setLocation(HEIGHT - 40, HEIGHT / 10);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(statusLabel);
    }

    private void addTimeLabel() {
        timeLabel = new JLabel("Time: 45");
        timeLabel.setLocation(HEIGHT + 140, HEIGHT / 10);
        timeLabel.setSize(200, 60);
        timeLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(timeLabel);
    }

    private void addResetButton() {
        JButton button = new JButton("Reset");
        button.addActionListener((e) -> {
            view.controller.reset();
        });
        button.setLocation(HEIGHT, HEIGHT / 10 + 74);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addSaveButton() {
        JButton button = new JButton("Save");
        button.setLocation(HEIGHT, HEIGHT / 10 + 148);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click save");
            String path = JOptionPane.showInputDialog("存档名");
            while (path.equals("")){
                JOptionPane.showMessageDialog(null, "存档名不能为空");
                path = JOptionPane.showInputDialog("存档名");
            }
            view.controller.saveGame(path);
            new LoadFrame();
        });
    }

    private void addLoadButton() {
        JButton button = new JButton("Load");
        button.setLocation(HEIGHT, HEIGHT / 10 + 222);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click load");
            boolean b = view.controller.loadGame();
            if (b) new LoadFrame();
        });
    }

    private void addRegretButton() {
        JButton button = new JButton("Regret");
        button.setLocation(HEIGHT, HEIGHT / 10 + 296);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click regret");
            view.controller.regretOneStep();
            if (view.controller.AIPlaying){
                view.controller.regretOneStep();
            }
        });
    }

    private void addPlaybackButton() {
        JButton button = new JButton("Playback");
        button.setLocation(HEIGHT, HEIGHT / 10 + 370);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click playback");
            view.controller.playback();
            view.controller.timer.time = 20;
        });
    }

    private void addChangeBoardButton() {
        JButton button = new JButton("Change Chessboard");
        button.setLocation(HEIGHT, HEIGHT / 10 + 444);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 14));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click change chessboard");
            Object[] options = {"Grass", "River", "Trap", "Dens"};
            String s = (String) JOptionPane.showInputDialog(null, "Choose the one you want to change",
                    "Change Color", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (s != null){
                ColorFrame colorFrame = new ColorFrame(s);
                colorFrame.gameFrame = this;
            }
        });
    }

    private void addChangeThemeButton() {
        JButton button = new JButton("Change Theme");
        button.setLocation(HEIGHT, HEIGHT / 10 + 518);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 16));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click change theme");
            view.changeTheme(isSpring);
            if (isSpring){
                remove(background);
                isSpring = false;
                background = autumnBG;
                add(background);
            } else {
                remove(background);
                isSpring = true;
                background = springBG;
                add(background);
            }
            repaint();
            revalidate();
        });
    }

    private void addBackButton() {
        JButton button = new JButton("Back");
        button.setLocation(HEIGHT, HEIGHT / 10 + 592);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click back");
            this.setVisible(false);
            beginFrame.setVisible(true);
        });
    }

}
