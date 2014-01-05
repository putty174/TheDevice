package com.pressx.thedevice;

import android.content.Intent;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.pressx.facebook.FacebookInterface;

public class MainActivity extends AndroidApplication implements FacebookInterface{
	FacebookActivity fb;
	Bundle saved;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        fb = new FacebookActivity();
        saved = savedInstanceState;
        initialize(new TheDevice(this), cfg);
    }
    
    public void switchActivity() {
    	Intent Facebook = new Intent(getApplicationContext(), FacebookActivity.class);
    	startActivity(Facebook);
    }
}