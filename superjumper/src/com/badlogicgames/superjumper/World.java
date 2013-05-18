package com.badlogicgames.superjumper;
/*MODEL*/
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class World implements UI, CONSTANTS {
	public interface WorldListener {
		public void jump ();
		public void highJump ();
		public void hit ();
		public void coin ();
		public void life ();
		public void projectile();
	}
	public static final float WORLD_WIDTH = 10;
	public static final float WORLD_HEIGHT = 10000;
	public final int PLATFORMS_DISTANCE = 10;
	public final int STARS_DISTANCE = 1;
	public final Bob bob;

	public final List<Platform> platforms;
	public final List<Star> stars;
	public final List<Spring> springs;
	public final List<Squirrel> squirrels;
	public final List<Projectile> projectiles;
	public final List<Projectile> projectenemy;
	public final List<Missile> rockets;
	public final List<Coin> coins;
	public final List<Button> buttons;
	public Castle castle;
	public final WorldListener listener;
	public final Random rand;
	public final Random randsquirrel;
	public float heightSoFar;
	public int score;
	public int state;
	public int shot=10;
	public int nosinuse=0;
	public boolean turbo=true;
	public int life=1;
	public float freeze=100;
	public int missiles = 0;
	public int print1times=0;
	public float bubbletimes;
	protected boolean freezeON = false;
	private Vector2 gravity = new Vector2(0,15);
	float enemyshotTime=0;
	protected boolean activemissile = false;
	public final LinkedList<Enemy> enemies;
	public boolean supermissileButton = false;
	public boolean bubbleButton = false;
	public int supermissiles = 0;
	public final LinkedList<Text> texts;
	Boolean decremento=false;
	Boolean decrementonos=false;
	LevelOption level=new LevelOption();
	LevelOption levelnos=new LevelOption();
	LinkedList<Explosion> explosions = new LinkedList<Explosion>();
	public Text scoretext = new Text(SCOREPOSITIONX,SCOREPOSITIONY,"SCORE: 0");
	public Text ammotext = new Text(AMMOPOSITIONX,AMMOPOSITIONY,"0x");
	public Text lifetext = new Text(LIFEPOSITIONX,LIFEPOSITIONY,"0x");


	public World (WorldListener listener) {
		this.bob = new Bob(4, 2);
		//this.charlie = new Enemy(5,200);
		this.platforms = new ArrayList<Platform>();
		this.stars = new ArrayList<Star>();
		this.projectiles = new ArrayList<Projectile>();
		this.springs = new ArrayList<Spring>();
		this.squirrels = new ArrayList<Squirrel>();
		this.coins = new ArrayList<Coin>();
		this.projectenemy=new ArrayList<Projectile>();
		this.rockets=new ArrayList<Missile>();
		this.listener = listener;
		this.rand = new Random();
		this.generateLevel();
		this.setGravity(0, 3);
		this.heightSoFar = 0;
		this.score = 0;
		this.state = CONSTANTS.GAME_RUNNING;
		this.randsquirrel=new Random();
		this.enemies = new LinkedList<Enemy>();
		this.texts = new LinkedList<Text>();
		this.texts.offer(scoretext);
		this.texts.offer(ammotext);
		this.texts.offer(lifetext);
		this.buttons = new ArrayList<Button>();



	}

	private void generateLevel () {
		Random rand = new Random(5000L);
		final float k = 5;
		float y = Platform.PLATFORM_HEIGHT / 2;
		//float maxJumpHeight = Bob.BOB_JUMP_VELOCITY * Bob.BOB_JUMP_VELOCITY / (2 * this.gravity.y);
		float maxJumpHeight = this.PLATFORMS_DISTANCE;
		float minJumpHeight = this.STARS_DISTANCE;
		while (y < WORLD_HEIGHT - WORLD_WIDTH / 2) {
			int type = y>WORLD_HEIGHT/3 && rand.nextFloat() > 0.5f ? Platform.PLATFORM_TYPE_MOVING : Platform.PLATFORM_TYPE_STATIC;
			float x = rand.nextFloat() > 0.5f ? rand.nextFloat() *k : WORLD_WIDTH - rand.nextFloat() *k/2;
			//star generate
			int type_star = Star.STAR_TYPE_STATIC;//star
			float y_star = rand.nextFloat() *100;//star
			float x_star = rand.nextFloat() *10;//star
			Star star = new Star(type_star, x_star, y_star);//star
			stars.add(star);
			//stars.add(star);
			//end star generate
			Platform platform = new Platform(type, x, y*2);
			platforms.add(platform);
			if (rand.nextFloat() > 0.2f && type != Platform.PLATFORM_TYPE_MOVING) {
				Spring spring = new Spring(platform.position.x, platform.position.y + Platform.PLATFORM_HEIGHT / 2 + Spring.SPRING_HEIGHT / 2);
				springs.add(spring);

				//	if (y > WORLD_HEIGHT / 3 && rand.nextFloat() > 0.8f) {
				Squirrel squirrel = new Squirrel(platform.position.x + rand.nextFloat()*k, platform.position.y*3
					+ Squirrel.SQUIRREL_HEIGHT + rand.nextFloat() * k);
				squirrels.add(squirrel);
				//}
			}
			if (rand.nextFloat() > 0.5f) {
				/*Coin coin = new Coin(platform.position.x + (platform.position.x < WORLD_WIDTH /2 ? 5*rand.nextFloat() : -k*rand.nextFloat()), 
					platform.position.y + (rand.nextFloat() > 0.5f ? k*rand.nextFloat() : -k*rand.nextFloat()));
				coins.add(coin);*/
			}
			y += (maxJumpHeight - 0.5f);
			//y -= rand.nextFloat() * (maxJumpHeight / 3);
			if (y > WORLD_HEIGHT / 2) {
				Coin coin = new Coin(platform.position.x + (platform.position.x < WORLD_WIDTH /2 ? 5*rand.nextFloat() : -k*rand.nextFloat()), 
					platform.position.y + (rand.nextFloat() > 0.5f ? k*rand.nextFloat() : -k*rand.nextFloat()));
				coins.add(coin);
			}
		}
		castle = new Castle(WORLD_WIDTH / 2, y);
	}

	public void editPosition(float deltaTime){
		if(decremento)
		{
			level.decremento(deltaTime);
			if(level.isEmpty)
			{
				freezeON=false;
				decremento=false;
			}
		}
		if(!decremento){level.incremento(deltaTime);}

		//if(world.bob.enablenos==1)
		{
			if(decrementonos)
			{
				levelnos.decremento(deltaTime);
				turbo=true;
				Turbo();
				if (rand.nextFloat() < 0.5f) Gdx.input.vibrate(new long[] { 10, 5, 5}, -1); 
				if(levelnos.isEmpty)
				{
					TurboLess();
					decrementonos=false;
				}
			}
			if(!decrementonos){levelnos.incremento(deltaTime);}
		}
	}

	public void setGravity(float x, float y){
		this.gravity.x = x;
		this.gravity.y = y;
		bob.setGravityBob(x, y);
	}

	public void LifeLess(){
		if (--life > 0) {
			if (life == 1) this.texts.offer(new FloatingText("WARNING!", 0));
		} else state = CONSTANTS.GAME_OVER;
	}

	public void LifeMore(){
		life++;
		this.texts.offer(new FloatingText("LIFE +1", 0));
	}

	public void ShotProjectile() {
		if(shot>0){
			Gdx.input.vibrate(new long[] { 1, 20, 10, 20}, -1); 
			Projectile projectile = new Projectile(bob.position.x,bob.position.y, Projectile.WIDTH, Projectile.HEIGHT);
			projectile.setVelocity(0,25);
			projectiles.add(projectile);
			shot=shot-1;
		}
	}
	public Vector2 getGravity(){
		return gravity;
	}

	public void Turbo(){
		if(turbo){
			bob.velocity.y=18;
			turbo=false;
		}
	}

	public void TurboLess()
	{
		bob.velocity.y=12;
	}


	private void addStarDynamic(){
		//star generate
		int type_star = rand.nextFloat()>0.3?Star.STAR_TYPE_STATIC:Star.STAR_TYPE_MOVING;//star light
		float y_star = rand.nextFloat() *10;//star
		float x_star = rand.nextFloat() *10;//star
		Star star = new Star(type_star, x_star, y_star+bob.position.y+13);
		Star star1 = new Star(type_star, x_star, y_star+bob.position.y+9);
		Star star2 = new Star(Star.STAR_TYPE_STATIC, x_star, y_star+bob.position.y-3);//aggiunto da poco
		stars.add(star);
		stars.add(star1);//aggiunto da poco
		stars.add(star2);//aggiunto da poco
		////end star generate
	}

	public void update (float deltaTime, float accelX) {
		switch (this.state) {

		case CONSTANTS.GAME_RUNNING:
			editPosition( deltaTime);
			score += (int)bob.velocity.y/10;
			if (this.freezeON)deltaTime /= 4;
			level.update(deltaTime);
			updateSprings(deltaTime);
			updateTexts(deltaTime);
			updateBob(deltaTime, accelX);
			updatePlatforms(deltaTime);
			updateSquirrels(deltaTime);
			updateCoins(deltaTime);
			addStarDynamic();
			updateStar( deltaTime);
			updateProjectiles(deltaTime);
			updateExplosions(deltaTime);
			updateProjectilenemys(deltaTime);//deltaTime*4 se si vuole mantenere la velocita del proiettile nemico anche durante il freezeing
			updateEnemy(deltaTime,bob);//deltaTime*4 se si vuole mantenere la velocita del nemico anche durante il freezeing
			updateunlockcharacter();
			checkCollisions();
			checkRemoveStars();
			checkRemoveSquirrel();
			checkRemoveExplosions();
			break;

		case CONSTANTS.GAME_LEVEL_END:
			break;
			
		case CONSTANTS.MAIN_MENU:
			break;

		case CONSTANTS.GAME_OVER:
		
			break;

		case CONSTANTS.GAME_PAUSED:
			for (int i = 0; i < buttons.size(); i++) {
				buttons.get(i).update(deltaTime);
			}
		}
	}


	private void updateTexts(float deltaTime) {
		ammotext.update(deltaTime, shot + "x");
		lifetext.update(deltaTime, life + "x");
		scoretext.update(deltaTime, "SCORE = " + score);
		for (int i = 0; i < this.texts.size(); i++) {
			Text text = this.texts.get(i);
			text.update(deltaTime);
			//Gdx.app.debug("floattext", "this.statetime " + text.stateTime + ", this.duration = " + text.duration + ", deltatime = " + deltaTime);
			if (text.duration != -1 && text.stateTime > text.duration) {
				texts.remove(i--);
			}
		}
	}

	private void updateExplosions(float deltaTime) {
		for (Explosion exp : this.explosions) {
			exp.update(deltaTime);
		}
	}
	
	private void updateSprings(float deltaTime) {
		for (Spring spring : this.springs) {
		spring.update(deltaTime);
		}
	}


	private void checkRemoveExplosions() {
		if (!explosions.isEmpty() && explosions.peek().stateTime > explosions.peek().duration)
			explosions.remove(0);
	}



	private void updateBob (float deltaTime, float accelX) {
		//if (bob.state != Bob.BOB_STATE_HIT && bob.position.y <= 0.5f) bob.hitPlatform();
		if (bob.state != Bob.BOB_STATE_HIT) bob.velocity.x = -accelX / 3f * Bob.BOB_MOVE_VELOCITY;
		if(bob.stateTime-bob.bubbletime>3f){
			bob.bubbletime=0;
			bob.enablebubble = false;
		}
		bob.update(deltaTime);
		heightSoFar = Math.max(bob.position.y, heightSoFar);
	}

	private void updateEnemy (float deltaTime, DynamicGameObject oggetto) {
		for (Enemy enemy : enemies) {
			enemy.update(deltaTime, oggetto);
			updateEnemyShotBob(enemy);
			if(bob.position.y>enemy.position.y-10)
			{
				enemy.active=1;
				if(enemy.killtime==0)enemy.killtime=enemy.stateTime;//imposto il killTime x sapere quanto tempo ce voluto x ucc charlie
			}
		}
		if (bob.position.y > WORLD_HEIGHT / 10 && enemies.size() < (this.bob.position.y * 5) / WORLD_HEIGHT) {
			enemies.offer(new Enemy(-10 + this.rand.nextFloat()*20, this.bob.position.y + this.rand.nextFloat()*100));
		}
	}

	private void updateEnemyShotBob(Enemy charlie) {
		//Gdx.app.debug("ENEMYSHOTBOB","init");
		float difficoltxfascio=0.5f;//incrementa x sparare su piu' punti x
		float delaysparo=1f;//decrementa x avere uno sparo piu' veloce

		if ((Math.abs(charlie.position.x-bob.position.x) < difficoltxfascio ) && (charlie.position.y-bob.position.y > 5f &&  charlie.position.y-bob.position.y < 15f) && (charlie.stateTime > charlie.enemyshotime + delaysparo)) {
			charlie.enemyshotime=charlie.stateTime;
			Projectile projectilenemy = new Projectile( charlie.position.x, charlie.position.y, Projectile.WIDTH, Projectile.HEIGHT);
			projectilenemy.state=1;
			projectilenemy.setVelocity(0,-20);
			projectenemy.add(projectilenemy);
			//	Gdx.app.debug("ENEMYSHOTBOB","x"+projectilenemy.position.x + "y"+projectilenemy.position.y);
		}
	}

	private void updateScoreEnemyDied(Enemy charlie)
	{
		
			//Gdx.app.debug("killtime:"+charlie.killtime, "stateTime:"+charlie.stateTime);
			if(charlie.stateTime-charlie.killtime<3f)
			{
				this.texts.offer(new FloatingText("EXCELLENT!",1.5f));
				this.texts.offer(new FloatingText("+1000",0));
				score=score+1000;
				charlie.killtime=-1;
			}
			else if(charlie.stateTime-charlie.killtime<5f)
			{
				this.texts.offer(new FloatingText("FAST!",1.5f));
				this.texts.offer(new FloatingText("+500",0));
				score=score+500;
				charlie.killtime=-1;
			}
			else if(charlie.stateTime-charlie.killtime<7f)
			{
				this.texts.offer(new FloatingText("good!",1.5f));
				this.texts.offer(new FloatingText("+200",0));
				score=score+200;
				charlie.killtime=-1;
			}
			else 
			{
				this.texts.offer(new FloatingText("slow.",1.5f));
				this.texts.offer(new FloatingText("+100",0));
				score=score+100;
				charlie.killtime=-1;
			}

			//charlie.state = Enemy.ENEMY_STATE_REM;//cambio stato di modo da segnalare una volta sola il risultato
		}
	

	private void updateunlockcharacter () //FIXME
	{
		if(Settings.highscores[0]<20000 && !(print1times>0))
		{
			if(score>20000)
			{
				this.texts.offer(new FloatingText("new alien",1.5f));
				print1times=1;
			}
		}
		else if(Settings.highscores[0]<40000 && !(print1times>1))
		{
			if(score>40000)
				this.texts.offer(new FloatingText("new alien.",1.5f));
			print1times=2;
		}
	}



	private void updateStar(float deltaTime){
		int len = stars.size();
		for (int i = 0; i < len; i++) {
			Star star= stars.get(i);
			star.update(deltaTime);
		}
	}

	private void updatePlatforms (float deltaTime) {
		for ( Platform plat : this.platforms) {
			if (plat.type == Platform.PLATFORM_TYPE_MOVING && bob.position.y-plat.position.y>-9){
				Utils.changeGravityTowards(plat, bob);
			}
			plat.update(deltaTime);
		}
		int len = platforms.size();
		for (int i = 0; i < len; i++) {
			Platform platform = platforms.get(i);
			platform.update(deltaTime);
			len = platforms.size();
		}
	}

	private void updateSquirrels (float deltaTime) {
		for (Squirrel squirrel : this.squirrels) {
			squirrel.update(deltaTime);
		}
		//Gdx.input.vibrate(new long[] { 1, 10, 6, 10}, -1); //FIXME
	}

	private void updateCoins (float deltaTime) 
	{
		int len = coins.size();
		for (int i = 0; i < len; i++) {
			Coin coin = coins.get(i);
			coin.update(deltaTime);
			len = coins.size();

		}
	}

	private void updateProjectiles (float deltaTime) {
		int len = projectiles.size();
		for (int i = 0; i < len; i++) {
			Projectile projectile = projectiles.get(i);
			projectile.update(deltaTime);
		}
	}

	private void updateProjectilenemys(float deltaTime) {
		int len = projectenemy.size();
		for (int i = 0; i < len; i++) {
			Projectile projectenemys = projectenemy.get(i);
			projectenemys.update(deltaTime);
		}
	}

	private void checkCollisions () {
		checkPlatformCollisions();
		//checkDoubleJump();
		checkSquirrelCollisions();
		checkItemCollisions();
		checkCastleCollisions();
		checkVelocity();
		checkProjectileCollisions();
		checkProjectileWorldCollisions();
		checkProjectilEnemyCollisions();
		checkProjectilBobCollisions();
		checkRemovePlatform();
		checkRemoveProjectile();
		checkRemoveCoin();
		checkGameOver();
		checkRemoveEnemyProjectile();
		CheckRemoveEnemey();
	}


	private void CheckRemoveEnemey() {
		for (int i = 0; i < enemies.size(); i++){
			Enemy charlie = enemies.get(i);
			if(charlie.life==0 )
			{
				explosions.offer(new Explosion(charlie.position.x, charlie.position.y,Enemy.ENEMY_WIDTH,Enemy.ENEMY_HEIGHT, 0));
				updateScoreEnemyDied(charlie);
				enemies.remove(i);
			}
		}
	}


	private void checkRemoveProjectile(){
		if (!projectiles.isEmpty()) {
			for (int i = 0; i < projectiles.size(); i++) {
				Projectile projectile = projectiles.get(i);
				if ( projectile.position.y > bob.position.y+11 ){ 
					projectiles.remove(i);
				}
			}
		}
	}

	private void checkRemoveEnemyProjectile(){
		if (!projectenemy.isEmpty()) {
			for (int i = 0; i < projectenemy.size(); i++) {
				Projectile projectilenem = projectenemy.get(i);
				if ( projectilenem.position.y < bob.position.y-5 ) 
					projectenemy.remove(i);
			}
		}
	}

	private void checkRemovePlatform() {
		if (!platforms.isEmpty()) { 
			if (bob.position.y > platforms.get(0).position.y+5  ) platforms.remove(0);
		}
	}


	private void checkRemoveStars() {
		if (!stars.isEmpty()) { 
			int len = stars.size();
			for (int i = 0; i < len; i++) {
				Star star = stars.get(i);
				if (bob.position.y > star.position.y+5  ) stars.remove(star);
				len = stars.size();
			}}
	}

	private void checkRemoveCoin() {
		if (!coins.isEmpty()) { 
			if (bob.position.y > coins.get(0).position.y+5  ) coins.remove(0);
		}
	}

	private void checkRemoveSquirrel() {
		if (!squirrels.isEmpty()) { 
			if (bob.position.y > squirrels.get(0).position.y+5  ) squirrels.remove(0);
		}
	}

	private void checkPlatformCollisions () {
		//if (bob.velocity.y > 0) return;
		int len = platforms.size();
		for (int i = 0; i < len; i++) {
			Platform platform = platforms.get(i);
			if (bob.position.y > platform.position.y) {
				if ( OverlapTester.overlapRectangles(bob.bounds, platform.bounds)) {
					bob.hitPlatform();
					Gdx.input.vibrate(new long[] { 1, 20,10, 5}, -1); 
					if(bob.enablebubble == false)
					{	
						LifeLess();
						score -= 300;
					}
					//else score += 300;
					explosions.offer(new Explosion(platform.position.x-Platform.PLATFORM_WIDTH/2, platform.position.y-Platform.PLATFORM_HEIGHT/2,Platform.PLATFORM_WIDTH*2,Platform.PLATFORM_HEIGHT*2,0));
					platforms.remove(platform);
					listener.hit();
					len = platforms.size();
					break;
				}
			}
		}
	}

	private void checkDoubleJump() {
		if(Bob.BOB_DOUBLE_JUMP){
			Bob.BOB_DOUBLE_JUMP=false;
			bob.hitPlatform();
			listener.hit();
		}
	}


	private void checkSquirrelCollisions () { 
		float random=randsquirrel.nextFloat();
		for (int i = 0; i < squirrels.size(); i++) {
			Squirrel squirrel = squirrels.get(i);
			if (!squirrel.inuse && OverlapTester.overlapRectangles(squirrel.bounds, bob.bounds)) {
				Gdx.input.vibrate(new long[] { 1, 10, 6, 10}, -1);
				if(random<=0.30f)	{
					Gdx.app.debug("checkSquirrelCollisions", "vita");
					//squirrel.state=Squirrel.LIFE_CLISION;
					LifeMore();
					//squirrel.inuse=true;
					//this.texts.offer(new FloatingText("vita",0));//FIXME
				} else if(random<= 0.5f && !this.supermissileButton) {    
					Gdx.app.debug("checkSquirrelCollisions", "supermissile");
					/*GameScreen si occupa di controllare il click sul nos x attivarlo*/
					squirrel.state=Squirrel.NOS_CLISION;
					//squirrel.inuse=true;
					//squirrel.nosTap=true;
					//bob.enablenos=1;
					if ((supermissiles+=10) > 0) this.supermissileButton = true;
					this.texts.offer(new FloatingText("supermissile",0));//FIXME

				} else if(random<=0.75f && this.bubbleButton == false && bob.enablebubble==false) {
					Gdx.app.debug("checkSquirrelCollisions", "bolla");
					squirrel.state=Squirrel.BUBBLE_CLISION;
					//bob.enablebubble=2;
					this.bubbleButton = true;
					squirrel.bubbleuse=1;
					squirrel.inuse=true;
					this.texts.offer(new FloatingText("bolla",0));//FIXME
				} else if(random<=0.85f && !this.activemissile) { 
					Gdx.app.debug("checkSquirrelCollisions", "missile");
					if ((missiles+=10) > 0) this.activemissile = true;
					this.texts.offer(new FloatingText("missile",0));//FIXME
					//GameScreen.attivatraj = true;
				} else { 
					Gdx.app.debug("checkSquirrelCollisions", "ammo");
					squirrel.state=Squirrel.PROJ_CLISION;
					shot+=30;
					squirrel.inuse=true;
					this.texts.offer(new FloatingText("ammo!",0));//FIXME
				}   
				listener.hit(); 
				squirrels.remove(squirrel);
				break;
			}
		}
	}


	private void checkItemCollisions () {
		int len = coins.size();
		for (int i = 0; i < len; i++) {
			Coin coin = coins.get(i);
			if (OverlapTester.overlapRectangles(bob.bounds, coin.bounds)) {
				Gdx.input.vibrate(new long[] { 1, 90, 40, 90},-1); 
				if(bob.enablebubble == false)
				{   
					bob.velocity.y=2;
					bob.setGravityBob(0, 3);
					LifeLess();
					nosinuse=0;
					score -= 300;
				}
				else
					score += 300;
				len = coins.size();
				listener.coin();
				explosions.offer(new Explosion(coin.position.x, coin.position.y,Coin.COIN_WIDTH,Coin.COIN_HEIGHT,0));
				coins.remove(coin);
				break;
			}
		}
	
		len = springs.size();
		for (int i = 0; i < len; i++) {
			Spring spring = springs.get(i);
				if (OverlapTester.overlapRectangles(bob.bounds, spring.bounds)) {
					explosions.offer(new Explosion(spring.position.x, spring.position.y,Spring.SPRING_WIDTH*2,Spring.SPRING_HEIGHT*2,1));
					springs.remove(spring);
					break;
				}
			}
		}
	


	private void checkProjectileCollisions(){
		int i = 0, j = 0;
		if (!projectiles.isEmpty() && !platforms.isEmpty()){

			/* Platform platformstmp;
      int len= platforms.size();
      int len1 = projectiles.size();
     if (platforms.get(0).position.y > projectiles.get(0).position.y)
              return;
          else if (OverlapTester.overlapRectangles(platforms.get(0).bounds, projectiles.get(0).bounds)){
              platforms.get(0).pulverize();
              projectiles.remove(0);
              return;
          }
          else {
              Platform primaplatform = platforms.get(i);
              for (; i < projectiles.size(); i++) {
                  //#FIXME
                  for (platformstmp = platforms.get(0); projectiles.get(j).position.y > primaplatform.position.y; j++){
                      if (OverlapTester.overlapRectangles(platformstmp.bounds, projectiles.get(j).bounds)) {
                        platforms.get(j).pulverize();
                         projectiles.remove(i);
                      }
                  }
              }
          }
			 */


			for(i=0;i<projectiles.size();i++)
			{
				for(j=0;j<platforms.size();j++)
				{
					Projectile projectile=projectiles.get(i);
					Platform platform=platforms.get(j);
					if (OverlapTester.overlapRectangles(platform.bounds, projectile.bounds)) {
						bob.hitPlatform();
						//Turbo();
						Gdx.input.vibrate(new long[] { 1, 20,10, 5}, -1); 
						score += 100;
						//turbo=turbo+1;
						//shot=shot+5;
						projectiles.remove(i);
						explosions.offer(new Explosion(platform.position.x-Platform.PLATFORM_WIDTH/2, platform.position.y-Platform.PLATFORM_HEIGHT/2,Platform.PLATFORM_WIDTH*2,Platform.PLATFORM_HEIGHT*2,0));
						platforms.remove(platform);
						//score += 100;
						;
						break;
					}

				}
			}
		}
	}

	private void checkProjectileWorldCollisions(){
		int i = 0, j = 0;
		for(i=0;i<projectiles.size();i++)
		{
			for(j=0;j<coins.size();j++)
			{
				Projectile projectile=projectiles.get(i);
				Coin coin=coins.get(j);
				if ( OverlapTester.overlapRectangles(coin.bounds, projectile.bounds)) {
					Gdx.input.vibrate(new long[] { 1, 20, 40, 20}, -1); 
					score += 100;
					coins.remove(j);
					explosions.offer(new Explosion(projectile.position.x, projectile.position.y,projectile.width,projectile.height,0));
					projectiles.remove(i);

					/*platforms.remove(j);*/
					break;
				}

			}
		}
	}


	private void checkProjectilEnemyCollisions(){
		int i = 0;
		if (!projectiles.isEmpty() ) {
			for (Enemy charlie : enemies){

				for(i=0;i<projectiles.size();i++)
				{
					Projectile projectile=projectiles.get(i);
					if (OverlapTester.overlapRectangles(charlie.bounds, projectile.bounds)) {
						Gdx.input.vibrate(new long[] { 1, 20, 40, 20}, -1); 
						score += 100;
						if((projectile.type==1||projectile.type==2)){
							explosions.offer(new Explosion(charlie.position.x, charlie.position.y,Platform.PLATFORM_WIDTH,Platform.PLATFORM_HEIGHT,0));
							projectiles.remove(projectile);
						}
						else if(projectile.type==0)projectiles.remove(i);
						charlie.life--;
						/*platforms.remove(j);*/
						break;
					}
				}
			}
		}
	}

	private void checkProjectilBobCollisions(){
		if (!projectenemy.isEmpty()){
			for(int i=0;i < projectenemy.size();i++)
			{
				Projectile projectilenem=projectenemy.get(i);
				if ((bob.enablebubble == false)&&OverlapTester.overlapRectangles(bob.bounds, projectilenem.bounds)) {
					Gdx.input.vibrate(new long[] { 1, 20, 40, 20}, -1); 
					score -= 100;
					LifeLess();
					explosions.offer(new Explosion(projectilenem.position.x, projectilenem.position.y,Projectile.WIDTH,Projectile.HEIGHT,0));
					projectenemy.remove(projectilenem);
					/*platforms.remove(j);*/
					break;
				}
			}
		}
	}

	private void checkVelocity () {
		if (bob.velocity.y > bob.MAXVELOCITY && nosinuse==0){
			bob.setGravityBob(0, 0);

		}
	}
	
	private void checkCastleCollisions () {
		/*if (OverlapTester.overlapRectangles(castle.bounds, bob.bounds))*/ 
		if(bob.position.y>castle.position.y){
			state = CONSTANTS.GAME_LEVEL_END;
		}
	}


	private void checkGameOver () {
		if (heightSoFar - 7.5f > bob.position.y) {
			state = CONSTANTS.GAME_OVER;
		}

		if (life<=0){ state = CONSTANTS.GAME_OVER;}
	}

}
