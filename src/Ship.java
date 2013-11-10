import org.newdawn.slick.*;

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

    private float accuracy;

    public Ship( float speed, float angleSpeed, Image image, int height, int weight, Map map ) throws SlickException {
        super( speed, 0, weight / 2, height / 2, image );

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
        } else if ( deltaAngle < angleSpeed ) {
            currentAngle = (float) finalAngle;
        }
        return currentAngle == finalAngle;
    }

    public void moveForward( ) {
        double calCos = Math.cos( currentAngle ) * speed;
        double calSin = Math.sin( currentAngle ) * speed;

        if ( canMove( x + calCos, y - calSin ) ) {
            x += calCos;
            y -= calSin;
        }
    }

    public float getAccuracy () {
        return (float) Math.random() * accuracy * 2 - accuracy;
    }

    public void moveBack( ) {
        double calCos = Math.cos( currentAngle ) * speed / 2;
        double calSin = Math.sin( currentAngle ) * speed / 2;

        if ( canMove( x - calCos, y + calSin ) ) {
            x -= calCos;
            y += calSin;
        }
    }

    public void moveLeft( ) {
        double calCos = Math.cos( currentAngle - HALF_PI ) * speed * 0.8;
        double calSin = Math.sin( currentAngle - HALF_PI ) * speed * 0.8;

        if ( canMove( x - calCos, y + calSin ) ) {
            x -= calCos;
            y += calSin;
        }
    }


    public void moveRight( ) {
        double calCos = Math.cos( currentAngle + HALF_PI ) * speed * 0.8;
        double calSin = Math.sin( currentAngle + HALF_PI ) * speed * 0.8;

        if ( canMove( x - calCos, y + calSin ) ) {
            x -= calCos;
            y += calSin;
        }
    }

    public void draw() {
        map.draw( x, y );
        float dX, dY;
            if ( map.getShiftX() > 0 ) {
                dX = halfWidth;
                if ( map.getShiftX() == map.getMapWidth() - width) {
                    dX = x - ( map.getMapWidth() - width);
                }
            } else {
                dX = x;
            }
            if ( map.getShiftY() > 0 ) {
                dY = halfHeight;
                if ( map.getShiftY() == map.getMapHeight() - height ) {
                    dY = y - ( map.getMapHeight() - height );
                }
            } else {
                dY = y;
            }
        image.setRotation( (float) Math.toDegrees( -currentAngle ) );
        image.draw( dX - image.getCenterOfRotationX(), dY - image.getCenterOfRotationY() );
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
}
