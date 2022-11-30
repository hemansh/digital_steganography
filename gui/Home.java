package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home implements ActionListener {
    JFrame home_frame ;
    JButton home_encode_btn , home_decode_btn ;
    JLabel AIO_label , icon_label;

    Container contain_logo ;

    Home(){
        home_frame = new JFrame("AIO Steganography");

        home_frame.getContentPane().setBackground(new java.awt.Color(239, 222, 205));

        AIO_label = new JLabel("AIO Steganography");
        AIO_label.setFont(new Font("Roboto",Font.BOLD, 30));
        AIO_label.setForeground(Color.BLACK);
        AIO_label.setFocusable(false);

        home_encode_btn  = new JButton("ENCODE");
        home_decode_btn = new JButton("DECODE");

        home_encode_btn.setForeground(Color.BLACK);
        home_decode_btn.setForeground(Color.BLACK);
        home_encode_btn.setFont(new Font("Roboto",Font.BOLD,15));
        home_decode_btn.setFont(new Font("Roboto",Font.BOLD,15));

        home_encode_btn.setFocusable(false);
        home_decode_btn.setFocusable(false);




        contain_logo = home_frame.getContentPane();
        icon_label = new JLabel();
        icon_label.setIcon(new ImageIcon("D:\\projects\\AIO-Steganography\\JAVA\\gui\\logo.png"));


        Image icon = Toolkit.getDefaultToolkit().getImage("D:\\projects\\AIO-Steganography\\JAVA\\gui\\logo.png");
        home_frame.setIconImage(icon);


        home_frame.setLayout(null);
        home_frame.setSize(500,400);
        home_frame.setResizable(false);
        home_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        home_frame.setLocationRelativeTo(null);
        home_frame.setVisible(true);
    }

    private void setHomeElements(){
            AIO_label.setBounds(125,20,300,35);

            home_encode_btn.setBounds(125,250,100,50);
            home_decode_btn.setBounds(275,250,100,50);

            icon_label.setBounds(170, 75, 150, 150);

            home_encode_btn.addActionListener(this);
            home_decode_btn.addActionListener(this);

    }

    private void addHomeElements(){
        home_frame.add(AIO_label);
        home_frame.add(home_encode_btn);
        home_frame.add(home_decode_btn);
        contain_logo.add(icon_label);

    }

    void initialiseHome(){
        setHomeElements();
        addHomeElements();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
            if(e.getSource() == home_encode_btn){
                home_frame.dispose();
                Encode encode = new Encode();
                encode.initialiseEncode();
            }
            else{
                home_frame.dispose();
                Decode decode = new Decode();
            }
    }
}
