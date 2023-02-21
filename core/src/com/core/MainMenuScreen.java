package com.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.Color;
import com.core.startscreen.StartScreen;


public class MainMenuScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private Box2DDebugRenderer box2DDebugRenderer;
    private SpriteBatch batch;
    private Stage stage;

    //    private Skin skin = new Skin();
    public MainMenuScreen(OrthographicCamera camera) {
        this.camera = camera;
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2, Boot.INSTANCE.getScreenHeight() / 2, 0));
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    public void show(){
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false); //Set true for debugging
        stage.addActor(table);

        // temporary until we have asset manager in
        Skin skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
//        skin.load(Gdx.files.internal("assets/biological-attack/skin/biological-attack-ui.json"));

        //create buttons
        TextButton resumeGame = new TextButton("Resume Game", skin);
        TextButton newGame = new TextButton("New Game", skin);
        TextButton saveGame = new TextButton("Save Game", skin);
        TextButton exit = new TextButton("Exit", skin);

        //add buttons to table
        table.add(resumeGame).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(newGame).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(saveGame).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();

        //event listeners for buttons
        resumeGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //resume game function
                Boot.INSTANCE.resumeGame();
//                Boot.INSTANCE.startGame();
            }
        });

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Boot.INSTANCE.displayStartScreen();
            }
        });

        saveGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //save game function called
            }
        });
    }
    public void update() {
        this.camera.update();

        batch.setProjectionMatrix(camera.combined);
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) { // change from 2 to something else
            //resume game function
        }
    }

    @Override
    public void render(float delta) {
        update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClearColor(1, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.input.setInputProcessor(stage);

        batch.begin();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        batch.end();

        // For debugging purposes:
        //this.box2DDebugRenderer.render(world, camera.combined.scl(Const.PPM)); // Will need to add our PPM or ignore it if not applicable (I.E. remove the argument)
    }
}
