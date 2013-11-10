import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vlad
 * Date: 12.10.13
 * Time: 13:30
 * To change this template use File | Settings | File Templates.
 */
public class Game extends BasicGameState {
    private int ID;

    private List<Integer> keyPressList = new ArrayList<Integer>( 4 );
    private List<Integer> keyPressedList = new ArrayList<Integer>( 4 );
    private List<Integer> currentKey = new ArrayList<Integer>( 4 );

    private Ship ship;
    private ShellContainer shellContainer;
    private Image shellImage;
    private Map map;

    private Image shipImage;
    private int number;

    private Server server;

    private int timeBetweenShoot = 0;
    private List<String> stringList = Collections.synchronizedList( new ArrayList<String>(  ) );
    private Set<Coordinates> coordinatesSet = new LinkedHashSet<Coordinates>(  );
    private Boolean thread = false;

    public Game ( int ID ) {
        this.ID = ID;

        server = new Server();
        shellContainer = new ShellContainer();
    }

    @Override
    public int getID () {
        return ID;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void init ( GameContainer gameContainer, StateBasedGame stateBasedGame ) throws SlickException {
        shipImage = new Image( "ship.png" );
        map = new Map( "map.jpg", gameContainer.getHeight(), gameContainer.getWidth() );
        ship = new Ship( 5.0f, 3.0f, shipImage, gameContainer.getHeight(), gameContainer.getWidth(), map );
        shellImage = new Image( "laser.png" );
        new Thread( new ReceiveData() ).start();
    }

    private void getShipsPosition() {
        for ( int i = 0; i < stringList.size(); i++ ) {
            String[] splitLine = stringList.get( i ).split( ";" );
            int num = Integer.parseInt( splitLine[0] );
            if ( num == number ) {
                continue;
            }
            if ( splitLine[1].equals( Code.SEND_COORDINATES ) ) {
                Coordinates coordinates = new Coordinates();
                coordinates.number = num;
                coordinates.x = Float.parseFloat( splitLine[2] );
                coordinates.y = Float.parseFloat( splitLine[3] );
                coordinates.angle = Float.parseFloat(splitLine[4]);
                if ( !coordinatesSet.add( coordinates ) ) {
                    coordinatesSet.remove( new Coordinates( num, 0, 0, 0 ) );
                }
                coordinatesSet.add( coordinates );
            }
            if ( splitLine[1].equals( Code.SEND_SHELL ) ) {
                //Shell ( float currentAngle, float radius , float x, float y, int timeToDestroy, float speed, Image image )
                //System.out.println( stringList.toString() );
                shellContainer.add( new Shell( Float.parseFloat( splitLine[2] ), Float.parseFloat( splitLine[3] ),
                                               Float.parseFloat( splitLine[4] ), Float.parseFloat( splitLine[5] ),
                                               Integer.parseInt( splitLine[6] ), Float.parseFloat( splitLine[7] ), shellImage.copy() ) );
                //System.out.println(  System.currentTimeMillis() - Long.parseLong( splitLine[8] ) );
            }
        }
    }

    private void drawShips() {
        Image tempImg = shipImage.copy();
        for ( Coordinates coordinates : coordinatesSet ) {
            tempImg.setRotation( (float) Math.toDegrees( -coordinates.angle ) );
            float dX, dY;
            if ( ship.getX() < ship.getHalfWidth() ) {
                dX = coordinates.x - tempImg.getCenterOfRotationX();
            } else if ( ship.getX() >= map.getMapWidth() - ship.getHalfWidth() ) {
                dX = coordinates.x - map.getMapWidth() + ship.getWidth() - tempImg.getCenterOfRotationX();
            } else {
                dX = coordinates.x - ship.getShiftX() - tempImg.getCenterOfRotationX();
            }
            if ( ship.getY() < ship.getHalfHeight() ) {
                dY = coordinates.y - tempImg.getCenterOfRotationY();
            } else if ( ship.getY() >= map.getMapHeight() - ship.getHalfHeight() ) {
                dY = coordinates.y + ship.getHeight() - map.getMapHeight() - tempImg.getCenterOfRotationY();
            } else {
                dY = coordinates.y - ship.getShiftY() - tempImg.getCenterOfRotationY();
            }
            tempImg.draw( dX, dY );
        }
    }

    private boolean isCode( String line ) {
        String[] splitLine = line.split( ";" );
        if ( splitLine[0].equals( Code.SET_ID ) ) {
            number = Integer.parseInt( String.valueOf( splitLine[1] ) );
            return true;
        }
        return false;
    }

    @Override
    public void render ( GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics ) throws SlickException {
        ship.draw();
        try {
            Thread.sleep( 3 );
        } catch ( InterruptedException ex ) {
            System.out.print( "!!!" );
        }
        getShipsPosition();
        drawShips();
        stringList.clear();
        shellContainer.updateShells( map.getShiftX(), map.getShiftY() );
    }

    public void edit( List<Integer> list, Input input ) {
        list.removeAll( list );
        if (input.isKeyDown( Input.KEY_A ) ) {
            list.add( Input.KEY_A );
        }
        if ( input.isKeyDown( Input.KEY_S ) ) {
            list.add( Input.KEY_S );
        }
        if ( input.isKeyDown( Input.KEY_W ) ) {
            list.add( Input.KEY_W );
        }
        if ( input.isKeyDown( Input.KEY_D ) ) {
            list.add( Input.KEY_D );
        }
    }


    @Override
    public void update ( GameContainer gameContainer, StateBasedGame stateBasedGame, int i ) throws SlickException {
        Input input = gameContainer.getInput();
        edit( keyPressList, input );

        if ( keyPressList.isEmpty() ) {
            currentKey.removeAll( currentKey );
        } else {
            if ( keyPressList.size() > keyPressedList.size() ) {
                for ( Integer keyPress : keyPressList ) {
                    if ( !keyPressedList.contains( keyPress ) ) {
                        currentKey.add( keyPress );
                    }
                }
            } else if ( keyPressList.size() < keyPressedList.size() ) {
                for ( Integer keyPressed : keyPressedList ) {
                    if ( !keyPressList.contains( keyPressed ) ) {
                        currentKey.remove( keyPressed );
                    }
                }
            }
            if ( currentKey.get( currentKey.size() - 1 ) == Input.KEY_A ) {
                ship.moveLeft();
            }
            if ( currentKey.get( currentKey.size() - 1 ) == Input.KEY_S ) {
                ship.moveBack();
            }
            if ( currentKey.get( currentKey.size() - 1 ) == Input.KEY_W ) {
                ship.moveForward();
            }
            if ( currentKey.get( currentKey.size() - 1 ) == Input.KEY_D ) {
                ship.moveRight();
            }
            server.sendData( Code.SEND_COORDINATES, ship.getX(), ship.getY(), ship.getCurrentAngle() );
        }

        if ( timeBetweenShoot > 0 ) {
            timeBetweenShoot--;
        }
        if ( input.isMouseButtonDown( Input.MOUSE_LEFT_BUTTON ) && timeBetweenShoot == 0 ) {
            timeBetweenShoot = 6;
            //ship.changeRadius();
            Shell shell = new Shell( ship.getCurrentAngle() + ship.getAccuracy(),
                    ship.getRadius(),
                    ship.getX(), ship.getY(),
                    30, 15.0f,
                    shellImage.copy() );
            shellContainer.add( shell );
            server.sendShell( Code.SEND_SHELL, shell.toString() );
        }
        if ( !ship.updateAngle( Mouse.getX(), Mouse.getY() ) && keyPressList.isEmpty() ) {
            server.sendData( Code.SEND_COORDINATES, ship.getX(), ship.getY(), ship.getCurrentAngle() );
        }

        edit( keyPressedList, input );
    }

    class ReceiveData extends Thread {
        @Override
        public void run() {
            while ( true ) {
                try {
                    String line = server.relieveData();
                    if ( !isCode( line ) ) {
                        stringList.add( line );
                    }
                } catch ( IOException ioEx ) {
                    System.err.println( "Dannie ne prinyati" );
                }
            }
        }
    }
}
