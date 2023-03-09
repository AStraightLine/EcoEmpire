package com.core.startscreen;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.core.Boot;
import com.core.Const;
import jdk.javadoc.internal.tool.Start;


public class StartBackground{

    private Body body;
    private float x,y;
    private int width, height;
    private StartScreen startScreen;
    private Texture texture;
    private PolygonShape shape;

    public StartBackground(StartScreen startScreen){

        this.x = 0;
        this.y = 0;

        this.texture = new Texture("start-screen.png");
        this.startScreen = startScreen;
        this.width = 1920;
        this.height = 1080;

        this.body = createBody();
    }

    public void update(){
    }


    public void render(SpriteBatch batch){
        batch.draw(texture, Gdx.graphics.getWidth()/2 - texture.getWidth()/2, Gdx.graphics.getHeight()/2 - texture.getHeight()/2);
    }
    private Body createBody(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x/ Const.PPM , y/Const.PPM);
        bodyDef.fixedRotation = true;

        Body body = startScreen.getWorld().createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/Const.PPM, height/2/Const.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 10000;

        shape.dispose();

        return body;

    }
}
