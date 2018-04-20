package com.game.src.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import classes.EntityA;

public class Bullet implements EntityA {

	private double x;
	private double y;

	private BufferedImage bullet;

	public Bullet(double x, double y, Textures tex, Game game) {
		this.x = x;
		this.y = y;

		bullet = tex.bullet;

	}

	public void tick() {

		y = y - 8;

	}

	public void render(Graphics g) {
		g.drawImage(bullet, (int) x, (int) y, null);
	}

	public double getY() {
		return y;
	}

	public double getX() {

		return x;
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 32, 32);
	}
}
