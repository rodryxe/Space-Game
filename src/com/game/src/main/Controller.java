package com.game.src.main;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Random;

import classes.EntityA;
import classes.EntityB;

public class Controller {

	private LinkedList<EntityA> listA = new LinkedList<EntityA>();
	private LinkedList<EntityB> listB = new LinkedList<EntityB>();

	private Random r = new Random();
	Textures tex;
	private Game game;

	public Controller(Textures tex, Game game) {
		this.tex = tex;
		this.game = game;
	}

	public void createEnemy(int enemy_count) {

		for (int i = 0; i < enemy_count; i++) {
			addEntity(new Enemy(r.nextInt(Game.WIDTH * Game.SCALE), -10, tex, game, this));
		}
	}

	public void tick() {

		for (int i = 0; i < listA.size(); i++) {
			listA.get(i).tick();
		}
		for (int i = 0; i < listB.size(); i++) {
			listB.get(i).tick();
		}
	}

	public void render(Graphics g) {

		for (int i = 0; i < listA.size(); i++) {
			listA.get(i).render(g);
		}
		for (int i = 0; i < listB.size(); i++) {
			listB.get(i).render(g);
		}

	}

	public void addEntity(EntityA e) {
		listA.add(e);
	}

	public void removeEntity(EntityA e) {
		listA.remove(e);
	}

	public void addEntity(EntityB e) {
		listB.add(e);
	}

	public void removeEntity(EntityB e) {
		listB.remove(e);
	}

	public LinkedList<EntityA> getListA() {
		return listA;
	}

	public LinkedList<EntityB> getListB() {
		return listB;
	}

}
