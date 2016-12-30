// Programmer: Nate Sobol
// Title: Classic Slot Machine
// Last Modifed: 11/29/16

import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.util.Random;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class ClassicSlotMachine extends JApplet {
    
    int counter;
    int bankAmount, playerAmount;
    boolean isMoving = false;
    ImageIcon slotImage1, slotImage2, slotImage3, slotImage4, slotImage5, slotImage6;
    AudioClip pulllever, wingame;
    JButton play, quit;
    JLabel bankLabel, playerLabel;
    SoftBevelBorder line;
    SlotPanel game;
    Font tokensFont;
    

    public ClassicSlotMachine() {
        
        // set bank values
        bankAmount = 1000;
        playerAmount = 1000;
        
        // Component Definitions
        JPanel buttonPanel = new JPanel();
        JButton play = new JButton("Spin");
        JButton quit = new JButton("Quit");
        JPanel score = new JPanel();
        bankLabel = new JLabel("Machine's Bank: " + bankAmount);
        playerLabel = new JLabel("Player's Bank:" + playerAmount);;
        
        // Component Graphical Properties
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 0));
        score.setLayout(new GridLayout(1, 2, 0, 0)); 
        game = new SlotPanel();
        game.setPreferredSize(new Dimension(300, 450));
        game.setBackground(Color.green);
        score.setBackground(Color.yellow);
        setLayout(new BorderLayout());

        // Import images
        slotImage1 = new ImageIcon("src/resources/images/lemon.jpg");
        slotImage2 = new ImageIcon("src/resources/images/cherry.jpg");
        slotImage3 = new ImageIcon("src/resources/images/bar.jpg");
        slotImage4 = new ImageIcon("src/resources/images/banana.jpg");
        slotImage5 = new ImageIcon("src/resources/images/melon.jpg");
        slotImage6 = new ImageIcon("src/resources/images/orange.jpg");
        
        // Import Sounds Files
        Class metaObject = this.getClass();
        URL audio1 = metaObject.getResource("resources/sounds/wingame.wav");
        URL audio2 = metaObject.getResource("resources/sounds/pulllever.wav");
        wingame = JApplet.newAudioClip(audio1);
        pulllever = JApplet.newAudioClip(audio2);

        // Compile Elements/Cleanup
        bankLabel.setHorizontalAlignment(JLabel.CENTER);
        play.setVerticalAlignment(AbstractButton.CENTER);
        add(game, BorderLayout.CENTER);
        add(score, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.EAST);
        buttonPanel.add(play);
        buttonPanel.add(quit);
        score.add(playerLabel);
        score.add(bankLabel);
        play.addActionListener(new playButtonHandler());
        game.validate();
        
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = JOptionPane.showConfirmDialog(null, "Do you want to end the game?", "Exit", JOptionPane.YES_NO_OPTION);
                if (i == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    class SlotPanel extends JPanel {
        int num1, num2, num3, nextRand1,
                nextRand2, nextRand3, horizontalPlacement, verticalPlacement;
        //ArrayList<Image> ImageArrList;
        Timer time;
        Random rand;

        public SlotPanel() {
            time = new Timer(10, new TimerListener());
            rand = new Random();
            num1 = getNext();
            num2 = getNext();
            num3 = getNext();
            nextRand1 = getNext();
            nextRand2 = getNext();
            nextRand3 = getNext();
        }

        public void play() {
            counter = 0;
            horizontalPlacement = getHeight() / 2 - 50;
            verticalPlacement = getHeight() / 2 - 150;
            time.start();
            pulllever.loop();
            isMoving = true;
            horizontalPlacement += 10;
        }

        public int getNext() {
            int next = rand.nextInt(6);
            return next;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!isMoving) {
                g.drawImage(getImage(num1), getWidth() / 2 - 150, getHeight() / 2 - 50, 100, 100, this);
                g.drawImage(getImage(num2), getWidth() / 2 - 50, getHeight() / 2 - 50, 100, 100, this);
                g.drawImage(getImage(num3), getWidth() / 2 + 50, getHeight() / 2 - 50, 100, 100, this);
            }

            if (isMoving) {
                g.drawImage(getImage(num1), getWidth() / 2 - 150, horizontalPlacement, 100, 100, this);
                g.drawImage(getImage(num2), getWidth() / 2 - 50, horizontalPlacement, 100, 100, this);
                g.drawImage(getImage(num3), getWidth() / 2 + 50, horizontalPlacement, 100, 100, this);

                if (horizontalPlacement != getHeight() / 2 - 50) {
                    g.drawImage(getImage(nextRand1), getWidth() / 2 - 150, verticalPlacement, 100, 100, this);
                    g.drawImage(getImage(nextRand2), getWidth() / 2 - 50, verticalPlacement, 100, 100, this);
                    g.drawImage(getImage(nextRand3), getWidth() / 2 + 50, verticalPlacement, 100, 100, this);
                    verticalPlacement += 5;
                    horizontalPlacement += 5;
                }

                if (horizontalPlacement > 200) {

                    counter++;
                    num1 = nextRand1;
                    num2 = nextRand2;
                    num3 = nextRand3;

                    nextRand1 = getNext();
                    nextRand2 = getNext();
                    nextRand3 = getNext();
                    horizontalPlacement = verticalPlacement;
                    verticalPlacement = getHeight() / 2 - 150;
                }
                if (counter > 50) {
                    time.stop();
                    pulllever.stop();
                    isMoving = false;
                    repaint();
                    if (num1 == num2 && num2 == num3) {
                        win();
                    } else {
                        lose();
                    }
                }
            }
        }

        public Image getImage(int imageNum) {
            
            ArrayList<Image> slotImage = new ArrayList<>();
            slotImage.add(slotImage1.getImage());
            slotImage.add(slotImage2.getImage());
            slotImage.add(slotImage3.getImage());
            slotImage.add(slotImage4.getImage());
            slotImage.add(slotImage5.getImage());
            slotImage.add(slotImage6.getImage());
            return slotImage.get(imageNum);
        }

        public void win() {

            wingame.play();
            playerAmount += bankAmount;
            bankAmount = 1000;
            bankLabel.setText("Machine's Bank: " + bankAmount);
            playerLabel.setText("Player's Bank: " + playerAmount);
            isMoving = false;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(null, "Jackpot! You win!");
                }
            });
        }

         public void lose() {
            
            isMoving = false;
            bankAmount++; playerAmount--;
            bankLabel.setText("Machine's Bank: " + bankAmount);
            playerLabel.setText("Player's Bank: " + playerAmount);

            if (playerAmount == 0) {
                int option = JOptionPane.showConfirmDialog(this, "You're out of funds!", "You've busted out!",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    bankAmount = 1000;
                    playerAmount = 1000;
                    bankLabel.setText("Machine's Bank: " + bankAmount);
                    playerLabel.setText("Player's Bank: " + playerAmount);

                }
                if (option == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            }
        }
        
        class TimerListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        }
    }

    class playButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isMoving) {
                game.play();
            }
        }
    }
    
    public static void main(String args[]){
        JFrame frame = new JFrame("Slot Machine");
        frame.getContentPane().add(new ClassicSlotMachine());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(400, 600);
        frame.setVisible(true);
    }
}




