package com.tybprojekt.ld35.game.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity {
	
	protected Sprite sprite;
	
	public void update(float dt){}
	public void render(SpriteBatch batch){}
	
	public float getX() { return sprite.getX(); }
	public float getY() { return sprite.getY(); }
	
	public void setX(float x) {
		sprite.setX(x);
	}
	
	public void setY(float y) {
		sprite.setY(y);
	}
	
	public void setPosition(float x, float y) {
		sprite.setPosition(x, y);
	}
	
	public float getHeight() {
		return sprite.getHeight();
	}
	
	public float getWidth() {
		return sprite.getWidth();
	}
	
	public float getHalfHeight() {
		return getHeight()/2;
	}
	
	public float getHalfWidth() {
		return getWidth()/2;
	}
	
	public void translate(float xAmount, float yAmount) {
		sprite.translate(xAmount, yAmount);
	}
	
	public void dispose() {
		sprite.getTexture().dispose();
	}
}
