package com.tybprojekt.ld35.game.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayState implements State {
	private Texture texture;
	
	public PlayState() {
		texture = new Texture("badlogic.jpg");
	}
	
	@Override
	public void update(float dt) {
		
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.begin();
		batch.draw(texture, 0, 0);
		batch.end();
	}

}
