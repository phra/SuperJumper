package com.badlogicgames.superjumper;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "superjumper";
		cfg.width = 320*2;
		cfg.height = 480*2;
		//cfg.useGL20 = false;
		
		new LwjglApplication(new SuperJumper(), cfg);
	}
}
