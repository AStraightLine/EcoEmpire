package com.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.core.clock.GameClock;
import com.core.startscreen.StartScreen;
import jdk.javadoc.internal.tool.Start;

public class Boot extends Game {

	public static Boot INSTANCE;
	private int screenWidth, screenHeight;
	private OrthographicCamera orthographicCamera;
	private int resolutionX;
	private int resolutionY;

	private GameScreen gameScreen;

	public Boot(int resolutionX, int resolutionY) {
		INSTANCE = this;
		this.resolutionX = resolutionX;
		this.resolutionY = resolutionY;

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
		this.gameScreen = new GameScreen(orthographicCamera, resolutionX, resolutionY);
		//will change from the start screen to the
		setScreen(this.gameScreen);
	}

	public void resumeGame(){

		if (GameClock.getIsPaused()){
			GameClock.setIsPaused(false);
			gameScreen.getGameClock().handlePause();
			gameScreen.getGameClock().incTimeMod();
			gameScreen.reactivateGameInputs();
			setScreen(this.gameScreen);
		}
	}

	public void displayMenu(Boolean end){
		//change screen to menu screen
		//add pause function here before changing screen
		if (!GameClock.getIsPaused()) {
			gameScreen.getGameClock().handlePause();

		}
		setScreen(new MainMenuScreen(orthographicCamera,end));

	}

	public void displayStartScreen(){
		setScreen(new StartScreen(orthographicCamera));
	}


	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}
}
