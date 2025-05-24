package outer.space;
import java.awt.Graphics2D;

public interface Projectile {
    public int getX();
    public int getY();
    public int getHeight();
    public int getWidth();
    public void update(Graphics2D g2);
    public void stopAnimation();
    public boolean animationActive();
}
