import org.newdawn.slick.Image;
import org.newdawn.slick.Graphics;


/**
 * Created with IntelliJ IDEA.
 * User: Vlad
 * Date: 17.11.13
 * Time: 16:50
 * To change this template use File | Settings | File Templates.
 */
public class Addon {
    private String name;
    private int HPBar;
    private float relX;
    private float relY;
    public Addon( String name, Image image ) {
        this.name = name;
        relX = image.getCenterOfRotationX() - name.length() * 4;
        relY = image.getCenterOfRotationY() + 20;
    }

    public void draw( float x, float y, Graphics g ) {
        g.drawString( name, (int) (x - relX), (int) (y - relY) );
    }
}
