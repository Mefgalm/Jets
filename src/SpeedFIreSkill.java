/**
 * Created with IntelliJ IDEA.
 * User: vladyslav
 * Date: 25.02.14
 * Time: 9:51
 * To change this template use File | Settings | File Templates.
 */
public class SpeedFIreSkill extends Skill {

    @Override
    public void use( ShellContainer shellContainer, Server server, Ship ship ) {
        Shell shell = new Shell( ship.getCurrentAngle(),   //Shell ( float currentAngle, float radius, float x, float y, int timeToDestroy, float speed, Image image )
                20, 10,
                ship.getX(), ship.getY(),
                40, 20.0f,
                Game.enemyShellImage.copy() );
        shellContainer.add( shell );
        server.sendShell( Code.SEND_SHELL, shell.toString() );
    }
}
