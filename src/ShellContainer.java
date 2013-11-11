import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vlad
 * Date: 19.10.13
 * Time: 13:10
 * To change this template use File | Settings | File Templates.
 */
public class ShellContainer {
    private List<Shell> shellList = new LinkedList<Shell>(  );
    private AnimationContainer animationContainer = new AnimationContainer();

    public void add ( Shell shell ) {
        shellList.add( shell );
    }

    public void updateShells( float X, float Y ) {
        for ( int i = 0; i < shellList.size(); i++ ) {
            if ( shellList.get( i ).move() ) {
                shellList.get( i ).draw( X, Y );
            } else {
                shellList.remove( shellList.get( i ) );
            }
        }
        animationContainer.update();
    }

    public int isCollids( float x, float y, float radius ) {
        int numberOfCollids = 0;
        for ( int i = 0; i < shellList.size() && shellList.get( i ) != null; i++ ) {
            if ( shellList.get( i ).getRadius() + radius
                    > Math.sqrt( Math.pow( x - shellList.get( i ).getX(), 2 ) + Math.pow( y - shellList.get( i ).getY(), 2 ) ) ) {
                animationContainer.add( shellList.get( i ).getX(), shellList.get( i ).getY(), 12 );
                shellList.remove( shellList.get( i ) );
                ++numberOfCollids;
            }
        }
        return numberOfCollids;
    }
}
