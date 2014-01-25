package main;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TestWindow implements ApplicationListener{
	
	Sprite bgArt;
	Sprite monsterTest;
	HashMap<Integer, Sprite> toAnimate;
	PseudoObject obj;
	float timer, width, height, animSpeed, xLoc, yLoc;
	int numFrames, framePos;
	float[] widthByHeight, frameOrder;
	boolean loopAnim, useFrameOrder;
	ArrayList<PseudoObject> objs;
	
	public TestWindow(){
		loopAnim = true;
		width = 128;
		height = 128;
		numFrames = 9;
		float[] temp = {0, 1, 2, 3, 4, 3, 2, 1, 0};
		widthByHeight = temp;
		animSpeed = 60;
		xLoc = 0;
		yLoc = 256;
		framePos = 0;
	}
	
	private void updateCycle(){
		ArrayList<PseudoObject> toRemove = new ArrayList<PseudoObject>();
		for(PseudoObject obj : objs){
			Sprite toAdd = obj.update();
			if(toAdd == null){
				toRemove.add(obj);
			}else{
				toAnimate.put(objs.indexOf(obj), toAdd);
			}
		}
		for(PseudoObject obj : toRemove){
			objs.remove(obj);
		}
		
		//System.out.println(toAnimate.size());
	}
	
	@Override
	public void render(){
		
		SpriteBatch batch = new SpriteBatch();
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.disableBlending();
		bgArt.draw(batch);
		batch.enableBlending();
		for(Sprite spr : toAnimate.values()){
			spr.draw(batch);
		}
		batch.end();
		
		updateCycle();
	}
	
	
	@Override
	public void dispose(){
		bgArt.getTexture().dispose();
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		Texture.setEnforcePotImages(false);
		objs = new ArrayList<PseudoObject>();
		objs.add(new PseudoObject());
		toAnimate = new HashMap<Integer, Sprite>();
		bgArt = new Sprite(new Texture(Gdx.files.internal("data/grass.png")));
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
}
