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

public class Galaxy extends DynamicGameObject {
	public static final int GALAXY_TYPE_STATIC = 0;
	public static final int GALAXY_TYPE_MOVING = 1;
	public static final int GALAXY_STATE_NORMAL = 0;
	public static final float GALAXY_VELOCITY = 2f;
	Random rand=new Random();
	public float size;
	int type,state,choose;
	float stateTime;


	public Galaxy (int type, float x, float y,float width,float height) {
		super(x, y, width, height);
		this.type = type;
		this.choose=(int)(rand.nextFloat() * 2);
		this.size=height;
		this.state = GALAXY_STATE_NORMAL;
		this.stateTime = 0;
	}

	public void update (float deltaTime) {
		if (type == GALAXY_TYPE_MOVING) {
			velocity.y=2.5f;
			position.add(velocity.x * deltaTime,velocity.y * deltaTime);
			stateTime += deltaTime;
		}
		if (type != GALAXY_TYPE_MOVING) {
			velocity.y=3.5f;
			position.add(velocity.x * deltaTime,velocity.y * deltaTime);
			stateTime += deltaTime;
		}
	}

}
