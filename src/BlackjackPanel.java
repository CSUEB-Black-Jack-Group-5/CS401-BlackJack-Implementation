
///  Black Jack Intro Panel

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

public class BlackjackPanel extends JPanel {
    ///  Set Font
    private final Font titleFont = new Font("Times New Roman", Font.BOLD, 60);
    ///  Set Button Font
    private final Font buttonFont = new Font("Arial", Font.BOLD, 24);
    private BlackjackIntroGUI parent;

    public BlackjackPanel(BlackjackIntroGUI parent) {
        this.parent = parent;
        setLayout(null);

        /// Start button
        IntroButton startButton = new IntroButton("Start Game");
        startButton.setBounds(300, 350, 200, 60);
        startButton.addActionListener(e -> {
            parent.showLoginGUI();
        });

        /// Exit button
        IntroButton exitButton = new IntroButton("Exit");
        exitButton.setBounds(300, 430, 200, 60);
        exitButton.addActionListener(e -> System.exit(0));

        add(startButton);
        add(exitButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        /// Draw background
        GradientPaint backgroundGradient = new GradientPaint(
                0, 0, BlackjackIntroGUI.DARK_GREEN,
                0, getHeight(), new Color(0, 50, 0));
        g2.setPaint(backgroundGradient);
        g2.fillRect(0, 0, getWidth(), getHeight());

        /// Draw table pattern
        g2.setColor(new Color(0, 80, 0));
        for (int i = 0; i < getWidth(); i += 20) {
            for (int j = 0; j < getHeight(); j += 20) {
                if ((i / 20 + j / 20) % 2 == 0) {
                    g2.fillRect(i, j, 20, 20);
                }
            }
        }

        /// Animated cards
        for (CardAnimate card : parent.getAnimatedCards()) {
            card.draw(g2);
        }

        ///  Poker Chips
        ChipRenderer.drawPokerChips(g2);

        /// Title
        drawTitle(g2);

        g2.dispose();
    }

    private void drawTitle(Graphics2D g2) {
        /// Shadow for title
        g2.setFont(titleFont);
        g2.setColor(new Color(0, 0, 0, 120));
        g2.drawString("BLACKJACK", 202, 152);

        ///  title color
        GradientPaint textGradient = new GradientPaint(
                200, 100, BlackjackIntroGUI.GOLD,
                200, 200, new Color(255, 215, 0));
        g2.setPaint(textGradient);
        g2.drawString("BLACKJACK", 200, 150);

        ///  Border outline for title
        g2.setColor(new Color(139, 69, 19));
        g2.setStroke(new BasicStroke(2));
        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout textLayout = new TextLayout("BLACKJACK", titleFont, frc);
        Shape outline = textLayout.getOutline(null);
        g2.translate(200, 150);
        g2.draw(outline);
        g2.translate(-200, -150);

        /// Subtitle
        g2.setFont(new Font("Arial", Font.ITALIC, 24));
        g2.setColor(Color.WHITE);
        g2.drawString("All in if you still have chance", 250, 200);

        /// Card suits as decoration
        SuitRenderer.drawCardSuit(g2, 180, 150, "♠", Color.BLACK);
        SuitRenderer.drawCardSuit(g2, 620, 150, "♥", Color.RED);
        SuitRenderer.drawCardSuit(g2, 160, 130, "♣", Color.BLACK);
        SuitRenderer.drawCardSuit(g2, 640, 130, "♦", Color.RED);
    }
}