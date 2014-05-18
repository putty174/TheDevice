package game.drawable;

import game.drawable.infomation.PositionObject;


public abstract class Drawable_Position extends Drawable {
	protected PositionObject position;
	private boolean isDead = false;
	
	/**
	 * Creates a drawable object with a position to draw to.
	 * 
	 * @param position an object that determines where to draw.
	 */
	public Drawable_Position(PositionObject position) {
		this.position = position;
	}//END Drawable_Position
	
	/* Z */
	@Override
	public float getZ() {
		return this.position.getZ() + this.z_offset;
	}//END getZ
	
	/* Destruction */
	@Override
	public boolean isDead() {
		return this.isDead || this.position.isDead();
	}//END isDead
	
	/**
	 * Force kills the drawable.
	 */
	public void kill() {
		this.isDead = true;
	}//END kill
}//END class Drawable_Position
