/**
 * Created with IntelliJ IDEA.
 * User: Vlad
 * Date: 01.03.14
 * Time: 15:59
 * To change this template use File | Settings | File Templates.
 */
public class FireSkill extends Skill {

    public FireSkill ( int FullCd ) {
        super( FullCd );
    }

    @Override
    public void use ( ShellContainer shellContainer, Server server, Ship ship ) {
        Shell shell = new Shell( ship.getCurrentAngle(),   //Shell ( float currentAngle, float radius, float x, float y, int timeToDestroy, float speed, Image image )
                20, 10,
                ship.getX(), ship.getY(),
                40, 14.0f,
                Game.shellImage.copy() );
        shellContainer.add( shell );
        server.sendShell( Code.SEND_SHELL, shell.toString() );
    }
}
