import org.newdawn.slick.Image;

/**
 * Created with IntelliJ IDEA.
 * User: Vlad
 * Date: 21.01.14
 * Time: 17:20
 * To change this template use File | Settings | File Templates.
 */
public class Player {
    private float x;
    private float y;
    private float angle;
    private String nickname;
    private int hp;
    private ShellContainer shellContainer = new ShellContainer( Game.map );
    private Image image;


    public Player ( float x, float y, float angle, String nickname, Image image, int hp ) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.nickname = nickname;
        this.image = image;
        this.hp = hp;
    }

    public void draw( Ship ship, WorldMap map ) {
        image.setRotation( (float) Math.toDegrees( -angle ) );
        float dX = -image.getCenterOfRotationX(), dY = -image.getCenterOfRotationY();
        if ( ship.getX() < ship.getHalfWidth() ) {
            dX += x;
        } else if ( ship.getX() >= map.getMapWidth() - ship.getHalfWidth() ) {
            dX += x - map.getMapWidth() + ship.getWidth();
        } else {
            dX += x - ship.getShiftX();
        }
        if ( ship.getY() < ship.getHalfHeight() ) {
            dY += y;
        } else if ( ship.getY() >= map.getMapHeight() - ship.getHalfHeight() ) {
            dY += y + ship.getHeight() - map.getMapHeight();
        } else {
            dY += y - ship.getShiftY();
        }
        image.draw( dX, dY );
    }

    public void changeCoordinates( float x, float y, float angle ) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getAgnel() {
        return angle;
    }

    public String getNickname() {
        return nickname;
    }

    public int getHp() {
        return hp;
    }

    public void updateShells() {
        shellContainer.updateShells();
    }

    public void addShell( Shell shell ) {
        shellContainer.add( shell );
    }

    public int collide( float x, float y ) {
        return shellContainer.isCollide( x, y );
    }

    public void removeShell( int value ) {
        shellContainer.removeShell( value );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(  );
        sb.append( x ).append( ";" ).
                append( y ).append( ";" ).
                append( angle ).append( ";" ).
                append( nickname ).append( ";" ).
                append( hp );
        return sb.toString();
    }
}
