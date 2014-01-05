package com.pressx.thedevice;

import com.pressx.facebook.FacebookFragment;
import com.pressx.facebook.FacebookInterface;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class FacebookActivity extends FragmentActivity implements FacebookInterface {
	private FacebookFragment mainFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState == null) {
			mainFragment = new FacebookFragment();
			getSupportFragmentManager().beginTransaction().add(android.R.id.content, mainFragment).commit();
		}
		else {
			mainFragment = (FacebookFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
		}
	}
	
	@Override
	public void switchActivity() {
		
	}
}