import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;

public class Main extends StateBasedGame {
    public final static int game = 0;
    public final static int login = 1;
    public final static int deathScreen = 2;

    public final static int width = 700;
    public final static int height = 700;
    public static AppGameContainer app;

    public Main ( String name ) {
        super( name );
        Server server = null;
        try {
            server = new Server();
        } catch ( IOException ex ) {
            System.out.println( "Server is down" );
            System.exit( 0 );
        }
        addState( new Login( login, server ) );
        addState( new Game( game, server ) );
        addState( new DeathScreen( deathScreen ) );
    }

    public static void main(String [] arguments) {
        try {
            app = new AppGameContainer( new Main( "Jets" ) );
            app.setDisplayMode( Main.width, Main.height, false );
            app.setTargetFrameRate( 60 );
            app.setVSync( true );
            app.setShowFPS( true );
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initStatesList ( GameContainer gameContainer ) throws SlickException {
        this.enterState( login );
    }
}