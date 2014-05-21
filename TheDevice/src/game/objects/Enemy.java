package game.objects;

import game.objects.behavior.Behavior_Enemy;

public class Enemy extends DeviceObject{
	
	//Target field so enemies know where to go and what to hit.
	DeviceObject target;
	
	//Extended enemy class to keep things organized. 
	//Only addition is the target field, w/ getters and setters.
	
	public Enemy(Room room, ObjectData object_data){
		super(room, object_data);
	}
	
	public void setTarget(DeviceObject obj){
		this.target = obj;
		((Behavior_Enemy)(behavior)).setTarget(obj);
	}
	
	public DeviceObject getTarget(){
		return target;
	}
	
}
