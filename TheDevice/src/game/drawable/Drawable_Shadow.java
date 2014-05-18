package game.drawable;

import com.badlogic.gdx.math.Vector2;

public class Drawable_Shadow extends Drawable {
	private Drawable_Sprite sprite;
	private float adjY;
	
	/* Construction */
	/**
	 * Constructs a shadow object.
	 * 
	 * @param sprite the sprite the shadow takes from.
	 */
	public Drawable_Shadow(Drawable_Sprite sprite, float adjY) {
		this.sprite = sprite;
		this.adjY = adjY;
	}//END Drawable_Sprite
	
	/* Draw */	
	@Override
	public void draw(Renderer renderer, float scalar) {
		//Destination
		Vector2 p = sprite.position.position_get();
		float adjHeight = sprite.dst_height * 0.3f;
		this.sprite.sprite.setBounds(
			scalar * (p.x - sprite.dst_alignment_x * sprite.dst_width - sprite.dst_offset_x),
			scalar * (p.y - sprite.dst_alignment_y * adjHeight - sprite.dst_offset_y - this.adjY),
			scalar * sprite.dst_width,
			scalar * adjHeight
		);
		//Color
		this.sprite.sprite.setColor(0.f, 0.f, 0.f, 0.5f);
		
		//Draw
		renderer.sprite_renderer.begin();
		this.sprite.sprite.draw(renderer.sprite_renderer);
		renderer.sprite_renderer.end();
	}//END draw
	
	/* Destruction */
	@Override
	public boolean isDead() {
		return this.sprite.isDead();
	}//END isDead
	
	/* Get Z */
	@Override
	public float getZ() {
		return this.sprite.getZ() + this.z_offset;
	}//END getZ
}//END class Drawable_Sprite
