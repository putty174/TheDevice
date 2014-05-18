package game.controls;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Controllable {
	protected Rectangle touch_area;
	
	/**
	 * Creates a controllable object which can be manipulated with user inputs.
	 * 
	 * @param touch_area the area the user can touch.
	 */
	public Controllable(Rectangle touch_area) {
		this.touch_area = touch_area;
	}//END touch_area
	
	/* Touch Event */
	/**
	 * Event indicating that the user that has just pressed the screen.
	 * 
	 * @param touchPosition the position the user pressed.
	 * @param pointer the pointer indicating what touch it is.
	 */
	protected void event_tdown(Vector2 touchPosition, int pointer) {
		if(this.touch_area.contains(touchPosition)) {
			this.touchDown(touchPosition, pointer);
		}//fi
	}//END event_tdown
	
	/**
	 * Event indicating that the user that has just released the screen.
	 * 
	 * @param touchPosition the position the user released.
	 * @param pointer the pointer indicating what touch it is.
	 */
	protected void event_tup(Vector2 touchPosition, int pointer) {
		if(this.touch_area.contains(touchPosition)) {
			this.touchUp(touchPosition, pointer);
		}//fi
	}//END event_tup
	
	/**
	 * Event indicating that the user that is dragging the cursor/finger across the screen.
	 * 
	 * @param touchPosition the position where the cursor/finger is.
	 * @param pointer the pointer indicating what touch it is.
	 */
	protected void event_tdrag(Vector2 touchPosition, int pointer) {
		if(this.touch_area.contains(touchPosition)) {
			this.touchUp(touchPosition, pointer);
		}//fi
	}//END event_tdrag
	
	/* Touch Behavior */
	/**
	 * Do what when there there is a touch down on the screen.
	 * 
	 * @param touchPosition the position where the screen is being pressed.
	 * @param pointer the pointer indicating what touch it is.
	 */
	protected void touchDown(Vector2 touchPosition, int pointer) {}
	
	/**
	 * Do what when the touch is being dragged.
	 * 
	 * @param touchPosition the position where the screen is being pressed.
	 * @param pointer the pointer indicating what touch it is.
	 */
	protected void touchDrag(Vector2 touchPosition, int pointer) {}
	
	/**
	 * Do what when the the user releases their touch.
	 * 
	 * @param touchPosition the position where the screen is being pressed.
	 * @param pointer the pointer indicating what touch it is.
	 */
	protected void touchUp(Vector2 touchPosition, int pointer) {}
	
	/**
	 * Do what when the user presses key.
	 * 
	 * @param keycode indicator to what the user pressed.
	 */
	protected void keyDown(int keycode) {}
}//END interface Controllable
