package game.drawable;

public abstract class Drawable {
	private String type = ""; //Used to identify what type of drawable this is.
	private float z; //Used for ordering how to draw.
	protected float z_offset; //Used to offset the ordering a bit.
	
	/* Color */
	protected float
		r = 1,	//The red component value of the drawable (0-1).
		g = 1,	//The green component value of the drawable (0-1).
		b = 1,	//The blue component value of the drawable (0-1).
		a = 1;	//The alpha component value of the drawable (0-1).
	
	/* Constructor */
	/* Z */
	/**
	 * Sets the z value, used for ordering what to draw.
	 * 
	 * @param z a float used for ordering how to draw. 
	 */
	public void setZ(float z) {
		this.z = z;
	}//END setZ
	
	/**
	 * Sets the z offset value, used for ordering what to draw.
	 * 
	 * @param zOff a float used for ordering how to draw. 
	 */
	public void setZOff(float zOff) {
		this.z_offset = zOff;
	}//END setZOff
	
	/* Color */
	/**
	 * Sets the color of the rectangle.
	 * 
	 * @param r the red value (0-1).
	 * @param g the green value (0-1).
	 * @param b the blue value (0-1).
	 * @param a the alpha value (0-1).
	 */
	public void setColor(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}//END setColor
	
	/**
	 * Returns the z value of this object.
	 * 
	 * @return a float representing the z of this object.
	 */
	public float getZ() {
		return this.z + this.z_offset;
	}//END getZ
	
	/* Draw */
	/**
	 * Renders this object into the game.
	 * 
	 * @param renderer the object that render drawable objects to the screen.
	 * @param scalar how much the object is scaled by.
	 */
	public void draw(Renderer renderer, float scalar) {
		//Override this function
	}//END draw
	
	/* Life */
	/**
	 * Whether or not this drawable is dead.
	 * 
	 * @return true if this is dead, false otherwise.
	 */
	public boolean isDead() {
		//Override this function
		return false;
	}//END isDead
	
	/* Type */
	/**
	 * Sets the type of the drawable.
	 * 
	 * @param type a string indicating the type of this drawable.
	 */
	public void setType(String type) {
		this.type = type;
	}//END setType
	
	/**
	 * Returns the type of drawable this is.
	 * 
	 * @return a string representing the drawable type.
	 */
	public String getType() {
		return this.type;
	}//END getType
}//END class
