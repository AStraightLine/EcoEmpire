package com.core.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

public class GameSound {

    private Music backgroundMusic;
    private float volume;


    public GameSound(){

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("temp-music.mp3"));

        this.volume = 0.25F;
        backgroundMusic.setVolume(this.volume);

        backgroundMusic.play();

    }


}
