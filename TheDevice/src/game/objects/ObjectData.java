package game.objects;

import java.util.HashMap;

import game.objects.behavior.Behavior;

public class ObjectData {	
	/* Collision */
	public boolean collision_solid;
	public float[] collision_hit = {
			0, //Hitbox width
			0, //Hitbox height
			0, //Hitbox alignmentX %
			0, //Hitbox alignmentY %
			0, //Hitbox offsetX
			0, //Hitbox offsetY
	};
	
	/* Behavior */
	public String identity;
	public boolean behavior_controllable;
	public Behavior behavior;
	
	/* Drawable */
	public String base_drawable;
	public String animation_id;
	public boolean drawable_scales;
	public float[] draw_dst = {
		0, //Draw width
		0, //Draw height
		0, //Draw alignmentX %
		0, //Draw alignmentY %
		0, //Draw offsetX
		0, //Draw offsetY
	};
	public boolean has_shadow;
	public float shadowAdjustY;
	
	/* Touch */
	public boolean touchable;
	public float[] touch = {
			0, //Touch width
			0, //Touch height
			0, //Touch alignmentX %
			0, //Touch alignmentY %
			0, //Touch offsetX
			0, //Touch offsetY
	};
	
	/* Properties */
	public float friction;
	public float mass;
	public HashMap<String, Float> attributes = new HashMap<String, Float>();
}//END ObjectData
