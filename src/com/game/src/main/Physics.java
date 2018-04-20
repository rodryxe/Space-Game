package com.game.src.main;

import java.util.LinkedList;

import classes.EntityA;
import classes.EntityB;

public class Physics {

	public static EntityB Collision(EntityA enta, LinkedList<EntityB> entb) {
		for (int i = 0; i < entb.size(); i++) {
			if (enta.getBounds().intersects(entb.get(i).getBounds())) {
				return entb.get(i);
			}
		}
		return null;
	}

	public static EntityA Collision(EntityB entb, LinkedList<EntityA> enta) {
		for (int i = 0; i < enta.size(); i++) {
			if (entb.getBounds().intersects(enta.get(i).getBounds())) {
				return enta.get(i);
			}
		}
		return null;
	}
}
