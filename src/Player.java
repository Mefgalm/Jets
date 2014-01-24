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

    public Player ( float x, float y, float angle, String nickname, int hp ) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.nickname = nickname;
        this.hp = hp;
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

    public Object[] collide( BasicObject bo ) {
        return shellContainer.isCollide( bo );
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
