import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created with IntelliJ IDEA.
 * User: Vlad
 * Date: 19.10.13
 * Time: 13:16
 * To change this template use File | Settings | File Templates.
 */
public class Shell extends BasicObject {
    private int timeToDestroy;
    private int timer;

    private double calCos;
    private double calSin;


    public Shell ( double currentAngle, float radius , float x, float y, int timeToDestroy, float speed, Image image ) throws SlickException {
        super( (float) (x + Math.cos( currentAngle ) * radius),(float) (y - Math.sin( currentAngle ) * radius), image );
        this.timeToDestroy = timeToDestroy;

        calCos = Math.cos( currentAngle ) * speed;
        calSin = Math.sin( currentAngle ) * speed;

        image.setRotation( (float) Math.toDegrees( -currentAngle ) );
        timer = 0;
    }

    public boolean move() {
        x += calCos;
        y -= calSin;
        return timer++ < timeToDestroy;
    }

    public void destroy() throws SlickException {
        image.destroy();
    }


    void draw ( float sX, float sY ) {
        image.draw( x - sX - image.getCenterOfRotationX(), y - sY - image.getCenterOfRotationY() );
    }
}
