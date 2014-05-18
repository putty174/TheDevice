package com.pressx.spawner;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.math.Vector2;
import com.pressx.editors.shared.LevelWave;
import com.pressx.editors.shared.SingleFormation;
import com.pressx.managers.Draw;
import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.objects.GameObject;
import com.pressx.objects.enemy.Enemy;
import com.pressx.objects.enemy.MonsterManager;
import com.pressx.screens.game.Room;

public class CustomSpawner {
	//////////Constants
	static final boolean LOOPWHENFINISHED = true;//Will start over if it is finished
	static final float LOOPDELAY = 3;//Delay when looping
	static final Vector2 MAP_CENTER = new Vector2(45,30);//The center of the map
	static final float MAP_BASEDISTANCE = 27;//The distance from the center to the corner
	static final float DELAYBETWEENWAVES = (float)Math.PI;//Delay (in seconds) between killing the last enemy in a wave and the next wave starting
	//////////END Constants
	
	GameObject gameObject;
	//GraphicsManager graphicsManager;
	Room room;
	
	MonsterManager monsterManager;
	
	private Random rand;
	private VectorMath vmath;
	
	private String levelName;
	
	private float formationtimer;
	private int currentWaveIndex,currentFormationIndex;
	private LevelLoader levelLoader;
	private AssetManager levelManager = new AssetManager();
	private ArrayList<LevelWave> waves;
	
	public float nextwavedelaytimer;
	
	public int ui_totalWaveCount,ui_currentWaveNumber,ui_enemiesLeftInWave;//for the UI
	
	private ArrayList<SingleFormation> currentWaveFormationOrder;
	
	public boolean isAtEndOfWave;
	
	public CustomSpawner(Draw d, Sounds s, Textures t, String levelname,GameObject gameObject/*,GraphicsManager graphicsManager*/,Room room){
		this.levelName = levelname;
		this.gameObject = gameObject;
		//this.graphicsManager = graphicsManager;
		this.room = room;
	
		this.monsterManager = new MonsterManager(d,s,t,gameObject, /*graphicsManager,*/ room);
		
		rand = new Random();
		vmath = new VectorMath();
		
		System.out.println("CUSTOM SPAWNER USED");
		
		reset();
	}
	
	/////Helpful Functions
	public void reset(){
		//System.out.println("GL HF!");
		currentWaveIndex = 0;
		formationtimer = 0;
		loadLevelFromFile(levelName);
	}
	
	public void onLevelFinished(){//Don't know what to do here yet
		//System.out.println("Level finished!");
		if(LOOPWHENFINISHED){
			//System.out.println("Resetting spawner!");
			reset();
			formationtimer = -LOOPDELAY;
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
		ui_totalWaveCount = waves.size();
		startWave();
	}
	
	/////Spawning Functions
	void spawnEnemy(FormationLoader.SpawnData data,float rotation){
		Vector2 pos = vmath.rotate(data.position,rotation);
		pos = new Vector2(MAP_CENTER.x+pos.x*MAP_BASEDISTANCE,MAP_CENTER.y-pos.y*MAP_BASEDISTANCE);//Negative Y because GDX's system has a positive Y go upward rather than downward
		Enemy enemy = monsterManager.spawnMonster(data.type+1,pos.x,pos.y);
		room.spawn_object(enemy);
	}
	void spawnFormation(SingleFormation formation){//currentFormationIndex is only needed if the wave isn't randomized
		float rotation = SingleFormation.spawnAngleToRadians(formation.spawnAngle,rand);
		for(FormationLoader.SpawnData spawn : getFormationData(formation.name))
			spawnEnemy(spawn,rotation);
	}
	ArrayList<FormationLoader.SpawnData> getFormationData(String name){
		return levelLoader.getFormationManager().get(name,FormationLoader.FormationData.class).list;
	}
	
	/////Update
	void startWave(){//initializes for current wave
		LevelWave currentWave = waves.get(currentWaveIndex);
		currentFormationIndex = 0;
		currentWaveFormationOrder = new ArrayList<SingleFormation>();
		for(int i = 0; i < currentWave.numFormationsUsed; i++){
			if(currentWave.isRandomized)
				currentWaveFormationOrder.add(currentWave.formations.get(rand.nextInt(currentWave.formations.size())));
			else
				currentWaveFormationOrder.add(currentWave.formations.get(currentFormationIndex));
		}
	}
	
	void nextWave(){//goes to next wave, or finishes if there is no next wave
		++currentWaveIndex;
		if(currentWaveIndex == waves.size())
			onLevelFinished();
		else
			startWave();
	}
	
	public void update(float dseconds){
		formationtimer += dseconds;
		
		if(waves.size() == 0){
			//System.out.println("PLEASE USE A LEVEL THAT ACTUALLY HAS WAVES IN IT!!!! (or maybe CustomSpawner is broken?)");
			return;
		}
		LevelWave currentWave = waves.get(currentWaveIndex);
		
		if(nextwavedelaytimer > 0){
			if(room.monsterCount == 0){
				nextwavedelaytimer -= dseconds;
				if(nextwavedelaytimer <= 0)
					nextWave();
			}
		}else if(formationtimer > currentWave.delayBetweenFormations){
			formationtimer = 0;
			if(currentFormationIndex == currentWave.numFormationsUsed){
				nextwavedelaytimer = DELAYBETWEENWAVES;
				isAtEndOfWave = true;
			}else{
				spawnFormation(currentWaveFormationOrder.get(currentFormationIndex));
				isAtEndOfWave = false;
			}
			++currentFormationIndex; 
		}
		
		/////Enemy counter (for UI)
		ui_currentWaveNumber = currentWaveIndex+1;
		ui_enemiesLeftInWave = room.monsterCount;
		for(int i = currentFormationIndex; i < currentWaveFormationOrder.size(); i++)
			ui_enemiesLeftInWave += getFormationData(currentWaveFormationOrder.get(i).name).size();
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