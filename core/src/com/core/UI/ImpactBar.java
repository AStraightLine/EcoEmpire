package com.core.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ImpactBar {

    private Skin skin = new Skin();
    private ProgressBar impactBar;

    public ImpactBar() {
        skin.addRegions(new TextureAtlas("biological-attack/skin/biological-attack-ui.atlas"));
        skin.load(Gdx.files.internal("biological-attack/skin/biological-attack-ui.json"));
        impactBar = new ProgressBar(0.0f, 1000, 0.01f, false, skin);
        impactBar.setValue(100);
        impactBar.setAnimateDuration(1);
    }

    public ProgressBar getImpactBar() {
        return this.impactBar;
    }
}
