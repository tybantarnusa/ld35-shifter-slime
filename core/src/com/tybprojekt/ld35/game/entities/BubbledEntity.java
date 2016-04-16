package com.tybprojekt.ld35.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tybprojekt.ld35.game.Animator;

public abstract class BubbledEntity extends Entity {
	protected Animator bubble;
	protected boolean bubbleShown;
	
	public void setBubbleShown(boolean shown) {
		bubbleShown = shown;
	}
	
	public void playBubble(float dt) {
		if (bubbleShown) bubble.play(dt);
		else bubble.reset();
	}
	
	public void drawBubble(SpriteBatch batch) {
		batch.begin();
		if (bubbleShown) batch.draw(bubble.getCurrentFrame(), getX() - getHalfWidth(), getY() + getHeight(), bubble.getCurrentFrame().getRegionWidth() * 2, bubble.getCurrentFrame().getRegionHeight() * 2);
		bubbleShown = false;
		batch.end();
	}
}
