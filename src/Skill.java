/**
 * Created with IntelliJ IDEA.
 * User: vladyslav
 * Date: 25.02.14
 * Time: 9:45
 * To change this template use File | Settings | File Templates.
 */
public abstract class Skill {
    private int FullCd = 0;
    private int cd = 0;

    public Skill ( int FullCd )  {
        this.FullCd = FullCd;
    }

    public void setFullCd() {
        cd = FullCd;
    }

    public int getCd() {
        return cd;
    }

    public void decrement() {
        if ( cd > 0 ) {
            cd--;
        }
    }

    public abstract void use( ShellContainer shellContainer, Server server, Ship ship );
}
