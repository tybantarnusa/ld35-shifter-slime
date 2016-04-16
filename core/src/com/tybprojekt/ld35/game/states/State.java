package com.tybprojekt.ld35.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface State {
	
	public void update(float dt);
	public void render(SpriteBatch batch);
	public void dispose();
	
}
