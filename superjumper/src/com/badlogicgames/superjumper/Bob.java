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

import com.badlogic.gdx.math.Vector2;

public class Bob extends DynamicGameObject {
	public static final int BOB_STATE_JUMP = 0;
	public static final int BOB_STATE_FALL = 1;
	public static final int BOB_STATE_HIT = 2;
	public static final float BOB_JUMP_VELOCITY = 11;
	public static final float BOB_MOVE_VELOCITY = 20;
	public static final float BOB_WIDTH = 0.8f;
	public static final float BOB_HEIGHT = 0.8f;
	public Vector2 gravity = new Vector2();
	
	public static boolean BOB_DOUBLE_JUMP = false;

	int state;
	float stateTime;
	public static float jumpTime;

	public Bob (float x, float y) {
		super(x, y, BOB_WIDTH, BOB_HEIGHT);
		state = BOB_STATE_FALL;
		stateTime = 0;
	}

	public void setGravityBob(float x, float y){
		this.gravity.x = x;
		this.gravity.y = y;
	}
	
	public void update (float deltaTime) {
		velocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		bounds.x = position.x - bounds.width / 2;
		bounds.y = position.y - bounds.height / 2;

		if (velocity.y > 0 && state != BOB_STATE_HIT) {
			if (state != BOB_STATE_JUMP) {
				state = BOB_STATE_JUMP;
				stateTime = 0;
			}
		}

		if (velocity.y < 0 && state != BOB_STATE_HIT) {
			if (state != BOB_STATE_FALL) {
				state = BOB_STATE_FALL;
				stateTime = 0;
			}
		}

		if (position.x < 0) position.x = World.WORLD_WIDTH;
		if (position.x > World.WORLD_WIDTH) position.x = 0;

		stateTime += deltaTime;
		jumpTime += deltaTime;
	}

	public void hitSquirrel () {
		velocity.set(0, 0);
		state = BOB_STATE_HIT;
		stateTime = 0;
	}

	public void hitPlatform () {
		velocity.y = BOB_JUMP_VELOCITY;
		state = BOB_STATE_JUMP;
		stateTime = 0;
	}

	public void hitSpring () {
		velocity.y = BOB_JUMP_VELOCITY * 1.5f;
		state = BOB_STATE_JUMP;
		stateTime = 0;
	}
}
