package com.pressx.objects.enemy.projectiles;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.pressx.control.GameTimer;
import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;
import com.pressx.objects.GameObject;
import com.pressx.objects.device.Device;
import com.pressx.objects.player.Player;

public class Seed extends AnimatedObject{
	
	GameTimer timeAlive;
	GameObject end;
	Vector2 direction;
	
	public Seed(GameObject start, GameObject target){
		super("seed", 30, start.get_positionX(), start.get_positionY(), 1, 1, 0, 0, 0, 0,
				false, 0, false, 10, 10, Textures.getArtAsset("plant3"),
				256, 256);
		
		this.animationManager = Textures.getAnimManager("Plant3").copy();
		this.animationManager.changeAnimation("Seed", 30, true);
		this.animationManager.setStdCondition("Seed");
		this.animationManager.setEndCondition("None");
		
		this.timeAlive = new GameTimer(5);
		
		this.end = target;
		this.position = new Vector2(start.get_positionX() - start.get_hitWidth()/4, 
				start.get_positionY() - start.get_hitHeight()/2);
		this.direction = (target.get_position().sub(new Vector2(target.get_hitOffsetX()/2, target.get_hitOffsetY()/2))).sub(this.get_position());
		this.animator = null;
		
	}
	
	@Override
	public void update(float dt, ArrayList<GameObject> objects){
		if(timeAlive.isDone()){
			this.terminate();
		}
		else{		
			timeAlive.update_timer(dt);
		}
		
	}
	
	@Override
	public void render(SpriteBatch spritebatch, float[] renderInfo){
		this.sprite = this.animationManager.update();
		this.sprite.setOrigin(this.position.x, 
				this.position.y);
		this.sprite.setScale((float)0.5);
		this.sprite.setPosition(renderInfo[2] * (this.position.x - end.get_hitWidth()/2), 
				renderInfo[2] * (this.position.y ));//end.get_hitWidth()/2));
		
		this.sprite.rotate(this.direction.angle() + 270 % 360);
		this.sprite.draw(spritebatch);
		this.position.add(direction.nor());
	}
	
	@Override
	public void behavior_collision(GameObject obj){
		try{
			Player p = (Player) obj;
			((Player)obj).stun(this);
			this.terminate();
		}
		catch(Exception e){
			
		}
		try{
			Device d = (Device) obj;
			obj.setHp(obj.getHp() - 1);
		}
		catch(Exception e){
			
		}
	}
	
}