package outer.space;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame implements ActionListener , KeyListener {
    private JPanel mainPanel;
    private JPanel infoPanel;
    private GamePanel gamePanel;

    private JLabel scoreBar;
    private Container c;

    public static JTextField score;

    private JButton startB;
    private JButton stopB;
    private JButton musicB;
    public static Integer count = 0;
    public static Integer count2 = 0;
    private SoundManager soundManager;
    public static boolean muted = false;
    

    public GameWindow(){
        setTitle("A Game");
        setSize(700, 600);


        scoreBar = new JLabel("Score:");
        scoreBar.setForeground(Color.WHITE);
        score = new JTextField(25);
        score.setEditable(false);        


        infoPanel = new JPanel();
        GridLayout grid = new GridLayout(1, 1);
        infoPanel.setBackground(new Color(27, 34, 89));
        infoPanel.setLayout(grid);
        infoPanel.add(scoreBar);
        infoPanel.add(score);

        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(640, 400));

        
        JPanel buttonPanel = new JPanel();
		grid = new GridLayout(1, 3);
		buttonPanel.setLayout(grid);
        startB = new JButton ("Start");
        stopB = new JButton ("Stop");
        musicB = new JButton("music");
        musicB.setBackground(new Color(125, 209, 148));
        startB.addActionListener(this);
        stopB.addActionListener(this);
        musicB.addActionListener(this);
		buttonPanel.add (startB);
        buttonPanel.add(stopB);
        buttonPanel.add(musicB);

        mainPanel = new JPanel();
        FlowLayout flow = new FlowLayout();
        mainPanel.setLayout(flow);
        mainPanel.add(infoPanel);
        mainPanel.add(gamePanel);
        mainPanel.add(buttonPanel);
        mainPanel.setBackground(new Color(108, 121, 163));
        mainPanel.addKeyListener(this);

        c = getContentPane();
        c.add(mainPanel);

        setVisible(true);

        soundManager = SoundManager.getInstance();
    }



    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_UP){
            gamePanel.setDirection("up");
        }
        if(key == KeyEvent.VK_DOWN){
            gamePanel.setDirection("down");
        }
        if(key == KeyEvent.VK_LEFT){
            gamePanel.setFallBack(true);
        }
        if(key == KeyEvent.VK_RIGHT){
            gamePanel.setBoost(true);
        }
        if(key == KeyEvent.VK_Q){
            gamePanel.shootLightning();
        }
        if(key == KeyEvent.VK_W){
            gamePanel.shootFire();
        }
        if(key == KeyEvent.VK_E){
            gamePanel.heal();

        }
    }



    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_RIGHT){
            gamePanel.setBoost(false);
        }
        if(key == KeyEvent.VK_LEFT){
            gamePanel.setFallBack(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(command.equals(startB.getText())){
            soundManager.playClip("btnPress", false);
            gamePanel.startGame();
            mainPanel.requestFocusInWindow();

        }    
        if(command.equals(stopB.getText())){
            soundManager.playClip("btnPress", false);
            System.exit(0);
        }
        if(command.equals("music")){
            mainPanel.requestFocusInWindow();
            soundManager.playClip("btnPress", false);
            if(muted){
                musicB.setBackground(new Color(125, 209, 148));
                soundManager.setVolume("game", 1.0f);
                muted = false;
            }
            else{
                musicB.setBackground(new Color(238, 238, 238));
                soundManager.setVolume("game", 0.0f);
                muted = true;
            }
        }
    }
}
