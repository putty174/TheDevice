package game.objects.behavior.tasks;

import com.badlogic.gdx.math.Vector2;

import game.objects.DeviceObject;

public class Task_Stun extends Task{
	
	float timer;
	
	public Task_Stun(){
		
	}
	
	@Override
	public void act(DeviceObject self, float dt){
		if(timer < 3){
			self.velocity_set(Vector2.Zero);
			timer += dt;
		}
		else{
			this.terminate();
		}
	}
	
}
