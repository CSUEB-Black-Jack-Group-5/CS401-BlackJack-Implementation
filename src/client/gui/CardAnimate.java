package client.gui;
///  Animated game.Card

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class CardAnimate {
    private double x, y;
    private int speed;
    private double rotation;
    private double scale;
    ///  Decoration sign
    private static final String[] suits = {"♠", "♥", "♦", "♣"};

    ///  Number of card
    private static final String[] values = {"A", "K", "Q", "J", "10", "9"};
    private String suit;
    private String value;
    private static final Random random = new Random();

    public CardAnimate(int x, int y, int speed, int rotation) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.rotation = Math.toRadians(rotation);
        this.scale = random.nextDouble() * 0.5 + 0.7; // Random size
        this.suit = suits[random.nextInt(suits.length)];
        this.value = values[random.nextInt(values.length)];
    }

    public void update() {
        x += speed * Math.cos(rotation);
        y += speed * Math.sin(rotation);

        ///  Bounce
        if (x < 0 || x > 800) {
            rotation = Math.PI - rotation;
        }
        if (y < 0 || y > 600) {
            rotation = -rotation;
        }

        ///  game.Card don't get over screen
        x = Math.max(0, Math.min(800, x));
        y = Math.max(0, Math.min(600, y));
    }

    public void draw(Graphics2D g2) {
        AffineTransform oldTransform = g2.getTransform();

        /// Scaling
        g2.translate(x, y);
        g2.rotate(rotation);
        g2.scale(scale, scale);

        /// game.Card Background
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(-30, -45, 60, 90, 10, 10);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(-30, -45, 60, 90, 10, 10);

        /// game.Card value and suit
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2.getFontMetrics();

        /// Set color
        if (suit.equals("♥") || suit.equals("♦")) {
            g2.setColor(Color.RED);
        } else {
            g2.setColor(Color.BLACK);
        }

        /// Draw value at top-left
        g2.drawString(value, -25, -25);

        /// Draw suit
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString(suit, -25, -5);

        /// Draw center suit
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        fm = g2.getFontMetrics();
        g2.drawString(suit, -fm.stringWidth(suit) / 2, 5);

        /// Draw value and suit at bottom-right
        g2.rotate(Math.PI);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString(value, -25, -25);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString(suit, -25, -5);

        g2.setTransform(oldTransform);
    }
}