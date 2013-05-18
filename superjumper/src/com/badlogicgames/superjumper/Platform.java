package com.badlogicgames.superjumper;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
	public static final int NTYPE = 4;
	private static final TextureRegion texture1 = Assets.meteoragrigiaRegion;
	private static final TextureRegion texture2 = Assets.meteorabluRegion;
	private static final TextureRegion texture3 = Assets.meteorarosaRegion;
	private static final TextureRegion texture4 = Assets.meteoragiallaRegion;
	
	//public static final int TYPE0 = 0, TYPE1 = 1, TYPE2 = 2, TYPE3 = 3, TYPE4 = 4;

	int type, rendertype;
	int state;
	float stateTime, rotation = 0;
	float raggio=0;

	public Platform (int type, float x, float y) {
		super(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT);
		this.type = type;
		this.rendertype = (int)(rand.nextFloat() * NTYPE);
		//Gdx.app.debug("Platform:", "rendertype="+rendertype);
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
		rotation += deltaTime*30;
		if (rotation > 360) rotation -= 360;
	}
	
	public void draw (SpriteBatch batch) {
		switch (this.rendertype) {
		case 0:
			//batch.draw(Assets.meteoragrigiaRegion, platform.position.x - 0.75f, platform.position.y - 0.75f, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT);
			batch.draw(texture1,this.position.x - 0.75f,this.position.y,Platform.PLATFORM_WIDTH/2,Platform.PLATFORM_HEIGHT/2, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT, 1, 1, this.rotation);
			break;
		case 1:
			//batch.draw(Assets.meteorabluRegion, platform.position.x - 0.75f, platform.position.y - 0.75f, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT);
			batch.draw(texture2,this.position.x - 0.75f,this.position.y,Platform.PLATFORM_WIDTH/2,Platform.PLATFORM_HEIGHT/2, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT, 1, 1, this.rotation);
			break;
		case 2:
			//batch.draw(Assets.meteorarosaRegion, platform.position.x - 0.75f, platform.position.y - 0.75f, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT);
			batch.draw(texture3,this.position.x - 0.75f,this.position.y,Platform.PLATFORM_WIDTH/2,Platform.PLATFORM_HEIGHT/2, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT, 1, 1, this.rotation);
			break;
		case 3:
			//batch.draw(Assets.meteoragiallaRegion, platform.position.x - 0.75f, platform.position.y - 0.75f, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT);
			batch.draw(texture4,this.position.x - 0.75f,this.position.y,Platform.PLATFORM_WIDTH/2,Platform.PLATFORM_HEIGHT/2, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT, 1, 1, this.rotation);
			break;
		default:
			//batch.draw(texture4,this.position.x - 0.75f,this.position.y,Platform.PLATFORM_WIDTH/2,Platform.PLATFORM_HEIGHT/2, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT, 1, 1);
			//batch.draw(Assets.meteoragiallaRegion,this.position.x - 0.75f,this.position.y,Platform.PLATFORM_WIDTH/2,Platform.PLATFORM_HEIGHT/2, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT, 1, 1, this.rotation);
			//Gdx.app.debug("RENDERPLATFORMS", "platform.rendertype = " + this.rendertype);
		}
	}
	
	/*public void draw (SpriteBatch batch) {
		switch (this.rendertype) {
		case 0:
			//batch.draw(Assets.meteoragrigiaRegion, platform.position.x - 0.75f, platform.position.y - 0.75f, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT);
			batch.draw(texture1,this.position.x - 0.75f,this.position.y,Platform.PLATFORM_WIDTH/2,Platform.PLATFORM_HEIGHT/2, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT, 1, 1);
			break;
		case 1:
			//batch.draw(Assets.meteorabluRegion, platform.position.x - 0.75f, platform.position.y - 0.75f, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT);
			batch.draw(texture2,this.position.x - 0.75f,this.position.y,Platform.PLATFORM_WIDTH/2,Platform.PLATFORM_HEIGHT/2, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT, 1, 1);
			break;
		case 2:
			//batch.draw(Assets.meteorarosaRegion, platform.position.x - 0.75f, platform.position.y - 0.75f, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT);
			batch.draw(texture3,this.position.x - 0.75f,this.position.y,Platform.PLATFORM_WIDTH/2,Platform.PLATFORM_HEIGHT/2, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT, 1, 1);
			break;
		case 3:
			//batch.draw(Assets.meteoragiallaRegion, platform.position.x - 0.75f, platform.position.y - 0.75f, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT);
			batch.draw(texture4,this.position.x - 0.75f,this.position.y,Platform.PLATFORM_WIDTH/2,Platform.PLATFORM_HEIGHT/2, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT, 1, 1);
			break;
		default:
			//batch.draw(texture4,this.position.x - 0.75f,this.position.y,Platform.PLATFORM_WIDTH/2,Platform.PLATFORM_HEIGHT/2, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT, 1, 1);
			//batch.draw(Assets.meteoragiallaRegion,this.position.x - 0.75f,this.position.y,Platform.PLATFORM_WIDTH/2,Platform.PLATFORM_HEIGHT/2, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT, 1, 1, this.rotation);
			//Gdx.app.debug("RENDERPLATFORMS", "platform.rendertype = " + this.rendertype);
		}
	}*/
}