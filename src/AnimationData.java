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
    public int frameNumber;

    public AnimationData( float x, float y, Animation animation, int duration ) {
        this.x = x;
        this.y = y;
        this.animation = animation;
        this.duration = duration;
        frameNumber = duration;
    }

    public boolean update() {
        animation.setCurrentFrame( frameNumber - duration );
        return --duration < 0;
    }
}
