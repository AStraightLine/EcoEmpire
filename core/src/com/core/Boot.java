package com.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.core.startscreen.StartScreen;
import jdk.javadoc.internal.tool.Start;

public class Boot extends Game {

	public static Boot INSTANCE;
	private int screenWidth, screenHeight;
	private OrthographicCamera orthographicCamera;

	public Boot() {
		INSTANCE = this;
	}


	@Override
	public void create () {
		this.screenWidth = Gdx.graphics.getWidth();
		this.screenHeight = Gdx.graphics.getHeight();
		this.orthographicCamera = new OrthographicCamera();
		this.orthographicCamera.setToOrtho(false, screenWidth, screenHeight);
		setScreen(new StartScreen(orthographicCamera));
	}

	public void startGame(){
		//will change from the start screen to the
		setScreen(new GameScreen(orthographicCamera));
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}
}
