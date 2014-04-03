package com.pressx.managers;

import java.io.BufferedReader;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;

public class AnimationManagerLoader {
	
	HashMap<String, AnimationManager> managers = new HashMap<String, AnimationManager>();
	
	public AnimationManagerLoader(){
		
	}
	
	public static HashMap<String, HashMap<String, AnimationManager>> placeHolderShit(){
		
		HashMap<String, HashMap<String, AnimationManager>> temp = new HashMap<String, HashMap<String, AnimationManager>>();
		BufferedReader handle = new BufferedReader((Gdx.files.internal("data/def.bloop").reader()));
		
		try{
			
			//System.out.println(handle.readLine());
			
			int numScenes = Integer.parseInt(handle.readLine());
			
			//LO AND BEHOLD THE MULTI NESTED FOR LOOP
			
			
			for(int sceneCounter = 0; sceneCounter < numScenes; sceneCounter++){
				
				String currentScene = handle.readLine();
				temp.put(currentScene, new HashMap<String, AnimationManager>());
				int numManagers = Integer.parseInt(handle.readLine());
				
				for(int managerCounter = 0; managerCounter < numManagers; managerCounter++){
					
					String managerName = handle.readLine();
					String texLocator = handle.readLine();
					AnimationManager tempManager = new AnimationManager(texLocator);
					//temp.get(currentScene).put(managerName, new AnimationManager(texLocator));
					int numAnimations = Integer.parseInt(handle.readLine());
					System.out.println(managerName);
					
					for(int animationCounter = 0; animationCounter < numAnimations; animationCounter++){
						
						String animationName = handle.readLine();
						System.out.println(animationName);
						int numFrames = Integer.parseInt(handle.readLine());
						int[][] frameDefinitions = new int[numFrames][4];
						
						
						for(int frameCounter = 0; frameCounter < numFrames; frameCounter++){
							
							String[] individualFrameDef = handle.readLine().split(" ");
							
							for(int element = 0; element < 4; element++){
								
								frameDefinitions[frameCounter][element] = 
										Integer.parseInt(individualFrameDef[element]);
								
							}
							
						}
						
						int framesToUse = Integer.parseInt(handle.readLine());
						String[] frameOrder = handle.readLine().split(" ");
						int[] framesInUse = new int[framesToUse];
						
						for(int frameCounter = 0; frameCounter < framesToUse; frameCounter++){
							
							framesInUse[frameCounter] = Integer.parseInt(frameOrder[frameCounter]);
						
						}
						
						tempManager.createAnimation(frameDefinitions, framesInUse, animationName, false);
						
					}
					
					temp.get(currentScene).put(managerName, tempManager);
					
				}
			}
			handle.close();
		}
		catch(Exception e){
			 System.out.println(e);
			 System.exit(-1);
		}
		
		return temp;
	}
	
//	private void loadManagers(){
//		BufferedReader handle = new BufferedReader((new FileHandle(new File("data/def.txt")).reader()));
//		try{
//			int numManagers = Integer.parseInt(handle.readLine());
//			for(int i = 0; i < numManagers; i ++){
//				String managerName = handle.readLine();
//				System.out.println(managerName);
//				String texName = handle.readLine();
//				System.out.println(texName);
//				managers.put()
//				int thing = Integer.parseInt(handle.readLine());
//				for(int j = 0; j < thing; j ++){
//					String animName = handle.readLine();
//					System.out.println(animName);
//					int numFrames = Integer.parseInt(handle.readLine());
//					int[][] frameDefs = new int[numFrames][4];
//					for(int frame = 0; frame < numFrames; frame ++){
//						String[] vertices = handle.readLine().split(" ");
//						int[] frizAme = {Integer.parseInt(vertices[0]),
//								Integer.parseInt(vertices[1]),
//								Integer.parseInt(vertices[2]),
//								Integer.parseInt(vertices[3])};
//						frameDefs[frame] = frizAme;
//					}
//					
//					int frameOrder = Integer.parseInt(handle.readLine());
//					int frameOrderDef[] = new int[frameOrder];
//					String defdef[] = handle.readLine().split(" ");
//					
//					for(int blah = 0; blah < frameOrder; blah ++){
//						frameOrderDef[blah] = Integer.parseInt(defdef[blah]);
//					}
//				}
//			}
//		}
//		catch(Exception e){
//			System.out.println("File is incorrectly formatted");
//			System.exit(-1);
//		}
//		
//	}
	
}
