package com.pressx.objects.device;

import com.pressx.managers.Textures;
import com.pressx.objects.AnimatedObject;

public class XP extends AnimatedObject {
	public XP(float posX, float posY, float speed, float direction)
	{
		super(
				"XP",
				2, //ID
				posX, posY, //Position
				1, //Mass
				100, //Friction
				3, 3, //Hit Height and Width
				0, 0, //Hit x and y offset
				false, //Solid or not
				0, //Touch Radius
				true, //Touchable
				3, 3, //Draw width and height
				Textures.getArtAsset("exp"), //Spritesheet
				30, 30 //srcwidth and height
				);
		
		double dir = direction * Math.PI / 180;
		float xComp = (float)(speed * Math.cos(dir));
		float yComp = (float)(speed * Math.sin(dir));
		this.set_velocity(xComp, yComp);
		this.worth = 5;
		
		this.add_animation("xp_float", 0, 0, 3, 6, true);
		this.set_animation("xp_float", true);
	}//END XP
}