package com.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class CameraInputs extends ApplicationAdapter implements InputProcessor
{
    private OrthographicCamera camera;
    private float defaultZoom = 1f;
    private float maxZoom = 0.2f;
    private Vector3 mousePos = new Vector3();
    private FitViewport viewport;
    private float worldWidth;
    private float worldHeight;
    public CameraInputs(OrthographicCamera camera, InputMultiplexer inputMultiplexer, FitViewport viewport)
    {
        this.camera = camera;
        this.camera.zoom = defaultZoom;
        this.viewport = viewport;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        inputMultiplexer.addProcessor(this);
    }

    @Override public void create () {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {

        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);


        if (amountY != 0)
        {
            if(camera.zoom > defaultZoom-0.5f)
            {
                camera.position.set(mousePos);
            }

            camera.zoom += 0.1f*amountY;
        }
        if (camera.zoom > defaultZoom)
        {
            camera.zoom = defaultZoom;
            camera.position.set(camera.viewportWidth /2f, camera.viewportHeight /2f, 0);
        }
        else if (camera.zoom < maxZoom)
        {
            camera.zoom = maxZoom;
        }




        cameraBounds();
        return true;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float x = Gdx.input.getDeltaX();
        float y = Gdx.input.getDeltaY();

        float beforeX = camera.position.x;
        float beforeY = camera.position.y;

        camera.translate(-x*camera.zoom,y*camera.zoom);

        cameraBounds();

        return true;
    }
    public void cameraBounds()
    {
        float cameraLeft = camera.position.x - camera.viewportWidth / 2 * camera.zoom;
        float cameraRight = camera.position.x + camera.viewportWidth / 2 * camera.zoom;
        float cameraBottom = camera.position.y - camera.viewportHeight / 2 * camera.zoom;
        float cameraTop = camera.position.y + camera.viewportHeight / 2 * camera.zoom;

        if (cameraLeft < 0) {
            camera.position.x = camera.viewportWidth / 2 * camera.zoom;
        }
        if (cameraRight > viewport.getWorldWidth()) {
            camera.position.x = viewport.getWorldWidth() - camera.viewportWidth / 2 * camera.zoom;
        }

        if (cameraBottom < 0) {
            camera.position.y = camera.viewportHeight / 2 * camera.zoom;
        }
        if (cameraTop > viewport.getWorldHeight()) {
            camera.position.y = viewport.getWorldHeight() - camera.viewportHeight / 2 * camera.zoom;
        }
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
