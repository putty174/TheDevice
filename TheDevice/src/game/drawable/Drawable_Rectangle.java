package game.drawable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import game.drawable.infomation.PositionObject;

public class Drawable_Rectangle extends Drawable_Position {
	/* Destination */
	//Draw_Point = dst_point - (alignment * size) - offset
	protected float
		dst_width = 0,			// The width of the dst rectangle.
		dst_height = 0,			// The height of the dst rectangle.
		dst_alignment_x = 0,	// A percent offset x alignment_x * width.
		dst_alignment_y = 0,	// A percent offset y alignment_y * height. 
		dst_offset_x = 0,		// An offset x.
		dst_offset_y = 0;		// An offset y.
	
	/* Construction */
	public Drawable_Rectangle(PositionObject position) {
		super(position);
		// TODO Auto-generated constructor stub
	}//END Drawable_Rectangle
	
	/* Destination Rectangle */
	/**
	 * Sets the dst rectangle's width and height.
	 * Draw_Point = dst_point - (alignment * size) - offset
	 * 
	 * @param width a float representing the width.
	 * @param height a float representing the height.
	 */
	public void setDstSize(float width, float height) {
		this.dst_width = width;
		this.dst_height = height;
	}//END setDstSize
	
	/**
	 * Sets the alignment of this object.
	 * Draw_Point = dst_point - (alignment * size) - offset
	 * 
	 * @param alignmentX a float representing the percentage offset in the x direction.
	 * @param alignmentY a float representing the percentage offset in the y direction.
	 */
	public void setDstAlignment(float alignmentX, float alignmentY) {
		this.dst_alignment_x = alignmentX;
		this.dst_alignment_y = alignmentY;
	}//END setDstAlignment
	
	/**
	 * Sets the offset of where to draw.
	 * Draw_Point = dst_point - (alignment * size) - offset
	 * 
	 * @param offsetX a float representing an x offset.
	 * @param offsetY a float representing a y offset.
	 */
	public void setDstOffset(float offsetX, float offsetY) {
		this.dst_offset_x = offsetX;
		this.dst_offset_y = offsetY;
	}//END setDstOffset
	
	/* Draw */
	@Override
	public void draw(Renderer renderer, float scalar) {
		//Destination
		Vector2 p = this.position.position_get();
		//Color
		renderer.shape_renderer.setColor(this.r, this.g, this.b, this.a);
		//Draw
		renderer.shape_renderer.begin(ShapeType.Filled);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		renderer.shape_renderer.rect(
				scalar * (p.x - this.dst_alignment_x * this.dst_width - this.dst_offset_x),
				scalar * (p.y - this.dst_alignment_y * this.dst_height - this.dst_offset_y),
				scalar * this.dst_width,
				scalar * this.dst_height);
		renderer.shape_renderer.end();
	}//END draw
}//END class Drawable_Rectangle
