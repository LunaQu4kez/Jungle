package view;

import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LoginFrame extends JFrame {
    AIFrame aiFrame;
    RegisterFrame registerFrame;
    ArrayList<User> users;
    JTextField tfAccount;
    JPasswordField tfPassword;

    public LoginFrame(AIFrame aiFrame) {
        this.aiFrame = aiFrame;
        this.users = aiFrame.users;

        setTitle("Login");
        setSize(380, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);

        JLabel lbAccount = new JLabel("ID");
        lbAccount.setFont(new Font("", Font.BOLD, 20));
        lbAccount.setBounds(55, 40, 50, 40);
        add(lbAccount);

        tfAccount = new JTextField();
        tfAccount.setBounds(110, 45, 200, 30);
        add(tfAccount);

        JLabel lbPassword = new JLabel("pw");
        lbPassword.setFont(new Font("", Font.BOLD, 20));
        lbPassword.setBounds(55, 90, 50, 40);
        add(lbPassword);

        tfPassword = new JPasswordField();
        tfPassword.setBounds(110, 95, 200, 30);
        add(tfPassword);

        addLoginButton();
        addRegisterButton();

        setVisible(true);
    }

    private void addLoginButton(){
        JButton button = new JButton("Login");
        button.addActionListener((e) -> {
            login();
        });
        button.setBounds(50, 150, 120, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 16));
        add(button);
    }

    private void addRegisterButton(){
        JButton button = new JButton("Register");
        button.addActionListener((e) -> {
            setVisible(false);
            registerFrame = new RegisterFrame(this);
        });
        button.setBounds(190, 150, 120, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 16));
        add(button);
    }

    private void login(){
        HashMap<String, String> id_pw = new HashMap<>();
        HashMap<String, User> id_user = new HashMap<>();
        if (users.size() != 0){
            for (int i = 0; i < users.size(); i++){
                id_pw.put(users.get(i).name, users.get(i).password);
            }
            for (int i = 0; i < users.size(); i++){
                id_user.put(users.get(i).name, users.get(i));
            }
        }

        String id = tfAccount.getText();
        char[] pwChars = tfPassword.getPassword();
        String pw = new String(pwChars);
        if (id.equals("")){
            JOptionPane.showMessageDialog(null, "Please input ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (pwChars.length == 0){
            JOptionPane.showMessageDialog(null, "Please input password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (id_pw.containsKey(id)){  // 包含此用户
            if (pw.equals(id_pw.get(id))){  // 密码正确
                setVisible(false);
                aiFrame.isLogin  = true;
                aiFrame.user = id_user.get(id);
                this.setVisible(false);
            } else {  // 密码错误
                JOptionPane.showMessageDialog(null, "Wrong password !", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {  // 没有此用户
            JOptionPane.showMessageDialog(null, "No such User", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
