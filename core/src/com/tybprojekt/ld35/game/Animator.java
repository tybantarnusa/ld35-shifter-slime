package com.tybprojekt.ld35.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator {
	
	private TextureRegion[][] frames;
	private int currentFrame;
	private float elapsedTime;
	private float frameTime;
	private boolean loop;
	
	public Animator(String texturePath, float frameTime) {
		Texture texture = new Texture(texturePath);
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		frames = new TextureRegion(texture).split(32, 32);
		currentFrame = 0;
		
		elapsedTime = 0;
		this.frameTime = frameTime;
		loop = true;
	}
	
	public Animator(String texturePath, float frameTime, boolean fromLast) {
		Texture texture = new Texture(texturePath);
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		frames = new TextureRegion(texture).split(32, 32);
		currentFrame = fromLast ? frames[0].length - 1 : 0;
		
		elapsedTime = 0;
		this.frameTime = frameTime;
		loop = true;
	}
	
	public void play(float dt) {
		if (loop || currentFrame < frames[0].length)
			elapsedTime += dt;
		
		if (elapsedTime > frameTime) {
			currentFrame++;
			if (loop)
				currentFrame %= frames[0].length;
			else if (currentFrame >= frames[0].length)
				currentFrame = frames[0].length - 1;
			elapsedTime = 0;
		}
	}
	
	public void playBackward(float dt) {
		System.out.println(elapsedTime + " | " + frameTime + " ");
		if (loop || currentFrame >= 0) {
			elapsedTime += dt;
		}
		
		if (elapsedTime > frameTime) {
			currentFrame--;
			
			if (loop && currentFrame < 0)
				currentFrame = frames[0].length - 1;
			else if (currentFrame < 0) 
				currentFrame = 0;
			elapsedTime = 0;
		}
	}
	
	public void reset() {
		currentFrame = 0;
	}
	
	public void finish() {
		currentFrame = frames[0].length - 1;
	}
	
	public TextureRegion getCurrentFrame() {
		return frames[0][currentFrame];
	}
	
	public void dispose() {
		for (TextureRegion[] textures : frames) {
			for (TextureRegion texture : textures) {
				texture.getTexture().dispose();
			}
		}
	}
	
	public void setLoop(boolean isLooping) {
		loop = isLooping;
	}
	
	public boolean isFinished() {
		if (loop) return false;
		return currentFrame == frames[0].length - 1;
	}
	
	public boolean isIndexZero() {
		return currentFrame == 0;
	}
	
	public void setTexture(String path) {
		
	}
}
