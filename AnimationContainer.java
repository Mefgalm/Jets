czxczxczxcimport org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vladyslav
 * Date: 11.11.13
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
public class AnimationContainer {
    private Animation animationBoom;
    private List<AnimationData> animationList = new ArrayList<AnimationData>();

    public AnimationContainer()  {
        try {
        animationBoom = new Animation( new Image[]{ new Image( "1.PNG" ),
                new Image( "2.PNG" ),
                new Image( "3.PNG" ),
                new Image( "4.PNG" ),
                new Image( "5.PNG" ),
                new Image( "6.PNG" ),
                new Image( "7.PNG" ),
                new Image( "8.PNG" ),
                new Image( "9.PNG" ),
                new Image( "10.PNG" ),
                new Image( "11.PNG" ),
                new Image( "12.PNG" )}, 40 );
        } catch ( SlickException ex ) {}
    }

    public void addBoomAnimation( float x, float y ) {
        animationList.add( new AnimationData( x, y, animationBoom.copy() ) );
    }

    public void update() {
        for ( int i = 0; i < animationList.size(); i++ ) {
            if ( !animationList.get( i ).update() ) {
                animationList.remove( animationList.get( i ) );
            }
        }
    }
}
