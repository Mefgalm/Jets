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
        for ( Shell shell : shellList ) {
            if ( shell.getRadius() + radius > Math.sqrt( Math.pow( x - shell.getX(), 2 ) + Math.pow( y - shell.getY(), 2 ) ) ) {
                System.out.println( shell.getRadius() );
                animationContainer.add( shell.getX(), shell.getY(), 12 );
                ++numberOfCollids;
            }
        }
        return numberOfCollids;
    }
}
