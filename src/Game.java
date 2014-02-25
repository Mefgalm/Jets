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
    public static Image shellImage;
    public static Image enemyShellImage;

    private Image shipImage;
    private int shipNumber;
    private int HP = 100;
    private Image hpBarImage;
    public static WorldMap map;

    private Server server;

    private int timeBetweenShoot = 0;
    private List<String> stringList = Collections.synchronizedList( new ArrayList<String>(  ) );
    private List<Coordinates> coordinatesSet = new LinkedList<Coordinates>(  );
    private List<Integer> hpList = new ArrayList<Integer>(  );
    private Map<Integer, Player> playerMap = new HashMap<Integer, Player>(  );

    private Map<Integer, Skill> skillMap = new HashMap<Integer, Skill>();

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
        System.out.println( "Init game" );
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
            server.streamOut.writeUTF( Code.ENTER_NEW_USER + ";500.0;500.0;0;" + Login.nickname + ";100" );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        shipImage = new Image( "ship.png" );
        hpBarImage = new Image( "hpBar.png" );
        map = new WorldMap( "map.jpg", gameContainer.getHeight(), gameContainer.getWidth() );
        ship = new Ship( 0.05f, 6, 3f, 500, 500, "das", shipImage.copy(), gameContainer.getHeight(), gameContainer.getWidth(), map );
        shellImage = new Image( "shellImg/1.png" );
        enemyShellImage = new Image ( "shellImg/2.png" );
        enemyShellContainer = new ShellContainer( map );
        shellContainer = new ShellContainer( map );
        skillMap.put( Input.KEY_Q,  new SpeedFIreSkill() );

        //load skills

        new Thread( new ReceiveData() ).start();
    }

    private void drawShips() {
        for ( Map.Entry< Integer,Player> entry : playerMap.entrySet() ) {
            entry.getValue().draw( ship, map );
        }
    }

    private void updatePlayersShell() {
        for ( Map.Entry<Integer,Player> entry : playerMap.entrySet() ) {
            entry.getValue().updateShells();
        }
    }

    private void collideShip() {
        for ( Map.Entry<Integer,Player> entry : playerMap.entrySet() ) {
            for ( Map.Entry<Integer,Player> entry2 : playerMap.entrySet() ) {
                if ( !entry.getKey().equals( entry2.getKey() ) ) {
                    entry.getValue().collide( entry2.getValue().getX(), entry2.getValue().getY() );
                }
            }
            shellContainer.isCollide( entry.getValue().getX(), entry.getValue().getY() );
            HP -= entry.getValue().collide( ship.getX(), ship.getY() );
        }

    }

    private void updateSkill( Graphics g ) {
        for ( Map.Entry<Integer,Skill> entry : skillMap.entrySet() ) {
            entry.getValue().decrement();
            g.drawString( String.valueOf( entry.getValue().getCd() ), 650, 600 );
        }
    }

    @Override
    public void render ( GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics ) throws SlickException {
        ship.draw( graphics );
        updateSkill( graphics );
        try {
            Thread.sleep( 5 );
        } catch ( InterruptedException ex ) {
            System.out.print( "!!!" );
        }
        drawShips();

        calculate();
        stringList.clear();
        updatePlayersShell();
        shellContainer.updateShells();
        if ( HP <= 0 ) {
            ship.setX( -500 );
            ship.setY( -500 );
            DeathScreen.setShip( ship );
            server.sendData( Code.SEND_COORDINATES, ship.getX(), ship.getY(), ship.getCurrentAngle() );
            Main.app.setDisplayMode( 200, 150, false );
            gameContainer.setMouseGrabbed(false);
            HP = 100;
            stateBasedGame.enterState( Main.deathScreen );
        }
        graphics.drawString( String.valueOf( HP ), 0, 0 );
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
            currentKey.clear();
            ship.setSpeed( 2 );   //сбросить скорость
        } else {
            ship.changeSpeed();   //прибавление к скорости ускорения
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
        /*
        if ( input.isKeyDown( Input.KEY_K ) ) {
            ship.setX( -500 );
            ship.setY( -500 );
            DeathScreen.setShip( ship );
            server.sendData( Code.SEND_COORDINATES, ship.getX(), ship.getY(), ship.getCurrentAngle() );
            Main.app.setDisplayMode( 200, 150, false );
            gameContainer.setMouseGrabbed(false);
            stateBasedGame.enterState( Main.deathScreen );
        }*/
        ship.updateAngle( Mouse.getX(), Mouse.getY() );
        server.sendData( Code.SEND_COORDINATES, ship.getX(), ship.getY(), ship.getCurrentAngle() );

        if ( timeBetweenShoot > 0 ) {
            timeBetweenShoot--;
        }
        if ( timeBetweenShoot == 0 ) {
            for ( Map.Entry<Integer,Skill> entry : skillMap.entrySet() ) {
                if ( input.isKeyDown( entry.getKey() ) && entry.getValue().getCd() == 0 ) {
                    entry.getValue().use( shellContainer, server, ship );
                    entry.getValue().setCd( 30 );
                    timeBetweenShoot = 6;
                    break;
                }
            }
        }
        if ( timeBetweenShoot == 0 && input.isMouseButtonDown( 0 ) ) {
            timeBetweenShoot = 6;
            Shell shell = new Shell( ship.getCurrentAngle(),   //Shell ( float currentAngle, float radius, float x, float y, int timeToDestroy, float speed, Image image )
                    20, 10,
                    ship.getX(), ship.getY(),
                    40, 14.0f,
                    shellImage.copy() );
            shellContainer.add( shell );
            server.sendShell( Code.SEND_SHELL, shell.toString() );
        }
        collideShip();
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
                              shipImage.copy(),
                              Integer.parseInt( splitLine[6]) ) );   //HP )) )
            }
            if ( splitLine[0].equals( Code.SEND_COORDINATES ) && playerMap.get( Integer.parseInt( splitLine[1] ) ) != null ) {
                playerMap.get( Integer.parseInt( splitLine[1] ) ).changeCoordinates( Float.parseFloat( splitLine[2] ),
                        Float.parseFloat( splitLine[3] ),Float.parseFloat( splitLine[4] ));
            }
            if ( splitLine[0].equals( Code.EXIT_USER ) && playerMap.get( Integer.parseInt( splitLine[1] ) ) != null  ) {
                playerMap.remove( Integer.parseInt( splitLine[1] ) );
            }
            if ( splitLine[0].equals( Code.SEND_SHELL ) && playerMap.get( Integer.parseInt( splitLine[8] ) ) != null ) {
                playerMap.get( Integer.parseInt( splitLine[8] ) ).addShell( new Shell( Integer.parseInt( splitLine[1] ),
                                Float.parseFloat( splitLine[2] ),
                                Float.parseFloat( splitLine[3] ),
                                Float.parseFloat( splitLine[4] ),
                                Float.parseFloat( splitLine[5] ),
                                Integer.parseInt( splitLine[6] ),
                                enemyShellImage.copy(),
                                Float.parseFloat( splitLine[7] ) ) );
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
