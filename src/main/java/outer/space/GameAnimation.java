package outer.space;
import java.awt.Graphics2D;

public interface GameAnimation {
    public void start();
    public void stop();
    public void update();
    public void draw(Graphics2D g2, int x, int y);
    public boolean isStillActive();
}
