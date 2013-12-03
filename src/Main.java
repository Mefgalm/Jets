import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Main extends StateBasedGame {
    public final static int game = 0;
    public final static int login = 1;
    public static AppGameContainer app;

    public Main ( String name ) {
        super( name );
        Server server = new Server();
        addState( new Login( login, server ) );
        addState( new Game( game, server ) );
    }

    public static void main(String [] arguments) {
        try {
            app = new AppGameContainer( new Main( "Jets" ) );
            app.setDisplayMode( 900, 900, false );
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