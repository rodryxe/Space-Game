package com.game.src.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import classes.EntityA;

public class Player extends GameObject implements EntityA {

	private double velX;
	private double velY;
	private int health;

	private BufferedImage player;
	private BufferedImage playerLeft;
	private BufferedImage playerRight;
	private BufferedImage playerDamaged;

	public Player(double x, double y, Textures tex) {
		super(x, y);
		health = 100;

		player = tex.player;
		playerLeft = tex.playerLeft;
		playerRight = tex.playerRight;
		playerDamaged = tex.playerDamaged;

	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void tick() {

		x = x + velX;
		y = y + velY;

		if (x <= 0)
			x = 0;
		if (y <= 0)
			y = 0;
		if (y >= 480 - 32)
			y = 480 - 32;
		if (x >= 640 - 22)
			x = 640 - 22;

	}

	public void render(Graphics g) {
		if (velX == 0 && health >= 100)
			g.drawImage(player, (int) x, (int) y, null);
		else if (velX < 0 && health >= 100)
			g.drawImage(playerLeft, (int) x, (int) y, null);
		else if (velX > 0 && health >= 100)
			g.drawImage(playerRight, (int) x, (int) y, null);
		else if (health < 100)
			g.drawImage(playerDamaged, (int) x, (int) y, null);

		g.setColor(Color.GRAY);
		g.fillRect((int) x - 9, (int) y + 25, 50, 7);
		g.setColor(Color.WHITE);
		g.drawRect((int) x - 9, (int) y + 25, 50, 7);
		g.setColor(Color.GREEN);
		g.fillRect((int) x - 9, (int) y + 25, health / 2, 7);

	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setVelX(double velX) {
		this.velX = velX;
	}

	public void setVelY(double velY) {
		this.velY = velY;
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 32, 32);
	}

}
