package game.objects;

import game.drawable.Animator;

public class Attachment {
	public Animator animator;
	private DeviceObject attached;
	private boolean isDead = false;
	
	/**
	 * Constructs a attachment object which is a link between a drawable sprite and an object.
	 * 
	 * @param attached the object this is attached to.
	 * @param animator the image and the animations associated with it.
	 */
	public Attachment(DeviceObject attached, Animator animator) {
		this.animator = animator;
		this.attached = attached;
	}//END Attachment
	
	/**
	 * Returns whether or not the object this is attached to is dead.
	 * 
	 * @return whether or not the object this is attached to is dead.
	 */
	public boolean isDead() {
		return isDead || attached.isDead();
	}//END isDead
	
	/**
	 * Sets the attachment to being dead and to be deleted.
	 */
	public void kill() {
		this.animator.get_drawable().kill();
		this.isDead = true;
	}//END kill
	
	/**
	 * Updates the attachment.
	 * 
	 * @param dt the time that has pass between the last update.
	 */
	public void update(float dt) {
		this.animator.update(dt);
	}//END update
}//END class Attachment
