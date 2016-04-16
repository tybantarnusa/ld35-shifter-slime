package com.tybprojekt.ld35.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator {
	
	private TextureRegion[][] frames;
	private int currentFrame;
	private float elapsedTime;
	private float frameTime;
	
	public Animator(String texturePath) {
		frames = new TextureRegion(new Texture(texturePath)).split(32, 32);
		currentFrame = 0;
		
		elapsedTime = 0;
		frameTime = 0.3f;
	}
	
	public Animator(String texturePath, int startFrame) {
		currentFrame = startFrame;
	}
	
	public void play(float dt) {
		elapsedTime += dt;
		if (elapsedTime > frameTime) {
			currentFrame++;
			currentFrame %= frames[0].length;
			elapsedTime = 0;
		}
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
	
}
