package com.ez_mode.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PickPipeFrame extends JFrame implements ActionListener {
    JButton up = new JButton("Up");
    JButton down = new JButton("Down");
    JButton right = new JButton("Right");
    JButton left = new JButton("Left");
    JLabel info = new JLabel("Which direction do you want to pick up a pipe? Choose: ");

    public PickPipeFrame(){
        this.setResizable(false);
        this.setSize(350,100);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        up.addActionListener(this);
        down.addActionListener(this);
        right.addActionListener(this);
        left.addActionListener(this);
        JPanel panel = new JPanel();
        panel.add(info);
        panel.add(up);
        panel.add(down);
        panel.add(right);
        panel.add(left);
        this.add(panel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == up) Controller.setChoice = 0;
        else if(e.getSource() == down) Controller.setChoice = 1;
        else if(e.getSource() == right) Controller.setChoice = 2;
        else if(e.getSource() == left) Controller.setChoice = 3;
        this.dispose();
    }
}
