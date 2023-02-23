package com.core;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.core.Boot;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

	private static int resolutionX = 1920;
	private static int resolutionY = 1080;


	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setIdleFPS(60);
		config.useVsync(true);
		config.setTitle("EcoEmpire");
		config.setWindowedMode(resolutionX, resolutionY);
		//config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		config.setWindowIcon("logo.png");
		new Lwjgl3Application(new Boot(resolutionX, resolutionY), config);
	}
}
