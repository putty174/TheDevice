package game.controls;

import game.ObjectTest;

import java.util.HashSet;
import java.util.Iterator;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class GameInputs implements InputProcessor {
	private HashSet<Controllable> controllables = new HashSet<Controllable>();
	private float scalar;	
	
	/* Constructor */
	/**
	 * Creates an object that manages user inputs.
	 * 
	 * @param scalar the scaler to pinpoint where the user has pressed.
	 */
	public GameInputs(float scalar) {
		this.scalar = scalar;
	}//END scalar
	
	/* Controllable Management */
	public void add_controllable(Controllable controllable) {
		this.controllables.add(controllable);
	}//END add_controllable
	
	public void clear() {
		this.controllables.clear();
	}//END clear
	
	/* Controls */
	@Override
	public boolean keyDown(int keycode) {
		//Nothing to control...
		if(this.controllables.isEmpty()) {
			return false;
		}//fi
		
		//Give input signal to all controllable objects
		Iterator<Controllable> iter = this.controllables.iterator();
		while(iter.hasNext()) {
			iter.next().keyDown(keycode);
		}//elihw
		return true;
	}//END keyDown

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}//END keyTyped

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}//END keyUp

	@Override
	public boolean mouseMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}//END mouseMoved

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}//END scrolled

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		//Nothing to control...
		if(this.controllables.isEmpty()) {
			return false;
		}//fi
		
		//Give input signal to all controllable objects
		Iterator<Controllable> iter = this.controllables.iterator();
		while(iter.hasNext()) {
			iter.next().event_tdown(
					new Vector2
					(
							(float)x/this.scalar,
							ObjectTest.BASE_HEIGHT - (float)y/this.scalar
					),
					pointer
			);
		}//elihw
		return true;
	}//END touchDown

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		//Nothing to control...
		if(this.controllables.isEmpty()) {
			return false;
		}//fi
		
		//Give input signal to all controllable objects
		Iterator<Controllable> iter = this.controllables.iterator();
		while(iter.hasNext()) {
			iter.next().event_tdrag(
					new Vector2
					(
							(float)x/this.scalar,
							ObjectTest.BASE_HEIGHT - (float)y/this.scalar
					),
					pointer
			);
		}//elihw
		return true;
	}//END touchDragged

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		//Nothing to control...
		if(this.controllables.isEmpty()) {
			return false;
		}//fi
		
		//Give input signal to all controllable objects
		Iterator<Controllable> iter = this.controllables.iterator();
		while(iter.hasNext()) {
			iter.next().event_tup(
					new Vector2
					(
							(float)x/this.scalar,
							ObjectTest.BASE_HEIGHT - (float)y/this.scalar
					),
					pointer
			);
		}//elihw
		return true;
	}//END touchUp
}//END class GameInputs
