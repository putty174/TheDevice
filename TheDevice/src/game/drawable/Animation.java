package game.drawable;

public class Animation {
	public final int[][] frames; //The frames of the animation ((x1,y1,x2,y2),...). Note using a 1 dimensional array.
	public final int[] animationOrder; //The order to play the animations in.
	
	/* Construction */
	/**
	 * Creates an animation object which stores the animation information for objects.
	 * 
	 * @param frames the frames of the animation.
	 * @param animationOrder the order of the animation.
	 */
	public Animation(int[][] frames, int[] animationOrder) {
		this.frames = frames;
		this.animationOrder = animationOrder;
	}//END Animation
}//END class Animation
