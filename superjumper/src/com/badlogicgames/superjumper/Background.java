package com.badlogicgames.superjumper;


public class Background {

	private float bgX, bgY, speedY;

	public Background(int x, int y){
		bgX = x;
		bgY = y;
		speedY = 0;
	}

	public void update() {
		bgY += speedY;

		if (bgY <= -2160){
			bgY += 4320;
		}
	}

	public float getBgX() {
		return bgX;
	}

	public float getBgY() {
		return bgY;
	}

	public float getSpeedY() {
		return speedY;
	}

	public void setBgX(float f) {
		this.bgX = f;
	}

	public void setBgY(float f) {
		this.bgY = f;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}




}
