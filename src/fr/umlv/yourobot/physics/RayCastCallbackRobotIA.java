package fr.umlv.yourobot.physics;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

public class RayCastCallbackRobotIA implements RayCastCallback {
	private int count;

	public void init() {
		count=0;
	}

	public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
		count++;
		return 1f;
	}
	
	public int getCount() {
		return this.count;
	}
};
