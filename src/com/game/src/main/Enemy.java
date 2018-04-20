package com.game.src.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import classes.EntityA;
import classes.EntityB;

public class Enemy extends GameObject implements EntityB {

	Random r = new Random();
	private int speed = r.nextInt(3) + 1;

	BufferedImage enemy;

	private Game game;
	private Controller c;

	public Enemy(double x, double y, Textures tex, Game game, Controller c) {
		super(x, y);
		this.x = x;
		this.y = y;
		enemy = tex.enemy;
		this.game = game;
		this.c = c;
	}

	public void tick() {
		y = y + speed;

		if (y > Game.HEIGHT * Game.SCALE) {
			y = -10;
			x = r.nextInt(Game.WIDTH * Game.SCALE);
			speed = r.nextInt(3) + 1;
		}
		EntityA a;
		if ((a = Physics.Collision(this, game.listA)) != null) {

			c.removeEntity(this);
			if (a != game.getP())
				c.removeEntity(a);
			else if (a == game.getP())
				game.getP().setHealth(game.getP().getHealth() - 10);
			game.setEnemy_killed(game.getEnemy_killed() + 1);
		}
	}

	public void render(Graphics g) {
		g.drawImage(enemy, (int) x, (int) y, null);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 32, 32);
	}

}
