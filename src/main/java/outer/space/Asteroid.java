package outer.space;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Random;

import javax.swing.JPanel;

public class Asteroid implements Obstacle{
    private JPanel panel;
    private int x;
    private int y;
    private int height = 70;
    private int width = 70;
    private Image asteroid;
    private int firstX;
    private int offset;
    private AsteroidAnimation animation = new AsteroidAnimation();

    
    public Asteroid(JPanel panel, int offset){
        this.panel = panel;
        this.offset = offset;
        this.x = newX();
        this.y = newY();
        this.firstX = this.x;
        this.asteroid = ImageManager.get_image("/images/monster1.jpg");
        animation.start();
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(asteroid, x ,y,null);
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }

    public int newX(){
        Random rand = new Random();
        return rand.nextInt(offset + panel.getWidth()/2, offset + (panel.getWidth() - (panel.getWidth()/10)));
    }
    public int newY(){
        Random rand = new Random();
        return rand.nextInt(panel.getHeight()/20, panel.getHeight() - (panel.getHeight()/4) + height);
    }

    public void update(int x, Graphics2D g2){
        this.x = (this.firstX - x);
        animation.update();
        animation.draw(g2, this.x, y);
    }

    public int getFirstX() {
        return this.firstX;
    }

    public void setFirstX(int firstX) {
        this.firstX=firstX;
    }
}
