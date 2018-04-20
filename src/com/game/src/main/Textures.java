package com.game.src.main;

import java.awt.image.BufferedImage;

public class Textures {
	public BufferedImage player, playerLeft, playerRight, playerDamaged, bullet, enemy;

	private SpriteSheet ss;

	public Textures(Game game) {
		ss = new SpriteSheet(game.getSpriteSheet());
		getTextures();
	}

	private void getTextures() {
		player = ss.grabImage(0, 0, 32, 32);
		playerLeft = ss.grabImage(0, 1, 32, 32);
		playerRight = ss.grabImage(0, 2, 32, 32);
		playerDamaged = ss.grabImage(0, 3, 32, 32);
		bullet = ss.grabImage(1, 0, 32, 32);
		enemy = ss.grabImage(2, 0, 32, 32);

	}
}
