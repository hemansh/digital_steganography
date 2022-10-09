package JAVA;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class gui {
    JFrame frame;
    JPanel panel_home, panel_encode, panel_decode;
    JButton button_encode, button_decode, open, btn_encode, btn_decode;
    JLabel headding, file_loc;
    Font font;
    public static String file_dir;
    JTextArea msg_input;
    private int WIDTH = 600, HEIGHT = 500;

    gui() {
        font = new Font("Serif", Font.BOLD, 20);
        frame = new JFrame();
        panel_home = new JPanel();
        panel_home.setLayout(null);
        panel_encode = new JPanel();
        panel_encode.setLayout(null);
        panel_decode = new JPanel();
        panel_decode.setLayout(null);
        headding = new JLabel("Digital Steganography");
        headding.setBounds(WIDTH/2 - 100, 5, 300, 100);
        headding.setFont(font);
        msg_input = new JTextArea();
        btn_encode = new JButton("Encode");
        btn_encode.setBounds(250, 375, 100, 25);
        btn_encode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Encrypt obj = new Encrypt();
                if (obj.encode(file_dir, msg_input.getText())) {
                    JOptionPane.showMessageDialog(frame, "Encoding Done");
                }
            }
        });
        btn_decode = new JButton("Decode");
        btn_decode.setBounds(250, 375, 100, 25);
        btn_decode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Decrypt obj = new Decrypt();
                String sec_mesg = obj.getText(file_dir);
                if (sec_mesg.length() > 0) {
                    JOptionPane.showMessageDialog(frame, sec_mesg);
                }
            }
        });
        file_loc = new JLabel();
        file_loc.setFont(new Font("Serif", Font.PLAIN, 15));
        open = new JButton("Open");
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                int r = jfc.showOpenDialog(null);
                if (r == JFileChooser.APPROVE_OPTION) {
                    file_dir = jfc.getSelectedFile().getAbsolutePath();
                } else {
                    file_dir = "please select file";
                }
                file_loc.setText(file_dir);
            }
        });
        button_encode = new JButton("Encode");
        button_encode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(panel_home);
                headding.setText("encode");
                headding.setBounds(175, 5, 300, 100);
                open.setBounds(20, 100, 100, 50);
                file_loc.setBounds(150, 100, 550, 50);
                msg_input.setBounds(20, 200, 550, 150);
                panel_encode.add(headding);
                panel_encode.add(open);
                panel_encode.add(file_loc);
                panel_encode.add(msg_input);
                panel_encode.add(btn_encode);
                frame.add(panel_encode);
                frame.validate();
            }
        });
        button_decode = new JButton("Decode");
        button_encode.setBounds(WIDTH/2 - 150, 200, 100, 50);
        button_decode.setBounds(WIDTH/2 +50, 200, 100, 50);
        button_decode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(panel_home);
                headding.setText("Decode");
                panel_decode.add(headding);
                panel_decode.add(open);
                open.setBounds(20, 100, 100, 50);
                file_loc.setBounds(150, 100, 550, 50);
                panel_decode.add(file_loc);
                panel_decode.add(btn_decode);
                frame.add(panel_decode);
                frame.validate();
            }
        });
        panel_home.add(headding);
        panel_home.add(button_encode);
        panel_home.add(button_decode);
        frame.add(panel_home);
        frame.setSize(WIDTH,HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        new gui();
    }
}
