import org.newdawn.slick.Animation;

/**
 * Created with IntelliJ IDEA.
 * User: Vlad
 * Date: 11.11.13
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
public class AnimationData {
    public float x;
    public float y;
    public Animation animation;
    public int duration;

    public AnimationData( float x, float y, Animation animation, int duration ) {
        this.x = x;
        this.y = y;
        this.animation = animation;
        this.duration = duration;
    }

    public boolean update() {
        return --duration < 0;
    }
}
