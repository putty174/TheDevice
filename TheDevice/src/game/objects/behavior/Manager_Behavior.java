package game.objects.behavior;

import java.util.HashMap;

public class Manager_Behavior {
	private HashMap<String, Behavior> behaviors = new HashMap<String, Behavior>();
	
	/* Construction */
	/**
	 * Creates a behavior manager object.
	 */
	public Manager_Behavior() {
		this.initialize();
	}//END BehaviorManager
	
	/* Behavior Management */
	/**
	 * Adds all the default behaviors hard coded in the game.
	 */
	private void initialize() {
		this.behaviors.put("behavior_none", new Behavior());
		this.behaviors.put("behavior_player", new Behavior_Player());
		this.behaviors.put("behavior_common", new Behavior_Common());
		this.behaviors.put("behavior_fuzz1", new Behavior_FuzzOne());
		this.behaviors.put("behavior_fuzz2", new Behavior_FuzzTwo());
		this.behaviors.put("behavior_fuzz3", new Behavior_FuzzThree());
	}//END initialize

	/**
	 * Returns a behavior corresponding to the inputed name.
	 * 
	 * @param behavior_name the name of the behavior.
	 * @return	a behavior object.
	 */
	public Behavior getBehavior(String behavior_name) {
		return this.behaviors.get(behavior_name);
	}//END getBehavior
}//END class BehaviorManager