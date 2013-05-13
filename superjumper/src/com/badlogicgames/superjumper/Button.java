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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Button extends DynamicGameObject {
	//public float stateTime;
	public Texture texture;
	public Vector2 gravity = new Vector2();

	public Button (float x, float y, Texture texture) {
		super(x, y, UI.BUTTONWIDTH, UI.BUTTONHEIGHT);
		this.texture = texture;
	}

	public void update(float deltaTime) {
		//this.stateTime += deltaTime;
	}

	public void draw(SpriteBatch batch) {
		//Gdx.app.debug("DRAWBUTTON", "this.position.x = " + this.position.x + ", this.position.y = " + this.position.y);
		batch.draw(texture,this.position.x,this.position.y,UI.BUTTONWIDTH, UI.BUTTONHEIGHT);
	}

}
