package outer.space;
import java.awt.Graphics2D;
import java.awt.Image;

public class AsteroidAnimation implements GameAnimation{
    
    private Animation animation;

    public AsteroidAnimation(){

        Image animImage = ImageManager.get_image("/images/asteroid1.jpg");
		Image animImage2 = ImageManager.get_image("/images/asteroid2.jpg");
		Image animImage3 = ImageManager.get_image("/images/asteroid3.jpg");
        Image animImage4 = ImageManager.get_image("/images/asteroid4.jpg");

		animation = new Animation(true);

        animation.addFrame(animImage, 100);
        animation.addFrame(animImage2, 100);
        animation.addFrame(animImage3, 100);
        animation.addFrame(animImage4, 100);


    }

    public void start() {
		animation.start();
	}

	public void stop(){
		animation.stop();
	}

    public void update() {
		if (!animation.isStillActive()){
			return;
		}
		animation.update();
	}

    public void draw(Graphics2D g2, int x, int y) {
		if (!animation.isStillActive())
			return;

		g2.drawImage(animation.getImage(), x, y, 100, 100, null);
	}

    public long getTotalDuration(){
        return animation.getTotalDuration();
    }

	public boolean isStillActive(){
		return animation.isStillActive();
	}
}