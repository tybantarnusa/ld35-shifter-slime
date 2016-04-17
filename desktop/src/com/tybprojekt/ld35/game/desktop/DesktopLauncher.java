package com.tybprojekt.ld35.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tybprojekt.ld35.game.LD35;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Slimy Thingy";
		config.width = 640;
		config.height = 480;
		config.resizable = false;
		new LwjglApplication(new LD35(), config);
	}
}
