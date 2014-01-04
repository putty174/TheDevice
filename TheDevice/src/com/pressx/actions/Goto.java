package com.pressx.actions;

import com.badlogic.gdx.math.Vector2;
import com.pressx.objects.GameObject;
import com.pressx.objects.attributes.MovementAttributes;

public class Goto extends Action {
private static final float bounds = 1;
	
	private MovementAttributes movement;
	
	private float x, y; //The coordinates of where to go.
	
	/* Constructor */
	/**
	 * An action that tells the object to move towards a point.
	 * <p>
	 * The ID of Goto is 1.
	 * 
	 * @param x
	 * @param y
	 * @param movement the movement attributes that indicate how the object moves
	 */
	public Goto(float x, float y, MovementAttributes movement)
	{
		super(1);
		
		this.x = x;
		this.y = y;
		
		this.movement = movement;
	}//END Goto
	
	/* Action */
	@Override
	public void act(GameObject self)
	{
		if(
			(self.get_positionX() >= this.x - bounds) && (self.get_positionX() <= this.x + bounds)
			&&
			(self.get_positionY() >= this.y - bounds) && (self.get_positionY() <= this.y + bounds)
		)
		{
			this.terminate();
		}//fi
		else
		{
			//TODO Complicate
			double direction = Math.atan2(y - self.get_positionY(), x - self.get_positionX());
			float xCompMax = (float)(movement.speed * Math.cos(direction));
			float yCompMax = (float)(movement.speed * Math.sin(direction));
			float xComp = (float)(movement.acceleration * Math.cos(direction));
			float yComp = (float)(movement.acceleration * Math.sin(direction));
			
			Vector2 v = self.get_velocity();
			
			if(xCompMax > 0)
			{
				if(v.x + xComp < xCompMax)
				{
					v.x += xComp;
				}//fi
				else
				{
					v.x = xCompMax;
				}//esle
			}//fi
			else
			{
				if(v.x + xComp > xCompMax)
				{
					v.x += xComp;
				}//fi
				else
				{
					v.x = xCompMax;
				}//esle
			}//esle
			
			if(yCompMax > 0)
			{
				if(v.y + yComp < yCompMax)
				{
					v.y += yComp;
				}//fi
				else
				{
					v.y = yCompMax;
				}//esle
			}//fi
			else
			{
				if(v.y + yComp > yCompMax)
				{
					v.y += yComp;
				}//fi
				else
				{
					v.y = yCompMax;
				}//esle
			}//esle
			
			self.set_velocity(v);
		}//esle
	}//END act
}