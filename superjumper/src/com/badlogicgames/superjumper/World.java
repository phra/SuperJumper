package com.badlogicgames.superjumper;
/*MODEL*/
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.sun.org.apache.bcel.internal.generic.AllocationInstruction;

public class World {
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
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_NEXT_LEVEL = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;
	public final int PLATFORMS_DISTANCE = 10;
	public final int STARS_DISTANCE = 1;
	public final Bob bob;
	//public Enemy charlie;
	public final List<Platform> platforms;
	public final List<Star> stars;
	public final List<Spring> springs;
	public final List<Squirrel> squirrels;
	public final List<Projectile> projectiles;
	public final List<Projectile> projectenemy;
	public final List<Missile> rockets;
	public final List<Coin> coins;
	public Castle castle;
	public final WorldListener listener;
	public final Random rand;
	public final Random randsquirrel;
	public float heightSoFar;
	public int score;
	public int state;
	public int shot=10;
	public int nosinuse=0;
	public int turbo=1;
	public int life=50;
	public float freeze=100;
	public float signal2screen=0;
	public int print1times=0;
	public float bubbletimes;
	protected boolean freezeON = false;
	private Vector2 gravity = new Vector2(0,15);
	float enemyshotTime=0;
	protected boolean activemissile = false;
	public LinkedList<Enemy> enemies;
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
		this.state = WORLD_STATE_RUNNING;
		this.randsquirrel=new Random();
		this.enemies = new LinkedList<Enemy>();

	}

	private void generateLevel () {
		Random rand = new Random(5000L);
		final float k = 5;
		float y = Platform.PLATFORM_HEIGHT / 2;
		//float maxJumpHeight = Bob.BOB_JUMP_VELOCITY * Bob.BOB_JUMP_VELOCITY / (2 * this.gravity.y);
		float maxJumpHeight = this.PLATFORMS_DISTANCE;
		float minJumpHeight = this.STARS_DISTANCE;
		while (y < WORLD_HEIGHT - WORLD_WIDTH / 2) {
			int type = y>WORLD_HEIGHT/3 ? Platform.PLATFORM_TYPE_MOVING : Platform.PLATFORM_TYPE_STATIC;
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

	public void setGravity(float x, float y){
		this.gravity.x = x;
		this.gravity.y = y;
		bob.setGravityBob(x, y);
	}


	public void update (float deltaTime, float accelX) {
		checkRemoveStars();

		if (this.freezeON) {
			deltaTime /= 4;
		}

		updateBob(deltaTime, accelX);
		updatePlatforms(deltaTime);
		updateSquirrels(deltaTime);
		updateCoins(deltaTime);
		//updateGravityPlanet(deltaTime);
		addStarDynamic();
		updateStar( deltaTime);
		updateProjectiles(deltaTime);
		updateProjectilenemys(deltaTime);//deltaTime*4 se si vuole mantenere la velocità del proiettile nemico anche durante il freezeing
		updateEnemy(deltaTime,bob);//deltaTime*4 se si vuole mantenere la velocità del nemico anche durante il freezeing
		updateunlockcharacter ();
		//if (rand.nextFloat() > 0.5f) 
		score += (int)bob.velocity.y/10;
		//if (bob.state != Bob.BOB_STATE_HIT) //caso in cui bob cade verso il basso quando muore senza sbattere con gli ogg restati
		checkCollisions();
		checkRemovePlatform();
		checkRemoveProjectile();
		checkRemoveCoin();
		checkGameOver();
		checkRemoveEnemyProjectile();
		CheckRemoveEnemey();
		/* checkRemoveSquirrel();
		checkRemoveBubble();*/

	}

	private void updateGravityPlanet(float deltaTime){
		for ( Platform plat : this.platforms) {
			if (plat.type != Platform.PLATFORM_TYPE_MOVING){
				Utils.changeGravityTowards(bob, plat);
			}
		}
	}

	public void LifeLess(){

		if(life>0){
			if(life<=2)signal2screen=3;
			life-=1;
		}
		else state = WORLD_STATE_GAME_OVER;
	}

	public void LifeMore(){
		//if(life<=4) life+=1;
		life++;
	}

	public void ShotProjectile()
	{
		if(shot>0){
			Gdx.input.vibrate(new long[] { 1, 20, 10, 20}, -1); 
			Projectile projectile = new Projectile(bob.position.x,bob.position.y);
			projectile.setVelocity(0,25);
			projectiles.add(projectile);
			shot=shot-1;}

	}
	public Vector2 getGravity(){
		return gravity;
	}

	public void Turbo(){
		if(turbo>=1){
			bob.velocity.y=18;
			turbo-=1;
		}
	}

	public void TurboLess()
	{
		bob.velocity.y=12;
	}


	private void updateBob (float deltaTime, float accelX) {
		//if (bob.state != Bob.BOB_STATE_HIT && bob.position.y <= 0.5f) bob.hitPlatform();
		if (bob.state != Bob.BOB_STATE_HIT) bob.velocity.x = -accelX / 3f * Bob.BOB_MOVE_VELOCITY;
		bob.update(deltaTime);
		heightSoFar = Math.max(bob.position.y, heightSoFar);
	}

	private void updateEnemy (float deltaTime, DynamicGameObject oggetto) {
		for (Enemy enemy : enemies) {
			enemy.update(deltaTime, oggetto);
			EnemyShotBob(enemy);
			if(bob.position.y>enemy.position.y-10)
			{
				enemy.active=1;

				if(enemy.killtime==0)enemy.killtime=enemy.stateTime;//imposto il killTime x sapere quanto tempo cè voluto x ucc charlie
			}
			if(enemy.life==0 && enemy.pulverizetime==0)
			{
				enemy.state = Enemy.ENEMY_STATE_DIE;//cabio stato x polverizzarlo e segnalare a schermo il punteggio
				enemy.pulverizetime= enemy.stateTime;//setto pulverizeTime da ora x la durata della polverizzazione
			}
			ScoreEnemyDied(enemy);
		}
		if (bob.position.y > 500 && enemies.size() < 2) {
			enemies.offer(new Enemy(-10 + this.rand.nextFloat()*20, this.bob.position.y + this.rand.nextFloat()*100));
		}
	}

	private void EnemyShotBob(Enemy charlie)
	{
		float difficoltxfascio=0.5f;//incrementa x sparare su piu' punti x
		float delaysparo=0.8f;//decrementa x avere uno sparo piu' veloce
		if(rand.nextFloat()>0.3f && rand.nextFloat()>0.5f && 
			charlie.position.x-bob.position.x <difficoltxfascio && charlie.position.y-bob.position.y <9f &&
			enemyshotTime<charlie.stateTime-delaysparo)
		{
			if(enemyshotTime<charlie.stateTime)enemyshotTime=charlie.stateTime;
			Projectile projectilenemy = new Projectile( charlie.position.x, charlie.position.y);
			projectilenemy.state=1;
			projectilenemy.setVelocity(0,-20);
			projectenemy.add(projectilenemy);
			Gdx.app.debug("ENEMYSHOTBOB","x"+projectilenemy.position.x + "y"+projectilenemy.position.y);
		}
	}

	private void ScoreEnemyDied(Enemy charlie)
	{
		if(charlie.state==Enemy.ENEMY_STATE_DIE)
		{
			//Gdx.app.debug("killtime:"+charlie.killtime, "stateTime:"+charlie.stateTime);
			if(charlie.stateTime-charlie.killtime<3f)
			{
				signal2screen=7;
				score=score+1000;
				charlie.killtime=-1;
			}
			else if(charlie.stateTime-charlie.killtime<5f)
			{
				signal2screen=6;
				score=score+500;
				charlie.killtime=-1;
			}
			else if(charlie.stateTime-charlie.killtime<7f)
			{
				signal2screen=5;
				score=score+200;
				charlie.killtime=-1;
			}
			else 
			{
				signal2screen=4;
				score=score+100;
				charlie.killtime=-1;
			}
			charlie.state = Enemy.ENEMY_STATE_REM;//cambio stato di modo da segnalare una volta sola il risultato
		}
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
			if (platform.state == Platform.PLATFORM_STATE_PULVERIZING && platform.stateTime > Platform.PLATFORM_PULVERIZE_TIME) { //FIXME
				platforms.remove(platform);
				len = platforms.size();
			}
		}
	}




	private void updateSquirrels (float deltaTime) {
		int len = squirrels.size();
		for (int i = 0; i < len; i++) {
			Squirrel squirrel = squirrels.get(i);
			squirrel.update(deltaTime);
			if(squirrel.state==Squirrel.BUBBLE_CLISION && squirrel.inuse==false){
				squirrels.remove(squirrel);
				bob.enablebubble=0;
			}
			else if(squirrel.state==Squirrel.NOS_CLISION && squirrel.inuse==false){
				TurboLess();
				nosinuse=0;
				squirrels.remove(squirrel);
				bob.enablenos=0;
			}
			else if(squirrel.state==Squirrel.LIFE_CLISION && squirrel.inuse==false||
				squirrel.state==Squirrel.PROJ_CLISION && squirrel.inuse==false)
			{
				squirrels.remove(squirrel);
			}
			else if(squirrel.state==Squirrel.NOS_CLISION && nosinuse==1)Gdx.input.vibrate(new long[] { 1, 10, 6, 10}, -1);
			len = squirrels.size();
		}
	}

	private void updateCoins (float deltaTime) 
	{
		int len = coins.size();
		for (int i = 0; i < len; i++) {
			Coin coin = coins.get(i);
			coin.update(deltaTime);
			if (coin.state == Coin.COIN_STATE_PULVERIZING && coin.stateTime > Coin.COIN_PULVERIZE_TIME) {
				coins.remove(coin);
				len = coins.size();

			}
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

	private void CheckRemoveEnemey() {
		for (int i = 0; i < enemies.size(); i++){
			Enemy charlie = enemies.get(i);
			if (charlie.state == Enemy.ENEMY_STATE_REM && charlie.stateTime - charlie.pulverizetime > Enemy.ENEMY_PULVERIZE_TIME) //FIXME
				enemies.remove(charlie);
		}
	}


	private void checkRemoveProjectile(){
		if (!projectiles.isEmpty()) {
			for (int i = 0; i < projectiles.size(); i++) {
				Projectile projectile = projectiles.get(i);
				if ( projectile.position.y > bob.position.y+11 ) 
					projectiles.remove(projectile);
			}
		}
	}

	private void checkRemoveEnemyProjectile(){
		if (!projectenemy.isEmpty()) {
			for (int i = 0; i < projectenemy.size(); i++) {
				Projectile projectilenem = projectenemy.get(i);
				if ( projectilenem.position.y < bob.position.y-5 ) 
					projectenemy.remove(projectilenem);
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
	}



	private void checkPlatformCollisions () {
		//if (bob.velocity.y > 0) return;
		int len = platforms.size();
		for (int i = 0; i < len; i++) {
			Platform platform = platforms.get(i);
			if (bob.position.y > platform.position.y) {
				if (platform.state != Platform.PLATFORM_STATE_PULVERIZING && OverlapTester.overlapRectangles(bob.bounds, platform.bounds)) {
					bob.hitPlatform();

					Gdx.input.vibrate(new long[] { 1, 20,10, 5}, -1); 
					if(bob.enablebubble!=1)
					{	
						LifeLess();
						score -= 300;
					}
					else score += 300;

					platform.pulverize();
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
					squirrel.state=Squirrel.LIFE_CLISION;
					LifeMore();
					squirrel.inuse=true;
					signal2screen=2;
					break;
				} else if(random<= 0.5f && bob.enablenos!=1) {    
					Gdx.app.debug("checkSquirrelCollisions", "nos");
					/*GameScreen si occupa di controllare il click sul nos x attivarlo*/
					squirrel.state=Squirrel.NOS_CLISION;
					squirrel.inuse=true;
					squirrel.nosTap=true;
					bob.enablenos=1;
					break;
				} else if(random<=0.75f && !(bob.enablebubble>0))	{
					Gdx.app.debug("checkSquirrelCollisions", "bolla");
					squirrel.state=Squirrel.BUBBLE_CLISION;
					bob.enablebubble=2;
					squirrel.bubbleuse=1;
					squirrel.inuse=true;
					break;
				} else if(random<=0.85f) { 
					Gdx.app.debug("checkSquirrelCollisions", "missile");
					this.activemissile = true;
					//GameScreen.attivatraj = true;
				} else { 
					Gdx.app.debug("checkSquirrelCollisions", "ammo");
					squirrel.state=Squirrel.PROJ_CLISION;
					shot=shot+10;
					squirrel.inuse=true;
					signal2screen=1;
					break;
				}   
				listener.hit(); 
				squirrels.remove(squirrel);
				i--;
				break;
			}
		}
	}


	private void checkItemCollisions () {
		int len = coins.size();
		for (int i = 0; i < len; i++) {
			Coin coin = coins.get(i);
			if (coin.state != Coin.COIN_STATE_PULVERIZING && OverlapTester.overlapRectangles(bob.bounds, coin.bounds)) {
				Gdx.input.vibrate(new long[] { 1, 90, 40, 90},-1); 
				if(bob.enablebubble!=1)
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
				coin.pulverize();
				listener.coin();
				coins.remove(coin);
				break;
			}
		}
		if (bob.velocity.y > 0) return;
		len = springs.size();
		for (int i = 0; i < len; i++) {
			Spring spring = springs.get(i);
			if (bob.position.y > spring.position.y) {
				if (OverlapTester.overlapRectangles(bob.bounds, spring.bounds)) {
					bob.hitSpring();
					listener.highJump();
					break;
				}
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
					if (platform.state != Platform.PLATFORM_STATE_PULVERIZING && 
						OverlapTester.overlapRectangles(platform.bounds, projectile.bounds)) {
						bob.hitPlatform();
						//Turbo();
						Gdx.input.vibrate(new long[] { 1, 20,10, 5}, -1); 
						score += 100;
						//turbo=turbo+1;
						//shot=shot+5;
						projectiles.remove(i);
						platform.pulverize();
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
		if (!projectiles.isEmpty() && !coins.isEmpty()){

			for(i=0;i<projectiles.size();i++)
			{
				for(j=0;j<coins.size();j++)
				{
					Projectile projectile=projectiles.get(i);
					Coin coin=coins.get(j);
					if (coin.state != Coin.COIN_STATE_PULVERIZING && OverlapTester.overlapRectangles(coin.bounds, projectile.bounds)) {
						Gdx.input.vibrate(new long[] { 1, 20, 40, 20}, -1); 
						score += 100;
						coin.pulverize(); 
						coins.remove(j);
						projectiles.remove(i);
						i--;
						/*platforms.remove(j);*/
						break;
					}

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
						projectiles.remove(i);
						charlie.life--;
						i--;
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
				if ((bob.enablebubble!=1)&&OverlapTester.overlapRectangles(bob.bounds, projectilenem.bounds)) {
					Gdx.input.vibrate(new long[] { 1, 20, 40, 20}, -1); 
					score -= 100;
					LifeLess();
					projectiles.remove(projectilenem);
					i--;
					/*platforms.remove(j);*/
					break;
				}
			}
		}
	}


	private void updateunlockcharacter () 
	{
		if(Settings.highscores[0]<20000 && !(print1times>=2))
		{
			if(score>20000)
			{
				signal2screen=8;
			}
		}
		else if(Settings.highscores[0]<40000 && !(print1times>=3))
		{
			if(score>40000)signal2screen=9;
		}
	}

	private void checkVelocity () {
		if (bob.velocity.y > bob.MAXVELOCITY && nosinuse==0){
			bob.setGravityBob(0, 0);

		}
	}

	public void bubbleActivate(){
		int len = squirrels.size();
		for (int i = 0; i < len; i++) {
			Squirrel squirrel = squirrels.get(i);
			if(squirrel.bubbleuse==1){
				if(squirrels.get(i).crashtime==0)squirrels.get(i).crashtime=squirrels.get(i).stateTime;
				Gdx.input.vibrate(new long[] { 1, 10, 6, 10}, -1);
				bob.enablebubble=1;
				squirrel.bubbleuse=2;
			}
		}
	}

	public void nosActivate(){
		int len = squirrels.size();
		for (int i = 0; i < len; i++) {
			Squirrel squirrel = squirrels.get(i);
			if(squirrel.nosTap==true){
				if(squirrels.get(i).nostime==0)squirrels.get(i).nostime=squirrels.get(i).stateTime;
				Turbo();
				turbo=turbo+1;
				squirrel.nosTap=false;
				nosinuse=1;
			}
		}
	}

	private void checkCastleCollisions () {
		/*if (OverlapTester.overlapRectangles(castle.bounds, bob.bounds))*/ 
		if(bob.position.y>castle.position.y){
			state = WORLD_STATE_NEXT_LEVEL;
		}
	}


	private void checkGameOver () {
		if (heightSoFar - 7.5f > bob.position.y) {
			state = WORLD_STATE_GAME_OVER;
		}

		if (life<=0){ state = WORLD_STATE_GAME_OVER;}
	}

}
