package game.drawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Renderer {
	/* Renderers */
	SpriteBatch sprite_renderer = new SpriteBatch(); //Handles Sprites
	ShapeRenderer shape_renderer = new ShapeRenderer(); //Handles Shapes
	
	/* Draw */
	private ArrayList<ArrayList<Drawable>> drawlist = new ArrayList<ArrayList<Drawable>>();
	private float scalar;
	
	/* Constructor and Destruction */
	/**
	 * Creates a object that renders stuff to the screen.
	 * 
	 * @param layers the layers used to organize rendering.
	 * @param scalar what to scale everything drawn so it looks correct.
	 */
	public Renderer(int layers, float scalar) {
		/* Setup Render */
		//Scalar and Layers
		this.scalar = scalar;
		for(int i = 0; i < layers; i++) {
			this.drawlist.add(new ArrayList<Drawable>());
		}//rof
	}//END Renderer
	
	/**
	 * Disposes of the renderer.
	 */
	public void dispose() {
		this.shape_renderer.dispose();
		this.sprite_renderer.dispose();
	}//END dispose
	
	/* Drawable Management */
	private class DrawableComparator implements Comparator<Drawable> {
		@Override
		public int compare(Drawable o1, Drawable o2) {
			if (o1.getZ() <= o2.getZ()) {
				return 1;
			}//fi
			//else...
			return -1;
		}//END
	}//END class DrableComparator
	
	/**
	 * Adds a drawable to the list of things to draw.
	 * 
	 * @param drawable the Drawable object to add.
	 * @param layer the layer to add the Drawable object to.
	 */
	public void add_drawable(Drawable drawable, int layer) {
		this.drawlist.get(layer).add(drawable);
	}//END add_drawable
	
	/**
	 * Removes all drawables of type.
	 * 
	 * @param type the type of the drawable.
	 */
	public void clear_type(String type) {
		/* Iterate through draw list */
		Iterator<ArrayList<Drawable>> iter = this.drawlist.iterator();
		while(iter.hasNext()) {
			ArrayList<Drawable> layer = iter.next();
			/* Iterate through the layer. */
			Iterator<Drawable> jter = layer.iterator();
			while(jter.hasNext()) {
				if(jter.next().getType().equals(type)) {
					jter.remove();
				}//fi
			}//elihw
		}//elihw
	}//END clear_type
	
	/* Draw */
	/**
	 * Draws all the objects to the screen.
	 */
	public void draw() {
		/* Clear Screen */
		Gdx.graphics.getGL20().glClearColor(1,1,1,0);
		Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

		/* Iterate through draw list */
		Iterator<ArrayList<Drawable>> iter = this.drawlist.iterator();
		while(iter.hasNext()) {
			ArrayList<Drawable> layer = iter.next();
			/* Sort the layer. */
			Collections.sort(layer, new DrawableComparator());
			/* Iterate through the layer. */
			Iterator<Drawable> jter = layer.iterator();
			while(jter.hasNext()) {
				Drawable drawable = jter.next();
				if (drawable.isDead()) {
					jter.remove(); //Remove if dead.
				}//fi
				else {
					drawable.draw(this, this.scalar); //Draw the drawable.
				}//else
			}//elihw
		}//elihw
	}//END draw
}//END Renderer