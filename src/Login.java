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
    private TextField passwordTextField;
    private Image backgroundImage;
    private Image launchImage;

    public Login ( int ID, Server server ) {
        System.out.println( "Login constructor" );
        this.server = server;
        this.ID = ID;
    }
    @Override
    public int getID () {
        return ID;
    }

    @Override
    public void init ( GameContainer gameContainer, StateBasedGame stateBasedGame ) throws SlickException {
        System.out.println( "Init Login" );
        Main.app.setDisplayMode( 200, 150, false );
        backgroundImage = new Image( "enterForm.jpg" );
        launchImage = new Image( "button.jpg" );
        nameTextField = new TextField(gameContainer, new TrueTypeFont(new Font(Font.SERIF,Font.BOLD, 14),false), 10, 50, 180, 25);
        passwordTextField = new TextField(gameContainer, new TrueTypeFont(new Font(Font.SERIF,Font.BOLD, 14),false), 10, 80, 180, 25);
    }

    @Override
    public void render ( GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics ) throws SlickException {
        backgroundImage.draw( );
        launchImage.draw( 50, 120 );
        nameTextField.render( gameContainer, graphics );
        passwordTextField.render( gameContainer, graphics );
    }

    @Override
    public void update ( GameContainer gameContainer, StateBasedGame stateBasedGame, int i ) throws SlickException {
        Input input = gameContainer.getInput();
        if ( input.getMouseX() > 50 && input.getMouseX() < 150 && input.getMouseY() > 120 && input.getMouseY() < 140 && input.isMouseButtonDown( 0 ) ) {

            Main.app.setDisplayMode( Main.width, Main.height, false );

            Main.gameState.initia( gameContainer, nameTextField.getText() );
            stateBasedGame.enterState( Main.game );
        }
    }
}
