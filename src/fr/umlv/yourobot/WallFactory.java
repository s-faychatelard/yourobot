package fr.umlv.yourobot;

import org.jbox2d.common.Vec2;

public class WallFactory {
	
	public static IceWall getIceWall(Vec2 p) {
		return new IceWall(p);
	}
	
	public static WoodWall getWoodWall(Vec2 p) {
		return new WoodWall(p);
	}
	
	public static StoneWall getStoneWall(Vec2 p) {
		return new StoneWall(p);
	}

}
