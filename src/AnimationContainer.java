import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vlad
 * Date: 11.11.13
 * Time: 16:24
 * To change this template use File | Settings | File Templates.
 */
public class AnimationContainer {
    private List<AnimationData> animationDataList = new LinkedList<AnimationData>(  );
    private Animation animation;

    public AnimationContainer() {
        try {
            animation = new Animation( new Image[]{
                                       new Image( "boom/1.PNG" ),
                                       new Image( "boom/2.PNG" ),
                                       new Image( "boom/3.PNG" ),
                                       new Image( "boom/4.PNG" ),
                                       new Image( "boom/5.PNG" ),
                                       new Image( "boom/6.PNG" ),
                                       new Image( "boom/7.PNG" ),
                                       new Image( "boom/8.PNG" ),
                                       new Image( "boom/9.PNG" ),
                                       new Image( "boom/10.PNG" ),
                                       new Image( "boom/11.PNG" ),
                                       new Image( "boom/12.PNG" )}, 50 );
        } catch ( SlickException seEx ) {
            System.out.println( "Can't load animation's images" );
        }
    }

    public void add( float x, float y, int duration ) {
        animationDataList.add( new AnimationData( x, y, animation.copy(), duration ) );
    }

    public void update() {
        for ( int i = 0; i < animationDataList.size(); i++ ) {
            if ( animationDataList.get( i ).update() ) {
                animationDataList.remove( animationDataList.get( i ) );
            } else {
                animationDataList.get( i ).animation.draw( animationDataList.get( i ).x, animationDataList.get( i ).y );
            }
        }
    }
}
