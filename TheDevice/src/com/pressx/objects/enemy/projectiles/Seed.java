package com.pressx.objects.enemy.projectiles;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.pressx.control.GameTimer;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;
import com.pressx.objects.GameObject;
import com.pressx.objects.device.Device;
import com.pressx.objects.player.Player;

public class Seed extends AnimatedObject{
	
	GameTimer timeAlive;
	GameObject end;
	Vector2 direction;
	boolean alreadyHit;
	
	public Seed(Draw d, Sounds s, Textures t, GameObject start, GameObject target){
		super(d, s, t, "seed", 30, start.get_positionX(), start.get_positionY(), 1, 1, 0, 0, 0, 0,
				false, 0, false, 10, 10, t.getArtAsset("plant3"),
				256, 256);
		
		this.animationManager = Textures.getAnimManager("Plant3").copy();
		this.animationManager.changeAnimation("Seed", 30, true);
		this.animationManager.setStdCondition("Seed");
		this.animationManager.setEndCondition("None");
		
		this.timeAlive = new GameTimer(5);
		
		this.end = target;
		this.position = new Vector2(this.position.x + this.drawWidth/2 + this.drawOffsetX, 
				this.position.y + this.drawHeight/2  + this.drawOffsetY);
		this.direction = (target.get_position().sub(new Vector2(target.get_hitOffsetX()/2,0))).sub(this.get_position());
		alreadyHit = false;
		//this.animator = null;
		
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
		
		this.sprite.setOrigin(renderInfo[2] * (this.drawWidth/2),
				renderInfo[2] * (this.drawHeight/2));
		this.sprite.setSize(renderInfo[2] * (this.drawWidth),
				renderInfo[2] * (this.drawHeight));
		this.sprite.setPosition(renderInfo[2] * (this.position.x - this.drawWidth/2 + this.drawOffsetX),
				renderInfo[2] * (this.position.y - this.drawHeight/2  + this.drawOffsetY));		
		this.sprite.rotate(this.direction.angle() + 270 % 360);
		this.sprite.draw(spritebatch);
		this.position.add(direction.nor());
	}
	
	@Override
	public void behavior_collision(GameObject obj){
		try{
			Player p = (Player) obj;
			Vector2 temp = direction.scl(10);
			p.slow();
			obj.impact(50, Math.atan2(temp.y, temp.x));
			
			this.terminate();
		}
		catch(Exception e){
			
		}
		try{
			if(!alreadyHit){
				Device d = (Device) obj;
				obj.setHp(obj.getHp() - 1);
				alreadyHit = true;
			}	
		}
		catch(Exception e){
			
		}
	}
	
}
