package game.drawable;


import game.drawable.infomation.PositionObject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Drawable_Sprite extends Drawable_Rectangle {	
	/* Source */
	protected Sprite sprite; //The spritemap of the image.
	
	/* Construction */
	/**
	 * Constructs a sprite object.
	 * 
	 * @param spritemap the sprite map image that this object cuts sprites from.
	 */
	public Drawable_Sprite(PositionObject position, Texture spritemap) {
		super(position);
		this.sprite = new Sprite(spritemap);
	}//END Drawable_Sprite
	
	/* Setup */
	//Source
	/**
	 * Sets up the source rectangle (the region cut out of the sprite map) of the sprite.
	 * 
	 * @param x left of the rectangle.
	 * @param y the top of the rectangle.
	 * @param width the width of the rectangle.
	 * @param height the height of the rectangle.
	 */
	public void setSrcRect(int x, int y, int width, int height) {
		this.sprite.setRegion(x, y, width, height);
	}//END setSrc
	
	/* Draw */	
	@Override
	public void draw(Renderer renderer, float scalar) {
		//Destination
		Vector2 p = this.position.position_get();
		this.sprite.setBounds(
			scalar * (p.x - this.dst_alignment_x * this.dst_width - this.dst_offset_x),
			scalar * (p.y - this.dst_alignment_y * this.dst_height - this.dst_offset_y),
			scalar * this.dst_width,
			scalar * this.dst_height
		);
		//Color
		this.sprite.setColor(this.r, this.g, this.b, this.a);
		//Draw
		renderer.sprite_renderer.begin();
		this.sprite.draw(renderer.sprite_renderer);
		renderer.sprite_renderer.end();
	}//END draw
}//END class Drawable_Sprite
