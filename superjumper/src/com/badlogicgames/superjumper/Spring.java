/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogicgames.superjumper;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Spring extends DynamicGameObject {
	public static final int NTYPE = 4;
	Random rand=new Random();
	float stateTime,rotation;
	int rendertype;
	private static final TextureRegion texture1 = Assets.mondofucRegion;
	private static final TextureRegion texture2 = Assets.mondolunaRegion;
	private static final TextureRegion texture3 = Assets.mondorosRegion;
	private static final TextureRegion texture4 = Assets.mondoterraRegion;
	private static final TextureRegion texture5 = Assets.starRotateRegion;

	public Spring (float x, float y) {
		super(x, y, UI.SPRING_WIDTH, UI.SPRING_HEIGHT);
		this.rendertype = (int)(rand.nextFloat() * NTYPE);
	}
	
	public void update(float deltaTime) {
		position.add(velocity.x*deltaTime, velocity.y*deltaTime);
		this.stateTime += deltaTime*5;
		rotation += deltaTime*70;
		if (rotation > 360) rotation -= 360;
		bounds.x = position.x - UI.SPRING_WIDTH / 2;
		bounds.y = position.y - UI.SPRING_HEIGHT / 2;
	}
//	FIXME si potrebbe modificare di modo da avere dimensioni di ogni pianeta a random diverse e anche la rotazione a random diversa
	public void draw (SpriteBatch batch) {
		switch (this.rendertype) {
		case 0:
			//batch.draw(Assets.meteoragrigiaRegion, platform.position.x - 0.75f, platform.position.y - 0.75f, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT);
			batch.draw(texture5,this.position.x - 0.75f,this.position.y,UI.SPRING_WIDTH/2,UI.SPRING_HEIGHT / 2, UI.SPRING_WIDTH ,UI.SPRING_HEIGHT , 1, 1, this.rotation);
			break;
		case 1:
			//batch.draw(Assets.meteorabluRegion, platform.position.x - 0.75f, platform.position.y - 0.75f, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT);
			batch.draw(texture5,this.position.x - 0.75f,this.position.y,UI.SPRING_WIDTH/2,UI.SPRING_HEIGHT / 2, UI.SPRING_WIDTH ,UI.SPRING_HEIGHT , 1, 1, this.rotation*0.8f);
			break;
		case 2:
			//batch.draw(Assets.meteorarosaRegion, platform.position.x - 0.75f, platform.position.y - 0.75f, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT);
			batch.draw(texture5,this.position.x - 0.75f,this.position.y,UI.SPRING_WIDTH/2,UI.SPRING_HEIGHT / 2, UI.SPRING_WIDTH ,UI.SPRING_HEIGHT , 1, 1, this.rotation*1.5f);
			break;
		case 3:
			//batch.draw(Assets.meteoragiallaRegion, platform.position.x - 0.75f, platform.position.y - 0.75f, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT);
			batch.draw(texture5,this.position.x - 0.75f,this.position.y,UI.SPRING_WIDTH/2,UI.SPRING_HEIGHT/ 2, UI.SPRING_WIDTH,UI.SPRING_HEIGHT , 1, 1, this.rotation/2);
			break;
		default:
			//Gdx.app.debug("RENDERPLATFORMS", "platform.rendertype = " + this.rendertype);
		}
	}
	
	public void draw(SpriteBatch batch,TextureRegion variable,float width,float height) {
		batch.draw(variable ,position.x, position.y, width/2, height/2,width, height, 1, 1, stateTime*15);
	}
	
	/*public void update(float deltaTime,DynamicGameObject dst) {
		this.stateTime += deltaTime*5;
		totaltime += deltaTime;
		this.position.x = (float) (dst.position.x + 1*Math.cos(totaltime));
		this.position.y = (float) (dst.position.y + 1*Math.sin(totaltime));
	}
	*/
}
