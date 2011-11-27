/**
 * ESIPE Project - IR2 2011/2012 - Advanced Java
 * Copyright (C) 2011 ESIPE - Universite Paris-Est Marne-la-Vallee 
 *
 * This is a free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * Please see : http://www.gnu.org/licenses/gpl.html
 * 
 * @author Damien Jubeau <djubeau@etudiant.univ-mlv.fr>
 * @author Sylvain Fay-Chatelard <sfaychat@etudiant.univ-mlv.fr>
 * @version 1.0
 */
package fr.umlv.yourobot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import fr.umlv.yourobot.physics.World;
import fr.umlv.yourobot.utils.ImageFactory;
import fr.umlv.zen.Application;
import fr.umlv.zen.ApplicationCode;
import fr.umlv.zen.ApplicationContext;
import fr.umlv.zen.ApplicationRenderCode;
import fr.umlv.zen.KeyboardEvent;
import fr.umlv.zen.KeyboardKey;

public class Main {
	public static int WIDTH = 800;
	public static int HEIGHT = 600;
	private static final int MAX_OF_PLAYERS=2;

	private static GameState gameState = GameState.MENU;

	private static Image ground;
	private static World world;
	private static int level=1;
	private static int numberOfPlayers=1;


	private enum GameState {
		PLAY,
		PAUSE,
		QUIT,
		MENU,
		WIN,
		LOSE
	}

	public static void main(String[] args) {
		// Get the current screen size
		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		WIDTH = screenSize.width-50;
		HEIGHT = screenSize.height-80;
		Application.run("YRobot", Main.WIDTH, Main.HEIGHT, new ApplicationCode() {
			@Override
			public void run(final ApplicationContext context) {
				//Let's start the game
				for(;;) {
					final KeyboardEvent event = context.pollKeyboard();
					switch(gameState) {
					case PLAY:
						play(context, event);
						break;
					case PAUSE:
						if(event != null && event.getKey() == KeyboardKey.P) {
							gameState = GameState.PLAY;
						}
						context.render(new ApplicationRenderCode() {
							@Override
							public void render(Graphics2D graphics) {
								graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
								graphics.setColor(Color.BLACK);
								graphics.setFont(new Font("Verdana", Font.ROMAN_BASELINE | Font.BOLD, 72));
								graphics.drawString("PAUSE", Main.WIDTH/2+2-125, Main.HEIGHT/2+2);
								graphics.setColor(Color.WHITE);
								graphics.setFont(new Font("Verdana", Font.ROMAN_BASELINE | Font.BOLD, 72));
								graphics.drawString("PAUSE", Main.WIDTH/2-125, Main.HEIGHT/2);
								sleep();
							}
						});
						break;
					case QUIT:
						return;
					case MENU:
						displayMenu(context, event);
						break;
					case WIN:
						context.render(new ApplicationRenderCode() {
							@Override
							public void render(Graphics2D graphics) {
								graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
								graphics.setColor(Color.BLACK);
								graphics.setFont(new Font("Verdana", Font.ROMAN_BASELINE | Font.BOLD, 72));
								graphics.drawString("You WIN", Main.WIDTH/2+2-125, Main.HEIGHT/2+2);
								graphics.setColor(Color.WHITE);
								graphics.setFont(new Font("Verdana", Font.ROMAN_BASELINE | Font.BOLD, 72));
								graphics.drawString("You WIN", Main.WIDTH/2-125, Main.HEIGHT/2);
								sleep();
							}
						});
						if(event != null && event.getKey() == KeyboardKey.C) {
							generateWorld(numberOfPlayers, ++level);
						} else if(event != null && event.getKey() == KeyboardKey.Q) {
							gameState = GameState.QUIT;
						}
						break;
					case LOSE:
						context.render(new ApplicationRenderCode() {
							@Override
							public void render(Graphics2D graphics) {
								graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
								graphics.setColor(Color.BLACK);
								graphics.setFont(new Font("Verdana", Font.ROMAN_BASELINE | Font.BOLD, 72));
								graphics.drawString("GAME OVER", Main.WIDTH/2+2-235, Main.HEIGHT/2+2);
								graphics.setColor(Color.WHITE);
								graphics.setFont(new Font("Verdana", Font.ROMAN_BASELINE | Font.BOLD, 72));
								graphics.drawString("GAME OVER", Main.WIDTH/2-235, Main.HEIGHT/2);
								sleep();
							}
						});
						if(event != null && event.getKey() == KeyboardKey.R) {
							generateWorld(numberOfPlayers, level);
						} else if(event != null && event.getKey() == KeyboardKey.Q) {
							gameState = GameState.QUIT;
						}
						break;
					}
				}
			}
		});
	}

	public static void Win() {
		gameState = GameState.WIN;
	}

	public static void Lose() {
		gameState = GameState.LOSE;
	}

	private static void generateWorld(int numberOfPlayers, int level) {
		//Create world (walls, robots, players, start, finish)
		world = new World(numberOfPlayers,level);
		//Load background textures
		ground = ImageFactory.getImage("ground.jpg");
		//Start game
		gameState = GameState.PLAY;
	}

	private static void play(ApplicationContext context, final KeyboardEvent event) {
		if(event != null && event.getKey() == KeyboardKey.P) {
			gameState = GameState.PAUSE;
			return;
		}
		context.render(new ApplicationRenderCode() {
			@Override
			public void render(Graphics2D graphics) {
				if(event != null) {
					world.setKey(event.getKey());
				}
				world.updateWorld();
				graphics.drawImage(ground, 0, 0, Main.WIDTH, Main.HEIGHT, Color.WHITE, null);
				world.render(graphics);
			}
		});
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			gameState = GameState.QUIT;
		}
	}

	/**
	 * Make sleep the current thread for 150ms
	 * Calling it into render method call allow to avoid useless CPU utilization
	 */
	private static void sleep() {
		//150ms is not visible by human eyes except Chuck Norris eye's
		try {Thread.sleep(150);} catch (InterruptedException e) {}
	}

	private static void displayMenu(ApplicationContext context, KeyboardEvent event) {

		if(event != null) {
			if(event.getKey() == KeyboardKey.UP || event.getKey() == KeyboardKey.DOWN) 
				numberOfPlayers = (numberOfPlayers % MAX_OF_PLAYERS) + 1;
			else if(event.getKey() == KeyboardKey.SPACE) 
				generateWorld(numberOfPlayers, level);
		}

		context.render(new ApplicationRenderCode() {
			@Override
			public void render(Graphics2D graphics) {
				graphics.setBackground(Color.WHITE);
				graphics.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
				graphics.drawImage(ImageFactory.getImage("menu.png"), (Main.WIDTH - 500)/2, (Main.HEIGHT - 400)/2, 500, 400, null);


				graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				graphics.setFont(new Font("Verdana", Font.ROMAN_BASELINE | Font.BOLD, 30));

				graphics.setColor(Color.GRAY);
				if(numberOfPlayers == 1)
					graphics.setColor(Color.BLACK);

				graphics.drawString(" 1 Player", Main.WIDTH/2 - 80, Main.HEIGHT/2 - 50); // Positioned text

				graphics.setColor(Color.GRAY);

				if(numberOfPlayers == 2)
					graphics.setColor(Color.BLACK);

				graphics.drawString("2 Players", Main.WIDTH/2 - 80, Main.HEIGHT/2); // Positioned text


			}
		});

		sleep();
	}
}
