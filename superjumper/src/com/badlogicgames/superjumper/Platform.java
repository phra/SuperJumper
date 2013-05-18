package com.badlogicgames.superjumper;

import java.util.Random;

import com.badlogic.gdx.Gdx;

public class Platform extends DynamicGameObject {
    public static final float PLATFORM_WIDTH = 1.5f;
    public static final float PLATFORM_HEIGHT = 1.5f;
    public static final int PLATFORM_TYPE_STATIC = 0;
    public static final int PLATFORM_TYPE_MOVING = 1;
    public static final int PLATFORM_STATE_NORMAL = 0;
    public static final int PLATFORM_STATE_PULVERIZING = 1;
    public static final float PLATFORM_PULVERIZE_TIME = 0.1f * 4;
    public static final float PLATFORM_VELOCITY = -2;
    public static final Random rand = new Random();
    public static final int NTYPE = 3;
    //public static final int TYPE0 = 0, TYPE1 = 1, TYPE2 = 2, TYPE3 = 3, TYPE4 = 4;

    int type, rendertype;
    int state;
    float stateTime,rotation;
    float raggio=0;

    public Platform (int type, float x, float y) {
        super(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT);
        this.type = type;
        this.rendertype = (int)(rand.nextFloat() * (NTYPE+1));
        Gdx.app.debug("Platform:", "rendertype="+rendertype);
        this.state = PLATFORM_STATE_NORMAL;
        this.stateTime = 0;
        if (type == PLATFORM_TYPE_MOVING) {
            velocity.y = PLATFORM_VELOCITY;
        } else {
            velocity.x = 0;
            velocity.y = 0;
        }
    }
    
    public Platform (int type, float x, float y, int rendertype) {
        super(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT);
        this.type = type;
        this.rendertype = rendertype;
        this.state = PLATFORM_STATE_NORMAL;
        this.stateTime = 0;
        if (type == PLATFORM_TYPE_MOVING) { 
            velocity.y = PLATFORM_VELOCITY;
        } else {
            velocity.x = 0;
            velocity.y = 0;
        }
    }

    public void update (float deltaTime) {
        if (type == PLATFORM_TYPE_MOVING) {
            position.add(velocity.x*deltaTime,velocity.y*deltaTime);
            bounds.x = position.x - PLATFORM_WIDTH / 2;
            bounds.y = position.y - PLATFORM_HEIGHT / 2;
            //if (position.x < PLATFORM_HEIGHT / 2) {
            //  velocity.y = -velocity.y;
            //  position.y = PLATFORM_HEIGHT / 2;
            //}
            //if (position.x > World.WORLD_HEIGHT - PLATFORM_HEIGHT / 2) {
            //  velocity.y = -velocity.y;
            //  position.x = World.WORLD_HEIGHT - PLATFORM_HEIGHT / 2;
            //}
            if (position.x > World.WORLD_WIDTH/2)velocity.x=-velocity.x;
            else if (position.x > World.WORLD_WIDTH/2)velocity.x=velocity.x;
        } else {
            position.add(velocity.x * deltaTime/2,velocity.y * deltaTime/2);
            bounds.x = position.x - PLATFORM_WIDTH / 2;
            bounds.y = position.y - PLATFORM_HEIGHT / 2;
            velocity.y=-5;
        }
        stateTime += deltaTime;
        rotation+=deltaTime*40;
     }
}