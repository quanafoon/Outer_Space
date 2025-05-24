package outer.space;
import java.awt.Graphics2D;

public class LightningProjectile implements Projectile{

    private int x;
    private int y;
    private int height = 50;
    private int width = 500;
    private int dx = 5;
    private GameAnimation animation = new LightningProjectileAnimation();

    public LightningProjectile(int x, int y){
        this.x = x;
        this.y = y;
        animation.start();
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

    public void stopAnimation(){
        animation.stop();
    }

    //Try updating animation within the update
    public void update(Graphics2D g2){
        this.x += dx;
        animation.update();
        animation.draw(g2, x, y);
    }

    public boolean animationActive(){
        return animation.isStillActive();
    }
}
