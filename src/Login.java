import javafx.application.Application;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import javax.swing.plaf.basic.BasicTextFieldUI;
import java.awt.*;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created with IntelliJ IDEA.
 * User: Vlad
 * Date: 17.11.13
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
public class Login extends BasicGameState {
    private int ID;
    private Server server;
    private TextField nameTextField;
    private Image backgroundImage;
    private Image launchImage;

    public Login ( int ID, Server server ) {
        this.server = server;
        this.ID = ID;
    }
    @Override
    public int getID () {
        return ID;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void init ( GameContainer gameContainer, StateBasedGame stateBasedGame ) throws SlickException {
        Main.app.setDisplayMode( 200, 150, false );
        backgroundImage = new Image( "enterForm.jpg" );
        launchImage = new Image( "button.jpg" );
        nameTextField = new TextField(gameContainer, new TrueTypeFont(new Font(Font.SERIF,Font.BOLD, 14),false), 10, 50, 180, 25);
    }

    @Override
    public void render ( GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics ) throws SlickException {
        backgroundImage.draw( );
        launchImage.draw( 50, 95 );
        nameTextField.render( gameContainer, graphics );
    }

    @Override
    public void update ( GameContainer gameContainer, StateBasedGame stateBasedGame, int i ) throws SlickException {
        Input input = gameContainer.getInput();
        if ( input.getMouseX() > 50 && input.getMouseX() < 150 && input.getMouseY() > 95 && input.getMouseY() < 115 && input.isMouseButtonDown( 0 ) ) {

            Main.app.setDisplayMode( 900, 900, false );
            stateBasedGame.enterState( Main.game );
        }
    }
}
