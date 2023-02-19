package com.core.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class UI {



    private Stage hudStage;
    private ProgressBar impactBar;

    public UI() {
        this.hudStage = new Stage();

        ImpactBar ib = new ImpactBar();
        this.impactBar = ib.getImpactBar();

        hudStage.addActor(impactBar);
    }

    public Stage getHudStage() {
        return this.hudStage;
    }

    public ProgressBar getImpactBar() {
        return impactBar;
    }
}
