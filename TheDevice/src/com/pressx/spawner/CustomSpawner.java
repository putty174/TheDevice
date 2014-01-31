package com.pressx.spawner;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.math.Vector2;
import com.pressx.editors.shared.LevelWave;
import com.pressx.editors.shared.SingleFormation;
import com.pressx.objects.GameObject;
import com.pressx.objects.enemy.Enemy;
import com.pressx.objects.enemy.MonsterManager;
import com.pressx.screens.game.Room;

public class CustomSpawner {
	//////////Constants
	static final boolean LOOPWHENFINISHED = true;//Will start over if it is finished
	static final float LOOPDELAY = 3;//Delay when looping
	static final Vector2 center = new Vector2(45,30);//The center of the map
	static final float baseDistance = 27;//The distance from the center to the corner
	//////////END Constants
	
	GameObject gameObject;
	//GraphicsManager graphicsManager;
	Room room;
	
	MonsterManager monsterManager;
	
	private Random rand;
	private VectorMath vmath;
	
	private String levelName;
	
	private float timer;
	private int currentWaveIndex,currentFormationIndex;
	//private HashMap<String,FormationLoader.FormationData> formationNameToData = new HashMap<String,FormationLoader.FormationData>();
	private LevelLoader levelLoader;
	private AssetManager levelManager = new AssetManager();
	private ArrayList<LevelWave> waves;
	
	public CustomSpawner(String levelname,GameObject gameObject/*,GraphicsManager graphicsManager*/,Room room){
		this.levelName = levelname;
		this.gameObject = gameObject;
		//this.graphicsManager = graphicsManager;
		this.room = room;
	
		this.monsterManager = new MonsterManager(gameObject, /*graphicsManager,*/ room);
		
		rand = new Random();
		vmath = new VectorMath();
		
		reset();
	}
	
	/////Helpful Functions
	public void reset(){
		System.out.println("GL HF!");
		currentWaveIndex = 0;
		timer = 0;
		loadLevelFromFile(levelName);
	}
	
	public void onLevelFinished(){//Don't know what to do here yet
		System.out.println("Level finished!");
		if(LOOPWHENFINISHED){
			System.out.println("Resetting spawner!");
			reset();
			timer = -LOOPDELAY;
		}
	}
	
	public void loadLevelFromFile(String filepath){
		if(levelLoader == null){
			levelLoader = new LevelLoader(new InternalFileHandleResolver());
			levelManager.setLoader(LevelLoader.LevelData.class,levelLoader);
			levelManager.load(filepath,LevelLoader.LevelData.class);
			levelManager.finishLoading();
		}
		waves = levelManager.get(filepath,LevelLoader.LevelData.class).waves;
	}
	
	/////Spawning Functions
	void spawnEnemy(FormationLoader.SpawnData data,float rotation){
		Vector2 pos = vmath.rotate(data.position,rotation);
		pos = new Vector2(center.x+pos.x*baseDistance,center.y-pos.y*baseDistance);//Negative Y because GDX's system has a positive Y go upward rather than downward
		Enemy enemy = monsterManager.spawnMonster(data.type+1,pos.x,pos.y);
		room.spawn_object(enemy);
	}
	void spawnFormationFrom(LevelWave currentWave,int currentFormationIndex){//currentFormationIndex is only needed if the wave isn't randomized
		SingleFormation formation;
		if(currentWave.isRandomized)
			formation = currentWave.formations.get(rand.nextInt(currentWave.formations.size()));
		else
			formation = currentWave.formations.get(currentFormationIndex);
		float rotation = SingleFormation.spawnAngleToRadians(formation.spawnAngle,rand);
		for(FormationLoader.SpawnData spawn : levelLoader.getFormationManager().get(formation.name,FormationLoader.FormationData.class).list){
			spawnEnemy(spawn,rotation);
		}
	}
	
	/////Update
	public void update(float dseconds){
		timer += dseconds;
		
		if(waves.size() == 0){
			System.out.println("PLEASE USE A LEVEL THAT ACTUALLY HAS WAVES IN IT!!!!");
			return;
		}
		LevelWave currentWave = waves.get(currentWaveIndex);
		
		if(timer > currentWave.delayBetweenFormations){
			timer = 0;
			if(currentFormationIndex == currentWave.numFormationsUsed){
				currentFormationIndex = 0;
				++currentWaveIndex;
				if(currentWaveIndex == waves.size())
					onLevelFinished();
			}else{
				spawnFormationFrom(currentWave,currentFormationIndex);
			}
			++currentFormationIndex;
		}
	}
	
	public float getProgress(){
		return levelManager.getProgress() + levelLoader.getProgress(); 
	}
	
