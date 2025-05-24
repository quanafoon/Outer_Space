package outer.space;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;


public class GamePanel extends JPanel implements Runnable{
    
    private Image background;
    private BufferedImage image;
    private boolean isRunning = false;
    private Thread thread;
    private int pos = 0;
    private int bdx = 10; 
    private int bWidth = 640;
    private int bHeight = 400;
    private int progress = 0;
    private Astronaut astronaut;
    private LinkedList<Obstacle> obstacles = new LinkedList<>();
    private LinkedList<Obstacle> nextObstacles = new LinkedList<>();
    private LinkedList<Ammunition> ammunitions = new LinkedList<>();
    private LinkedList<Ammunition> nextAmmunitions = new LinkedList<>();
    private HashMap<GameAnimation, Obstacle> collection = new HashMap<>();
    private LinkedList<Projectile> projectiles = new LinkedList<>();
    private Integer scoreCount = 0;
    private SoundManager soundManager;
	private AmmuntionAnimation ammunitionAnimation;
    private String direction;
    private boolean boost = false;
    private boolean fallBack = false;
    private Integer healthCount = 100;
    private int healthOffset = 20;
    private int ammunitionCount = 100;
    private Rectangle2D healthBar;
    private Rectangle2D healthLevel;
    private Rectangle2D ammunitionBar;
    private Rectangle2D ammunitionLevel;



    public GamePanel() {
        background = ImageManager.get_image("/images/space.jpg");
        image = new BufferedImage (bWidth, bHeight, BufferedImage.TYPE_INT_RGB);
        soundManager = SoundManager.getInstance();
    }

