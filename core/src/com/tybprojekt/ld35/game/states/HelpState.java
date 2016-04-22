package com.tybprojekt.ld35.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HelpState implements State {
	
	private GameStateManager gsm;
	private Texture bg;
	
	public HelpState(GameStateManager gsm) {
		this.gsm = gsm;
		bg = new Texture("help.png");
	}
	
	@Override
	public void update(float dt) {
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			gsm.pop();
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.begin();
		batch.draw(bg, 0, 0);
		batch.end();
	}

	@Override
	public void dispose() {
		bg.dispose();
	}

}
