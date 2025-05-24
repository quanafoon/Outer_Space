package outer.space;
import java.awt.Graphics2D;
import java.awt.Image;

public class LightningProjectileAnimation implements GameAnimation{
    
    private Animation animation;
    public LightningProjectileAnimation(){
        Image lightning1 = ImageManager.get_image("/images/lightning1.jpg");
        Image lightning2 = ImageManager.get_image("/images/lightning2.jpg");
        
        animation = new Animation(false);

        animation.addFrame(lightning1, 100);
        animation.addFrame(lightning2, 100);
        animation.addFrame(lightning1, 100);
        animation.addFrame(lightning2, 100);
    }

    public void start() {
		animation.start();
	}

	public void stop(){
		animation.stop();
	}

    public void update() {
		if (!animation.isStillActive())
			return;

		animation.update();
	}

    public void draw(Graphics2D g2, int x, int y) {
		if (!animation.isStillActive())
			return;
		g2.drawImage(animation.getImage(), x, y, 500, 100, null);
	}

    public boolean isStillActive(){
		return animation.isStillActive();
	}
}
