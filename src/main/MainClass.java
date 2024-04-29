package main;

import utilz.Constants;

import javax.swing.*;

public class MainClass {
    private static String selectedDifficulty;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game Difficulty");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Choose game difficulty:");
        label.setBounds(50, 20, 200, 30);

        String[] options = {"Easy", "Medium", "Hard"};
        JComboBox<String> difficultyComboBox = new JComboBox<>(options);
        difficultyComboBox.setBounds(50, 60, 200, 30);

        JButton okButton = new JButton("OK");
        okButton.setBounds(100, 100, 100, 30);
        okButton.addActionListener(e -> {
            Constants.setDIFFICULTY((String) difficultyComboBox.getSelectedItem());
            frame.dispose();
            new Game();
        });

        frame.add(label);
        frame.add(difficultyComboBox);
        frame.add(okButton);

        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
