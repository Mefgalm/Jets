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
    private Server server;
    private int shipNumber;

    public ShellContainer( Map map, Server server, int shipNumber ) {
        this.server = server;
        this.map = map;
        this.shipNumber = shipNumber;
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

    public boolean isCollide ( Ship ship ) {
        boolean numberOfCollide = false;
        for ( int i = 0; i < shellList.size() && shellList.get( i ) != null; i++ ) {
            if ( shellList.get( i ).getRadius() + ship.getRadius()
                    > Math.sqrt( Math.pow( ship.getX() - shellList.get( i ).getX(), 2 ) + Math.pow( ship.getY() - shellList.get( i ).getY(), 2 ) ) ) {
                animationContainer.add( shellList.get( i ).getX() - map.getShiftX(), shellList.get( i ).getY() - map.getShiftY(), 12 );
                server.sendShell( Code.DELETE_SHELL, String.valueOf( shipNumber ) + ";" + String.valueOf( shellList.get( i ).getNumber() ) );
                shellList.remove( shellList.get( i ) );
                numberOfCollide = true;
            }
        }
        return numberOfCollide;
    }

    public void remove ( int shipNumber, int shellNumber ) {
        for ( int i = 0; i < shellList.size(); i++ ) {
            if ( shellList.get( i ).getNumber() == shellNumber) {
                shellList.remove( shellList.get( i ) );
                animationContainer.add( shellList.get( i ).getX() - map.getShiftX(), shellList.get( i ).getY() - map.getShiftY(), 12 );
            }
        }
    }
}
