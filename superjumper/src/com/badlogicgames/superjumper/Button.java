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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Button extends DynamicGameObject {
	public float stateTime;
	public Texture texture;
	public static float x1=UI.SCREENWIDTH;
	public static float y1=UI.SCREENHEIGHT/2;
	public float x2=0;
	public float y2=0;

public Vector2 gravity = new Vector2();

public Button (float x, float y, Texture texture) {
	super(x1, y1, UI.BUTTONWIDTH, UI.BUTTONHEIGHT);
	x2=x;
	y2=y;
	this.texture = texture;
	}

public void update(float deltaTime) {
	velocity.x = (x2 - position.x)*15;
	velocity.y = (y2 - position.y)*15;
	velocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
	position.add(velocity.x * deltaTime, velocity.y * deltaTime);
	bounds.x = position.x - bounds.width / 2;
	bounds.y = position.y - bounds.height / 2;
	stateTime += deltaTime;

}

public void draw(SpriteBatch batch) {
	//Gdx.app.debug("DRAWBUTTON", "this.position.x = " + this.position.x + ", this.position.y = " + this.position.y);
	batch.draw(texture,this.position.x,this.position.y,UI.BUTTONWIDTH, UI.BUTTONHEIGHT);
	}

}
