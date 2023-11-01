import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class Game {
    private class Card {
        String ranks;
        String suits;

        Card(String ranks, String suits) {
            this.ranks = ranks;
            this.suits = suits;
        }

        public String toString() {
            return ranks + "-" + suits;
        }

        public int getValue() {
            if (ranks == "A") {
                return 1;
            }
            if (ranks == "J") {
                return 0;
            }
            if (ranks == "Q") {
                return 0;
            }
            if (ranks == "K") {
                return 0;
            }
            return Integer.parseInt(ranks);
        }

        public String getImgPath() {
            return "./PNG-cards-1.3/" + toString() + ".png";
        }
    }

    ArrayList<Card> deck;

    ArrayList<Card> dealerHand;
    int dealerSum;
    ArrayList<Card> playerHand;
    int playerSum;

    JFrame window = new JFrame();
    JPanel gamePanel = new JPanel() {
        int cardWidth = 110;
        int cardHeight = 154;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            try {
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./PNG-cards-1.3/back_card.png")).getImage();
                g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null);
                g.drawImage(hiddenCardImg, 150, 20, cardWidth, cardHeight, null);

                int x = 20;
                int y = 20;
                for (int i = 0; i < dealerHand.size(); i++) {
                    // Card card = dealerHand.get(i);
                    // Image cardImg = new ImageIcon(getClass().getResource(card.getImgPath())).getImage();
                    // g.drawImage(cardImg, x, y, cardWidth, cardHeight, null);

                    Image backcardImg = new ImageIcon(
                            getClass().getResource("./PNG-cards-1.3/back_card.png")).getImage();
                    g.drawImage(backcardImg, x, y, cardWidth, cardHeight, null);
                    x += 130;
                }

                x = 20;
                y = 330;
                // draw player card
                for (int i = 0; i < playerHand.size(); i++) {
                    Card card = playerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImgPath())).getImage();
                    g.drawImage(cardImg, x, y, cardWidth, cardHeight, null);
                    x += 130;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    JPanel buttonPanel = new JPanel();
    JButton drawButton = new JButton("DRAW");
    JButton stayButton = new JButton("STAY");
    JLabel scoreLabel = new JLabel();
    JPanel scorePanel = new JPanel();

    Game() {
        startGame();

        window.setVisible(true);
        window.setTitle("Pok Deng Card Game.");
        window.setSize(800, 600);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));

        window.add(gamePanel);

        buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 255));

        drawButton = new JButton("DRAW");
        drawButton.setFocusable(false);
        stayButton = new JButton("STAY");
        stayButton.setFocusable(false);

        buttonPanel.add(drawButton);
        buttonPanel.add(stayButton);

        scorePanel = new JPanel();
        scorePanel.setBackground(new Color(255, 255, 255));
        scorePanel.setSize(800, 50);
        scoreLabel.setText("Your value is " + playerSum);
        scoreLabel.setFont(new Font("Sans-serif", Font.BOLD, 18));
        scorePanel.add(scoreLabel);
        buttonPanel.add(scorePanel);

        window.add(buttonPanel, BorderLayout.AFTER_LAST_LINE);

        drawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Draw card");
                Card card = deck.remove(deck.size() - 1);
                playerSum += card.getValue();
                playerHand.add(card);
                if (playerSum >= 10)
                    playerSum -= 10;
                System.out.println("PLAYER : ");
                System.out.println(playerHand);
                System.out.println(playerSum);
                if (playerHand.size() == 3)
                    drawButton.setEnabled(false);

                scoreLabel.setText("Your value is " + playerSum);

                gamePanel.repaint();
            }
        });

        stayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawButton.setEnabled(false);
                stayButton.setEnabled(false);

                Object[] options = { "Play again" };

                if (playerSum > dealerSum) {
                    String text = "You win. \n";
                    text += "You have " + playerHand + "\n";
                    text += "value is " + playerSum + "\n";
                    text += "Dealer have " + dealerHand + "\n";
                    text += "value is " + dealerSum + "\n";
                    int a = JOptionPane.showOptionDialog(null,
                            text, null,
                            JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);

                    if (a == JOptionPane.OK_OPTION) {
                        System.out.println("ok");
                        restart();
                    }
                } else {
                    String text = "Too bad, You've lost. \n";
                    text += "You have " + playerHand + "\n";
                    text += "value is " + playerSum + "\n";
                    text += "Dealer have " + dealerHand + "\n";
                    text += "value is " + dealerSum + "\n";
                    int a = JOptionPane.showOptionDialog(null,
                            text, null,
                            JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);

                    if (a == JOptionPane.OK_OPTION) {
                        System.out.println("ok");
                        restart();
                    }
                }
            }
        });

        gamePanel.repaint();

    }

    public void startGame() {
        buildDeck();
        shuffleDeck();

        Card card;
        // dealer
        dealerSum = 0;
        dealerHand = new ArrayList<Card>();
        for (int i = 0; i < 2; i++) {
            card = deck.remove(deck.size() - 1);
            dealerSum += card.getValue();
            dealerHand.add(card);
            if (dealerSum >= 10)
                dealerSum -= 10;
        }
        System.out.println("DEALER : ");
        System.out.println(dealerHand);
        System.out.println(dealerSum);

        // player
        playerSum = 0;
        playerHand = new ArrayList<Card>();
        for (int i = 0; i < 2; i++) {
            card = deck.remove(deck.size() - 1);
            playerSum += card.getValue();
            playerHand.add(card);
            if (playerSum >= 10)
                playerSum -= 10;
        }
        System.out.println("PLAYER : ");
        System.out.println(playerHand);
        System.out.println(playerSum);

    }

    public void buildDeck() {
        deck = new ArrayList<Card>();
        String[] ranks = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
        String[] suits = { "Spade", "Heart", "Diamond", "Club" };

        for (int i = 0; i < suits.length; i++) {
            for (int j = 0; j < ranks.length; j++) {
                Card card = new Card(ranks[j], suits[i]);
                deck.add(card);

            }
        }

        System.out.println("BUILD DECK:");
        System.out.println(deck);

    }

    public void shuffleDeck() {
        Collections.shuffle(deck);
        System.out.println("AFTER SHUFFLE:");
        System.out.println(deck);
    }

    public void restart() {
        drawButton.setEnabled(true);
        stayButton.setEnabled(true);
        gamePanel.repaint();
        buttonPanel.revalidate();
        startGame();    
        scoreLabel.setText("Your value is " + playerSum);    
    }
}
