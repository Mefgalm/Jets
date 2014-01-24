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
    private AnimationContainer animationContainer = new AnimationContainer();
    private Map map;

    public ShellContainer( Map map ) {
        this.map = map;
    }

    public void add ( Shell shell ) {
        shellList.add( shell );
    }

    public void updateShells( ) {
        for ( int i = 0; i < shellList.size(); i++ ) {
            if ( shellList.get( i ).move( ) ) {
                shellList.get( i ).draw( map.getShiftX(), map.getShiftY() );
            } else {
                shellList.remove( shellList.get( i ) );
            }
        }
        animationContainer.update();
    }

    public Object[] isCollide ( BasicObject bo ) {
        List<Integer>  list = new ArrayList<Integer>(  );
        for ( int i = 0; i < shellList.size(); i++ ) {
            if ( Math.sqrt( ( shellList.get( i ).getX() - bo.getX() ) *  shellList.get( i ).getX() - bo.getX() +
                            ( shellList.get( i ).getY() - bo.getY() ) *  shellList.get( i ).getY() - bo.getY() )
                            <= bo.getRadius() + shellList.get( i ).getRadius() ) {
                shellList.remove( i );
                list.add( i );
            }
        }
        return list.toArray();
    }

    public void removeShell( int index ) {
        shellList.remove( index );
    }
}
