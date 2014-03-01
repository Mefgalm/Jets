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

    public void draw() {
        image.setRotation( (float) Math.toDegrees( -angle ) );
        image.draw( x - image.getCenterOfRotationX(), y - image.getCenterOfRotationY() );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(  );
        sb.append( x ).append( ";" ).append( y ).append( ";" ).append( angle );
        return sb.toString();
    }
}
