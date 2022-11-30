package gui;

import javax.swing.*;

import imagestego.ImageHider;
import videoStego.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.*;

public class Decode {

    JFrame decode_frame;
    JRadioButton Radio_Text_image, Radio_image_image, Radio_text_audio, Radio_text_video;
    JLabel decodeLabel;
    ButtonGroup decode_button_group;
    JButton button_decode, button_select_file,button_home;
    public String file_dir;

    Decode() {
        decode_frame = new JFrame("Decode Frame");
        decode_frame.setLayout(null);
        decode_frame.setSize(500, 400);
        decode_frame.setResizable(false);
        decode_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        decode_frame.setLocationRelativeTo(null);
        decode_frame.setVisible(true);

        decode_frame.getContentPane().setBackground(new java.awt.Color(239, 222, 205));

        Image icon = Toolkit.getDefaultToolkit().getImage("D:\\projects\\AIO-Steganography\\JAVA\\gui\\logo.png");
        decode_frame.setIconImage(icon);


        Radio_Text_image = new JRadioButton("Text from Image");
        Radio_image_image = new JRadioButton("Image from Image");
        Radio_text_audio = new JRadioButton("Text from Audio");
        Radio_text_video = new JRadioButton("Text from Video");

        Radio_image_image.setFocusable(false);
        Radio_Text_image.setFocusable(false);
        Radio_text_video.setFocusable(false);
        Radio_text_audio.setFocusable(false);



        decode_button_group = new ButtonGroup();

        button_decode = new JButton("DECODE");
        button_decode.setFocusable(false);
        button_decode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleDecodeButton();
            }
        });

        button_select_file = new JButton("Select file");
        button_select_file.setFocusable(false);
        button_select_file.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleFileChooser();
            }
        });

        button_home = new JButton("HOME");
        button_home.setFocusable(false);
        button_home.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleHome();
            }
        });

        decodeLabel = new JLabel("Decode");
        decodeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        decodeLabel.setForeground(Color.BLACK);

        setDecodeElements();
        addDecodeElements();
    }

    void setDecodeElements() {
        decodeLabel.setBounds(175, 5, 300, 35);

        Radio_Text_image.setBounds(100, 70, 150, 50);
        Radio_image_image.setBounds(250, 70, 150, 50);
        Radio_text_audio.setBounds(100, 120, 150, 50);
        Radio_text_video.setBounds(250, 120, 150, 50);

        button_decode.setBounds(190, 275, 100, 40);
        button_select_file.setBounds(190, 200, 100 , 30);
        button_home.setBounds(20,275,100,40);
    }

    void addDecodeElements() {
        decode_frame.add(decodeLabel);

        decode_button_group.add(Radio_Text_image);
        decode_button_group.add(Radio_image_image);
        decode_button_group.add(Radio_text_audio);
        decode_button_group.add(Radio_text_video);

        decode_frame.add(Radio_Text_image);
        decode_frame.add(Radio_image_image);
        decode_frame.add(Radio_text_audio);
        decode_frame.add(Radio_text_video);

        decode_frame.add(button_decode);
        decode_frame.add(button_select_file);
        decode_frame.add(button_home);
    }

    void handleFileChooser() {
        File workingDirectory = new File(System.getProperty("user.dir"));
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(workingDirectory);
        int r = jfc.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            file_dir = jfc.getSelectedFile().getAbsolutePath();
        } else {
            file_dir = "please select a file";
        }
    }

    void handleDecodeButton() {
        if (Radio_Text_image.isSelected()) {
            String sec_mesg = textImageStego.Steganographer.decode(file_dir, getSecretKey());
            if (sec_mesg.length() > 0) {
                JOptionPane.showMessageDialog(decode_frame, sec_mesg);
            }
        } else if (Radio_image_image.isSelected()) {
            ImageHider objimg = new ImageHider();
            objimg.reveal(file_dir, "Imageoutput.png");
            JOptionPane.showMessageDialog(decode_frame, imagestego.Messages.IMAGE_EXTRACTED_SUCCESSFULLY.getmessage());
        } else if (Radio_text_audio.isSelected()) {
            File audioFile = new File(file_dir);
            try {
                JOptionPane.showMessageDialog(decode_frame, audioStego.AudioSteganography.decodeMessage(audioFile));
            } catch (Exception e) {
                System.out.println("Exception in audioStegnography");
            }
        } else if (Radio_text_video.isSelected()) {
            try {
                Path inp = Paths.get(file_dir);
                RC4 rc4 = new RC4(getSecretKey().getBytes());
                FLVCrypto flv = new FLVCrypto();
                byte[] inpufileBytes = IO.readFileBytes(inp);
                byte[] encryptedByte = flv.extract(inpufileBytes);
                String msg = new String(rc4.decrypt(encryptedByte));
                JOptionPane.showMessageDialog(decode_frame, msg);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    String getSecretKey() {
        return JOptionPane.showInputDialog(decode_frame, "Enter your Secret Key");
    }

    void handleHome(){
        decode_frame.dispose();
        Home obj = new Home();
        obj.initialiseHome();
       
    }

}
