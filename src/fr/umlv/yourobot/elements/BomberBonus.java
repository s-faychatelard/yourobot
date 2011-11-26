package fr.umlv.yourobot.elements;

import java.util.LinkedList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import fr.umlv.yourobot.physics.World;

public class BomberBonus extends Bonus {
	private static final String imagePath = "bomberBonus.png";

	public BomberBonus(Vec2 position) {
		//Null is test by super
		super(position);
	}

	@Override
	public String getImagePath() {
		return imagePath;
	}

	@Override
	public void execute(RobotPlayer robot) {
		LinkedList<Element> elements = World.getAllElement();
		for(Element element : elements) {
			if((element instanceof RobotPlayer) || (element instanceof EndPoint)) continue;
			//Get the distance from the robot to the element
			Vec2 pos = new Vec2(robot.getBody().getPosition());
			int x = (int)robot.getBody().getPosition().x - (int)element.getBody().getPosition().x;
			int y = (int)robot.getBody().getPosition().y - (int)element.getBody().getPosition().y;
			double distance = Math.sqrt(x*x + y*y);
			if(distance>QUARTER_DIAGONAL) continue;

			//Calculate the coefficient force (more if the element is near the robot)
			double coeffForce = QUARTER_DIAGONAL/distance;

			Vec2 force = pos.sub(element.getBody().getPosition()).negate();
			//TODO change coeffForce for wall type
			double coeffWallForce = 10000*coeffForce;

			final BodyType oldType = element.getBody().getType();
			element.getBody().setType(BodyType.DYNAMIC);	
			element.getBody().setLinearDamping(.8f);
			element.getBody().setAwake(true);
			element.getBody().applyForce(new Vec2((int)(force.x*coeffWallForce),(int)(force.y*coeffWallForce)), element.getBody().getPosition());

			//Wait 2 seconds before set STATIC for wall
			final Element e = element;
			new Thread(new Runnable() {
				private Element element=e;
				@Override
				public void run() {
					try {
						Thread.sleep(2500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					element.getBody().setType(oldType);
				}
			}).start();
		}
	}
}
