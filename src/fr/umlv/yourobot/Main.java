package fr.umlv.yourobot;

import java.awt.Color;
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
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private static GameState gameState = GameState.MENU;
	private static MenuState menuState = MenuState.ONE_PLAYER;

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

	private enum MenuState {
		ONE_PLAYER,
		TWO_PLAYERS,
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
								graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
								graphics.setColor(Color.BLACK);
								graphics.setFont(new Font("Roman", Font.ROMAN_BASELINE | Font.BOLD, 72));
								graphics.drawString("PAUSE", Main.WIDTH/2+2-125, Main.HEIGHT/2+2);
								graphics.setColor(Color.WHITE);
								graphics.setFont(new Font("Roman", Font.ROMAN_BASELINE | Font.BOLD, 72));
								graphics.drawString("PAUSE", Main.WIDTH/2-125, Main.HEIGHT/2);
								try {
									//150ms is not visible by human
									Thread.sleep(150);
								} catch (InterruptedException e) { }
							}
						});
						break;
					case QUIT:
						return;
					case MENU:
							displayMenu(context, event);
						break;
					case WIN:
						System.out.println("YOU WIN LEVEL UP");
						context.render(new ApplicationRenderCode() {
							@Override
							public void render(Graphics2D graphics) {
								graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
								graphics.setColor(Color.BLACK);
								graphics.setFont(new Font("Roman", Font.ROMAN_BASELINE | Font.BOLD, 72));
								graphics.drawString("You WIN", Main.WIDTH/2+2-125, Main.HEIGHT/2+2);
								graphics.setColor(Color.WHITE);
								graphics.setFont(new Font("Roman", Font.ROMAN_BASELINE | Font.BOLD, 72));
								graphics.drawString("You WIN", Main.WIDTH/2-125, Main.HEIGHT/2);
								try {
									//150ms is not visible by human
									Thread.sleep(150);
								} catch (InterruptedException e) { }
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
								graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
								graphics.setColor(Color.BLACK);
								graphics.setFont(new Font("Roman", Font.ROMAN_BASELINE | Font.BOLD, 72));
								graphics.drawString("GAME OVER", Main.WIDTH/2+2-125, Main.HEIGHT/2+2);
								graphics.setColor(Color.WHITE);
								graphics.setFont(new Font("Roman", Font.ROMAN_BASELINE | Font.BOLD, 72));
								graphics.drawString("GAME OVER", Main.WIDTH/2-125, Main.HEIGHT/2);
								try {
									//150ms is not visible by human
									Thread.sleep(150);
								} catch (InterruptedException e) { }
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
	
	/**
	 * Make sleep the current thread for 150ms
	 * Calling it into render method call allow to avoid useless CPU utilisation
	 */
	private static void sleep() {
		try {Thread.sleep(150);} catch (InterruptedException e) {}
	}
	
	
	/**
	 * Make sleep the current thread for 150ms
	 * Calling it into render method call allow to avoid useless CPU utilisation
	 */
	private static void displayMenu(ApplicationContext context, KeyboardEvent event) {
		context.render(new ApplicationRenderCode() {
			@Override
			public void render(Graphics2D graphics) {
				graphics.setBackground(Color.WHITE);
				graphics.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
				graphics.drawImage(ImageFactory.getImage("menu.png"), (Main.WIDTH - 500)/2, (Main.HEIGHT - 400)/2, 500, 400, null);

				System.out.println(menuState);
				
				switch(menuState) {
					case ONE_PLAYER:
						graphics.drawString("2 joueurs", Main.WIDTH/2, Main.HEIGHT/2);
					break;
					case TWO_PLAYERS:
						graphics.drawString("1 joueurs", 100, 100);
					break;
				}
			}
		});
		sleep();
		if(event != null && (event.getKey() == KeyboardKey.UP || event.getKey() == KeyboardKey.DOWN)) {
			if(menuState == MenuState.ONE_PLAYER)
				menuState = MenuState.TWO_PLAYERS;
			else 
				menuState = MenuState.ONE_PLAYER;
		}
	}
}
