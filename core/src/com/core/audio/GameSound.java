package com.core.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class GameSound {

    private Music backgroundMusic;
    private Sound tileSelectSound;

    public GameSound(){

        this.backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("temp-music.mp3"));
        this.tileSelectSound = Gdx.audio.newSound(Gdx.files.internal("tile-select.mp3"));

    }

    /**
     * Will start playing the background music for the main game
     * @param volume float value between 0-1 for how loud you want the music
     */
    public void startBackgroundMusic(float volume){

        this.backgroundMusic.setVolume(volume);
        this.backgroundMusic.play();
    }

    /**
     * Will play the sound for selecting a tile
     */
    public void playTileSelectSound(){
        this.tileSelectSound.play();
    }
}
