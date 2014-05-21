package game.objects;

import game.controls.Controllable;
import game.drawable.Animator;
import game.drawable.Drawable_Rectangle;
import game.drawable.Drawable_Shadow;
import game.drawable.Drawable_Sprite;
import game.drawable.Manager_Animation;
import game.drawable.Manager_Texture;
import game.drawable.Renderer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Room extends Controllable {
	/* Object Management */
	public ArrayList<DeviceObject> objects = new ArrayList<DeviceObject>();
	private HashSet<Spawn_Pair> spawn_list = new HashSet<Spawn_Pair>();
	private ArrayList<DeviceObject> controllable_objects = new ArrayList<DeviceObject>();
	private ArrayList<String> monsterID = new ArrayList<String>();
	
	/* Attachment Management */
	public HashSet<Attachment> attachments = new HashSet<Attachment>();
	public HashSet<Attachment> attachments_spawn = new HashSet<Attachment>();
	
	/* Renderer */
	private Renderer renderer;
	private final static int LAYER_SHADOW = 0;
	private final static int LAYER_OBJECT = 1;
	private final static int LAYER_DEBUG = 2;
	//Debug Display
	private boolean display_hitbox = false;
	private boolean display_touchArea = false;
	
	/* Object Managers */
	private Manager_Object loader;
	private Manager_Texture textures;
	private Manager_Animation animations;
	
	/* Construction */
	/**
	 * Constructs a room object.
	 * 
	 * @param renderer 
	 */
	public Room(Rectangle playble_area, Renderer renderer,
			Manager_Object loader, Manager_Texture textures, Manager_Animation animations) {
		super(playble_area);
		this.renderer = renderer;
		this.loader = loader;
		this.textures = textures;
		this.animations = animations;
		
		//Definitions of monsterIDs to determine which objects receive device as target in update(float)
		monsterID.add("fuzz1");
		monsterID.add("fuzz2");
		monsterID.add("fuzz3");
		monsterID.add("plant1");
		monsterID.add("plant2");
		monsterID.add("plant3");
		
	}//END Room
	
	/* Object Management */
	private class Spawn_Pair {
		public final String name;
		public final Vector2 position;
		
		/**
		 * Creates an object containing the name of the object to be spawned and the position where it is spawned.
		 * 
		 * @param name the name of the object to spawn.
		 * @param position the vector position of where to spawn the specified object.
		 */
		public Spawn_Pair(String name, Vector2 position) {
			this.name = name;
			this.position = position;
		}//END Spawn_Pair
	}//END class Spawn_Pair
	
	/**
	 * Generates an attachment for an object in the room.
	 * 
	 * @param attachment_id the name of the image to use.
	 * @param animation_id the name of the animation for the attachment to use.
	 * @param attached the attached object.
	 * @param w the width of the drawable.
	 * @param h the height of the drawable.
	 * @param aX the alignment X of the drawable.
	 * @param aY the alignment Y of the drawable.
	 * @param oX the offset X of the drawable.
	 * @param oY the offset Y of the drawable.
	 * @return the attachment that is creates.
	 */
	public Attachment spawn_attachment(String attachment_id, String animation_id, DeviceObject attached,
		float w, float h, float aX, float aY, float oX, float oY, float zOffset) {
		/* Generate Drawable */
		Drawable_Sprite sprite = new Drawable_Sprite(attached, this.textures.get_texture(attachment_id));
		this.renderer.add_drawable(sprite, LAYER_OBJECT);
		sprite.setDstSize(w, h);
		sprite.setDstAlignment(aX, aY);
		sprite.setDstOffset(oX, oY);
		sprite.setZOff(zOffset);
		
		/* Generate Animator */
		Animator animator = null;
		if(!animation_id.equals("none")) {
			animator = new Animator(this.animations.get_animations(animation_id), sprite);
		}//fi
		
		/* Generate Attachment */
		Attachment attachment = new Attachment(attached, animator);
		
		/* Return */
		this.attachments_spawn.add(attachment);
		return attachment;
	}//END spawn_attachment
	
	/**
	 * Generates an attachment for an object in this room.
	 * 
	 * @param attachment the attachment to generate.
	 */
	private void generate_attachment(Attachment attachment) {
		this.attachments.add(attachment);
	}//END generate_attachment
	
	/**
	 * Spawns an object into the room.
	 * 
	 * @param object_id the id of the object to add.
	 * @param p the position where the object will be spawned.
	 */
	public void spawn_object(String object_id, Vector2 p) {
		this.spawn_list.add(new Spawn_Pair(object_id, p));
	}//END spawn_object
	
	/**
	 * Spawns an object into the room.
	 * 
	 * @param pair the spawning information.
	 */
	private void generate_object(Spawn_Pair pair) {
		String object_id = pair.name;
		Vector2 p = pair.position;
		
		/* Construct object. */
		ObjectData data = this.loader.get_objData(object_id);
		DeviceObject object = null;
		if(monsterID.contains(object_id)){
			object = new Enemy(this, data);
		}
		else{
			object = new DeviceObject(this, data);
		}
		object.position_set(p);
		
		/* Add object to the manager */
		this.objects.add(object);
		if(object.isControllable()) {
			this.controllable_objects.add(object);
		}//fi
		
		/* Generate Drawable */
		Drawable_Sprite sprite = new Drawable_Sprite(object, this.textures.get_texture(data.base_drawable));
		this.renderer.add_drawable(sprite, LAYER_OBJECT);
		sprite.setDstSize(data.draw_dst[0], data.draw_dst[1]);
		sprite.setDstAlignment(data.draw_dst[2], data.draw_dst[3]);
		sprite.setDstOffset(data.draw_dst[4], data.draw_dst[5]);
		
		/* Generate Shadow */
		if(data.has_shadow) {
			Drawable_Shadow shadow = new Drawable_Shadow(sprite,data.shadowAdjustY);
			this.renderer.add_drawable(shadow, LAYER_SHADOW);
		}//fi
		
		/* Generate Animator */
		Animator animator = null;
		if(!data.animation_id.equals("none")) {
			animator = new Animator(this.animations.get_animations(data.animation_id), sprite);
		}//fi
		object.setAnimator(animator);
		
		/* Display Hitbox */
		if(this.display_hitbox) {
			this.generate_hitbox(object);
		}//fi
		
		/* Display Touch Area */
		if(this.display_touchArea) {
			this.generate_touchArea(object);
		}//fi
		
		/* Initialize New Object */
		object.initialize();
		
	}//END add_object
	
	/* Debug Display */
	/**
	 * Sets to draw a hitbox for specified object.
	 * 
	 * @param obj the object to draw a hitbox for.
	 */
	private void generate_hitbox(DeviceObject obj) {
		Drawable_Rectangle hitbox = new Drawable_Rectangle(obj);
		this.renderer.add_drawable(hitbox, LAYER_DEBUG);
		hitbox.setType("debug_hitbox");
		if(obj.isSolid) {
			hitbox.setColor(0, 0, 1, 0.3f);
		}//fi
		else {
			hitbox.setColor(0, 0.5f, 1, 0.3f);
		}//esle
		hitbox.setDstSize(obj.collision_width, obj.collision_height);
		hitbox.setDstAlignment(obj.collision_alignmentX, obj.collision_alignmentY);
		hitbox.setDstOffset(obj.collision_offsetX, obj.collision_offsetY);
	}//END generate_hitbox
	
	/**
	 * Sets to draw a touch area for specified object.
	 * 
	 * @param obj the object to draw a touch area for.
	 */
	private void generate_touchArea(DeviceObject obj) {
		if(obj.isTouchable()) {
			Drawable_Rectangle touchbox = new Drawable_Rectangle(obj);
			this.renderer.add_drawable(touchbox, LAYER_DEBUG);
			touchbox.setType("debug_touch");
			touchbox.setColor(1, 0, 0, 0.3f);
			touchbox.setDstSize(obj.touch_width, obj.touch_height);
			touchbox.setDstAlignment(obj.touch_alignmentX, obj.touch_alignmentY);
			touchbox.setDstOffset(obj.touch_offsetX, obj.touch_offsetY);
		}//fi
	}//END generate_hitbox
	
	/**
	 * Toggles the display of all the hitboxes of all the objects.
	 */
	private void display_hitbox() {
		if(this.display_hitbox) {
			Iterator<DeviceObject> iter = this.objects.iterator();
			while(iter.hasNext()) {
				generate_hitbox(iter.next());
			}//elihw
		}//fi
		else {
			this.renderer.clear_type("debug_hitbox");
		}//esle
	}//END display_hitbox
	
	/**
	 * Toggles the display of all the touch areas of all the objects
	 */
	private void display_touchArea() {
		if(this.display_touchArea) {
			Iterator<DeviceObject> iter = this.objects.iterator();
			while(iter.hasNext()) {
				generate_touchArea(iter.next());
			}//elihw
		}//fi
		else {
			this.renderer.clear_type("debug_touch");
		}//esle
	}//END display_touchArea
	
	/* Update */
	/**
	 * Updates the room and all the objects in the room.
	 * 
	 * @param dt the change in time between this update and the last update.
	 */
	public void update(float dt) {
		//Update objects in room.
		Iterator<DeviceObject> iter = this.objects.iterator();
		DeviceObject target = null;
		ArrayList<DeviceObject> deliverTargets = new ArrayList<DeviceObject>();
		while(iter.hasNext()) {
			DeviceObject obj = iter.next();
			obj.update(dt);
			if(obj.isDead()) {
				obj.finalize();
				iter.remove();
				continue;
			}//fi
			if(obj.identity.equals("box")){
				target = obj;
			}
			else if(monsterID.contains(obj.identity)){
				deliverTargets.add(obj);
			}
		}//elihw
		for(DeviceObject obj : deliverTargets){
			((Enemy)(obj)).setTarget(target);

		}
		
		//Update attachments in room.
		Iterator<Attachment> jter = this.attachments.iterator();
		while(jter.hasNext()) {
			Attachment att = jter.next();
			att.update(dt);
			if(att.isDead()) {
				jter.remove();
			}//fi
		}//elihw
		
		//Spawn objects in room.
		Iterator<Spawn_Pair> kter = this.spawn_list.iterator();
		while(kter.hasNext()) {
			this.generate_object(kter.next());
			kter.remove();
		}//elihw
		
		//Spawn attachments in room.
		Iterator<Attachment> lter = this.attachments_spawn.iterator();
		while(lter.hasNext()) {
			this.generate_attachment(lter.next());
			lter.remove();
		}//elihw
	}//END update

	/* Controls */
	/**
	 * Obtains and returns the object that was touched.
	 * 
	 * @param touch_position the position the user pressed.
	 * @return the object that was touched or null if nothing was touched.
	 */
	private DeviceObject getTouched(Vector2 touch_position) {
		/* Initial Setup */
		DeviceObject touched_object = null;
		float min_area = Float.POSITIVE_INFINITY;
		
		/* Find object that has been touched */
		Iterator<DeviceObject> iter = this.controllable_objects.iterator();
		while(iter.hasNext()) {
			DeviceObject obj = iter.next();
			if(obj.isTouchable()) {
				/* Get Object Info */
				Rectangle touch_area = obj.touch_area();
				Vector2 touch_center = new Vector2(
						touch_area.x + touch_area.width/2,
						touch_area.y + touch_area.height/2
				);
				
				/* Find the touched object that is closest to the touched point. */
				if(touch_area.contains(touch_position)) {
					float area = Math.abs((touch_center.x - touch_position.x) * (touch_center.y - touch_position.y));
					if(area < min_area) {
						touched_object = obj;
						min_area = area;
					}//fi
				}//fi
			}//fi
		}//elihw
		
		return touched_object;
	}//END getTouched
	
	@Override
	protected void touchUp(Vector2 touch_position, int pointer) {
		/* Get touched object */
		DeviceObject touched_object = getTouched(touch_position);
		
		/* Handle touch up behavior. */
		Iterator<DeviceObject> iter = this.controllable_objects.iterator();
		while(iter.hasNext()) {
			DeviceObject obj = iter.next();
			obj.touch_up(touched_object, touch_position);
			if(obj.isDead()) {
				iter.remove();
			}//fi
		}//elihw
	}//END touchUp
	
	@Override
	protected void touchDown(Vector2 touch_position, int pointer) {
		/* Get touched object */
		DeviceObject touched_object = getTouched(touch_position);
		
		/* Handle touch up behavior. */
		Iterator<DeviceObject> iter = this.controllable_objects.iterator();
		while(iter.hasNext()) {
			DeviceObject obj = iter.next();
			obj.touch_down(touched_object, touch_position);
			if(obj.isDead()) {
				iter.remove();
			}//fi
		}//elihw
	}//END touchDown

	@Override
	protected void event_tdrag(Vector2 touch_position, int pointer) {
		this.touchDrag(touch_position, pointer);
	}//END event_tdrag
	
	@Override
	protected void touchDrag(Vector2 touch_position, int pointer) {
		/* Get touched object */
		DeviceObject touched_object = getTouched(touch_position);
		
		/* Handle touch up behavior. */
		Iterator<DeviceObject> iter = this.controllable_objects.iterator();
		while(iter.hasNext()) {
			DeviceObject obj = iter.next();
			obj.touch_drag(touched_object, touch_position);
			if(obj.isDead()) {
				iter.remove();
			}//fi
		}//elihw
	}//END touchDrag
	
	@Override
	protected void keyDown(int keycode) {
		//TODO: add rebindable keys.
		switch (keycode) {
			case Input.Keys.F1:
				this.display_hitbox = !this.display_hitbox;
				this.display_hitbox();
				break;
			case Input.Keys.F2:
				this.display_touchArea = !this.display_touchArea;
				this.display_touchArea();
				break;
		}//hctiws
	}//END keyDown
}//END class Room
