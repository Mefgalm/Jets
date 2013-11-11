import org.newdawn.slick.Image;

/**
 * Created with IntelliJ IDEA.
 * User: Vlad
 * Date: 15.10.13
 * Time: 20:26
 * To change this template use File | Settings | File Templates.
 */
public abstract class BasicObject {
    protected float x;
    protected float y;
    protected float radius;

    protected float currentAngle;
    protected float speed;

    private int mapHeight;
    private int mapWeight;
    protected Image image;


    public BasicObject ( float speed, float currentAngle, float x, float y, Image image )  {
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.currentAngle = currentAngle;
        this.image = image;
        this.radius = 40;
    }

    public BasicObject ( float speed, float currentAngle, float radius, float x, float y, Image image )  {
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.currentAngle = currentAngle;
        this.image = image;
        this.radius = radius;
    }

    public static float chooseRadius ( Image image ) {
        return (float) Math.random() * ( image.getCenterOfRotationX() / 2) + image.getCenterOfRotationX() / 2;
    }

    public float getRadius() {
        return radius;
    }

    public float getSpeed() {
        return speed;
    }

    public void drawImage( float x, float y ) {
        image.draw( x, y );
    }

    public void setMapSize ( int mapHeight, int mapWeight ) {
        this.mapHeight = mapHeight;
        this.mapWeight = mapWeight;
    }

    public boolean canMove ( double x, double y ) {
        return x >= image.getCenterOfRotationX() && x < mapWeight - image.getCenterOfRotationX()
                && y >= image.getCenterOfRotationY() && y < mapHeight - image.getCenterOfRotationY();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getCurrentAngle() {
        return currentAngle;
    }
}
