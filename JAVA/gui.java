package JAVA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import JAVA.imagestego.ImageHider;
import JAVA.imagestego.Messages;

public class gui {
    JFrame frame;
    JPanel panel_home, panel_encode, panel_decode;
    JButton button_encode, button_decode, open, open2, btn_encode, btn_decode;
    JLabel headding, file_loc, file_loc2;
    Font font;
    public static String file_dir, file_dir2;
    JTextArea msg_input;
    JRadioButton encode_radio_image, encode_radio_text,decode_radio_image,decode_radio_text;
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
        headding.setBounds(WIDTH / 2 - 100, 5, 300, 100);
        headding.setFont(font);
        msg_input = new JTextArea();
        btn_encode = new JButton("Encode");
        btn_encode.setBounds(250, 385, 100, 25);
        btn_encode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(encode_radio_image.isSelected())
                {
                    ImageHider obj = new ImageHider();
                    obj.hide(file_dir, file_dir2, "output.png");
                    JOptionPane.showMessageDialog(frame,Messages.IMAGE_HIDDEN_SUCCESSFULLY.getmessage());
                }
                else if(encode_radio_text.isSelected())
                {
                    Encrypt obj = new Encrypt();
                    if (obj.encode(file_dir, msg_input.getText())) {
                        JOptionPane.showMessageDialog(frame, "Encoding Done");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(frame, "Invalid input");
                }
            }
        });
        btn_decode = new JButton("Decode");
        btn_decode.setBounds(250, 375, 100, 25);
        btn_decode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(decode_radio_image.isSelected())
                {
                    ImageHider objimg = new ImageHider();
                    objimg.reveal(file_dir, "Testoutput.png");
                    JOptionPane.showMessageDialog(frame, Messages.IMAGE_EXTRACTED_SUCCESSFULLY.getmessage());
                }
                else if(decode_radio_text.isSelected())
                {
                    Decrypt obj = new Decrypt();
                    String sec_mesg = obj.getText(file_dir);
                    if (sec_mesg.length() > 0) {
                        JOptionPane.showMessageDialog(frame, sec_mesg);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(frame, "Invalid input!!");
                }
            }
        });
        file_loc = new JLabel();
        file_loc.setFont(new Font("Serif", Font.PLAIN, 15));
        file_loc2 = new JLabel();
        file_loc2.setFont(new Font("Serif", Font.PLAIN, 15));
        open = new JButton("Open");
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File workingDirectory = new File(System.getProperty("user.dir"));
                JFileChooser jfc = new JFileChooser();
                jfc.setCurrentDirectory(workingDirectory);
                int r = jfc.showOpenDialog(null);
                if (r == JFileChooser.APPROVE_OPTION) {
                    file_dir = jfc.getSelectedFile().getAbsolutePath();
                } else {
                    file_dir = "please select a file";
                }
                file_loc.setText(file_dir);
            }
        });
        open2 = new JButton("open2");
        open2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                File workingDirectory = new File(System.getProperty("user.dir"));
                jfc.setCurrentDirectory(workingDirectory);
                int r = jfc.showOpenDialog(null);
                if (r == JFileChooser.APPROVE_OPTION) {
                    file_dir2 = jfc.getSelectedFile().getAbsolutePath();
                } else {
                    file_dir2 = "please select a file";
                }
                file_loc2.setText(file_dir2);
            }
        });
        button_encode = new JButton("Encode");
        button_encode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                encode_radio_image = new JRadioButton("Image");
                encode_radio_text = new JRadioButton("Text");
                frame.remove(panel_home);
                headding.setText("encode");
                headding.setBounds((WIDTH/2)-25, 5, 300, 100);
                open.setBounds(20, 100, 100, 50);
                file_loc.setBounds(150, 100, 550, 50);
                encode_radio_image.setBounds(30, 150, 100, 30);
                encode_radio_text.setBounds(30, 180, 100, 30);
                ButtonGroup bg = new ButtonGroup();
                bg.add(encode_radio_image);
                bg.add(encode_radio_text);
                msg_input.setBounds(20, 220, 550, 150);
                panel_encode.add(headding);
                panel_encode.add(open);
                panel_encode.add(file_loc);
                panel_encode.add(encode_radio_image);
                panel_encode.add(encode_radio_text);
                encode_radio_image.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (encode_radio_image.isSelected()) {
                            panel_encode.remove(msg_input);
                            open2.setBounds(20, 220, 100, 50);
                            file_loc2.setBounds(150, 220, 550, 50);
                            panel_encode.add(open2);
                            panel_encode.add(file_loc2);
                            panel_encode.add(btn_encode);
                            panel_encode.repaint();
                        }
                    }
                });

                encode_radio_text.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (encode_radio_text.isSelected()) {
                            panel_encode.remove(open2);
                            panel_encode.remove(file_loc2);
                            panel_encode.add(msg_input);
                            panel_encode.add(btn_encode);
                            panel_encode.repaint();
                        }
                    }
                });
                frame.add(panel_encode);
                frame.validate();
            }
        });
        button_decode = new JButton("Decode");
        button_encode.setBounds(WIDTH / 2 - 150, 200, 100, 50);
        button_decode.setBounds(WIDTH / 2 + 50, 200, 100, 50);
        button_decode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                decode_radio_image = new JRadioButton("Image from image");
                decode_radio_text = new JRadioButton("Text from image");
                decode_radio_image.setBounds(30, 100, 150, 30);
                decode_radio_text.setBounds(30, 130, 150, 30);
                ButtonGroup g = new ButtonGroup();
                g.add(decode_radio_image); g.add(decode_radio_text);
                frame.remove(panel_home);
                headding.setText("Decode");
                headding.setBounds((WIDTH/2)-35, 5, 300, 100);
                panel_decode.add(headding);
                panel_decode.add(open);
                open.setBounds(20, 200, 100, 50);
                file_loc.setBounds(150, 200, 550, 50);
                panel_decode.add(file_loc);
                panel_decode.add(btn_decode);
                panel_decode.add(decode_radio_image);
                panel_decode.add(decode_radio_text);
                frame.add(panel_decode);
                frame.validate();
            }
        });
        panel_home.add(headding);
        panel_home.add(button_encode);
        panel_home.add(button_decode);
        frame.add(panel_home);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        new gui();
    }
}
