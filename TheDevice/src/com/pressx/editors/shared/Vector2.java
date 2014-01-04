/////Vector2 class by Masana Pawlan

package com.pressx.editors.shared;

public class Vector2 {
	static final float pi = (float)Math.PI;
    
    public float x,y;
	
    public Vector2(float x,float y){
    	this.x = x;
    	this.y = y;
    }
    public Vector2(double x,double y){
    	this.x = (float)x;
    	this.y = (float)y;
    }
    public Vector2(java.awt.Point p){
	this.x = p.x;
	this.y = p.y;
    }
    public Vector2(){
    	this(0,0);
    }
    
    //Static values
    public static Vector2 zero(){return new Vector2();}
    public static Vector2 one(){return new Vector2(1,1);}
    
    //Methods
    public static boolean equals(Vector2 v1,Vector2 v2){return distance(v1,v2) < .1f;}
    public boolean equals(Vector2 v){return equals(this,v);}
    public String toString(){return "{"+x+","+y+"}";}
    
    public static float distance(Vector2 v1,Vector2 v2){return Vector2.sub(v1,v2).magnitude();}
    public static boolean distanceLessThan(Vector2 v1,Vector2 v2,float dist){
    	return Math.abs(v1.x-v2.x) < dist && Math.abs(v1.y-v2.y) < dist && distance(v1,v2) < dist;
    }
    	
    public static float magnitude(Vector2 v){return (float)Math.sqrt(v.x*v.x+v.y*v.y);}
    public float magnitude(){return magnitude(this);}
    
    //Use radians
    public static float toAngle(Vector2 v){
    	float r = (float)Math.atan(v.y/v.x);
    	return v.x < 0 ? pi+r : r;
    }
    public static float toAngle(Vector2 v1,Vector2 v2){
    	return toAngle(sub(v2,v1));
    }
    public static Vector2 fromAngle(float angle){return new Vector2(Math.cos(angle),Math.sin(angle));}
    public static Vector2 fromAngle(float angle,float magnitude){return mul(fromAngle(angle),magnitude);}
    
    //Gets a Vector2 between two points
    public static Vector2 lerp(Vector2 v1,Vector2 v2,float t){return Vector2.add(v1,Vector2.mul(Vector2.sub(v2,v1),t));}
    public Vector2 lerp(Vector2 other,float t){return lerp(this,other,t);}
    
    //Normalize
    //Gives a vector that will have a length of one.
    //Example:
    //Vector2.normalize(new Vector2(0,10)).magnitude() -->1
    //Vector2.normalize(new Vector2(23409,234987)).magnitude() -->1
    public static Vector2 normalize(Vector2 v){return fromAngle(toAngle(v));}
    public static Vector2 normalize(Vector2 v,float magnitude){return mul(normalize(v),magnitude);}//Will multiply for you
    public static Vector2 normalize(Vector2 v1,Vector2 v2){return normalize(sub(v2,v1));}
    public static Vector2 normalize(Vector2 v1,Vector2 v2,float magnitude){return mul(normalize(v1,v2),magnitude);}
    
    //Rotates a vector
    public static Vector2 rotate(Vector2 v,float angle){return mul(fromAngle(toAngle(v)+angle),magnitude(v));}
    
    /////Operators
    public static Vector2 add(Vector2 v1,Vector2 v2){return new Vector2(v1.x+v2.x,v1.y+v2.y);}
    public static Vector2 sub(Vector2 v1,Vector2 v2){return new Vector2(v1.x-v2.x,v1.y-v2.y);}
    public static Vector2 mul(Vector2 v1,Vector2 v2){return new Vector2(v1.x*v2.x,v1.y*v2.y);}
    public static Vector2 div(Vector2 v1,Vector2 v2){return new Vector2(v1.x/v2.x,v1.y/v2.y);}
    
    public static Vector2 add(Vector2 v,float f){return new Vector2(v.x+f,v.y+f);}
    public static Vector2 sub(Vector2 v,float f){return new Vector2(v.x-f,v.y-f);}
    public static Vector2 mul(Vector2 v,float f){return new Vector2(v.x*f,v.y*f);}
    public static Vector2 div(Vector2 v,float f){return new Vector2(v.x/f,v.y/f);}
    
    public Vector2 add(Vector2 v2){return new Vector2(x+v2.x,y+v2.y);}
    public Vector2 sub(Vector2 v2){return new Vector2(x-v2.x,y-v2.y);}
    public Vector2 mul(Vector2 v2){return new Vector2(x*v2.x,y*v2.y);}
    public Vector2 div(Vector2 v2){return new Vector2(x/v2.x,y/v2.y);}
    
    public Vector2 add(float f){return new Vector2(x+f,y+f);}
    public Vector2 sub(float f){return new Vector2(x-f,y-f);}
    public Vector2 mul(float f){return new Vector2(x*f,y*f);}
    public Vector2 div(float f){return new Vector2(x/f,y/f);}
}