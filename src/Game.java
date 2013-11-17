import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private ShellContainer enemyShellContainer;
    private Image shellImage;
    private Image enemyShellImage;
    private Map map;

    private Image shipImage;
    private int shipNumber;
    private int HP = 100;
    private Image hpBarImage;

    private Server server;

    private int timeBetweenShoot = 0;
    private List<String> stringList = Collections.synchronizedList( new ArrayList<String>(  ) );
    private List<Coordinates> coordinatesSet = new LinkedList<Coordinates>(  );
    private List<Integer> hpList = new ArrayList<Integer>(  );

    public Game ( int ID, Server server ) {
        this.ID = ID;
        this.server = server;
    }

    @Override
    public int getID () {
        return ID;
    }

    @Override
    public void init ( GameContainer gameContainer, StateBasedGame stateBasedGame ) throws SlickException {
        String line;
        StringBuilder sb = new StringBuilder(  );
        try {
            BufferedReader reader = new BufferedReader( new InputStreamReader(new FileInputStream( "name.txt" ) ) );
            while ((line = reader.readLine()) != null) {
                sb.append( line );
            }
        } catch ( Exception e )  {
            System.out.println( "not found" );
        }

        server.sendNewUser( Code.ENTER_NEW_USER, sb.toString(), 100, 500, 500, 0 );
        //server.send( Code.REQUIRE_FOR_NUMBER );
       /* try {
            shipNumber = Integer.parseInt( server.relieveData() );
            System.out.println( shipNumber );
        } catch ( IOException ioeX ) {
            System.out.println( "Can't reacive ship NUmber " );
        } */
        shipImage = new Image( "ship.png" );
        hpBarImage = new Image( "hpBar.png" );
        map = new Map( "map.jpg", gameContainer.getHeight(), gameContainer.getWidth() );
        ship = new Ship( 5.0f, 3.0f, 500, 500, sb.toString(), shipImage, gameContainer.getHeight(), gameContainer.getWidth(), map );
        shellImage = new Image( "shell.png" );
        enemyShellImage = new Image ( "enemyShell.png" );
        enemyShellContainer = new ShellContainer( map, server );
        shellContainer = new ShellContainer( map, server );
        new Thread( new ReceiveData() ).start();
    }

    private void getShipsPosition() {
        for ( int i = 0; i < stringList.size(); i++ ) {
            String[] splitLine = stringList.get( i ).split( ";" );
            int num = Integer.parseInt( splitLine[0] );
            if ( num == shipNumber && !splitLine[1].equals( Code.SEND_ALL_DATE ) ) {
                continue;
            }
            if ( splitLine[1].equals( Code.SEND_COORDINATES ) ) {
                int index = coordinatesSet.indexOf( new Coordinates( Integer.parseInt( splitLine[0] ) ) );
                if ( index != -1 ) {
                    coordinatesSet.get( index ).changeData( Float.parseFloat( splitLine[2] ),
                                                            Float.parseFloat( splitLine[3] ),
                                                            Float.parseFloat( splitLine[4] ) );
                }

            }
            if ( splitLine[1].equals( Code.SEND_SHELL ) ) {
                //Shell ( int shipNumber, int number, float currentAngle, float radius, float x, float y, int timeToDestroy, Image image, float speed )
                enemyShellContainer.add( new Shell( Integer.parseInt( splitLine[0] ),
                                               Integer.parseInt( splitLine[2] ),
                                               Float.parseFloat( splitLine[3] ),
                                               Float.parseFloat( splitLine[4] ),
                                               Float.parseFloat( splitLine[5] ),
                                               Float.parseFloat( splitLine[6] ),
                                               Integer.parseInt( splitLine[7] ),
                                               enemyShellImage.copy(),
                                               Float.parseFloat( splitLine[8] ) ) );
            }
            if ( splitLine[1].equals( Code.DELETE_SHELL) ) {
                System.out.println( stringList.get( i ) );
                enemyShellContainer.remove( Integer.parseInt( splitLine[2] ), Integer.parseInt( splitLine[3] ) );
                shellContainer.remove( Integer.parseInt( splitLine[2] ), Integer.parseInt( splitLine[3] ) );
            }
            if ( splitLine[1].equals( Code.SEND_ALL_DATE ) ) {
                shipNumber = Integer.parseInt( splitLine[0] );
                String[] splitLineSlash = stringList.get( i ).split( "/" );
                if ( splitLine.length < 4 ) return;
                for ( int j = 0; j < splitLineSlash.length; j++ ) {
                    if ( j == 0 ) {
                        //Coordinates( String name, int HP, float x, float y, float angle, int shipNum )
                        coordinatesSet.add( new Coordinates( splitLineSlash[2],
                                            Integer.parseInt( splitLineSlash[3] ),
                                            Float.parseFloat( splitLineSlash[4] ),
                                            Float.parseFloat( splitLineSlash[5] ),
                                            Float.parseFloat( splitLineSlash[6] ),
                                            Integer.parseInt( splitLineSlash[7] )) );
                    } else {
                        coordinatesSet.add( new Coordinates( splitLineSlash[0],
                                            Integer.parseInt( splitLineSlash[1] ),
                                            Float.parseFloat( splitLineSlash[2] ),
                                            Float.parseFloat( splitLineSlash[3] ),
                                            Float.parseFloat( splitLineSlash[4] ),
                                            Integer.parseInt( splitLineSlash[5] )) );
                    }
                }
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

    @Override
    public void render ( GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics ) throws SlickException {
        ship.draw( graphics );
        try {
            Thread.sleep( 5 );
        } catch ( InterruptedException ex ) {
            System.out.print( "!!!" );
        }
        getShipsPosition();
        drawShips();
        stringList.clear();
        shellContainer.updateShells(  );
        enemyShellContainer.updateShells(  );
        if ( enemyShellContainer.isCollide( ship ) ) {
            HP -= 1;
        }
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
    public void update( GameContainer gameContainer, StateBasedGame stateBasedGame, int i ) throws SlickException {
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
            timeBetweenShoot = 4;
            Shell shell = new Shell( shipNumber, ship.getCurrentAngle() + ship.getAccuracy(),
                    5,
                    ship.getX(), ship.getY(),
                    50, 14.0f,
                    shellImage.copy() );
            shellContainer.add( shell );
            server.sendShell( Code.SEND_SHELL, shell.toString() );
        }

        if ( ship.updateAngle( Mouse.getX(), Mouse.getY() ) && keyPressList.isEmpty() ) {
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
                    stringList.add( line );
                } catch ( IOException ioEx ) {
                    System.err.println( "Dannie ne prinyati" );
                }
            }
        }
    }
}
