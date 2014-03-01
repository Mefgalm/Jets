import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created with IntelliJ IDEA.
 * User: Vlad
 * Date: 14.10.13
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */
public class WorldMap {
    private Image image;
    private float shiftX;
    private float shiftY;
    private int halfScreenWidth;
    private int halfScreenHeight;

    public WorldMap ( String mapPath, int screenHeight, int screenWidth ) throws SlickException {
        image = new Image( mapPath );
        halfScreenHeight = screenHeight / 2;
        halfScreenWidth = screenWidth / 2;
    }

    public void draw ( float x, float y ) {
        if ( x >= halfScreenWidth ) {
            shiftX = x - halfScreenWidth;
            if ( x > image.getWidth() - halfScreenWidth ) {
                shiftX = image.getWidth() - halfScreenWidth * 2;
            }
        } else {
            shiftX = 0;
        }
        if ( y >= halfScreenHeight ) {
            shiftY = y - halfScreenHeight;
            if ( y >  image.getHeight() - halfScreenHeight ) {
                shiftY = image.getHeight() - halfScreenHeight * 2;
            }
        } else {
            shiftY = 0;
        }
        image.draw( -shiftX, -shiftY );
    }

    public float getShiftX() {
        return shiftX;
    }

    public float getShiftY() {
        return shiftY;
    }

    public float getHalfHeight() {
        return halfScreenHeight;
    }

    public float getHalfWidth() {
        return halfScreenWidth;
    }

    public int getMapWidth () {
        return image.getWidth();
    }

    public int getMapHeight () {
        return image.getHeight();
    }
}
