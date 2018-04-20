package com.game.src.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFrame;

import classes.EntityA;
import classes.EntityB;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 320;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 2;
	public final String TITLE = "Space Game";
	Font titleFont = new Font("bold", Font.BOLD, 60);
	Font avgFont = new Font("bold", Font.BOLD, 12);

	private boolean running = false;
	private Thread thread;
	int updates = 0, frames = 0;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private BufferedImage spriteSheet = null;
	private BufferedImage backGroundGame = null;
	private BufferedImage backGroundMenu = null;

	private BufferedImage playButton = null;
	private BufferedImage quitButton = null;
	private BufferedImage helpButton = null;

	private Player p;
	private Controller c;
	private Textures tex;

	public enum STATES {
		MENU, GAME, GAME_OVER, PAUSE, HELP
	};

	public STATES state;

	private boolean shooting = false;

	private int enemy_count = 1;
	private int enemy_killed = 0;

	public LinkedList<EntityA> listA;
	public LinkedList<EntityB> listB;

	public void init() {
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			spriteSheet = loader.loadImage("/sprite_sheet.png");
			backGroundGame = loader.loadImage("/starBackground.png");
			backGroundMenu = loader.loadImage("/back.png");
			playButton = loader.loadImage("/play_button.png");
			quitButton = loader.loadImage("/quit_button.png");
			helpButton = loader.loadImage("/help_button.png");

		} catch (IOException e) {
			e.printStackTrace();
		}

		requestFocus();
		addKeyListener(this);
		addMouseListener(this);

		tex = new Textures(this);

		p = new Player(200, 200, tex);
		c = new Controller(tex, this);
		c.addEntity(p);

		state = STATES.MENU;
		listA = c.getListA();
		listB = c.getListB();

		c.createEnemy(enemy_count);

	}

	public void run() {

		init();
		long lastTime = System.nanoTime();
		final double amountofTicks = 60.0;
		double ns = 1000000000.0 / amountofTicks;
		double delta = 0;

		long timer = System.currentTimeMillis();
		while (running) {
			long now = System.nanoTime();
			delta = delta + (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				updates++;
				delta--;
				render();
				frames++;
			}

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + "ticks, " + frames + "frames");
				updates = 0;
				frames = 0;
			}
		}
		stop();

	}

	private void tick() {

		switch (state) {

		case MENU:

			break;
		case GAME:
			p.tick();
			c.tick();

			if (enemy_killed >= enemy_count) {
				enemy_killed = 0;
				enemy_count += 2;
				c.createEnemy(enemy_count);
			}

			if (p.getHealth() <= 0) {
				state = STATES.GAME_OVER;
				c.getListB().clear();
				c.getListA().clear();
				c.addEntity(p);
			}

			break;
		case GAME_OVER:
			break;

		case PAUSE:
			break;

		case HELP:
			break;
		}

	}

	private void render() {

		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		////////////////////////////////////
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

		switch (state) {

		case MENU:
			g.drawImage(backGroundMenu, 0, 0, this);

			g.setFont(titleFont);
			g.setColor(Color.ORANGE);
			g.drawString(TITLE, 100, 75);

			g.drawImage(playButton, 200, 150, this);
			g.drawImage(quitButton, 200, 250, this);
			g.drawImage(helpButton, 200, 350, this);

			break;
		case GAME:
			g.drawImage(backGroundGame, 0, 0, this);

			p.render(g);

			c.render(g);

			break;
		case GAME_OVER:

			g.setFont(titleFont);
			g.setColor(Color.RED);
			g.drawString("GAME OVER", 135, 100);
			g.drawString("Pulsa una tecla", 110, 375);

			break;
		case PAUSE:
			g.setFont(titleFont);
			g.setColor(Color.WHITE);
			g.drawString("PAUSE", (WIDTH * SCALE / 2) - 100, HEIGHT * SCALE / 2);
			g.setFont(avgFont);
			g.drawString("Pulsa cualquier tecla para continuar", (WIDTH * SCALE / 2) - 100, (HEIGHT * SCALE / 2) + 50);
			break;
		case HELP:
			g.setFont(new Font("bold", Font.BOLD, 40));
			g.setColor(Color.yellow);
			g.drawString("MOVERSE CON FLECHAS", 75, 100);
			g.drawString("DIRECCIONALES", 150, 150);
			g.drawString("ATACAR CON ESPACIO", 110, 375);
			g.setFont(new Font("bold", Font.BOLD, 25));
			g.drawString("(Pulsa una tecla para salir de ayuda)", 118, 400);
			break;
		}
		////////////////////////////////////
		g.dispose();
		bs.show();

	}

	private synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	private synchronized void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);

	}

	public static void main(String args[]) {

		Game game = new Game();
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		JFrame frame = new JFrame(game.TITLE);
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		game.start();
	}

	public void keyPressed(KeyEvent e) {

		switch (state) {
		case MENU:

			break;
		case GAME:
			if (e.getKeyCode() == KeyEvent.VK_UP)
				p.setVelY(-5);
			else if (e.getKeyCode() == KeyEvent.VK_DOWN)
				p.setVelY(5);
			else if (e.getKeyCode() == KeyEvent.VK_LEFT)
				p.setVelX(-5);
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				p.setVelX(5);
			else if (e.getKeyCode() == KeyEvent.VK_SPACE && !shooting) {
				shooting = true;
				c.addEntity(new Bullet(p.getX(), p.getY() - 18, tex, this));
			} else if (e.getKeyCode() == KeyEvent.VK_P)
				state = STATES.PAUSE;
			break;
		case GAME_OVER:
			state = STATES.GAME;
			p.setHealth(100);
			enemy_count = 1;
			break;
		case PAUSE:
			state = STATES.GAME;

			break;
		case HELP:
			state = STATES.MENU;
			break;
		}
	}

	public void keyReleased(KeyEvent e) {
		switch (state) {
		case MENU:

			break;
		case GAME:
			if (e.getKeyCode() == KeyEvent.VK_UP)
				p.setVelY(0);
			else if (e.getKeyCode() == KeyEvent.VK_DOWN)
				p.setVelY(0);
			else if (e.getKeyCode() == KeyEvent.VK_LEFT)
				p.setVelX(0);
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				p.setVelX(0);
			else if (e.getKeyCode() == KeyEvent.VK_SPACE)
				shooting = false;
			break;
		case GAME_OVER:

			break;
		case PAUSE:

			break;
		case HELP:
			break;
		}

	}

	public void keyTyped(KeyEvent e) {
	}

	public int getEnemy_count() {
		return enemy_count;
	}

	public void setEnemy_count(int enemy_count) {
		this.enemy_count = enemy_count;
	}

	public int getEnemy_killed() {
		return enemy_killed;
	}

	public void setEnemy_killed(int enemy_killed) {
		this.enemy_killed = enemy_killed;
	}

	public BufferedImage getSpriteSheet() {
		return spriteSheet;
	}

	public void mouseClicked(MouseEvent e) {

		switch (state) {
		case MENU:
			int clickX, clickY;

			clickX = e.getX();
			clickY = e.getY();

			if ((clickX < playButton.getWidth() + 200) && (clickX > 200) && (clickY > 150)
					&& (clickY < 150 + playButton.getHeight())) {
				state = STATES.GAME;
			}
			if ((clickX < quitButton.getWidth() + 200) && (clickX > 200) && (clickY > 250)
					&& (clickY < 250 + playButton.getHeight())) {
				System.exit(0);
			}
			if ((clickX < helpButton.getWidth() + 200) && (clickX > 200) && (clickY > 350)
					&& (clickY < 350 + playButton.getHeight())) {
				state = STATES.HELP;
			}

			break;
		case GAME:

			break;
		case GAME_OVER:
			state = STATES.GAME;
			p.setHealth(100);
			enemy_count = 1;
		case PAUSE:

			break;
		case HELP:
			break;
		}
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public Player getP() {
		return p;
	}

	public void mouseReleased(MouseEvent e) {

	}
}
