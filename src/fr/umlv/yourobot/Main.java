package fr.umlv.yourobot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import fr.umlv.zen.Application;
import fr.umlv.zen.ApplicationCode;
import fr.umlv.zen.ApplicationContext;
import fr.umlv.zen.ApplicationRenderCode;
import fr.umlv.zen.KeyboardEvent;

public class Main {
	public static void main(String[] args) {
		final int WIDTH = 800;
		final int HEIGHT = 600;
		final int SIZE = 30;

		Application.run("You robot", WIDTH, HEIGHT, new ApplicationCode() {
			@Override
			public void run(final ApplicationContext context) {
				final Ellipse2D ellipse = new Ellipse2D.Float(100 - SIZE/2, 100 - SIZE/2, SIZE, SIZE);
				final Ellipse2D ellipse2 = new Ellipse2D.Float(100 - SIZE/2, 100 - SIZE/2, SIZE, SIZE);
				for(;;) {
					final KeyboardEvent event = context.waitKeyboard();
					if (event == null) {
						return;
					}
					context.render(new ApplicationRenderCode() {
						@Override
						public void render(Graphics2D graphics) {
							double x = ellipse.getX();
							double y = ellipse.getY();
							double x2 = ellipse2.getX();
							double y2 = ellipse2.getY();
							graphics.setBackground(new Color(238, 238, 238));
							graphics.setColor(new Color(238, 238, 238));
							Rectangle2D rectangle = new Rectangle2D.Float(0, 0, WIDTH, HEIGHT);
							graphics.fill(rectangle);
							switch(event.getKey()) {
							case UP:
								y-=10;
								break;
							case DOWN:
								y+=10;
								break;
							case LEFT:
								x-=10;
								break;
							case RIGHT:
								x+=10;
								break;
							case Z:
								y2-=10;
								break;
							case S:
								y2+=10;
								break;
							case Q:
								x2-=10;
								break;
							case D:
								x2+=10;
								break;
							default:
							}
							ellipse.setFrame(x, y, ellipse.getWidth(), ellipse.getHeight());
							ellipse2.setFrame(x2, y2, ellipse.getWidth(), ellipse.getHeight());
							graphics.setColor(Color.BLACK);
							graphics.fill(ellipse);
							graphics.fill(ellipse2);
						}
					});
				}
			}
		});
	}
}