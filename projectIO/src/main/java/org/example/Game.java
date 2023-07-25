package org.example;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game {
    JPanel panel;
    JFrame frame;
    JButton[][] buttons;
    Position currentPosition;

    public Game(JFrame frame) {
        this.frame = frame;
        this.buttons = new JButton[25][25];
        this.createLabel();
        this.init();
        frame.add(this.panel);
        frame.setVisible(true);
    }

    public void init() {
        for(int i = 0; i < 25; ++i) {
            for(int j = 0; j < 25; ++j) {
                this.buttons[i][j] = new JButton();
                this.buttons[i][j].setBackground(Color.white);
                this.panel.add(this.buttons[i][j]);
            }
        }

        this.currentPosition = new Position(0, 0);
        this.buttons[0][0].setBackground(Color.BLACK);
        this.buttons[this.currentPosition.x][this.currentPosition.y].addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 38) {
                    Game.this.move(Game.this.currentPosition.x - 1, Game.this.currentPosition.y);
                } else if (e.getKeyCode() == 40) {
                    Game.this.move(Game.this.currentPosition.x + 1, Game.this.currentPosition.y);
                } else if (e.getKeyCode() == 37) {
                    Game.this.move(Game.this.currentPosition.x, Game.this.currentPosition.y - 1);
                } else if (e.getKeyCode() == 39) {
                    Game.this.move(Game.this.currentPosition.x, Game.this.currentPosition.y + 1);
                }

            }

            public void keyReleased(KeyEvent e) {
            }
        });
        this.panel.setFocusable(true);
        this.panel.requestFocusInWindow();
    }

    public void move(int x, int y) {
        if (x < 25 && x >= 0 && y < 25 && y >= 0) {
            this.buttons[this.currentPosition.x][this.currentPosition.y].setBackground(Color.GREEN);
            this.currentPosition.x = x;
            this.currentPosition.y = y;
            this.buttons[this.currentPosition.x][this.currentPosition.y].setBackground(Color.BLACK);
            System.out.println(this.isClosed(x, y));
        }
    }

    public void createLabel() {
        this.panel = new JPanel();
        this.panel.setSize(700, 700);
        this.panel.setBackground(Color.white);
        this.panel.setLayout(new GridLayout(25, 25));
    }

    public boolean isClosed(int x, int y) {
        boolean[][] visited = new boolean[25][25];
        return this.isClosedHelper(x, y, visited);
    }

    private boolean isClosedHelper(int x, int y, boolean[][] visited) {
        if (x >= 0 && x < 25 && y >= 0 && y < 25 && !visited[x][y]) {
            visited[x][y] = true;
            boolean left = this.isClosedHelper(x - 1, y, visited);
            boolean right = this.isClosedHelper(x + 1, y, visited);
            boolean up = this.isClosedHelper(x, y - 1, visited);
            boolean down = this.isClosedHelper(x, y + 1, visited);
            return left && right && up && down;
        } else {
            return true;
        }
    }
}
