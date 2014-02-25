import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created with IntelliJ IDEA.
 * User: vladyslav
 * Date: 24.02.14
 * Time: 10:33
 * To change this template use File | Settings | File Templates.
 */
public class DeathScreen extends BasicGameState {
    private Image launchImage;
    private static Ship ship;
    private int ID;

    public DeathScreen( int ID ) {
        this.ID = ID;
    }

    public static void setShip( Ship ship ) {
        DeathScreen.ship = ship;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        launchImage = new Image( "button.jpg" );
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        launchImage.draw( 50, 95 );
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        Input input = gameContainer.getInput();
        if ( input.getMouseX() > 50 && input.getMouseX() < 150 && input.getMouseY() > 95 && input.getMouseY() < 115 && input.isMouseButtonDown( 0 ) ) {

            Main.app.setDisplayMode( Main.width, Main.height, false );
            ship.setRespawnCoordinates( 500, 500 );
            gameContainer.setMouseGrabbed( true );
            stateBasedGame.enterState( Main.game );
        }
    }
}
