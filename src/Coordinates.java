/**
 * Created with IntelliJ IDEA.
 * User: Vlad
 * Date: 21.10.13
 * Time: 22:28
 * To change this template use File | Settings | File Templates.
 */
public class Coordinates {
    public String name;
    public int HP;
    public float x;
    public float y;
    public float angle;
    public int shipNum;

    public Coordinates( String name, int HP, float x, float y, float angle, int shipNum ) {
        this.name = name;
        this.HP = HP;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.shipNum = shipNum;
    }

    public Coordinates( int shipNum ) {
        this.shipNum = shipNum;
    }



    public void changeData( float x, float y, float angle ) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    @Override
    public boolean equals( Object o ) {
        return shipNum == ( (Coordinates) o ).shipNum;
    }

    @Override
    public int hashCode() {
        return shipNum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(  );
        sb.append( name ).append( ";" ).append( HP ).append( ";" ).append( x ).append( ";" ).append( y ).append( ";" ).append( angle );
        return sb.toString();
    }
}
