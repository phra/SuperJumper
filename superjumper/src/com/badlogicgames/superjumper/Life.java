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

public class Life extends GameObject {
	public static final float LIFE_WIDTH = 0.5f;
	public static final float LIFE_HEIGHT = 0.8f;
	public static final int LIFE_SCORE = 10;

	float stateTime;

	public Life (float x, float y) {
		super(x, y, LIFE_WIDTH, LIFE_HEIGHT);
		stateTime = 0;
	}

	public void update (float deltaTime) {
		stateTime += deltaTime;
	}
}
