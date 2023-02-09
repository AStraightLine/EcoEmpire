package com.core.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * @author Hannah Whitham
 * This class can be used to control music for a game
 */
public class GameSound {

    private static Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("temp-music.mp3"));
    private static Sound tileSelectSound = Gdx.audio.newSound(Gdx.files.internal("tile-select.mp3"));

    /**
     * Stops background music
     */
    public static void stopBackgroundMusic(){
        backgroundMusic.stop();
    }


    /**
     * Will start playing the background music for the main game
     * @param volume float value between 0-1 for how loud you want the music
     */
    public static void startBackgroundMusic(float volume){

        backgroundMusic.setVolume(volume);
        backgroundMusic.play();
    }

    /**
     * Will play the sound for selecting a tile
     */
    public static void playTileSelectSound(){
        tileSelectSound.play();
    }

}
