package game;

import game.controls.GameInputs;
import game.drawable.Manager_Animation;
import game.drawable.Manager_Texture;
import game.drawable.Renderer;
import game.objects.Manager_Object;
import game.objects.Room;
import game.objects.behavior.Manager_Behavior;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ObjectTest implements ApplicationListener {
	/* Draw */
	private Renderer renderer;
	private GameInputs inputs;
	
	/* Objects */
	private Room room;
	
	/* Test */
	public static final float BASE_HEIGHT = 100;
	
	/* Application Listener */
	@Override
	public void create() {
		/* Test */			
		Manager_Animation animation = new Manager_Animation();
		Manager_Behavior behavior = new Manager_Behavior();
		Manager_Object test_loader = new Manager_Object(behavior);
		test_loader.load_data("data/test_object.dat");
		animation.load_animations("data/test_animation.dat");
		
		Manager_Texture textures = new Manager_Texture();
		textures.load_ids("data/test_drawable.dat");
		
		float base_width = BASE_HEIGHT * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
		float scalar = Gdx.graphics.getHeight() / BASE_HEIGHT;
		this.inputs = new GameInputs(scalar);
		this.renderer = new Renderer(3, scalar);
		Rectangle room_area = new Rectangle(0,0,BASE_HEIGHT,base_width);
		this.room = new Room(room_area, this.renderer, test_loader, textures, animation);
		
		this.room.spawn_object("player", new Vector2(50,50));
		this.room.spawn_object("box", new Vector2(50,50));
		this.room.spawn_object("fuzz1", new Vector2(80, 80));
		this.room.spawn_object("fuzz2", new Vector2(30,30));
		this.room.spawn_object("fuzz3", new Vector2(10,60));
		
		/* Inputs */
		Gdx.input.setInputProcessor(this.inputs);
		this.inputs.add_controllable(this.room);
		
		
	}//END create

	@Override
	public void dispose() {
		this.renderer.dispose();
	}//END dispose

	@Override
	public void pause() {
		// TODO Auto-generated method stub		
	}//END pause
	
	int num = 1;
	
	@Override
	public void render() {
		/* Get Delta-Time */
		float dt = Gdx.graphics.getDeltaTime();
//		System.out.println("FPS:" + 1/dt);
//		System.out.println("OBJ:" + num);
		num++;
		/* Update Room */
		this.room.update(dt);
		
		/* Draw */
		this.renderer.draw();
	}//END render

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub
	}//END resize

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}//END resume
}//END class ObjectTest
