package com.pressx.thedevice;

import android.content.Intent;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.pressx.social.Social;

public class MainActivity extends AndroidApplication implements Social{
	Bundle saved;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        saved = savedInstanceState;
        initialize(new TheDevice(), cfg);
    }
    
    public void switchActivity() {
    	Intent soc = new Intent(getApplicationContext(), SocialActivity.class);
    	startActivity(soc);
    }
}