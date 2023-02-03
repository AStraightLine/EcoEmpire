package com.core.startscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.core.Boot;

public class StartScreen extends ScreenAdapter{

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;

    private StartBackground startBackground;

    public StartScreen(OrthographicCamera camera){

        this.camera = camera;
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth()/2, Boot.INSTANCE.getScreenHeight()/2, 0));

        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0,0), false);

        this.startBackground = new StartBackground(this);

    }

    public World getWorld(){return world;}

    public void update(){

        //Press space to start the game
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            Boot.INSTANCE.startGame();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            //quits the game
            Gdx.app.exit();
        }

    }

    @Override
    public void render(float delta){
        update();

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        this.startBackground.render(batch);

        batch.end();
    }

}