package com.pressx.control;

public class GameTimer {
	private boolean paused = false;
	private float interval_time;
	private float interval;
	private boolean isDone = false;
	
	/* Constructor */
	public GameTimer(float interval)
	{
		this.set_interval(interval);
		this.reset_timer();
	}//END GameTimer
	
	/* Time */
	/**
	 * Sets the interval and scales the remaining time correspondingly.
	 * <p>
	 * NewTime = (NewInverval/OldInterval) * OldTime
	 * 
	 * @param interval
	 */
	public void scale_interval(float interval)
	{
		this.interval_time = (interval/this.interval) * this.interval_time;
		this.interval = interval;
	}//END scale_interval
	/**
	 * Sets the interval time to the indicated value.
	 * 
	 * @param interval the time per interval.
	 */
	public void set_interval(float interval)
	{
		this.interval = interval;
	}//END set_interval
	
	/**
	 * Returns the the time of each interval.
	 * 
	 * @return 
	 */
	public float get_interval()
	{
		return this.interval;
	}//END get_interval
	
	/**
	 * Returns the time remaining on the interval.
	 * 
	 * @return the current time on the interval.
	 */
	public float get_time()
	{
		return this.interval_time;
	}//END get_time
	
	/**
	 * Resets the timer
	 */
	public void reset_timer()
	{
		this.interval_time = this.interval;
		this.isDone = false;
	}//END reset_timer
	
	/**
	 * Pauses the timer.
	 */
	public void pause()
	{
		this.paused = true;
	}//END pause
	
	/**
	 * Unpauses the timer.
	 */
	public void unpause()
	{
		this.paused = false;
	}//END unpause
	
	/**
	 * Tells whether or not the timer is paused or not.
	 */
	public boolean ispaused()
	{
		return paused;
	}//END ispaused
	
	public boolean isDone()
	{
		return this.isDone;
	}//END isDone
	
	/**
	 * Updates the timer
	 * 
	 * @param dt the time that has passed.
	 */
	public void update_timer(float dt)
	{
		if(!paused)
		{
			this.interval_time -= dt;
		}//fi
		
		if(this.interval_time <= 0)
		{
			this.isDone = true;
		}//fi
	}//END interval_time
	
	/**
	 * Changes the interval time.
	 * 
	 * @param t The new time that it takes for the timer to complete.
	 */
	public void set_interval_time(float t){
		if(t < 0){
			return;
		}
		this.interval_time = t;
	}
}