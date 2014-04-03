package com.pressx.thedevice2;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.pressx.thedevice.TheDevice;

public class MainActivity extends AndroidApplication{
	Bundle saved;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        saved = savedInstanceState;
        initialize(new TheDevice(), cfg);
    }
}