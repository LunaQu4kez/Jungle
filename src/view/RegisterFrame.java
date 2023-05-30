package view;

import model.User;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;

public class RegisterFrame extends JFrame {
    LoginFrame loginFrame;
    JTextField tfAccount;
    JPasswordField tfPassword;

    public RegisterFrame(LoginFrame loginFrame){
        setTitle("Register");
        setSize(380, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);

        JLabel lbAccount = new JLabel("ID");
        lbAccount.setFont(new Font("", Font.BOLD, 20));
        lbAccount.setBounds(50, 50, 50, 40);
        add(lbAccount);

        tfAccount = new JTextField();
        tfAccount.setBounds(110, 50, 200, 40);
        add(tfAccount);

        JLabel lbPaasword = new JLabel("pw");
        lbPaasword.setFont(new Font("", Font.BOLD, 20));
        lbPaasword.setBounds(50, 100, 50, 40);
        add(lbPaasword);

        tfPassword = new JPasswordField();
        tfPassword.setBounds(110, 100, 200, 40);
        add(tfPassword);

        addRegisterButton();

        this.loginFrame = loginFrame;

        setVisible(true);
    }

    private void addRegisterButton(){
        JButton button = new JButton("Register");
        button.addActionListener((e) -> {
            String id = tfAccount.getText();
            char[] pwChar = tfPassword.getPassword();
            String pw = new String(pwChar);
            if (id.equals("")){
                JOptionPane.showMessageDialog(null, "Please input ID", "", JOptionPane.ERROR_MESSAGE);
            } else if (pwChar.length == 0){
                JOptionPane.showMessageDialog(null, "Please input password", "", JOptionPane.ERROR_MESSAGE);
            } else {
                boolean b = true;
                for (int i = 0; i < loginFrame.aiFrame.users.size(); i++) {
                    if (id.equals(loginFrame.aiFrame.users.get(i).name)){
                        JOptionPane.showMessageDialog(null, "ID has already exist.", "", JOptionPane.ERROR_MESSAGE);
                        b = false;
                        break;
                    }
                }

                if (b){
                    try {
                        FileWriter fileWriter = new FileWriter("user\\users",true);
                        fileWriter.write(id);
                        fileWriter.write(" ");
                        fileWriter.write(pw);
                        fileWriter.write(" ");
                        fileWriter.write("0");
                        fileWriter.write("\n");

                        fileWriter.close();
                    } catch (Exception e1){}

                    JOptionPane.showMessageDialog(null, "Register succeed.", "", JOptionPane.INFORMATION_MESSAGE);
                    loginFrame.aiFrame.users.add(new User(id, pw, 0));
                    setVisible(false);
                }
            }
        });
        button.setBounds(50, 150, 120, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 16));
        add(button);
    }

}
