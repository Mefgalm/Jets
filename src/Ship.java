import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;


/**
 * Created with IntelliJ IDEA.
 * User: Vlad
 * Date: 14.10.13
 * Time: 0:16
 * To change this template use File | Settings | File Templates.
 */
public class Ship extends BasicObject {
    private final double CIRCLE_DEGREE = Math.PI * 2;
    private final double ONE_DEGREE = 0.01745329252;
    private final double HALF_PI = Math.PI / 2;

    private double angleSpeed;

    private double finalAngle;

    private int height;     //высода и ширина должны быть кратными 2
    private int width;
    private int halfHeight;
    private int halfWidth;
    private Map map;
    private float rlX, rlY;

    private float accuracy;
    private Addon addon;
    private float maxSpeed;
    private float acceleration;
    private float speed;

    public Ship( float acceleration ,float maxSpeed, float angleSpeed, int x, int y, String name, Image image, int height, int weight, Map map ) throws SlickException {
        super( 0, 0, 50 , x, y, image );
        addon = new Addon( name, image );
        this.maxSpeed = maxSpeed;
        this.acceleration = acceleration;

        this.map = map;
        this.angleSpeed =  angleSpeed * ONE_DEGREE;
        this.width = weight;
        this.height = height;
        halfHeight = height / 2;
        halfWidth = weight / 2;
                     //String mapPath, int screenWidth, int screenHeight
        super.setMapSize( map.getMapHeight(), map.getMapWidth() );

        finalAngle = 0;
        accuracy = 0.051f;
    }

    public void changeSpeed( ) {
        if ( speed + acceleration > maxSpeed ) {
            speed = maxSpeed;
        } else {
            speed += acceleration;
        }
    }

    public void setSpeed( float speed ) {
        this.speed = speed;
    }

    public boolean updateAngle( int mouseX, int mouseY ) {
        finalAngle =  Math.atan( ( ( height - mouseY + map.getShiftY() ) - y ) / ( ( mouseX + map.getShiftX()) - x ) );
        if ( mouseX + map.getShiftX() >= x ) {
            finalAngle = -finalAngle;
            if ( ( height - mouseY + map.getShiftY() ) >= y ) {
                finalAngle = CIRCLE_DEGREE + finalAngle;
            }
        } else {
            finalAngle = Math.PI - finalAngle;
        }

        double deltaAngle = Math.abs( finalAngle - currentAngle );
        if ( deltaAngle > angleSpeed ) {
            if ( deltaAngle < CIRCLE_DEGREE - deltaAngle ) {
                if ( currentAngle < finalAngle ) {
                    currentAngle += angleSpeed;
                } else if ( currentAngle > finalAngle ) {
                    currentAngle -= angleSpeed;
                }
            } else {
                if ( currentAngle < finalAngle ) {
                    if ( currentAngle >= 0 && currentAngle < angleSpeed ) {
                        currentAngle = (float) CIRCLE_DEGREE + currentAngle;
                    }
                    currentAngle -= angleSpeed;
                } else if ( currentAngle > finalAngle ) {
                    if ( currentAngle >= CIRCLE_DEGREE - angleSpeed ) {
                        currentAngle = (float) ( -angleSpeed + ( CIRCLE_DEGREE - currentAngle ) );
                    }
                    currentAngle += angleSpeed;
                }
            }
            return true;
        } else if ( deltaAngle < angleSpeed ) {
            currentAngle = (float) finalAngle;
            return true;
        }
        return false;
    }

    public void moveForward( ) {
        double calCos = Math.cos( currentAngle ) * speed;
        double calSin = Math.sin( currentAngle ) * speed;

        if ( canMove( x + calCos, y - calSin ) ) {
            x += calCos;
            y -= calSin;
        }
    }

    public void setRespawnCoordinates( float x, float y ) {
        this.x = x;
        this.y = y;
    }

    public float getAccuracy () {
        return (float) Math.random() * accuracy * 2 - accuracy;
    }

    public void moveBack( ) {
        double calCos = Math.cos( currentAngle ) * speed;
        double calSin = Math.sin( currentAngle ) * speed;

        if ( canMove( x - calCos, y + calSin ) ) {
            x -= calCos;
            y += calSin;
        }
    }

    public void moveLeft( ) {
        double calCos = Math.cos( currentAngle - HALF_PI ) * speed;
        double calSin = Math.sin( currentAngle - HALF_PI ) * speed;

        if ( canMove( x - calCos, y + calSin ) ) {
            x -= calCos;
            y += calSin;
        }
    }


    public void moveRight( ) {
        double calCos = Math.cos( currentAngle + HALF_PI ) * speed;
        double calSin = Math.sin( currentAngle + HALF_PI ) * speed;

        if ( canMove( x - calCos, y + calSin ) ) {
            x -= calCos;
            y += calSin;
        }
    }

    public void draw( Graphics g ) {
        map.draw( x, y );
            if ( map.getShiftX() > 0 ) {
                rlX = halfWidth;
                if ( map.getShiftX() == map.getMapWidth() - width) {
                    rlX = x - ( map.getMapWidth() - width);
                }
            } else {
                rlX = x;
            }
            if ( map.getShiftY() > 0 ) {
                rlY = halfHeight;
                if ( map.getShiftY() == map.getMapHeight() - height ) {
                    rlY = y - ( map.getMapHeight() - height );
                }
            } else {
                rlY = y;
            }
        addon.draw( rlX, rlY, g );
        image.setRotation( (float) Math.toDegrees( -currentAngle ) );
        image.draw( rlX - image.getCenterOfRotationX(), rlY - image.getCenterOfRotationY() );
    }

    public float getShiftX() {
        return x - halfWidth;
    }

    public float getShiftY() {
        return y - halfHeight;
    }

    public int getHalfHeight() {
        return halfHeight;
    }

    public int getHalfWidth() {
        return halfWidth;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public float getRlX() {
        return rlX;
    }

    public float getRlY() {
        return rlY;
    }

    public void setX( float x ) {
        this.x = x;
    }

    public void setY( float y )  {
        this.y = y;
    }
}
