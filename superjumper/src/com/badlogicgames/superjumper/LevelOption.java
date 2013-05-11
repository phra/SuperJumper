package com.badlogicgames.superjumper;

public class LevelOption {
	float stateTime;
	float constant;
	Boolean isEmpty=false;
	public LevelOption()
	{
		this.constant=0;
		this.stateTime=0;
	}
	public void update(float deltaTime)
	{

		
		stateTime=deltaTime;
	}
	public void decremento(float deltaTime){
		if(this.constant>0)this.constant-=deltaTime*4;
		else isEmpty=true;
	}
	
	public void incremento(float deltaTime){
		isEmpty=false;
		if(this.constant>15)return;
		else this.constant+=deltaTime;
	}


}

