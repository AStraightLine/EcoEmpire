package com.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class CameraInputs extends ApplicationAdapter implements InputProcessor
{
    private OrthographicCamera camera;
    private InputMultiplexer inputMultiplexer;

    public CameraInputs(OrthographicCamera camera, InputMultiplexer inputMultiplexer, float actorsWidthTotal, float actorsHeightTotal)
    {
        this.camera = camera;
        this.inputMultiplexer = inputMultiplexer;
        inputMultiplexer.addProcessor(this);
    }

    @Override public void create () {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (amountY != 0)
        {
            camera.zoom += 0.1f*amountY;
        }
        if (camera.zoom > 1)
        {
            camera.zoom = 1;
            camera.position.set(camera.viewportWidth /2f, camera.viewportHeight /2f, 0);
        }
        else if (camera.zoom < 0.2f)
        {
            camera.zoom = 0.2f;
        }

        return true;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float x = Gdx.input.getDeltaX();
        float y = Gdx.input.getDeltaY();

        camera.translate(-x,y);

        return true;
    }
    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

}
