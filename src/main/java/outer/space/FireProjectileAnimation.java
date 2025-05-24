package outer.space;
import java.awt.Graphics2D;
import java.awt.Image;

public class FireProjectileAnimation implements GameAnimation{
    
    private Animation animation;

    public FireProjectileAnimation(){

        Image animImage = ImageManager.get_image("/images/fireshoot1.jpg");
		Image animImage2 = ImageManager.get_image("/images/fireshoot2.jpg");
		Image animImage3 = ImageManager.get_image("/images/fireshoot3.jpg");
        Image animImage4 = ImageManager.get_image("/images/fireshoot4.jpg");

		animation = new Animation(false);

        animation.addFrame(animImage, 50);
        animation.addFrame(animImage2, 300);
        animation.addFrame(animImage3, 500);
        animation.addFrame(animImage4, 2000);


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

		g2.drawImage(animation.getImage(), x, y, 200, 200, null);
	}

    public long getTotalDuration(){
        return animation.getTotalDuration();
    }

	public boolean isStillActive(){
		return animation.isStillActive();
	}
}