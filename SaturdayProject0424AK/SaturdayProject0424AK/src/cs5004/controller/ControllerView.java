package cs5004.controller;
import javax.swing.*;

public class ControlerPt2 {

    public static void main(String[] args){
        JFrame frame = new JFrame("Test");
        frame.setVisible(true);
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("testing");
        JPanel panel = new JPanel();
        panel.add(label);

        JButton playButton = new JButton("Playeasdasdasdasd");
        panel.add(playButton);

        frame.add(panel);

    }
}
