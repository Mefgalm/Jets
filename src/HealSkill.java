/**
 * Created with IntelliJ IDEA.
 * User: Vlad
 * Date: 01.03.14
 * Time: 17:25
 * To change this template use File | Settings | File Templates.
 */
public class HealSkill extends Skill {
    public HealSkill ( int FullCd ) {
        super( FullCd );
    }

    @Override
    public void use ( ShellContainer shellContainer, Server server, Ship ship ) {
        Laser laser = new Laser( ship.getX(), ship.getY(), ship.getCurrentAngle(), Game.laserImage );
        shellContainer.addLaser( laser );
        server.sendShell( Code.SEND_LASER, laser.toString() );
    }
}