    public void startGame(){
        if(isRunning){
            return;
        }
        scoreCount = 0;
        GameWindow.score.setText(scoreCount.toString());
        pos=0;
        createEntities();
        ammunitionAnimation.start();
        soundManager.playClip("game", true);
        if(!GameWindow.muted){
            soundManager.setVolume("game", 1.0f);
        }
        else{
            soundManager.setVolume("game", 0.0f);
        }
        direction = "up";
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void createEntities(){
        this.astronaut = new Astronaut(30, 100);
        Random rand = new Random();
        for(int i=0; i < rand.nextInt(2, 5); i++){
            Obstacle obstacle = new Monster(this, 0);
            obstacles.add(obstacle);
        }
        for(int i=0; i < rand.nextInt(1, 2); i++){
            Obstacle obstacle =new Asteroid(this, 0);
            obstacles.add(obstacle);
        }
        for(int i=0; i < rand.nextInt(2, 5); i++){
            Obstacle obstacle = new Monster(this, bWidth);
            nextObstacles.add(obstacle);
        }
        for(int i=0; i < rand.nextInt(1, 2); i++){
            Obstacle obstacle =new Asteroid(this, bWidth);
            nextObstacles.add(obstacle);
        }
        for(int i=0; i < rand.nextInt(0, 2); i++){
            Ammunition ammunition = new Ammunition(this, 0);
            ammunitions.add(ammunition);
        }
        for(int i=0; i < rand.nextInt(0, 2); i++){
            Ammunition ammunition = new Ammunition(this, bWidth);
            nextAmmunitions.add(ammunition);
        }
        ammunitionAnimation = new AmmuntionAnimation();
        healthBar = new Rectangle2D.Double(5, 10, 80, 30); 
        healthLevel = new Rectangle2D.Double(5, 10, 80, 30);
        ammunitionBar = new Rectangle2D.Double(5, bHeight - 110, 20, 100); 
        ammunitionLevel = new Rectangle2D.Double(5, bHeight - 110, 20, 100);
    }

    public void drawEntities(){
        Graphics2D imageContext = (Graphics2D) image.getGraphics();
		imageContext.drawImage(background, pos*-1, 0, null);
        imageContext.drawImage(background, bWidth - pos, 0, null);
        showGameBars(imageContext);
        moveObjects(imageContext);
        moveProjectiles(imageContext);
        astronaut.draw(imageContext);
        animateAmmunitions(imageContext);

		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.drawImage(image, 0, 0, bWidth, bHeight, null);
        pos+= bdx;
        
        if(pos >= bWidth){
            newBackground();
        }

		imageContext.dispose();
		g2.dispose();
    }


    /**
     * Handle the creation of a new background image by maintaining the game state
     */
    public void newBackground(){
        progress++;
        pos -= (bWidth);
        for(Map.Entry<GameAnimation, Obstacle> entry : collection.entrySet()){
            entry.getKey().stop();
        }
        shiftObjects();

        this.obstacles = new LinkedList<>(nextObstacles);
        this.ammunitions = new LinkedList<>(nextAmmunitions);
        nextObstacles.clear();
        collection.clear();
        nextAmmunitions.clear();
        Random rand = new Random();
        for(int i=0; i < rand.nextInt(2, 5); i++){
            Obstacle obstacle = new Monster(this, bWidth);
            nextObstacles.add(obstacle);
        }
        for(int i=0; i < rand.nextInt(1, 2); i++){
            Obstacle obstacle = new Asteroid(this, bWidth);
            nextObstacles.add(obstacle);
        }
        for(int i=0; i < rand.nextInt(0, 2); i++){
            Ammunition ammunition = new Ammunition(this, bWidth);
            nextAmmunitions.add(ammunition);
        }
        if((bdx < 20 && progress%5==0)){
            bdx+=5;
        }
    }


    /**
     * Show the Ammunition and Health bars
     */
    public void showGameBars(Graphics2D g2){
        g2.setColor(Color.WHITE);
        g2.fill(healthBar);
        healthLevel.setRect(5, 10, healthCount -healthOffset, 30);
        g2.setColor(Color.RED);
        g2.fill(healthLevel);
        g2.setColor(Color.WHITE);
        g2.fill(ammunitionBar);
        if(ammunitionCount < 0){
            ammunitionCount = 0;
        }
        ammunitionLevel.setRect(5, bHeight - ammunitionCount - 10, 20, ammunitionCount);
        g2.setColor(Color.YELLOW);
        g2.fill(ammunitionLevel);
    }

    /**
     * Move the astronaut in a given direction
     * @param direction
     */
    public void moveAstronaut(String direction){
        enforceBoundaries();
        astronaut.move(direction);
        unlock();
    }

    /*
     * Move all objects that move with the background
     */
    public void moveObjects(Graphics2D g2){
        for(Obstacle obstacle : obstacles){
            obstacle.update(pos, g2);
        }

        for(Ammunition ammunition : ammunitions){
            ammunition.update(pos, g2);
        }

        if(pos >= bWidth/2){
            for(Obstacle obstacle : nextObstacles){
                obstacle.update(pos, g2);
            }
            for(Ammunition ammunition : nextAmmunitions){
                ammunition.update(pos, g2);
            }
        }
    }

    /**
     * Ensures that the astronaut remains within the game panel but setting locks for movement
     */
    public void enforceBoundaries(){
        int astronautX = astronaut.getX();
        int astronautY = astronaut.getY();
        if(astronautY <= 0){
            astronaut.setLockUp(true);
        }
        if (astronautY + astronaut.getHeight() >= bHeight){
            astronaut.setLockDown(true);
        }
        if (astronautX <= 10){
            astronaut.setLockLeft(true);
        }
        if (astronautX + astronaut.getWidth() >= bWidth - 50){
            astronaut.setLockRight(true);
        }
    }


    /**
     * Sets the placement of all objects to the current panel view
     */
    public void shiftObjects(){
        for(Obstacle obstacle : nextObstacles){
            obstacle.setFirstX(obstacle.getFirstX() - bWidth);
        }
        for(Ammunition ammunition : nextAmmunitions){
            ammunition.setFirstX(ammunition.getFirstX() - bWidth);
            
        }
    }

    /**
     * Stops the game and resets to "start of game" configuration
     */
    public void collisionAction(){
        soundManager.playClip("damage", false);
        healthCount -= 5;
        if(healthCount <= 0){
            progress=0;
            healthCount = 100;
            ammunitionCount = 100;
            bdx=10;
            obstacles.clear();
            nextObstacles.clear();
            ammunitions.clear();
            nextAmmunitions.clear();
            collection.clear();
            soundManager.stopClip("game");
            isRunning = false;
        }
    }


    /**
     * Resets all directional locks to false
     */
    public void unlock(){
        astronaut.setLockUp(false);
        astronaut.setLockDown(false);
        astronaut.setLockLeft(false);
        astronaut.setLockRight(false);
    }


    /**
     * Collects an ammunition if touched, adds it to the Map of collected ammunitions and removes it from the list of current ammunitions Also updates the score and initiates the animation
     * @param astronautX
     * @param astronautY
     */
    public void checkCollections(){
        int astronautX = astronaut.getPos().get('x');
        int astronautY = astronaut.getPos().get('y');
        Obstacle removedAmmunition = null;
        Obstacle removedObstacle = null;
        if(astronautX < 0){
            collisionAction();
        }
        for(Ammunition ammunition : ammunitions){
            if(ammunition.getX() >= astronautX && ammunition.getX() <= astronautX + astronaut.getWidth()
                && ammunition.getY() >= astronautY && ammunition.getY() <= astronautY + astronaut.getHeight()){
                ammunitionCount = 100;
                soundManager.playClip("collect", false);
                removedAmmunition = ammunition;
                GameAnimation ammunitionAnimation = new AmmuntionAnimation();
                collection.put(ammunitionAnimation, removedAmmunition);
                ammunitionAnimation.start();
            }
        }
        ammunitions.remove(removedAmmunition);

        removedObstacle = checkObstacleCollisions(astronaut, obstacles);
        obstacles.remove(removedObstacle);
        removedObstacle = checkObstacleCollisions(astronaut, nextObstacles);
        nextObstacles.remove(removedObstacle);
    }


/**
 * Handles the animation of ammunitions that have been collected
 * @param imageContext
 */
    public void animateAmmunitions(Graphics2D g2){
        for(Map.Entry<GameAnimation, Obstacle> entry : collection.entrySet()){
            GameAnimation animation = entry.getKey();
            Obstacle ammunition = entry.getValue();
            animation.update();
            animation.draw(g2, ammunition.getFirstX() - pos, ammunition.getY());
        }
    }

    /**
     * Checks for any interaction involving a list of obstacles, whether for projectiles or the astronaut
     * @param astronaut
     * @param obstacles
     * @return The monster to be removed or null if no interaction occured
     */
    public Obstacle checkObstacleCollisions(Astronaut astronaut, LinkedList<Obstacle> obstacles){
        Obstacle removedObstacle = null;
        int astronautX = astronaut.getX();
        int astronautY = astronaut.getY();
        for(Obstacle obstacle : obstacles){
            if(astronautX < obstacle.getX() + obstacle.getWidth() &&
                astronautX + astronaut.getWidth() -20 > obstacle.getX() &&
                astronautY < obstacle.getY() + obstacle.getHeight() &&
                astronautY + astronaut.getHeight() -20> obstacle.getY()){
                collisionAction();
                if(obstacle instanceof Asteroid){
                    astronaut.setLockRight(true);
                    astronaut.setX(astronautX - bdx);
                }
            }
            for(Projectile projectile: projectiles){
                if(allowed(projectile, obstacle)){
                    if(projectile.getX() < obstacle.getX() + obstacle.getWidth() &&
                    projectile.getX() + projectile.getWidth() > obstacle.getX() &&
                    projectile.getY() < obstacle.getY() + obstacle.getHeight() &&
                    projectile.getY() + projectile.getHeight() > obstacle.getY()){
                        removedObstacle = obstacle;
                        GameAnimation fireAnimation = new FireAnimation();
                        collection.put(fireAnimation, removedObstacle);
                        fireAnimation.start();
                        updateScore(removedObstacle);
                    }
                }
            }
        }
        return removedObstacle;
    }

    public void updateScore(Obstacle obstacle){
        if(obstacle instanceof Asteroid){
            scoreCount+= 10;
        }
        if(obstacle instanceof Monster){
            scoreCount+= 5;
        }
        GameWindow.score.setText(scoreCount.toString());
    }

    /**
     * Shoot a fire projectile
     */
    public void shootFire(){
        if(ammunitionCount < 40)
            return;
        soundManager.playClip("shootFire", false);
        int astronautX = astronaut.getX();
        int astronautY = astronaut.getY();
        Projectile fire = new FireProjectile(astronautX+30, astronautY-50);
        projectiles.add(fire);
        ammunitionCount -= 40;
    }

    public void heal(){
        if(ammunitionCount < 100 || healthCount == 100){
            return;
        }
        soundManager.playClip("heal", false);
        healthCount = 100;
        ammunitionCount = 0;
    }

    public void shootLightning(){
        if(ammunitionCount < 10){
            return;
        }
        soundManager.playClip("lightning", false);
        int astronautX = astronaut.getX();
        int astronautY = astronaut.getY();
        Projectile lightning = new LightningProjectile(astronautX + 30, astronautY);
        projectiles.add(lightning);
        ammunitionCount -= 10;
    }

    /**
     * Returns true if there is a valid interaction between an obstacle and projectile
     * @param projectile
     * @param obstacle
     * @return
     */
    public boolean allowed(Projectile projectile, Obstacle obstacle){
        if((projectile instanceof LightningProjectile) && (obstacle instanceof Asteroid)){
            return false;
        }
        return true;
    }

    /**
     * Update the positions of projectiles along the x-axis and draw them
     * @param g2
     */
    public void moveProjectiles(Graphics2D g2){
        try{
            for (Projectile projectile : projectiles){
                projectile.update(g2);
            }
        }
        catch (Exception e){
            return;
        }
    }

    /**
     * Ensure that projectiles are removed from the game when their animation is complete
     */
    public void manageProjectiles(){
        Projectile removedProjectile = null;
        for (Projectile projectile : projectiles){
            if(!projectile.animationActive()){
                removedProjectile = projectile;
            }
        }
        projectiles.remove(removedProjectile);
    }

    public void handleMovement(){
        if(boost && !astronaut.isLockRight()){
            pos+=bdx;
            moveAstronaut("right");
        }
        else if(fallBack){
            moveAstronaut("left");
        }
        moveAstronaut(direction);
    }

    public String getDirection(){return this.direction;}
    public boolean isBoosting(){return this.boost;}
    public boolean isFallingBack(){return this.fallBack;}
    public void setDirection(String direction){this.direction = direction;}
    public void setBoost(boolean boost){this.boost = boost;}
    public void setFallBack(boolean fallBack){this.fallBack = fallBack;}

    @Override
    public void run() {
        while(isRunning){
            try {
                drawEntities();
                handleMovement();
                checkCollections();
                manageProjectiles();
                Thread.sleep(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
