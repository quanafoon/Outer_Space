package outer.space;
import java.awt.Graphics2D;

public interface Obstacle {

    public void draw(Graphics2D g2);
    public int getX();
    public int getY();
    public int getHeight();
    public int getWidth();
    public void update(int pos, Graphics2D g2);
    public int getFirstX();
    public void setFirstX(int firstX);
    public int newX();
    public int newY();
}