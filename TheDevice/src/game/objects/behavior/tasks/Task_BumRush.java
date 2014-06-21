package game.objects.behavior.tasks;

import com.badlogic.gdx.math.Vector2;

import game.objects.Attachment;
import game.objects.DeviceObject;

public class Task_BumRush extends Task{
	
	private DeviceObject target;
	private float chargeTime, rushTime;
	private Vector2 chargeDir;
	private Attachment att;
	
	public Task_BumRush(DeviceObject objToRush){
		this.target = objToRush;
	}
	
	@Override
	public void act(DeviceObject self, float dt){
		if(this.chargeTime < 3){
			if(!self.animator.isAnimation("Charging")){
				self.animator.play("Charging", 0, 10, true);
			}
			chargeTime += dt;
		}
		else{
			if(chargeDir == null){
				chargeDir = target.position_get().cpy().sub(self.position_get());
				att = self.room.spawn_attachment("dust", "dust", self, 25, 25, 0.5f, 0, -3, -13, -0.01f);
				att.animator.play("dust", 0, 10, true);
			}
			if(rushTime < 5){
				self.velocity_set(chargeDir.cpy().nor().scl(self.attribute_get("movement_speed") * 2));
				rushTime += dt;
			}
			else{
				att.kill();
				this.terminate();
			}
		}
	}
}
