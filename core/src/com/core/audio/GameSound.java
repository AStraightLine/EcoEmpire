package com.core.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class GameSound {

    private Music backgroundMusic;


    public GameSound(){

        this.backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("temp-music.mp3"));


    }

    /**
     * Will start playing the background music for the main game
     * @param volume float value between 0-1 for how loud you want the music
     */
    public void startBackgroundMusic(float volume){

        this.backgroundMusic.setVolume(volume);
        this.backgroundMusic.play();
    }


}
