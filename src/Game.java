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

    private List<Integer> keyPressList = new ArrayList<Integer>(  );
    private List<Integer> keyPressedList = new ArrayList<Integer>(  );
    private List<Integer> currentKey = new ArrayList<Integer>(  );

    private Ship ship;
    private ShellContainer shellContainer;
    private ShellContainer enemyShellContainer;
    private Image shellImage;
    private Image enemyShellImage;

    private Image shipImage;
    private int shipNumber;
    private int HP = 100;
    private Image hpBarImage;
    private Map map;

    private Server server;

    private int timeBetweenShoot = 0;
    private List<String> stringList = Collections.synchronizedList( new ArrayList<String>(  ) );
    private List<Coordinates> coordinatesSet = new LinkedList<Coordinates>(  );
    private List<Integer> hpList = new ArrayList<Integer>(  );
    private java.util.Map<Integer, Player> playerMap = new HashMap<Integer, Player>(  );

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

        try {
            server.streamOut.writeUTF( Code.ENTER_NEW_USER + ";500.0;500.0;0;Mef;100" );
        } catch ( IOException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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

    private void drawShips() {
        Image tempImg = shipImage.copy();
        for (java.util.Map.Entry<Integer,Player> entry : playerMap.entrySet()) {
            tempImg.setRotation( (float) Math.toDegrees( -entry.getValue().getAgnel() ) );
            float dX = -tempImg.getCenterOfRotationX(), dY = -tempImg.getCenterOfRotationY();
            if ( ship.getX() < ship.getHalfWidth() ) {
                dX += entry.getValue().getX();
            } else if ( ship.getX() >= map.getMapWidth() - ship.getHalfWidth() ) {
                dX += entry.getValue().getX() - map.getMapWidth() + ship.getWidth();
            } else {
                dX += entry.getValue().getX() - ship.getShiftX();
            }
            if ( ship.getY() < ship.getHalfHeight() ) {
                dY += entry.getValue().getY();
            } else if ( ship.getY() >= map.getMapHeight() - ship.getHalfHeight() ) {
                dY += entry.getValue().getY() + ship.getHeight() - map.getMapHeight();
            } else {
                dY += entry.getValue().getY() - ship.getShiftY();
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
        //getShipsPosition();
        calculate();
        drawShips();
        stringList.clear();
        shellContainer.updateShells(  );
        enemyShellContainer.updateShells(  );
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
        }
        ship.updateAngle( Mouse.getX(), Mouse.getY() );
        server.sendData( Code.SEND_COORDINATES, ship.getX(), ship.getY(), ship.getCurrentAngle() );

        /*if ( timeBetweenShoot > 0 ) {
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
        }*/

        edit( keyPressedList, input );
    }

    private void calculate() {
        for ( int i = 0; i < stringList.size(); i++ ) {
            String[] splitLine = stringList.get( i ).split( ";" );
            if ( splitLine[0].equals( Code.ENTER_NEW_USER ) ) {
                playerMap.put( Integer.parseInt( splitLine[1] ),
                       new Player( Float.parseFloat( splitLine[2] ), //x
                              Float.parseFloat( splitLine[3] ),      //y
                              Float.parseFloat( splitLine[4] ),      //angle
                              splitLine[5],                          //Name
                              Integer.parseInt( splitLine[6]) ) );   //HP )) )
            }
            if ( splitLine[0].equals( Code.SEND_COORDINATES ) && playerMap.get( Integer.parseInt( splitLine[1] ) ) != null ) {
                playerMap.get( Integer.parseInt( splitLine[1] ) ).changeCoordinates( Float.parseFloat( splitLine[2] ),
                        Float.parseFloat( splitLine[3] ),Float.parseFloat( splitLine[4] ));
            }
            if ( splitLine[0].equals( Code.EXIT_USER) ) {
                playerMap.remove( Integer.parseInt( splitLine[1] ) );
            }
        }
    }

    class ReceiveData extends Thread {
        @Override
        public void run() {
            boolean use = true;
            while ( use ) {
                try {
                    stringList.add( server.streamIn.readUTF() );
                } catch ( IOException ioEx ) {
                    use = false;
                    ioEx.printStackTrace();
                }
            }
        }
    }
}
