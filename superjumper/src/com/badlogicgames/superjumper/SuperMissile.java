package com.badlogicgames.superjumper;

import java.util.LinkedList;
import java.util.List;

public class SuperMissile extends Missile {
	public static float HEIGHT = 1f;
	public static float WIDTH = 1f;
	private static final float DISTANCE = 10f;
	private final List<Projectile> list;
	private boolean flag = true;
	private final LinkedList<Enemy> enemylist;
	public static final int TYPE = 2;
	public int type;

	public SuperMissile (float x, float y, float width, float height, GameObject target, List<Projectile> projectiles, LinkedList<Enemy> enemylist) {
		super(x, y, width, height, target);
		this.list = projectiles;
		this.enemylist = enemylist;
		this.type = TYPE;
	}

	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
		if (flag && Utils.distance(this, target) < DISTANCE) {
			flag = false;
			this.type = Missile.TYPE;
			for (Enemy enemy : enemylist){
				if (this.target != enemy){
					list.add(new Missile(this.position.x,this.position.y,Missile.WIDTH,Missile.HEIGHT,enemy));
				}
			}
		}
	}
}