	public boolean getUpdate(){
		return levelManager.update() && levelLoader.getUpdate();
	}
	}
	
class VectorMath{
	static final float pi = (float)Math.PI;
	
	public float x,y;
	
	//Static values
	public Vector2 zero(){return new Vector2();}
	public Vector2 one(){return new Vector2(1,1);}
	
	//Methods
	public boolean equals(Vector2 v1,Vector2 v2){return distance(v1,v2) < .1f;}
	
	public float distance(Vector2 v1,Vector2 v2){return magnitude(sub(v1,v2));}
	public boolean distanceLessThan(Vector2 v1,Vector2 v2,float dist){//optimized
		float x = v1.x-v2.x;
		if(Math.abs(x) >= dist) return false;
		float y = v2.y-v1.y;
		return Math.abs(y) < dist && x*x+y*y < dist*dist;
	}
		
	public float magnitude(Vector2 v){return (float)Math.sqrt(v.x*v.x+v.y*v.y);}
	
	//Use radians
	public float toAngle(Vector2 v){
		float r = (float)Math.atan(v.y/v.x);
		return v.x < 0 ? pi+r : r;
	}
	public float toAngle(Vector2 v1,Vector2 v2){
		return toAngle(sub(v2,v1));
	}
	public Vector2 fromAngle(float angle){return new Vector2((float)Math.cos(angle),(float)Math.sin(angle));}
	public Vector2 fromAngle(float angle,float magnitude){return mul(fromAngle(angle),magnitude);}
	
	//Gets a Vector2 between two points
	public Vector2 lerp(Vector2 v1,Vector2 v2,float t){return add(v1,mul(sub(v2,v1),t));}
	
	//Normalize
	//Gives a vector that will have a length of one.
	//Example:
	//Vector2.normalize(new Vector2(0,10)).magnitude() -->1
	//Vector2.normalize(new Vector2(23409,234987)).magnitude() -->1
	//Vector2.normalize(new Vector2(1337,9001)).magnitude() -->1
	public Vector2 normalize(Vector2 v){return fromAngle(toAngle(v));}
	public Vector2 normalize(Vector2 v,float magnitude){return mul(normalize(v),magnitude);}//Will multiply for you
	public Vector2 normalize(Vector2 v1,Vector2 v2){return normalize(sub(v2,v1));}
	
	//Rotates a vector
	public Vector2 rotate(Vector2 v,float angle){return mul(fromAngle(toAngle(v)+angle),magnitude(v));}
	
	//Arithmetic
	public Vector2 add(Vector2 v1,Vector2 v2){return new Vector2(v1.x+v2.x,v1.y+v2.y);}
	public Vector2 sub(Vector2 v1,Vector2 v2){return new Vector2(v1.x-v2.x,v1.y-v2.y);}
	public Vector2 mul(Vector2 v1,Vector2 v2){return new Vector2(v1.x*v2.x,v1.y*v2.y);}
	public Vector2 div(Vector2 v1,Vector2 v2){return new Vector2(v1.x/v2.x,v1.y/v2.y);}
	public Vector2 add(Vector2 v1,float f){return new Vector2(v1.x+f,v1.y+f);}
	public Vector2 sub(Vector2 v1,float f){return new Vector2(v1.x-f,v1.y-f);}
	public Vector2 mul(Vector2 v1,float f){return new Vector2(v1.x*f,v1.y*f);}
	public Vector2 div(Vector2 v1,float f){return new Vector2(v1.x/f,v1.y/f);}
	
	//Getting line collisions with horizontal/vertical lines
	public Vector2 getLineCollision_Horizontal(Vector2 p1,Vector2 p2,float y){
		float disttoy = y-p1.y;
		float slope = (p2.x-p1.x)/(p2.y-p1.y);
		return new Vector2(p1.x+disttoy*slope,y);
	}
	public Vector2 getLineCollision_Vertical(Vector2 p1,Vector2 p2,float x){
		float disttox = x-p1.x;
		float slope = (p2.y-p1.y)/(p2.x-p1.x);
		return new Vector2(x,p1.y+disttox*slope);
	}
	
	//Returns a vector with the minimum/maximum x and y from two vectors
	public Vector2 minVector(Vector2 v1,Vector2 v2){
		return new Vector2(v1.x < v2.x ? v1.x : v2.x,v1.y < v2.y ? v1.y : v2.y);
	}
	public Vector2 maxVector(Vector2 v1,Vector2 v2){
		return new Vector2(v1.x >= v2.x ? v1.x : v2.x,v1.y >= v2.y ? v1.y : v2.y);
	}
}