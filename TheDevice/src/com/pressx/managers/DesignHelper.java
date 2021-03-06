package com.pressx.managers;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.pressx.objects.GameObject;
import com.pressx.objects.device.Device;
import com.pressx.objects.enemy.MonsterManager;
import com.pressx.objects.items.MineDrop;
import com.pressx.screens.game.Room;
import com.pressx.thedevice.GameStats;

public class DesignHelper extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MonsterManager mon;
	Draw d;
	Sounds s;
	Textures t;
	Room r;
	GameStats stats;
	
	public DesignHelper(Draw d, Sounds s, Textures t, final GameObject o, Room room, GameStats stats){
		mon = new MonsterManager(d, s, t, o, room);
		//MineDrop drop1 = new MineDrop(d, s, t, stats, 30, 30);
		this.d = d;
		this.s = s;
		this.t = t;
		this.stats = stats;
		r = room;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton spawnFuzzOne = new JButton("Spawn Fuzz One");
		
		spawnFuzzOne.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					mon.addMonster(1);
				}
		});
		
		JButton spawnFuzzTwo = new JButton("Spawn Fuzz Two");
		
		spawnFuzzTwo.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					mon.addMonster(2);
			}
		});
		
		JButton spawnFuzzThree = new JButton("Spawn Fuzz Three");
		
		spawnFuzzThree.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						mon.addMonster(3);
				}
		});
		
		JButton spawnPlantOne = new JButton("Spawn Plant One");
		
		spawnPlantOne.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						mon.addMonster(4);
				}
		});
		
		JButton spawnPlantTwo = new JButton("Spawn Plant Two");
		
		spawnPlantTwo.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						mon.addMonster(5);
				}
		});
		
		JButton spawnPlantThree = new JButton("Spawn Plant Three");
		
		spawnPlantThree.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						mon.addMonster(6);
				}
		});
		
		JButton addMine = new JButton("Add Mine");
		
		addMine.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try{
						Device d = (Device) o;
						d.toggleGodMode();
					}
					catch(Exception ex){
						
					}
				}
			}
		);
		
		JButton addVort = new JButton("Add Vortex");
		
		JButton setDeviceInvincible = new JButton("God Mode");
		
		setDeviceInvincible.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						
					}
				});
		
		this.getContentPane().setLayout(new GridLayout(5,2));
		this.add(spawnFuzzOne);
		this.add(spawnFuzzTwo);
		this.add(spawnFuzzThree);
		this.add(spawnPlantOne);
		this.add(spawnPlantTwo);
		this.add(spawnPlantThree);
		this.add(addMine);
		this.add(addVort);
		this.add(setDeviceInvincible);
		this.pack();
		this.setVisible(true);
	}
	
	private void addMonster(int ID){
		GameObject obj = mon.spawnMonster(ID, 1, 1);
		r.spawn_object(obj);
	}
}
