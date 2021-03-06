
import java.util.ArrayList;
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
    private List<Laser> laserList = new LinkedList<Laser>(  );
    private AnimationContainer animationContainer = new AnimationContainer();
    private WorldMap map;

    public ShellContainer( WorldMap map ) {
        this.map = map;
    }

    public void add ( Shell shell ) {
        shellList.add( shell );
    }

    public void addLaser ( Laser laser ) {
        laserList.add( laser );
    }

    public void update ( Ship ship ) {
        for ( int i = 0; i < shellList.size(); i++ ) {
            if ( shellList.get( i ).move( ) ) {
                shellList.get( i ).draw( map.getShiftX(), map.getShiftY() );
            } else {
                shellList.remove( shellList.get( i ) );
            }
        }
        for ( int i = 0; i < laserList.size(); i++ ) {
            laserList.get( i ).draw( ship, map );
            laserList.remove( laserList.get( i ) );
        }
    }

    public int isCollide ( float x, float y ) {
        int count = 0;
        for ( int i = 0; i < shellList.size(); i++ ) {
            if ( Math.sqrt( ( shellList.get( i ).getX() - x ) * ( shellList.get( i ).getX() - x ) +
                            ( shellList.get( i ).getY() - y ) * ( shellList.get( i ).getY() - y ) )
                            <= 40 + shellList.get( i ).getRadius() ) {
                shellList.remove( i );
                count += 1;
            }
        }
        return count;
    }

    public void removeShell( int value ) {
        shellList.remove( new Shell( value ) );
    }
}
