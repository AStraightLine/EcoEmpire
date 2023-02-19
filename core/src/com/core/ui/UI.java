package com.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.core.Boot;

public class UI {

    private ScreenViewport viewport;
    private int resX, resY, gameWidth, gameHeight;

    private Skin skin;

    private Stage stage;
    private Table topTable;
    private Table sideTable;

    public UI(ScreenViewport viewport, int resX, int resY, int gameWidth, int gameHeight) {
        this.viewport = viewport;
        this.resX = resX;
        this.resY = resY;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;

        skin = new Skin();
        skin.addRegions(new TextureAtlas("metal/skin/metal-ui.atlas"));
        skin.load(Gdx.files.internal("metal/skin/metal-ui.json"));

        stage = new Stage(viewport);


        topTable = new Table();
        sideTable = new Table();

        topTable.setBounds(0, resY - (resY - gameHeight), resX, resY - gameHeight);

        topTable.add(new Label("Hello World", skin));



        stage.addActor(topTable);
    }

    public void update() {
        stage.act();
        stage.draw();
    }
}
