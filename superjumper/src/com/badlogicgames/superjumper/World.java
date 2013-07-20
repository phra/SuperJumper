package com.badlogicgames.superjumper;
/*MODEL*/
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.PauseableThread;

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
	public final LinkedList<Platform> platforms;
	public final LinkedList<Star> stars;
	public final LinkedList<Galaxy> galaxies;
	public final LinkedList<Spring> springs;
	public final LinkedList<Squirrel> squirrels;
	public final List<Projectile> projectiles;
	public final List<Projectile> projectenemy;
	public final List<Missile> rockets;
	public final LinkedList<Coin> coins;
	public final List<Button> buttons;
	public Castle castle;
	public final Random rand;
	public Random randgenerate = new Random();
	public float heightSoFar;
	public int score;
	public int state;
	public int shot=10;
	public int nosinuse=0;
	public int premiumlife=0;
	public boolean turbo=true;
	public int life=10;
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
	public Shake shakera=new Shake();
	public LinkedList<Text> texts;
	public LinkedList<Text> semaforo;
	Boolean decremento=false;
	Boolean decrementonos=false;
	LevelOption level=new LevelOption();
	LevelOption levelnos=new LevelOption();
	Semaphore start;
	LinkedList<Explosion> explosions = new LinkedList<Explosion>();
	public Text scoretext = new Text(SCOREPOSITIONX,SCOREPOSITIONY,"SCORE: 0");
	public Text ammotext = new Text(AMMOPOSITIONX,AMMOPOSITIONY,"0x");
	public Text lifetext = new Text(LIFEPOSITIONX,LIFEPOSITIONY,"0x");


	public World () {
		this.bob = new Bob(4, 2);
		this.galaxies=new LinkedList<Galaxy>();
		this.platforms = new LinkedList<Platform>();
		this.stars = new LinkedList<Star>();
		this.projectiles = new ArrayList<Projectile>();
		this.springs = new LinkedList<Spring>();
		this.squirrels = new LinkedList<Squirrel>();
		this.coins = new LinkedList<Coin>();
		this.projectenemy=new ArrayList<Projectile>();
		this.rockets=new ArrayList<Missile>();
		this.rand = new Random();
		this.generateLevel();
		this.setGravity(0, 3);
		this.heightSoFar = 0;
		this.score = 0;
		this.state = CONSTANTS.GAME_READY;
		this.enemies = new LinkedList<Enemy>();
		this.texts = new LinkedList<Text>();
		this.semaforo = new LinkedList<Text>();
		this.texts.offer(scoretext);
		this.texts.offer(ammotext);
		this.texts.offer(lifetext);
		this.semaforo.offer(new Text(SCREENWIDTH/2,SCREENHEIGHT/2+SCREENHEIGHT/3,"3"));
		this.semaforo.offer(new Text(SCREENWIDTH/2,SCREENHEIGHT/2+SCREENHEIGHT/3,"2"));
		this.semaforo.offer(new Text(SCREENWIDTH/2,SCREENHEIGHT/2+SCREENHEIGHT/3,"1"));
		this.start = new Semaphore(semaforo);
		this.buttons = new ArrayList<Button>();
		this.springs.offer(new Spring(randgenerate.nextFloat() * WORLD_WIDTH, 18));
		this.squirrels.offer(new Squirrel(randgenerate.nextFloat() * WORLD_WIDTH, 20 + randgenerate.nextFloat() * 10));
		this.coins.offer(new Coin(WORLD_WIDTH*randgenerate.nextFloat(), 10));
	}

	protected void generateLevel () {
		castle = new Castle(WORLD_WIDTH / 2, WORLD_HEIGHT - 10);
	}

	public void generateSemaphore(float deltaTime){
		if(!semaforo.isEmpty())
			semaforo.getFirst().update(deltaTime);
		if(semaforo.isEmpty()){
			this.texts.offer(new FloatingText("GO!",0.6f));
			Assets.playSound(Assets.soundShot);
			this.state=CONSTANTS.GAME_RUNNING;
		}
		semaforo=start.updateCount(deltaTime);
	}


	private void generateStars(){
		//star generate
		if (stars.size() < 512) {
			int type_star = rand.nextFloat()>0.3?Star.STAR_TYPE_STATIC:Star.STAR_TYPE_MOVING;//star light
			float y_star = rand.nextFloat() *10;//star
			float x_star = rand.nextFloat() *10;//star
			Star star = new Star(type_star, x_star, y_star+bob.position.y+13);
			Star star1 = new Star(type_star, x_star, y_star+bob.position.y+9);
			Star star2 = new Star(Star.STAR_TYPE_STATIC, x_star, y_star+bob.position.y-3);//aggiunto da poco
			stars.offer(star);
			stars.offer(star1);//aggiunto da poco
			stars.offer(star2);//aggiunto da poco
			////end star generate
		}
	}

	private void generateGalaxies(){
		//galaxies generate
		if (galaxies.size() < 5) {
			int type_galaxies = rand.nextFloat()>0.5?Galaxy.GALAXY_TYPE_STATIC:Galaxy.GALAXY_TYPE_MOVING;//star light
			float y_galaxies = rand.nextFloat() *10;//star
			float x_galaxies = rand.nextFloat() * -2;//star
			float size = rand.nextFloat() * 25f;
			//size=size >10f?rand.nextFloat() * 15f:size;
			Galaxy galaxy = new Galaxy(type_galaxies, x_galaxies, y_galaxies+bob.position.y+13,size,size);
			galaxies.offer(galaxy);
			////end galaxies generate
		}
	}

	public void generatePlatforms(){
		float difficultnumber=80;
		int type;
		if(bob.position.y>WORLD_HEIGHT/2){
			difficultnumber=130;
			type=randgenerate.nextFloat()>0.5f ? Platform.PLATFORM_TYPE_MOVING : Platform.PLATFORM_STATE_CIRCLE;
		}
		else type=Platform.PLATFORM_TYPE_STATIC;
		if (bob.position.y > 10 && platforms.size() < (this.bob.position.y/(WORLD_HEIGHT/difficultnumber)) ) {
			platforms.offer(new Platform(type,-10 + this.randgenerate.nextFloat()*20, this.bob.position.y + this.randgenerate.nextFloat()*100+10));
		}

	}

	private void generateCoins() {
		if (bob.position.y > WORLD_HEIGHT / 2 && coins.size() < 3) {
			final int k = 10;
			float x1 = randgenerate.nextFloat() * WORLD_WIDTH;
			if (coins.isEmpty() || !coins.isEmpty() && coins.getLast().position.y < bob.position.y + k) 
				coins.offer( new Coin(x1 + (x1 < WORLD_WIDTH /2 ? k*randgenerate.nextFloat() : -k*randgenerate.nextFloat()), bob.position.y + (randgenerate.nextFloat() > 0.5f ? k*randgenerate.nextFloat() : -k*randgenerate.nextFloat())));
		}
	}

	private void generateSprings() {
		final int k = 15;
		if (springs.isEmpty() || !this.springs.isEmpty() && springs.size() < 2 && this.springs.getLast().position.y < bob.position.y + k*2){
			float x_rand=randgenerate.nextFloat() * WORLD_WIDTH;
			float y_rand= bob.position.y + k*2;
//			springs.offer(new Spring( x_rand > WORLD_WIDTH /2? -x_rand - 2f : x_rand + 2f ,y_rand));
			springs.offer(new Spring( x_rand > 7.5f? x_rand - 2f : x_rand + 1f ,y_rand));
			platforms.offer(new Platform(Platform.PLATFORM_STATE_CIRCLE,springs.getLast()));
		}		
	}

	private void generateSquirrels() {
		final int k = 50;
		if (squirrels.isEmpty() || !this.squirrels.isEmpty() && squirrels.size() < 2 && squirrels.getLast().position.y < bob.position.y + k){
			float x_rand=randgenerate.nextFloat() * WORLD_WIDTH;
			float y_rand= bob.position.y + k*2;
			squirrels.offer(new Squirrel(x_rand > 9f? x_rand - 2f : x_rand  ,y_rand));
//			squirrels.offer(new Squirrel(randgenerate.nextFloat() * WORLD_WIDTH, bob.position.y+20 + randgenerate.nextFloat() * k));
		}
	}


	public void editPosition(float deltaTime){
		if(decremento){
			level.decremento(deltaTime);
			if(level.isEmpty){
				freezeON=false;
				decremento=false;
			}
		}
		if(!decremento){level.incremento(deltaTime);}
		if(decrementonos){
			levelnos.decremento(deltaTime);
			turbo=true;
			Turbo();
			Gdx.input.vibrate(new long[] { 5, 3, 3}, -1); 
			if(levelnos.isEmpty){
				TurboLess();
				decrementonos=false;
			}
		}
		if(!decrementonos){levelnos.incremento(deltaTime);}}


	public void setGravity(float x, float y){
		this.gravity.x = x;
		this.gravity.y = y;
		bob.setGravityBob(x, y);
	}

	public Vector2 getGravity(){
		return gravity;
	}

	public void LifeLess(){
		if (--life > 0) {
			if (life == 1) this.texts.offer(new FloatingText("WARNING!", 0));
		} else state = CONSTANTS.GAME_OVER;
	}


	private void LifeMore(){
		life++;
		this.texts.offer(new FloatingText("life +1", 0));
	}

	public void Turbo() {
		if(turbo){
			bob.velocity.y=18;
			turbo=false;
		}
	}

	public void TurboLess()	{
		bob.velocity.y=12;
	}

	public void PremiumLife()	{
		if(premiumlife>=10){
			this.texts.offer(new FloatingText("premium life +5", 0));
			life=life+5;
			premiumlife=0;
		}
	}

	public void Slows(){
		bob.velocity.y=4;
		bob.setGravityBob(0, 3);
	}

	public void ShotProjectile() {
		if(shot>0){
			Assets.playSound(Assets.soundShot);
			Gdx.input.vibrate(new long[] { 1, 20, 10, 20}, -1); 
			Projectile projectile = new Projectile(bob.position.x,bob.position.y, Projectile.WIDTH, Projectile.HEIGHT);
			projectile.setVelocity(0,25);
			projectiles.add(projectile);
			shot=shot-1;
		}
	}



	public void update (float deltaTime, float accelX) {
		switch (this.state) {

		case CONSTANTS.GAME_RUNNING:
			editPosition(deltaTime);
			score += (int)bob.velocity.y/10;
			if (this.freezeON) deltaTime /= 4;
			generatePlatforms();
			generateGalaxies();
			generateStars();
			generateSprings();
			generateSquirrels();
			generateCoins();
			level.update(deltaTime);
			PremiumLife();
			updateSprings(deltaTime);
			updateTexts(deltaTime);
			updateBob(deltaTime, accelX);
			updatePlatforms(deltaTime);
			updateSquirrels(deltaTime);
			updateCoins(deltaTime);
			updateGalaxy(deltaTime);
			updateStar(deltaTime);
			updateProjectiles(deltaTime);
			updateExplosions(deltaTime);
			updateProjectilenemys(deltaTime);
			updateEnemy(deltaTime,bob);
			updateunlockcharacter();
			checkCollisions();
			checkVelocity();
			break;

		case CONSTANTS.GAME_LEVEL_END:
			break;

		case CONSTANTS.MAIN_MENU:
			break;

		case CONSTANTS.GAME_OVER:
			System.out.println("Stato Game Over in World");
			break;

		case CONSTANTS.GAME_READY:
			generateSemaphore( deltaTime);
			break;

		case CONSTANTS.GAME_PAUSED:
			for (Button button : this.buttons)
				button.update(deltaTime);
			break;
		}
	}


	public void updateTexts(float deltaTime) {
		ammotext.update(deltaTime, shot + "x");
		lifetext.update(deltaTime, life + "x");
		scoretext.update(deltaTime, "SCORE = " + score);
		for (int i = 0; i < this.texts.size() && i >= 0; i++) {
			Text text = this.texts.get(i);
			text.update(deltaTime);
			if (text.duration != -1 && text.stateTime > text.duration)
				texts.remove(i--);
		}
	}

	private void updateExplosions(float deltaTime) {
		if (!explosions.isEmpty() && explosions.peek().stateTime > explosions.peek().duration)
			explosions.remove(0);
		for (Explosion exp : this.explosions) {
			exp.update(deltaTime);
		}
	}

	private void updateSprings(float deltaTime) {
		if (springs.size() > 1 && springs.getLast().position.y < bob.position.y - 10)
			springs.remove(0);
		for (Spring spring : this.springs) {
			spring.update(deltaTime);
		}
	}

	private void updateBob (float deltaTime, float accelX) {
		if (bob.state != Bob.BOB_STATE_HIT) bob.velocity.x = -accelX / 5f * Bob.BOB_MOVE_VELOCITY;
		if(bob.stateTime-bob.bubbletime>7f && bob.enablebubble){
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
		Assets.playSound(Assets.soundExplosion1);
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
		if(Settings.firstScore()<20000 && !(print1times>0)){
			if(this.score>20000){
				this.texts.offer(new FloatingText("Alien Unlocked",1.5f));
				print1times=1;
			}
		}
		else if(Settings.firstScore()< 70000 && !(print1times>1)){
			if(this.score>70000){
				this.texts.offer(new FloatingText("Militar Alien unlocked.",1.5f));
				print1times=2;
			}
		}
	}

	private void updateStar(float deltaTime){
		/*if (stars.size() > 1 && bob.position.y > stars.peek().position.y+5) {
			stars.remove(0);
		}*/
		for (int i = 0; i < stars.size() && i >= 0; i++) {
			Star star=stars.get(i);
			star.update(deltaTime);
			if (star.position.y < bob.position.y-5){ 
				stars.remove(i--);}
		}
	}

	private void updateGalaxy(float deltaTime){
		for (int i = 0; i < galaxies.size() && i >= 0; i++) {
			Galaxy galaxy = galaxies.get(i);
			galaxy.update(deltaTime);
			if (galaxy.position.y < bob.position.y-30){ 
				galaxies.remove(i--);}
		}
	}

	private void updatePlatforms (float deltaTime) {
		if (platforms.size() > 1 && bob.position.y > platforms.get(0).position.y+5) 
			platforms.remove(0);
		for (int i = 0; i < platforms.size() && i >= 0; i++) {
			Platform plat = platforms.get(i);

			if (plat.type == Platform.PLATFORM_TYPE_MOVING && bob.position.y-plat.position.y>-9){
				Utils.changeGravityTowards(plat, bob);
			}

			plat.update(deltaTime);
//			if (plat.position.y < bob.position.y-5){ 
//				platforms.remove(i--);
//			}
		}

	}
	private void updateSquirrels (float deltaTime) {
		if (squirrels.size() > 1 && squirrels.peek().position.y < bob.position.y - 5 ) 
			squirrels.remove(0);
		for (Squirrel squirrel : this.squirrels) {
			squirrel.update(deltaTime);
		}
		//Gdx.input.vibrate(new long[] { 1, 10, 6, 10}, -1); //FIXME
	}

	private void updateCoins (float deltaTime) {
		if (coins.size() > 1 && coins.peek().position.y < bob.position.y - 10)
			coins.remove(0);
		for (Coin coin : coins) {
			coin.update(deltaTime);
		}
	}

	private void updateProjectiles (float deltaTime) {
		for (int i = 0; i < projectiles.size() && i >= 0; i++) {
			Projectile projectile = projectiles.get(i);
			projectile.update(deltaTime);
			if (projectile.position.y > bob.position.y+11){ 
				projectiles.remove(i--);
			}
		}
	}


	private void updateProjectilenemys(float deltaTime) {
		for (int i = 0; i < projectenemy.size() && i >= 0; i++) {
			Projectile projectenemys = projectenemy.get(i);
			projectenemys.update(deltaTime);
			if ( projectenemys.position.y < bob.position.y-5) 
				projectenemy.remove(i--);
		}
	}

	private void checkCollisions () {
		checkProjectileEnemyCollisions();
		checkPlatformCollisions();
		checkSquirrelCollisions();
		checkItemCollisions();
		checkCastleCollisions();
		checkProjectileCollisions();
		checkProjectileWorldCollisions();
		checkProjectilEnemyCollisions();
		checkProjectilBobCollisions();
		checkGameOver();
		CheckRemoveEnemey();
	}

	private void CheckRemoveEnemey() {
		for (int i = 0; i < enemies.size() && i >= 0; i++){
			Enemy charlie = enemies.get(i);
			if(charlie.life==0) {
				explosions.offer(new Explosion(charlie.position.x, charlie.position.y,Enemy.ENEMY_WIDTH,Enemy.ENEMY_HEIGHT, 0));
				updateScoreEnemyDied(charlie);
				enemies.remove(i--);
			}
		}
	}


	private void checkPlatformCollisions () {
		for (int i = 0; i < platforms.size() && i >= 0; i++) {
			Platform platform = platforms.get(i);
			if (bob.position.y > platform.position.y) {
				if ( OverlapTester.overlapRectangles(bob.bounds, platform.bounds)) {
					if(bob.enablebubble == false){	
						Slows();
						LifeLess();
						shakera.shakethis(1.5f);
						score -= 300;
						Assets.playSound(Assets.soundExplosion);
					}
					Gdx.input.vibrate(new long[] { 1, 10,5, 5}, -1); 
					if(platform.type == Platform.PLATFORM_STATE_CIRCLE)
						explosions.offer(new Explosion(platform.position.x-Platform.PLATFORM_WIDTH/2, platform.position.y-Platform.PLATFORM_HEIGHT/2,Platform.PLATFORM_WIDTH*2,Platform.PLATFORM_HEIGHT*2,0));
					else explosions.offer(new Explosion(platform.position.x-0.75f, platform.position.y-0.75f ,UI.SPRING_WIDTH*2, UI.SPRING_HEIGHT*2, 0.25f));
					platforms.remove(i--);
					Assets.playSound(Assets.soundExplosion);
					break;
				}
			}
		}
	}

	private void checkSquirrelCollisions () { 
		float random = rand.nextFloat();
		for (int i = 0; i < squirrels.size() && i >= 0; i++) {
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
					this.texts.offer(new FloatingText("bubble",0));//FIXME
				} else if(random<=0.85f && !this.activemissile) { 
					Gdx.app.debug("checkSquirrelCollisions", "missile");
					if ((missiles+=10) > 0) this.activemissile = true;
					this.texts.offer(new FloatingText("rockets +10",0));//FIXME
					//GameScreen.attivatraj = true;
				} else { 
					Gdx.app.debug("checkSquirrelCollisions", "ammo");
					squirrel.state=Squirrel.PROJ_CLISION;
					shot+=30;
					squirrel.inuse=true;
					this.texts.offer(new FloatingText("munitions +30!",0));//FIXME
				}   
				Assets.playSound(Assets.coinSound);
				squirrels.remove(i--);
				break;
			}
		}
	}


	private void checkItemCollisions () {
		for (int i = 0; i < coins.size() && i >= 0; i++) {
			Coin coin = coins.get(i);
			if (OverlapTester.overlapRectangles(bob.bounds, coin.bounds)) {
				Gdx.input.vibrate(new long[] { 1, 50, 20, 50},-1); 
				if(bob.enablebubble == false)	{   
					LifeLess();
					nosinuse=0;
					score -= 300;
				} //else score += 300;
				explosions.offer(new Explosion(coin.position.x, coin.position.y,Coin.COIN_WIDTH,Coin.COIN_HEIGHT,0));
				coins.remove(i--);
				break;
			}
		}
		for (int i = 0; i < springs.size() && i >= 0; i++) {
			Spring spring = springs.get(i);
			if (OverlapTester.overlapRectangles(bob.bounds, spring.bounds)) {
//				Assets.playSound(Assets.soundExplosion1);
				Assets.playSound(Assets.coinSound);
				score += 300;
				premiumlife++;
//				Gdx.app.debug("premiumlife= ", "pl= "+premiumlife);
//				explosions.offer(new Explosion(spring.position.x-0.75f, spring.position.y-0.75f ,UI.SPRING_WIDTH*2, UI.SPRING_HEIGHT*2, 0.25f));
				springs.remove(i--);
				break;
			}
		}
	}



	private void checkProjectileCollisions(){

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
		int i = 0, j = 0;
		boolean flag = true;
		for(i=0;i<projectiles.size() && i >= 0 && flag;i++)	{
			Projectile projectile=projectiles.get(i);
			for(j=0;j<platforms.size() && j >= 0;j++) {
				Platform platform=platforms.get(j);
				if (OverlapTester.overlapRectangles(platform.bounds, projectile.bounds)) {
					Assets.playSound(Assets.soundExplosion1);
					Gdx.input.vibrate(new long[] { 1, 20}, -1); 
					score += 100;
					projectiles.remove(i--);
					explosions.offer(new Explosion(platform.position.x-Platform.PLATFORM_WIDTH/2, platform.position.y-Platform.PLATFORM_HEIGHT/2,Platform.PLATFORM_WIDTH*2,Platform.PLATFORM_HEIGHT*2,0));
					platforms.remove(j--);
					//score += 100;
					flag = false;
					break;
				}
			}
		}
	}

	private void checkProjectileEnemyCollisions(){
		int i = 0, j = 0;
		boolean flag = true;
		for(i=0;i<projectenemy.size() && i >= 0 && flag;i++)	{
			Projectile projectile=projectenemy.get(i);
			for(j=0;j<platforms.size() && j >= 0;j++) {
				Platform platform=platforms.get(j);
				if (OverlapTester.overlapRectangles(platform.bounds, projectile.bounds)) { 
					projectenemy.remove(i--);
					explosions.offer(new Explosion(platform.position.x-Platform.PLATFORM_WIDTH/2, platform.position.y-Platform.PLATFORM_HEIGHT/2,Platform.PLATFORM_WIDTH*2,Platform.PLATFORM_HEIGHT*2,0));
					platforms.remove(j--);
					flag = false;
					break;
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
						else if(projectile.type==0)projectiles.remove(projectile);
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


	public void checkGameOver () {

		if (life<=0){ state = CONSTANTS.GAME_OVER;}
	}

}
