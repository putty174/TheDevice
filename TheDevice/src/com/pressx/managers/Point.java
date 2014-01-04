package com.pressx.managers;

public class Point implements Comparable<Point> {
	public float x;
	public float y;
	
	public Point(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int compareTo(Point point)
	{
		if (this.y < point.y)
			return 1;
		else if (this.y > point.y)
			return -1;
		else
			if(this.x < point.x)
				return 1;
			else if (this.x > point.x)
				return -1;
			else
				return 0;
	}
}