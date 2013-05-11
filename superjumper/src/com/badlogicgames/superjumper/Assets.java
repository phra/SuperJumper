
package com.badlogicgames.superjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	public static ParticleEffect particleEffect;
	public static ParticleEffect particleClouds;
	public static Texture level;
	public static Texture enemy;
	public static Texture enemy1;
	public static Texture star;
	public static Texture star1;
	public static Texture star2;
	public static Texture shuttle;
	public static Texture shuttle1;
	public static Texture shuttlef;
	public static Texture shuttlef1;
	public static Texture shuttlemilitar;
	public static Texture shuttlemilitar1;
	public static Texture shuttlemilitar2;
	public static Texture portaproj;
	public static Texture portalife;
	public static Texture portanos;
	public static Texture disegno;
	public static Texture nuvole;
	public static Texture nuvole1;
	public static Texture nuvole2;
	public static Texture nuvole3;
	public static Texture nuvole4;
	public static Texture bubblestart;
	public static Texture bubble;
	public static Texture bubble1;
	public static Texture bubble2;
	public static Texture nos1;
	public static Texture nos2;
	public static Texture coin1;
	public static Texture coin2;
	public static Texture coin3;
	public static Texture coin4;
	public static Texture coin6;
	public static Texture coin7;
	public static Texture coin8;
	public static Texture tubo;
	public static Texture coin10;
	public static Texture coin11;
	public static Texture Pause;
	public static Texture background;
	public static Texture background1;
	public static Texture background2;
	public static Texture backgroundmain4;
	public static Texture backgroundmain5;
	public static Texture backgroundmain6;
	public static Texture backgroundmain7;
	public static Texture projectile;
	public static Texture items;
	public static Texture icontext;
	public static Texture icontextback;
	public static Texture life;
	public static Texture life1;
	public static Texture mainmenu;
	public static Texture SoundOn;
	public static Texture SoundOff;
	public static Texture tmptext;
	public static Texture tmptext1;
	public static Texture tmprectblack;
	public static Texture tmprectwhite;
	public static Texture lock,locked;
	public static Texture welcome;
	public static Texture choose;
	public static Texture welcomemulti;
	public static Texture welcomehigh;
	public static Texture welcomepaused;
	public static Texture resume,quit,ospita,partecipa;
	public static Texture swipetext,swipe,swipe1,swipe2;
	public static TextureRegion enemyRegion;
	public static TextureRegion enemyRegion1;
	public static TextureRegion starRegion;
	public static TextureRegion bubbles;
	public static TextureRegion nos;
	public static TextureRegion bubblesstart;
	public static TextureRegion star1Region;
	public static TextureRegion backgroundRegion;
	public static TextureRegion backgroundRegion1;
	public static TextureRegion backgroundRegion2;
	public static TextureRegion backgroundRegion3;
	public static TextureRegion disegno1;
	public static TextureRegion backgroundRegion4;
	public static TextureRegion backgroundRegion5;
	public static TextureRegion backgroundRegion10;
	public static TextureRegion backgroundRegion11;
	public static TextureRegion backgroundRegionmain;
	public static TextureRegion mainMenu;
	public static TextureRegion pauseMenu;
	public static TextureRegion ready;
	public static TextureRegion gameOver;
	public static TextureRegion highScoresRegion;
	public static TextureRegion logo;
	public static TextureRegion soundOn;
	public static TextureRegion soundOff;
	public static TextureRegion arrow;
	public static TextureRegion pause;
	public static TextureRegion spring;
	public static TextureRegion castle;
	public static TextureRegion bobHit;
	public static TextureRegion  rect;
	public static Animation shutmilitAnim;
	public static Animation swipeAnim;
	public static Animation coinAnim;
	public static Animation nosAnim;
	public static Animation bubbleAnim;
	public static Animation backAnim;
	public static Animation lifeAnim;
	public static Animation projAnim;
	public static Animation bobJump;
	public static Animation bobfJump;
	public static Animation breakanim;
	public static Animation bobFall;
	public static Animation squirrelFly;
	public static Animation platform;
	public static Animation brakingPlatform;
	public static Animation portagadget;
	public static Animation staranim;
	public static BitmapFont font;
	public static BitmapFont fontsmall;
	public static BitmapFont handfontsmall;
	public static BitmapFont handfontsmaller;
	public static Pixmap pixmap;
	public static Pixmap pixmap1;
	public static Pixmap pixmap2;
	public static Pixmap pixmap3;
	public static Sound jumpSound;
	public static Sound highJumpSound;
	public static Sound hitSound;
	public static Sound coinSound;
	public static Sound clickSound;
	public static Music music;
	public static Color colore;

	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load () {
		CreateRect4Gradient();
		RectBlack();
		RectWhite();
		//particelle effetto fuoco
		particleEffect = new ParticleEffect();
		particleClouds = new ParticleEffect();
		particleEffect.load(Gdx.files.internal("data/pfire.p"), Gdx.files.internal("data"));
		particleClouds.load(Gdx.files.internal("data/pfire1.p"), Gdx.files.internal("data"));
		particleClouds.getEmitters();
		particleEffect.getEmitters();
		particleClouds.allowCompletion();
		particleEffect.allowCompletion();
		//fine particelle effetto fuoco
		level = loadTexture("data/level.png");
		enemy = loadTexture("data/enemy.png");
		enemy1 = loadTexture("data/enemy1.png");
		star = loadTexture("data/particle.png");
		bubble = loadTexture("data/bubble.png");
		bubblestart = loadTexture("data/bubblestart.png");
		bubble1 = loadTexture("data/bubblestart1.png");
		bubble2 = loadTexture("data/bubblestart2.png");
		nos1 = loadTexture("data/portanos2.png");
		nos2 = loadTexture("data/portanos3.png");
		star1 = loadTexture("data/particle1.png");
		star2 = loadTexture("data/particle2.png");
		nuvole = loadTexture("data/cloud.png");
		nuvole1 = loadTexture("data/cloud1.png");
		nuvole2 = loadTexture("data/cloud2.png");
		nuvole3 = loadTexture("data/cloud3.png");
		nuvole4 = loadTexture("data/cloud4.png");
		coin1 = loadTexture("data/coin1.png");
		coin2 = loadTexture("data/coin2.png");
		coin3 = loadTexture("data/coin3.png");
		coin4 = loadTexture("data/coin4.png");
		coin6 = loadTexture("data/explo.png");
		coin7 = loadTexture("data/explo1.png");
		coin8 = loadTexture("data/explo2.png");
		tubo = loadTexture("data/tubo.png");
		//coin10 = loadTexture("data/tubo1.png");
		//coin11 = loadTexture("data/tubo2.png");
		background = loadTexture("data/worldini.png");
		backgroundmain4 = loadTexture("data/main4.png");
		backgroundmain5 = loadTexture("data/main5.png");
		backgroundmain6 = loadTexture("data/main6.png");
		backgroundmain7 = loadTexture("data/main7.png");
		Pause = loadTexture("data/pause.png");
		shuttle = loadTexture("data/alieno1.png");
		shuttle1 = loadTexture("data/alieno2.png");
		shuttlef = loadTexture("data/aliena.png");
		shuttlef1 = loadTexture("data/aliena2.png");
		shuttlemilitar = loadTexture("data/militaralien.png");
		shuttlemilitar1 = loadTexture("data/militaralien1.png");
		shuttlemilitar2 = loadTexture("data/militaralien2.png");
		SoundOn = loadTexture("data/play.png");
		SoundOff = loadTexture("data/stop.png");
		life = loadTexture("data/life.png");
		life1= loadTexture("data/life1.png");
		projectile= loadTexture("data/projectile.png");
		portaproj= loadTexture("data/portaproj.png");
		portalife=loadTexture("data/portalife.png");
		portanos=loadTexture("data/portanos.png");
		icontext=loadTexture("data/icontext1.png");
		icontextback=loadTexture("data/icontextback.png");
		swipe=loadTexture("data/swipe.png");
		swipe1=loadTexture("data/swipe1.png");
		swipe2=loadTexture("data/swipe2.png");
		swipetext=loadTexture("data/swipetext.png");
		lock=loadTexture("data/lock.png");
		locked=loadTexture("data/locked.png");
		welcome=loadTexture("data/welcome.png");
		choose=loadTexture("data/choosen.png");
		welcomemulti=loadTexture("data/multi.png");
		welcomehigh=loadTexture("data/high.png");
		welcomepaused=loadTexture("data/paused.png");
		resume=loadTexture("data/resume.png");
		quit=loadTexture("data/quit.png");
		ospita=loadTexture("data/ospita.png");
		partecipa=loadTexture("data/partecipa.png");
		SetFilter();//setto i filtri sulle texture
		//load texture region,animation & sound
		items = loadTexture("data/items.png");
		//pauseMenu = new TextureRegion(items, 224, 128, 192, 96);
		//gameOver = new TextureRegion(items, 352, 256, 160, 96);
		//highScoresRegion = new TextureRegion(Assets.items, 0, 257, 300, 110 / 3);
		//logo = new TextureRegion(items, 0, 352, 274, 142);
		//bobHit = new TextureRegion(items, 128, 128, 32, 32);
		//	squirrelFly = new Animation(0.2f, new TextureRegion(items, 0, 160, 32, 32), new TextureRegion(items, 32, 160, 32, 32));
		ready = new TextureRegion(items, 320, 224, 192, 32);		
		spring = new TextureRegion(items, 128, 0, 32, 32);
		castle = new TextureRegion(items, 128, 64, 64, 64);
		starRegion= new TextureRegion(star, 0, 0, 128, 128);
		bubbles= new TextureRegion(bubble, 0, 0, 256, 256);
		nos= new TextureRegion(portanos, 0, 0, 256, 256);
		enemyRegion = new TextureRegion(enemy, 0, 0, 512, 512);
		enemyRegion1 = new TextureRegion(enemy1, 0, 0, 512, 512);
		bubblesstart= new TextureRegion(bubblestart, 0, 0, 256, 256);
		backgroundRegion = new TextureRegion(shuttle, 0, 0, 512, 512);
		backgroundRegion10=new TextureRegion(shuttlef,0,0,512,512);
		backgroundRegion11=new TextureRegion(shuttlemilitar,0,0,512,512);
		backgroundRegion1 = new TextureRegion(background, 2, 4, 1024, 1024);
		backgroundRegionmain = new TextureRegion(backgroundmain4, 3,0, 1024, 1024);
		soundOff = new TextureRegion(SoundOff, 0, 0, 128, 128);
		star1Region = new TextureRegion(star2, 0, 0, 128, 128);
		soundOn = new TextureRegion(SoundOn, 0, 0, 128, 128);
		arrow = new TextureRegion(items, 0, 64, 64, 64);
		pause = new TextureRegion(Pause, 0, 0, 128, 128);
		coinAnim = new Animation(0.2f, new TextureRegion(coin1, 0, 0, 128, 128), new TextureRegion(coin2, 0, 0, 128, 128),
			new TextureRegion(coin3, 0, 0, 128, 128), new TextureRegion(coin4, 0, 0, 128, 128));
		backAnim = new Animation(0.25f, new TextureRegion(backgroundmain4, 0, 0, 1024, 1024), new TextureRegion(backgroundmain5, 0, 0, 1024, 1024), new TextureRegion(backgroundmain6, 0, 0, 1024, 1024),
			new TextureRegion(backgroundmain5, 0, 0, 1024, 1024), new TextureRegion(backgroundmain4, 0, 0, 1024, 1024),
			new TextureRegion(backgroundmain5, 0, 0, 1024, 1024), new TextureRegion(backgroundmain6, 0, 0, 1024, 1024),
			new TextureRegion(backgroundmain5, 0, 0, 1024, 1024));
		nosAnim = new Animation(0.1f, new TextureRegion(portanos, 0, 0, 256, 256), new TextureRegion(nos1, 0, 0, 256, 256),new TextureRegion(nos2, 0, 0, 256, 256));
		bubbleAnim = new Animation(0.1f, new TextureRegion(bubblestart, 0, 0, 256, 256), new TextureRegion(bubble1, 0, 0, 256, 256), new TextureRegion(bubble2, 0, 0, 256, 256));
		staranim = new Animation(0.2f, new TextureRegion(star1, 0, 0, 128, 128), new TextureRegion(star2, 0, 0, 128, 128));
		swipeAnim = new Animation(0.7f, new TextureRegion(swipe, 0, 0, 512, 512), new TextureRegion(swipe1, 0, 0, 512, 512), new TextureRegion(swipe2, 0, 0, 512, 512));
		portagadget = new Animation(0.12f, new TextureRegion(portaproj, 0, 0, 256, 256), new TextureRegion(portalife, 0, 0, 256, 256), new TextureRegion(portanos, 0, 0, 256, 256),new TextureRegion(bubblesstart, 0, 0, 256, 256));
		breakanim = new Animation(3.9f, new TextureRegion(tubo, 0, 0, 512,512),new TextureRegion(tubo, 0, 0, 512,512),new TextureRegion(tubo, 0, 0, 512,512));
		lifeAnim = new Animation(0.5f, new TextureRegion(life, 0, 0, 120, 128), new TextureRegion(life1, 0, 0, 120, 128));
		projAnim = new Animation(0.2f, new TextureRegion(projectile, 0, 0, 64, 64), new TextureRegion(projectile, 1, 0, 64, 64));
		bobJump = new Animation(0.2f, new TextureRegion(shuttle, 0, 0, 512, 512), new TextureRegion(shuttle1, 0, 0, 512, 512));
		bobfJump = new Animation(0.2f, new TextureRegion(shuttlef, 0, 0, 512, 512), new TextureRegion(shuttlef1, 0, 0, 512, 512));
		bobFall = new Animation(0.2f, new TextureRegion(shuttle, 0, 0, 512, 512), new TextureRegion(shuttle1, 0, 0, 512, 512));
		shutmilitAnim=new Animation(0.2f, new TextureRegion(shuttlemilitar, 0, 0, 512, 512), new TextureRegion(shuttlemilitar1, 0, 0, 512, 512), new TextureRegion(shuttlemilitar2, 0, 0, 512, 512));
		platform = new Animation(0.1f, new TextureRegion(nuvole, 0, 0, 250, 250), new TextureRegion(nuvole1, 0, 0, 250, 250),
			new TextureRegion(nuvole2, -1, -3, 250, 250), new TextureRegion(nuvole3, 0, 0, 250, 250),new TextureRegion(nuvole4, 0, 0, 250, 250));
		brakingPlatform = new Animation(0.2f, new TextureRegion(coin6, 0, 0, 512, 512),new TextureRegion(coin7, 0, 0, 512, 512),new TextureRegion(coin8, 0, 0, 512, 512));
		font = new BitmapFont(Gdx.files.internal("data/font.fnt"), Gdx.files.internal("data/font.png"), false);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		fontsmall = new BitmapFont(Gdx.files.internal("data/font1.fnt"), Gdx.files.internal("data/font1.png"), false);
		fontsmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		handfontsmall = new BitmapFont(Gdx.files.internal("data/handfontsmall.fnt"), Gdx.files.internal("data/handfontsmall.png"), false);
		handfontsmaller = new BitmapFont(Gdx.files.internal("data/handfontsmaller.fnt"), Gdx.files.internal("data/handfontsmaller.png"), false);
		handfontsmaller.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		music = Gdx.audio.newMusic(Gdx.files.internal("data/music.mp3"));
		music.setLooping(true);
		music.setVolume(0.5f);
		if (Settings.soundEnabled) music.play();
		jumpSound = Gdx.audio.newSound(Gdx.files.internal("data/jump.wav"));
		highJumpSound = Gdx.audio.newSound(Gdx.files.internal("data/highjump.wav"));
		hitSound = Gdx.audio.newSound(Gdx.files.internal("data/hit.wav"));
		coinSound = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));
		clickSound = Gdx.audio.newSound(Gdx.files.internal("data/click.wav"));
	}


	private static void SetFilter(){
		level.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		shuttlemilitar.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		shuttlemilitar1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		shuttlemilitar2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		ospita.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		partecipa.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		quit.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		resume.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		welcomepaused.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		welcomehigh.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		welcomemulti.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		welcome.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		choose.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		lock.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		locked.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		swipe.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		swipe1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		swipe2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		swipetext.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		icontextback.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		icontext.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		tubo.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		portaproj.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		portalife.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		portanos.setFilter(TextureFilter.Nearest, TextureFilter.Linear);
		bubblestart.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		backgroundmain4.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		backgroundmain5.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		backgroundmain6.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		backgroundmain7.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		shuttle.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		shuttle1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		shuttlef.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		shuttlef1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		star1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		star2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		SoundOn.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		SoundOff.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Pause.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		life.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		life1.setFilter(TextureFilter.Linear, TextureFilter.Linear);

	}

	private static void CreateRect4Gradient(){
		pixmap1=new Pixmap(512, 512, Pixmap.Format.RGBA8888);
		tmptext1 = new Texture(pixmap1);
		colore=new Color();
		colore.set(0, 0, 1, 1);
		colore.mul(4.7f);
		pixmap1.setColor(colore);
		pixmap1.fillRectangle(0, 0, 512, 512);
		tmptext1.draw(pixmap1, 0, 0);
		rect = new TextureRegion(tmptext1, 0, 0, 10, 15);
	}
	
	public static void RectWhite()
	{
		pixmap2=new Pixmap(512, 512, Pixmap.Format.RGBA8888);
		pixmap2.setColor(Color.WHITE);
		pixmap2.fillRectangle(0, 0, 512, 512);
		tmprectwhite = new Texture(pixmap2);
	}
	public static void RectBlack()
	{
		pixmap3=new Pixmap(512, 512, Pixmap.Format.RGBA8888);
		pixmap3.setColor(Color.BLACK);
		pixmap3.fillRectangle(0, 0, 512, 512);
		tmprectblack = new Texture(pixmap3);
	}
	public static void playSound (Sound sound) {
		if (Settings.soundEnabled) sound.play(1);
	}
}