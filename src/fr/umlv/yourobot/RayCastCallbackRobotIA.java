package fr.umlv.yourobot;

import java.util.concurrent.LinkedBlockingDeque;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

public class RayCastCallbackRobotIA implements RayCastCallback {
        
        Fixture fixture;
        Vec2 point;
        Vec2 normal;
        
        public void init() {
                this.fixture = null;
        }
        
        public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
        		LinkedBlockingDeque<Element> elements = PhysicsWorld.getAllElement();
        		for(Element e : elements) {
        			if(e.getBody().equals(fixture.getBody())) {
        				System.out.println("Robot hit");
        				this.fixture = fixture;
                        this.point = point;
                        this.normal = normal;
                        return fraction;
        			}
        		}
                return -1f;
        }
        
};