package fr.umlv.yourobot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import fr.umlv.yourobot.physics.World;
import fr.umlv.zen.Application;
import fr.umlv.zen.ApplicationCode;
import fr.umlv.zen.ApplicationContext;
import fr.umlv.zen.ApplicationRenderCode;
import fr.umlv.zen.KeyboardEvent;
import fr.umlv.zen.KeyboardKey;

public class Main {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private static GameState gameState = GameState.MENU;
	private static Image ground;
	private static World world;
	private static int level=1;

	public enum GameState {
		PLAY,
		PAUSE,
		QUIT,
		MENU,
		WIN,
		LOSE
	}

	public static void main(String[] args) {
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
								graphics.drawString("PAUSE", Main.WIDTH/2, Main.HEIGHT/2);
							}
						});
						break;
					case QUIT:
						return;
					case MENU:
						System.out.println("MENU");
						if(event != null && event.getKey() == KeyboardKey.P) {
							generateWorld(level);
						}
						break;
					case WIN:
						System.out.println("YOU WIN LEVEL UP");
						context.render(new ApplicationRenderCode() {
							@Override
							public void render(Graphics2D graphics) {
								graphics.drawString("WIN", Main.WIDTH/2, Main.HEIGHT/2);
							}
						});
						if(event != null && event.getKey() == KeyboardKey.C) {
							generateWorld(++level);
						} else if(event != null && event.getKey() == KeyboardKey.Q) {
							gameState = GameState.QUIT;
						}
						break;
					case LOSE:
						System.out.println("YOU LOSE QUIT GAME OR RETRY");
						context.render(new ApplicationRenderCode() {
							@Override
							public void render(Graphics2D graphics) {
								graphics.drawString("GAME OVER", Main.WIDTH/2, Main.HEIGHT/2);
							}
						});
						if(event != null && event.getKey() == KeyboardKey.R) {
							generateWorld(level);
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
	
	private static void generateWorld(int level) {
		//Create world (walls, robots, players, start, finish)
		world = new World(1,level);
		//Load background textures
		ground = Toolkit.getDefaultToolkit().getImage("ground.jpg");
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
}
