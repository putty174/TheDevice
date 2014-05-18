package game.objects;

import game.objects.behavior.Manager_Behavior;

import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Manager_Object {
	private HashMap<String, ObjectData> objData = new HashMap<String, ObjectData>();
	private Manager_Behavior behavior;
	
	/* Construction */
	/**
	 * Creates an object that loads game objects into the game.
	 * @param behavior the behavior manager for the loaded game objects.
	 */
	public Manager_Object(Manager_Behavior behavior) {
		this.behavior = behavior;
	}//END ObjectLoader
	
	/* Data Management */
	public ObjectData get_objData(String data_identifier) {		
		return this.objData.get(data_identifier);
	}//END get_objData
	
	/**
	 * Loads object information from a file.
	 * 
	 * @param file_name the name of the file being loaded.
	 */
	public void load_data(String file_name) {
		/* Load Content */
		FileHandle handle = Gdx.files.internal(file_name);
		String content[] = handle.readString().split("\n");
		
		/* Iterate and parse through content. */
		String obj_name = null;
		ObjectData obj = null;
		int line_number = -1;
		int overall_ln = 0;
		for(int i = 0; i < content.length; i++) {
			overall_ln++;
			if (line_number != -1) { //If has began reading then...
				if(content[i].equals("end")) { //If reads an "end" then...
					this.objData.put(obj_name, obj); //Store the read data.
					line_number = -1;
				}//fi
				else if (line_number == 0) { //If first line.
					if(this.objData.containsKey(content[i])) { //Can't have blank name.
						System.err.println(String.format("Duplicate object \"%s\" at line %d.", content[i], overall_ln));
						System.exit(1);
					}//fi
					
					obj_name = content[i]; //This should be the name of the object.
					line_number++;
				}//fi esle
				else { //Otherwise is just reading regular code.
					this.parse_code(obj, content[i], line_number, overall_ln); //So parse the code.
					line_number++;
				}//esle
			}
			else if(content[i].equals("begin")) { //If scans "begin" then...
				line_number = 0; //Start reading
				obj = new ObjectData(); //Start new instance of object data.
			}//fi else
		}//rof
	}//END load_data
	
	private void parse_code(ObjectData obj, String content, int line_number, int overall_ln) {
		try {
			switch (line_number) {
				case 1: //Identity
					obj.identity = content;
					break;
				case 2: //Base Drawable
					String drawable_string[] = content.split(" ");
					obj.base_drawable = drawable_string[0];
					obj.animation_id = drawable_string[1];
					break;
				case 3: //Destination Information
					String dst_string[] = content.split(" ");
					obj.drawable_scales = Boolean.parseBoolean(dst_string[0]);
					for(int i = 0; i < obj.draw_dst.length; i++) {
						obj.draw_dst[i] = Float.parseFloat(dst_string[i+1]);
					}//rof
					break;
				case 4: //Shadow
					String shd_string[] = content.split(" ");
					obj.has_shadow = Boolean.parseBoolean(shd_string[0]);
					obj.shadowAdjustY = Float.parseFloat(shd_string[1]);
					break;
				case 5: //Behavior
					String behavior_string[] = content.split(" ");
					obj.behavior = this.behavior.getBehavior(behavior_string[0]);
					obj.behavior_controllable = Boolean.parseBoolean(behavior_string[1]);
					break;
				case 6: //Collision Information
					String collision_string[] = content.split(" ");
					obj.collision_solid = Boolean.parseBoolean(collision_string[0]);
					for(int i = 0; i < obj.collision_hit.length; i++) {
						obj.collision_hit[i] = Float.parseFloat(collision_string[i+1]);
					}//rof
					break;
				case 7: //Touch Information
					String touch_string[] = content.split(" ");
					line_number = -1; //Go back to not reading.
					obj.touchable = Boolean.parseBoolean(touch_string[0]);
					for(int i = 0; i < obj.touch.length; i++) {
						obj.touch[i] = Float.parseFloat(touch_string[i+1]);
					}//rof
					break;
				case 8: //Friction
					obj.friction = Float.parseFloat(content);
					break;
				case 9: //Mass
					obj.mass = Float.parseFloat(content);
					break;
				default: //Stats					
					String stats[] = content.split(" ");
					if (obj.attributes.containsKey(stats[0])) {
						System.err.println(String.format("Duplicate statistic \"%s\" at line %d.", stats[0], overall_ln));
						System.exit(1);
					}//fi
					
					obj.attributes.put(stats[0], Float.parseFloat(stats[1]));
					break;
			}//hctiws
		}//yrt
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(String.format("Could not read \"%s\" at line %d.", content, overall_ln));
			System.exit(1);
		}//END hctac
	}//END parse_code
	
	/**
	 * Generates a list of strings that indicate what is to be drawn.
	 * 
	 * @return a list strings that are drawable ID's.
	 */
	public String[] getDrawableInfo() {
		String[] drawable_list = new String[this.objData.size()];
		
		/* Iterate through attributes */
		int c = 0;
		Iterator<ObjectData> iter = this.objData.values().iterator();
		while(iter.hasNext()) {
			drawable_list[c] = iter.next().base_drawable; //Add base drawable to list.
			c++;
		}//elihw
		
		return drawable_list;
	}//END getDrawableInfo
}//END class Manager_Object
