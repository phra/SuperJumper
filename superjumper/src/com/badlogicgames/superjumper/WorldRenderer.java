package com.badlogicgames.superjumper;
/*VIEW*/
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WorldRenderer {
	public Text gameOver = new Text(UI.SCREENWIDTH/2 ,UI.SCREENHEIGHT*2/3,"G A M E  O V E R");;
	static final float FRUSTUM_WIDTH = 10;
	static final float FRUSTUM_HEIGHT = 15;
	private static float[] verts = new float[20];
	//static int a = 0;
	World world;
	OrthographicCamera cam, screencam;
	SpriteBatch batch;
	TextureRegion background;
	TextureRegion portaproj; //FIXME


	public WorldRenderer (SpriteBatch batch, World world, OrthographicCamera screencam) {
		this.world = world;
		this.cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		this.cam.position.set(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2, 0);
		this.screencam = screencam;
		this.batch = batch;
		this.portaproj = new TextureRegion(Assets.portaproj);
	}


	public void render () {
		batch.enableBlending();
		cam.position.y = world.bob.position.y+4;
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		renderBackground();
		batch.begin();
		world.shakera.update(cam);
		renderObjects();
		batch.end();
		batch.begin();
		batch.setProjectionMatrix(screencam.combined);
		renderUI();
		batch.end();
		batch.disableBlending();
		switch (world.state){

		/*case CONSTANTS.GAME_RUNNING :
			batch.enableBlending();
			cam.position.y = world.bob.position.y+4;
			cam.update();
			batch.setProjectionMatrix(cam.combined);
			renderBackground();
			batch.begin();
			world.shakera.update(cam);
			renderObjects();
			batch.end();
			batch.begin();
			batch.setProjectionMatrix(screencam.combined);
			renderUI();
			batch.end();
			batch.disableBlending();
			break;*/
		
		case CONSTANTS.GAME_PAUSED:
			batch.begin();
			batch.setProjectionMatrix(screencam.combined);
			batch.draw(Assets.welcomepaused,0,0,UI.SCREENPOSITIONX,UI.SCREENPOSITIONY);
			batch.enableBlending();
			for (Button button : world.buttons) {
				button.draw(batch);}
			batch.end();
			break;
			
		case CONSTANTS.MAIN_MENU:
			break;

		case CONSTANTS.GAME_LEVEL_END:
			batch.begin();
			batch.enableBlending();
			String topText = "not enough planets destroy";
			String bottomText = "... Tap to Continue!";
			float topWidth = Assets.font.getBounds(topText).width;
			float bottomWidth = Assets.font.getBounds(bottomText).width;
			Assets.handfontsmall.draw(batch, topText, UI.SCREENWIDTH/2 - topWidth / 2, UI.SCREENHEIGHT - UI.SCREENHEIGHT/3);
			Assets.handfontsmall.draw(batch, bottomText, UI.SCREENWIDTH/2 - bottomWidth / 2, UI.SCREENHEIGHT - UI.SCREENHEIGHT/2);
			batch.disableBlending();
			batch.end();
			break;

		case CONSTANTS.GAME_OVER:
			batch.begin();
			batch.enableBlending();
			Assets.handfontsmall.scale(1.1f);
			gameOver.draw(batch);
			Assets.handfontsmall.scale(-1.1f);
			batch.disableBlending();
			batch.end();
			break;

		case CONSTANTS.GAME_READY:
			
			batch.begin();
			batch.enableBlending();
			batch.setProjectionMatrix(screencam.combined);
			renderUI();
			if(!world.semaforo.isEmpty()){
				Assets.handfontsmall.scale(1.7f);
				world.semaforo.getFirst().draw(batch);
				Assets.handfontsmall.scale(-1.7f);
				}
			batch.disableBlending();
			batch.end();
			
			break;
		}
	}

	private void renderUI() {
		renderTexts();
		renderButtons();
		batch.draw(Assets.pause,UI.POSITIONPAUSEX - UI.INDICATORSIZE/2 , UI.POSITIONPAUSEY  - UI.INDICATORSIZE/2, UI.INDICATORSIZE, UI.INDICATORSIZE);
		batch.draw(Assets.portaproj, UI.POSITIONPORTAPROJX, UI.POSITIONPORTAPROJY, UI.INDICATORSIZE, UI.INDICATORSIZE);
		batch.draw(Assets.portalife, UI.POSITIONPORTALIFEX,UI.POSITIONPORTALIFEY, UI.INDICATORSIZE, UI.INDICATORSIZE);
		batch.draw(Assets.tmprectwhite, UI.BARPOSITIONX,UI.BARPOSITIONY, UI.BARSIZEX, UI.BARSIZEY);
		batch.draw(Assets.tmprectwhite,UI.SECONDBARPOSITIONX, UI.BARPOSITIONY,  UI.BARSIZEX,  UI.BARSIZEY);
		batch.draw(Assets.tmprectblack,  UI.BARPOSITIONX , UI.BARPOSITIONY, UI.BARSIZEX, UI.BARCONSTANT * world.level.constant);
		batch.draw(Assets.tmprectblack, UI.SECONDBARPOSITIONX, UI.BARPOSITIONY,  UI.BARSIZEX,  UI.BARCONSTANT*world.levelnos.constant);
		batch.draw(Assets.level, UI.LEVELPOSITIONX, UI.LEVELPOSITIONY, UI.LEVELSIZEX, UI.LEVELSIZEY);
		batch.draw(Assets.tubo, UI.TUBOPOSITIONX, UI.TUBOPOSITIONY, UI.TUBOWIDTH, UI.TUBOHEIGHT);
	}

	public void renderBackground () {
		GLCommon gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT | GL10.GL_STENCIL_BUFFER_BIT);
		//Gradient Background 
		batch.disableBlending();
		batch.begin();
		drawGradient(batch, Assets.rect, 0, -2, 10, 100,Color.BLACK,Assets.colore, false);
		batch.end();
		batch.enableBlending();
	}

	public void renderObjects () {
	
		renderGalaxies ();
		renderStars();
		renderBob();
		renderPlatforms();
		renderItems();
		renderSquirrels();
		renderCastle();
		renderEnemy();
		renderProjectiles();
		renderProjectilesenemy();
		renderBubble();
		renderExplosions();
	}

	private void renderTexts() {
		for (Text text : world.texts) {
			text.draw(batch);
		}
	}

	private void renderExplosions() {
		for (Explosion exp : world.explosions) {
			TextureRegion keyFrame=Assets.brakingPlatform.getKeyFrame( exp.stateTime, Animation.ANIMATION_LOOPING);
			batch.draw(keyFrame, exp.position.x, exp.position.y, exp.width, exp.height);
		}
	}

	private void renderBob () {
		TextureRegion keyFrame;
		int i;
		//render world terra
		//Color c = new Color(batch.getColor()); 
		//batch.setColor(0,1,0,1);
		batch.draw(Assets.backgroundRegion1, 0, -3, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		//batch.setColor(c);
		switch (world.bob.state) {
		case Bob.BOB_STATE_FALL:
			keyFrame = Assets.bobFall.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);
			break;
		case Bob.BOB_STATE_JUMP:
			keyFrame = Assets.bobJump.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);
			break;
		case Bob.BOB_STATE_HIT:
		default:
			keyFrame = Assets.bobHit;
		}

		/*  float side = world.bob.velocity.x < 0 ? -1 : 1;*/
		//Particelle di fuoco dietro Bob

		if(world.nosinuse==1)
		{
			Assets.particleClouds.start();
			Assets.particleClouds.setPosition(world.bob.position.x,world.bob.position.y-1);
			Assets.particleClouds.draw(batch, Gdx.graphics.getDeltaTime());
		}
		else
		{    
			Assets.particleEffect.start();
			Assets.particleEffect.setPosition(world.bob.position.x,world.bob.position.y-1);
			Assets.particleEffect.draw(batch, Gdx.graphics.getDeltaTime());
		}
		if(CharScreen.state==1 )
			batch.draw(keyFrame, world.bob.position.x -0.65f, world.bob.position.y -2.5f, 2.3f, 3f);
		else if(CharScreen.state==0 ){
			keyFrame = Assets.bobfJump.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);
			batch.draw(keyFrame, world.bob.position.x -0.65f, world.bob.position.y -2.5f, 2.3f, 3f);
		}
		else if(CharScreen.state==2 ){
			keyFrame = Assets.shutmilitAnim.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);
			batch.draw(keyFrame, world.bob.position.x -1.55f, world.bob.position.y -2.3f, 3f, 3.2f);
		}
	}

	private void renderPlatforms () {
		int i = 0;
		//batch.disableBlending();
		for (Platform platform : world.platforms) {
			platform.draw(batch);
			//Gdx.app.debug("RENDERPLATFORMS", "platform.rendertype = " + platform.rendertype + ", i = " + i++);
		}
		//batch.enableBlending();
	}

	private void renderItems () {
		for (Spring spring : world.springs) {
			spring.draw(batch);
		}
		for (Coin coin : world.coins) {
			TextureRegion keyFrame = Assets.platform.getKeyFrame(coin.stateTime, Animation.ANIMATION_LOOPING);
			batch.draw(keyFrame, coin.position.x - 1, coin.position.y - 0.25f, 2.5f, 2.5f);
		}
	}


	private void renderProjectiles(){
		TextureRegion keyFrame;
		for (Projectile projectile : world.projectiles) {
			//TextureRegion keyFrame = Assets.projAnim.getKeyFrame(projectile.stateTime, Animation.ANIMATION_LOOPING);
			switch (projectile.type) {
			case Projectile.TYPE:
				keyFrame = Assets.projAnim.getKeyFrame(projectile.stateTime, Animation.ANIMATION_LOOPING);
				batch.draw(keyFrame, projectile.position.x -0.07f , projectile.position.y+0.4f, 0.3f,0.6f);
				break;
			case Missile.TYPE:
				keyFrame = Assets.missileRegion;
				batch.draw(keyFrame, projectile.position.x-0.4f , projectile.position.y+0.2f, 1f,1.4f);
				break;
			case SuperMissile.TYPE:
				keyFrame = Assets.missileRegion;
				batch.draw(keyFrame, projectile.position.x-0.4f , projectile.position.y+0.2f, 1f,1.4f);
				break;
			}
		}
	}

	private void renderProjectilesenemy(){
		for (Projectile projectenemy : world.projectenemy) {
			TextureRegion keyFrame = Assets.projAnim.getKeyFrame(projectenemy.stateTime, Animation.ANIMATION_LOOPING);  
			batch.draw(keyFrame,projectenemy.position.x -0.07f ,projectenemy.position.y+0.4f,0, 0, 0.3f, 0.6f, 1, 1, 180);
			//batch.draw(keyFrame, projectenemy.position.x -0.07f , projectenemy.position.y+0.4f, 0.3f,0.6f);
		}
	}

	private void renderSquirrels () {
		for (Squirrel squirrel : world.squirrels) {
			TextureRegion keyFrame = Assets.portagadget.getKeyFrame(squirrel.stateTime, Animation.ANIMATION_LOOPING);
			batch.draw(keyFrame, squirrel.position.x - 0.7f, squirrel.position.y - 0.4f, 1.3f, 1.3f);
		}
	}

	private void renderBubble() {
		if (world.bob.enablebubble == true) {
			TextureRegion keyFrame = Assets.bubbles;
			batch.draw(keyFrame,world.bob.position.x-1.2f , world.bob.position.y-2.3f , 2.5f, 3f);
		}
	}

	private void renderButtons() {
		if (world.activemissile == true ){
			TextureRegion keyFrame;
			if (world.enemies.isEmpty())keyFrame = Assets.portamissilebnRegion;
			else keyFrame = Assets.nosAnim.getKeyFrame(0, Animation.ANIMATION_LOOPING);//must add stateTime
			batch.draw(keyFrame, UI.MISSILEPOSITIONX - UI.INDICATORSIZE/2, UI.MISSILEPOSITIONY - UI.INDICATORSIZE/2, UI.INDICATORSIZE, UI.INDICATORSIZE);
		}
		if (world.supermissileButton == true ){
			TextureRegion keyFrame;
			if (world.enemies.isEmpty())keyFrame = Assets.doubleportamissilebnRegion;
			else keyFrame = Assets.doubleportamissileRegion;
			batch.draw(keyFrame,UI.SUPERMISSILEPOSITIONX - UI.INDICATORSIZE/2, UI.SUPERMISSILEPOSITIONY - UI.INDICATORSIZE/2, UI.INDICATORSIZE, UI.INDICATORSIZE);
		}
		if (world.bubbleButton == true ){
			TextureRegion keyFrame = Assets.bubbleAnim.getKeyFrame(0, Animation.ANIMATION_LOOPING);
			batch.draw(keyFrame, UI.BUBBLEPOSITIONX - UI.INDICATORSIZE/2, UI.BUBBLEPOSITIONY - UI.INDICATORSIZE/2, UI.INDICATORSIZE, UI.INDICATORSIZE);

		}
	}

	private void renderCastle () {
		Castle castle = world.castle;
		batch.draw(Assets.castle, castle.position.x - 1, castle.position.y - 1, 2, 2);
	}

	private void renderStars () {
		int len = world.stars.size();
		for (int i = 0; i < len; i++) {
			Star star = world.stars.get(i);
			TextureRegion keyFrame = Assets.star1Region;
			if (star.type == Star.STAR_TYPE_MOVING ) {
				keyFrame = Assets.staranim.getKeyFrame(star.stateTime, Animation.ANIMATION_LOOPING);
				batch.draw(keyFrame, star.position.x , star.position.y , 0.18f, 0.15f);
			}
			else if(star.type != Star.STAR_TYPE_MOVING ){
				keyFrame = Assets.star1Region;
				batch.draw(keyFrame, star.position.x , star.position.y , 0.13f, 0.1f);}
		}
	}

	private void renderGalaxies () {
		int len = world.galaxies.size();
		for (int i = 0; i < len; i++) {
			Galaxy galaxy = world.galaxies.get(i);
			TextureRegion keyFrame = Assets.star1Region;
			if (galaxy.type == Galaxy.GALAXY_TYPE_MOVING ) {
				switch(galaxy.choose){
				case 0:
					keyFrame = Assets.patternfucsRegion;
					batch.draw(keyFrame,galaxy.position.x , galaxy.position.y ,galaxy.size/2,galaxy.size/2, galaxy.size, galaxy.size, 1, 1, galaxy.choose*2);
					//batch.draw(keyFrame, galaxy.position.x , galaxy.position.y , galaxy.size,  galaxy.size);
				case 1:
					keyFrame = Assets.patterngreenRegion;
					batch.draw(keyFrame,galaxy.position.x , galaxy.position.y ,galaxy.size/2,galaxy.size/2, galaxy.size, galaxy.size, 1, 1, galaxy.choose*3);
				}}
			else if(galaxy.type == Galaxy.GALAXY_TYPE_STATIC ){
				switch(galaxy.choose){
				case 0:
					keyFrame = Assets.patternfucsRegion;
					batch.draw(keyFrame,galaxy.position.x , galaxy.position.y ,galaxy.size/2,galaxy.size/2, galaxy.size, galaxy.size, 1, 1, galaxy.choose);
				case 1:
					keyFrame = Assets.patterngreenRegion;
					batch.draw(keyFrame,galaxy.position.x , galaxy.position.y ,galaxy.size/2,galaxy.size/2, galaxy.size, galaxy.size, 1, 1, galaxy.choose*-4);
				}
			}
		}}

	private void renderEnemy(){
		TextureRegion keyFrame;
		for (Enemy charlie : world.enemies){
			switch(charlie.type){
			case 0:
				keyFrame = Assets.enemyRegion;
				batch.draw(keyFrame, charlie.position.x-1.5f , charlie.position.y-0.8f , 2f, 2f);
			case 1:
				keyFrame= Assets.enemyRegion1;
				batch.draw(keyFrame, charlie.position.x-1.5f , charlie.position.y-0.8f , 2f, 2f);
			}
		}
	}


	public static void drawGradient(SpriteBatch batch, TextureRegion tex, float x, float y,
		float width, float height, Color a, Color b, boolean horiz) {
		float ca = a.toFloatBits();
		float cb = b.toFloatBits();

		int idx = 0;
		verts[idx++] = x;
		verts[idx++] = y;
		verts[idx++] = horiz ? ca : cb; // bottom left
		verts[idx++] = tex.getU(); //NOTE: texture coords origin is top left
		verts[idx++] = tex.getV2();

		verts[idx++] = x;
		verts[idx++] = y + height;
		verts[idx++] = ca; // top left
		verts[idx++] = tex.getU();
		verts[idx++] = tex.getV();

		verts[idx++] = x + width;
		verts[idx++] = y + height;
		verts[idx++] = horiz ? cb : ca; // top right
		verts[idx++] = tex.getU2();
		verts[idx++] = tex.getV();

		verts[idx++] = x + width;
		verts[idx++] = y;
		verts[idx++] = cb; // bottom right
		verts[idx++] = tex.getU2();
		verts[idx++] = tex.getV2();

		batch.draw(tex.getTexture(), verts, 0, verts.length);
	}



}