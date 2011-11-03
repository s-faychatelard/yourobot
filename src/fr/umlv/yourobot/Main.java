package fr.umlv.yourobot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Ellipse2D;
import java.util.Random;

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
		final int STRIDE = 100;

		Application.run("Keyboard", WIDTH, HEIGHT, new ApplicationCode() {
			@Override
			public void run(final ApplicationContext context) {
				final Random random = new Random(0);
				final Font font = new Font("arial", Font.BOLD, 30);

				for(;;) {
					final KeyboardEvent event = context.waitKeyboard();
					if (event == null) {
						return;
					}
					context.render(new ApplicationRenderCode() {
						@Override
						public void render(Graphics2D graphics) {
							float x,y;
							Color color;
							for(int i = 0; i < STRIDE; i++) {
								x = random.nextInt(WIDTH);
								y = random.nextInt(HEIGHT);

								color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255), random.nextInt(255));
								RadialGradientPaint paint = new RadialGradientPaint(x, y, SIZE, new float[]{0f, 1f}, new Color[]{color, Color.WHITE});
								graphics.setPaint(paint);
								graphics.fill(new Ellipse2D.Float(x - SIZE/2, y - SIZE/2, SIZE, SIZE));
							}
							x = random.nextInt(WIDTH);
							y = random.nextInt(HEIGHT);

							color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
							graphics.setPaint(color);
							graphics.setFont(font);
							graphics.drawString(event.toString(), x, y);
						}
					});
				}
			}
		});
	}
}