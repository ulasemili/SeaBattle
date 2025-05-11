import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        int boardSize = 8;
        int buttonSize = 50;
        int[] player1hp = {17};
        int[] player2hp = {17};
        boolean[][] board1placed = new boolean[boardSize][boardSize];
        boolean[][] board2placed = new boolean[boardSize][boardSize];
        boolean[][] board1attacked = new boolean[boardSize][boardSize];
        boolean[][] board2attacked = new boolean[boardSize][boardSize];
        boolean[] attackPhase = {false};
        int[] turn = {1};

        JFrame frame = new JFrame("Amiral Battı");
        frame.setSize(1100, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JPanel panel1 = new JPanel(null);
        panel1.setBounds(70, 50, 410, 410);
        panel1.setBackground(Color.LIGHT_GRAY);
        frame.add(panel1);

        JPanel panel2 = new JPanel(null);
        panel2.setBounds(620, 50, 410, 410);
        panel2.setBackground(Color.LIGHT_GRAY);
        frame.add(panel2);

        JButton[][] buttons1 = new JButton[boardSize][boardSize];
        JButton[][] buttons2 = new JButton[boardSize][boardSize];

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                JButton b1 = new JButton();
                b1.setBounds(col * (buttonSize + 1), row * (buttonSize + 1), buttonSize, buttonSize);
                b1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                b1.setOpaque(true);
                b1.setBackground(Color.WHITE);
                buttons1[row][col] = b1;
                panel1.add(b1);

                JButton b2 = new JButton();
                b2.setBounds(col * (buttonSize + 1), row * (buttonSize + 1), buttonSize, buttonSize);
                b2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                b2.setOpaque(true);
                b2.setBackground(Color.WHITE);
                buttons2[row][col] = b2;
                panel2.add(b2);
            }
        }

        JLabel labelPlayer1 = new JLabel("1. Oyuncu");
        labelPlayer1.setBounds(240,10,100,30);
        frame.add(labelPlayer1);

        JLabel labelPlayer2 = new JLabel("2. Oyuncu");
        labelPlayer2.setBounds(790,10,100,30);
        frame.add(labelPlayer2);

        JLabel labelPlayer = new JLabel("Sıra: 1. Oyuncu yerleştiriyor");
        labelPlayer.setBounds(450, 500, 200, 30);
        frame.add(labelPlayer);

        JLabel labelShipsInfo = new JLabel("Gemi boyutu: 5x1");
        labelShipsInfo.setBounds(450, 530, 120, 30);
        frame.add(labelShipsInfo);

        JButton yerleştirButton = new JButton("Yerleştir");
        yerleştirButton.setBounds(570, 530, 120, 30);
        frame.add(yerleştirButton);


        int[] shipSizes = {5, 4, 3, 3, 2};
        int[] currentShipIndex = {0};
        int[] currentPlayer = {1};
        ArrayList<Point> currentSelection = new ArrayList<>();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                int finalRow = row;
                int finalCol = col;

                buttons1[row][col].addActionListener(e -> {
                    if (!attackPhase[0]) {
                        if (currentPlayer[0] != 1) return;
                        if (board1placed[finalRow][finalCol]) return;
                        Point p = new Point(finalRow, finalCol);
                        if (currentSelection.contains(p)) {
                            currentSelection.remove(p);
                            buttons1[finalRow][finalCol].setBackground(Color.WHITE);
                        } else if (currentSelection.size() < shipSizes[currentShipIndex[0]]) {
                            currentSelection.add(p);
                            buttons1[finalRow][finalCol].setBackground(Color.CYAN);
                        }
                    }
                });

                buttons2[row][col].addActionListener(e -> {
                    if (!attackPhase[0]) {
                        if (currentPlayer[0] != 2) return;
                        if (board2placed[finalRow][finalCol]) return;
                        Point p = new Point(finalRow, finalCol);
                        if (currentSelection.contains(p)) {
                            currentSelection.remove(p);
                            buttons2[finalRow][finalCol].setBackground(Color.WHITE);
                        } else if (currentSelection.size() < shipSizes[currentShipIndex[0]]) {
                            currentSelection.add(p);
                            buttons2[finalRow][finalCol].setBackground(Color.CYAN);
                        }
                    } else {
                        if (turn[0] != 1) return;
                        if (board2attacked[finalRow][finalCol]) return;
                        board2attacked[finalRow][finalCol] = true;

                        if (board2placed[finalRow][finalCol]) {
                            buttons2[finalRow][finalCol].setBackground(Color.DARK_GRAY);
                            player2hp[0]--;
                            if (player2hp[0] == 0) {
                                JOptionPane.showMessageDialog(frame, "1. Oyuncu kazandı!");
                                frame.dispose();
                            }
                        } else {
                            buttons2[finalRow][finalCol].setBackground(Color.BLUE);
                            turn[0] = 2;
                            labelPlayer.setText("Sıra: 2. Oyuncu atış yapıyor");
                        }
                    }
                });

                buttons1[row][col].addActionListener(e -> {
                    if (!attackPhase[0]) return;
                    if (turn[0] != 2) return;
                    if (board1attacked[finalRow][finalCol]) return;
                    board1attacked[finalRow][finalCol] = true;

                    if (board1placed[finalRow][finalCol]) {
                        buttons1[finalRow][finalCol].setBackground(Color.DARK_GRAY);
                        player1hp[0]--;
                        if (player1hp[0] == 0) {
                            JOptionPane.showMessageDialog(frame, "2. Oyuncu kazandı!");
                            frame.dispose();
                        }
                    } else {
                        buttons1[finalRow][finalCol].setBackground(Color.BLUE);
                        turn[0] = 1;
                        labelPlayer.setText("Sıra: 1. Oyuncu atış yapıyor");
                    }
                });
            }
        }

        yerleştirButton.addActionListener(e -> {
            int size = shipSizes[currentShipIndex[0]];
            if (currentSelection.size() != size) {
                JOptionPane.showMessageDialog(frame, "Gemi boyutu kadar hücre seçmelisiniz!");
                return;
            }

            if (!isValidSelection(new ArrayList<>(currentSelection))) {
                JOptionPane.showMessageDialog(frame, "Seçim yatay veya dikey ve bitişik olmalı!");
                return;
            }

            if (currentPlayer[0] == 1) {
                for (Point p : currentSelection) {
                    board1placed[p.x][p.y] = true;
                    buttons1[p.x][p.y].setBackground(Color.DARK_GRAY);
                }
            } else {
                for (Point p : currentSelection) {
                    board2placed[p.x][p.y] = true;
                    buttons2[p.x][p.y].setBackground(Color.DARK_GRAY);
                }
            }

            currentSelection.clear();
            currentShipIndex[0]++;

            if (currentShipIndex[0] >= shipSizes.length) {
                if (currentPlayer[0] == 1) {
                    for (int i = 0; i < boardSize; i++) {
                        for (int j = 0; j < boardSize; j++) {
                            if (board1placed[i][j]) {
                                buttons1[i][j].setBackground(Color.WHITE);
                            }
                        }
                    }
                    currentPlayer[0] = 2;
                    currentShipIndex[0] = 0;
                    labelPlayer.setText("Sıra: 2. Oyuncu yerleştiriyor");
                    labelShipsInfo.setText("Gemi boyutu: " + shipSizes[0] + "x1");
                } else {
                    for (int i = 0; i < boardSize; i++) {
                        for (int j = 0; j < boardSize; j++) {
                            if (board2placed[i][j]) {
                                buttons2[i][j].setBackground(Color.WHITE);
                            }
                        }
                    }
                    yerleştirButton.setEnabled(false);
                    labelShipsInfo.setText("Atış aşaması başladı!");
                    attackPhase[0] = true;
                    labelPlayer.setText("Sıra: 1. Oyuncu atış yapıyor");
                }
            } else {
                labelShipsInfo.setText("Gemi boyutu: " + shipSizes[currentShipIndex[0]] + "x1");
            }
        });

        frame.setVisible(true);
    }

    private static boolean isValidSelection(ArrayList<Point> selection) {
        if (selection.size() < 2) return true;

        boolean sameRow = true;
        boolean sameCol = true;
        int baseRow = selection.get(0).x;
        int baseCol = selection.get(0).y;

        for (Point p : selection) {
            if (p.x != baseRow) sameRow = false;
            if (p.y != baseCol) sameCol = false;
        }

        if (!sameRow && !sameCol) return false;

        boolean finalSameRow = sameRow;
        selection.sort((a, b) -> finalSameRow ? Integer.compare(a.y, b.y) : Integer.compare(a.x, b.x));

        for (int i = 0; i < selection.size() - 1; i++) {
            Point p1 = selection.get(i);
            Point p2 = selection.get(i + 1);
            int diff = finalSameRow ? (p2.y - p1.y) : (p2.x - p1.x);
            if (diff != 1) return false;
        }

        return true;
    }
}
