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

    private int number;
    private static int count = 0;

    public Shell ( float currentAngle, float radius, float x, float y, int timeToDestroy, float speed, Image image ) {
        super( speed, currentAngle, (float) ( x + Math.cos( currentAngle ) * radius ), (float) ( y - Math.sin( currentAngle ) * radius ), image );
        this.timeToDestroy = timeToDestroy;
        if ( count > 100 ) {
            number = 0;
        } else {
            number = count;
        }
        count++;

        calCos = Math.cos( currentAngle ) * speed;
        calSin = Math.sin( currentAngle ) * speed;

        image.setRotation( (float) Math.toDegrees( -currentAngle ) );
        timer = 0;
    }

    public Shell ( int number, float currentAngle, float radius, float x, float y, int timeToDestroy, Image image, float speed ) {
        super( speed, currentAngle, radius, x, y, image );
        this.timeToDestroy = timeToDestroy;
        this.number = number;

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

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(  );
        sb.append( number ).append( ";" ).
           append( super.getCurrentAngle() ).append( ";" ).
           append( super.getRadius() ).append( ";" ).
           append( super.getX() ).append( ";" ).
           append( super.getY() ).append( ";" ).
           append( timeToDestroy ).append( ";" ).
           append( super.getSpeed() );
        return sb.toString();
    }
}
