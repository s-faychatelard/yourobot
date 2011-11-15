package fr.umlv.yourobot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import org.jbox2d.common.Vec2;

public class IceWall extends Wall {
	
	private Image image;

	public IceWall(Vec2 position) {
		super(position);
	}

	@Override
	public int attackWithIce() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int attackWithStone() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int attackWithWood() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public Image getImage() {
		return this.image;
	}
	
	@Override
	public void setImage(Image img) {
		this.image = img;
	}

}
