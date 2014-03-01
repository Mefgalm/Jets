import org.newdawn.slick.Image;

/**
 * Created with IntelliJ IDEA.
 * User: Vlad
 * Date: 01.03.14
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
public class Laser {
    private float x;
    private float y;
    private float angle;
    private Image image;


    public Laser( float x, float y, float angle, Image image ) {
        this.image = image;
        this.angle = angle;
        this.x = (float) ( x + Math.cos( angle ) * ( 20 + image.getCenterOfRotationX() ) );
        this.y = (float) ( y - Math.sin( angle ) * ( 20 + image.getCenterOfRotationX() ) );
    }

    public Laser( float x, float y, Image image, float angle ) {
        this.image = image;
        this.angle = angle;
        this.x = x;
        this.y = y;
    }

    public void draw( Ship ship, WorldMap map ) {
        image.setRotation( (float) Math.toDegrees( -angle ) );
        float dX = -image.getCenterOfRotationX(), dY = -image.getCenterOfRotationY();
        if ( ship.getX() < ship.getHalfWidth() ) {
            dX += x;
        } else if ( ship.getX() >= map.getMapHeight() - ship.getHalfHeight() ) {
            dX += x - map.getMapWidth() + ship.getWidth();
        } else {
            dX += x - ship.getShiftX();
        }
        if ( ship.getY() < ship.getHalfHeight() ) {
            dY += y;
        } else if ( ship.getY() >= map.getMapHeight() - ship.getHalfHeight() ) {
            dY += y + ship.getHeight() - map.getMapHeight();
        } else {
            dY += y - ship.getShiftY();
        }
        image.draw( dX, dY );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(  );
        sb.append( x ).append( ";" ).append( y ).append( ";" ).append( angle );
        return sb.toString();
    }
}
