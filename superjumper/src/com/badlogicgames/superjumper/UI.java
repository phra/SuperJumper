package com.badlogicgames.superjumper;

import java.util.Random;

import com.badlogic.gdx.Gdx;

public interface UI {
	public static final float SCREENWIDTH = Gdx.graphics.getWidth();
	public static final float SCREENHEIGHT = Gdx.graphics.getHeight();
	public static final float SCOREPOSITIONX = SCREENWIDTH * 0.26f;
	public static final float SCOREPOSITIONY = SCREENHEIGHT * 0.92f;
	public static final float AMMOPOSITIONX = SCREENWIDTH - 70f;
	public static final float AMMOPOSITIONY = SCREENHEIGHT * 0.9f;
	public static final float LIFEPOSITIONX = AMMOPOSITIONX;
	public static final float LIFEPOSITIONY = SCREENHEIGHT * 0.8f;
	public static final float INDICATORSIZE = 75f;
	public static final float POSITIONPORTAPROJX = AMMOPOSITIONX+10f;
	public static final float POSITIONPORTALIFEX = POSITIONPORTAPROJX;
	public static final float POSITIONPAUSEX = POSITIONPORTAPROJX;
	public static final float POSITIONPAUSEY = SCREENHEIGHT - 53;
	public static final float POSITIONPORTAPROJY = SCREENHEIGHT *0.8f;
	public static final float POSITIONPORTALIFEY = SCREENHEIGHT *0.7f;
	public static final float TUBOWIDTH = SCREENWIDTH * 0.6f;
	public static final float TUBOHEIGHT = SCREENHEIGHT * 0.18f;
	public static final float TUBOPOSITIONX = 0;
	public static final float TUBOPOSITIONY = SCREENHEIGHT * 0.8f;
	public static final float LEVELPOSITIONX = -53;
	public static final float LEVELPOSITIONY = 0;
	public static final float BUTTONRESUMEPOSITIONX = SCREENWIDTH/2;
	public static final float BUTTONQUITPOSITIONX = BUTTONRESUMEPOSITIONX;
	public static final float BUTTONRESUMEPOSITIONY = SCREENHEIGHT/2;
	public static final float BUTTONQUITPOSITIONY = BUTTONRESUMEPOSITIONY-100f;
	public static final float BUTTONWIDTH = SCREENWIDTH/5f;
	public static final float BUTTONHEIGHT = SCREENHEIGHT/9f;
	public static final float TEXTSCALE = -SCREENWIDTH *0.0006f;
	public static final float MISSILEPOSITIONX = AMMOPOSITIONX;
	public static final float MISSILEPOSITIONY = SCREENHEIGHT * 0.4f;
	public static final float SUPERMISSILEPOSITIONX = AMMOPOSITIONX;
	public static final float SUPERMISSILEPOSITIONY = SCREENHEIGHT * 0.3f;
	public static final float BUBBLEPOSITIONX = AMMOPOSITIONX;
	public static final float BUBBLEPOSITIONY = SCREENHEIGHT * 0.2f;
}
