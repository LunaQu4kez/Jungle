package view;

import model.CellType;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorFrame extends JFrame {
    private JLabel R;
    private JLabel G;
    private JLabel B;

    private JSlider slider1;
    private JSlider slider2;
    private JSlider slider3;

    private JLabel jl1;
    private JLabel jl2;
    private JLabel jl3;

    private JTextArea jta;// 大色块
    private JButton btn;

    public GameFrame gameFrame;

    public ColorFrame(String s) {

        R = new JLabel("R");
        G = new JLabel("G");
        B = new JLabel("B");

        slider1 = new JSlider(0, 255, 127);
        slider2 = new JSlider(0, 255, 127);
        slider3 = new JSlider(0, 255, 127);

        jl1 = new JLabel("127");
        jl2 = new JLabel("127");
        jl3 = new JLabel("127");

        jta = new JTextArea(8, 14);
        btn = new JButton("OK");
        btn.addActionListener((e) -> {
            CellView[][] cellViews = gameFrame.getBoardView().gridViews;
            Color c = new Color(slider1.getValue(), slider2.getValue(), slider3.getValue());
            if (s.equals("Grass")){
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 7; j++) {
                        if (cellViews[i][j].type == CellType.SPRING_GRASS
                                || cellViews[i][j].type == CellType.AUTUMN_GRASS
                                || cellViews[i][j].type == CellType.OTHER_GRASS){
                            cellViews[i][j].type = CellType.OTHER_GRASS;
                            cellViews[i][j].background = c;
                            cellViews[i][j].repaint();
                            cellViews[i][j].revalidate();
                        }
                    }
                }
            } else if (s.equals("River")){
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 7; j++) {
                        if (cellViews[i][j].type == CellType.SPRING_RIVER
                                || cellViews[i][j].type == CellType.AUTUMN_RIVER
                                || cellViews[i][j].type == CellType.OTHER_RIVER){
                            cellViews[i][j].type = CellType.OTHER_RIVER;
                            cellViews[i][j].background = c;
                            cellViews[i][j].repaint();
                            cellViews[i][j].revalidate();
                        }
                    }
                }
            } else if (s.equals("Trap")){
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 7; j++) {
                        if (cellViews[i][j].type == CellType.TRAP || cellViews[i][j].type == CellType.OTHER_TRAP){
                            cellViews[i][j].type = CellType.OTHER_TRAP;
                            cellViews[i][j].background = c;
                            cellViews[i][j].repaint();
                            cellViews[i][j].revalidate();
                        }
                    }
                }
            } else if (s.equals("Dens")){
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 7; j++) {
                        if (cellViews[i][j].type == CellType.DEN || cellViews[i][j].type == CellType.OTHER_DEN){
                            cellViews[i][j].type = CellType.OTHER_DEN;
                            cellViews[i][j].background = c;
                            cellViews[i][j].repaint();
                            cellViews[i][j].revalidate();
                        }
                    }
                }
            }
            gameFrame.getBoardView().repaint();
            gameFrame.getBoardView().revalidate();
            this.dispose();
        });
        init();

        setTitle("ColorChooser");
        setLocationRelativeTo(null);
        setSize(500, 200);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void init() {
        // 左侧大面板、
        JPanel jp = new JPanel();
        JPanel jp1 = new JPanel();
        JPanel jp2 = new JPanel();
        JPanel jp3 = new JPanel();

        jp1.add(R);
        jp1.add(slider1);
        jp1.add(jl1);

        jp2.add(G);
        jp2.add(slider2);
        jp2.add(jl2);

        jp3.add(B);
        jp3.add(slider3);
        jp3.add(jl3);

        jp.setLayout(new GridLayout(3, 1));
        jp.add(jp1);
        jp.add(jp2);
        jp.add(jp3);
        this.add(jp, BorderLayout.CENTER);

        // 右侧调色块
        JPanel jpColor = new JPanel();
        jta.setEditable(false);
        jta.setBackground(new Color(127, 127, 127));
        jpColor.add(jta);
        this.add(jpColor, BorderLayout.EAST);

        this.add(btn, BorderLayout.SOUTH);

        addEventHandler();
    }

    public void addEventHandler() {// 添加事件处理器
        ChangeListener lis1 = new ChangeListener() {// 滑块监听器
            public void stateChanged(ChangeEvent e) {
                int r = slider1.getValue();// 获取滑块的值
                int g = slider2.getValue();
                int b = slider3.getValue();

                jl1.setText(r + "");
                jl2.setText(g + "");
                jl3.setText(b + "");
                Color c = new Color(r, g, b);// 用得到的色值创建Color对象
                jta.setBackground(c);// 设置色块颜色
            }
        };

        slider1.addChangeListener(lis1);// 注册事件监听（滑块）
        slider2.addChangeListener(lis1);
        slider3.addChangeListener(lis1);
    }
}
