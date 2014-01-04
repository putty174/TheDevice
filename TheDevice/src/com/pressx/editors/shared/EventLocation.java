package com.pressx.editors.shared;

public class EventLocation{
    public Vector2 position;
    public SpawnType type;
    
    public EventLocation(Vector2 pos,SpawnType typ){
		position = pos;
		type = typ;
    }
    
    float sign(float f){return f < 0 ? -1 : 1;}
    public void useSnap_Cartesian(float snap){
    	position = new Vector2((int)(position.x/snap+.5f*sign(position.x))*snap,(int)(position.y/snap+.5f*sign(position.y))*snap);
    }
    public void useSnap_Polar(float anglesnap,float distsnap){
    	float angle = anglesnap*(int)(Vector2.toAngle(position)/anglesnap+.5f);
    	float dist = (int)(position.magnitude()/distsnap+.5f)*distsnap;
    	position = Vector2.fromAngle(angle,dist);
    }
}
