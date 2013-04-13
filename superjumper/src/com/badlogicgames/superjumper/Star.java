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

public class Star extends DynamicGameObject {
	public static final float STAR_WIDTH = 0.2f;
	public static final float STAR_HEIGHT = 0.5f;
	public static final int STAR_TYPE_STATIC = 0;
	public static final int STAR_TYPE_MOVING = 1;
	public static final int STAR_STATE_NORMAL = 0;
	public static final int STAR_STATE_PULVERIZING = 1;
	public static final float STAR_PULVERIZE_TIME = 0.2f * 4;
	public static final float STAR_VELOCITY = 0.8f;

	int type;
	int state;
	float stateTime;

	public Star (int type, float x, float y) {
		super(x, y, STAR_WIDTH, STAR_HEIGHT);
		this.type = type;
		this.state = STAR_STATE_NORMAL;
		this.stateTime = 0;
		if (type == STAR_TYPE_MOVING) {
			//velocity.x = STAR_VELOCITY;
		//	velocity.y = STAR_VELOCITY;
		}
	}

	public void update (float deltaTime) {
		if (type == STAR_TYPE_MOVING) {
			velocity.y=2;
			position.add(velocity.x * deltaTime,velocity.y * deltaTime);
			bounds.x = position.x - STAR_WIDTH / 2;
			bounds.y = position.y - STAR_HEIGHT / 2;
/*
			if (position.x < World.WORLD_WIDTH/ 2) {
				velocity.x = -velocity.x;
				//position.x = -World.WORLD_WIDTH / 2;
			}
			if (position.x > World.WORLD_WIDTH / 2) {
				velocity.x = +velocity.x;
				//	position.x = World.WORLD_WIDTH / 2;
			}
			
			if (position.y < World.WORLD_HEIGHT / 2) {
				velocity.y = +velocity.y+4;
				//position.y =  World.WORLD_HEIGHT / 2;
			}
			if (position.y >  World.WORLD_HEIGHT / 2) {
				velocity.y = +velocity.y+4;
				//position.y =  World.WORLD_HEIGHT / 2;
			}
*/
			stateTime += deltaTime;
		}
		if (type != STAR_TYPE_MOVING) {
			velocity.y=3;
			position.add(velocity.x * deltaTime,velocity.y * deltaTime);
			bounds.x = position.x - STAR_WIDTH / 2;
			bounds.y = position.y - STAR_HEIGHT / 2;
			stateTime += deltaTime;}
	}
	public void pulverize () {
		state = STAR_STATE_PULVERIZING;
		stateTime = 0;
		velocity.x = 0;
	}
}
