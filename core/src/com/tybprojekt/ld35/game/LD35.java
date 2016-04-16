package com.tybprojekt.ld35.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tybprojekt.ld35.game.states.GameStateManager;
import com.tybprojekt.ld35.game.states.PlayState;

public class LD35 extends ApplicationAdapter {
	private SpriteBatch batch;
	private GameStateManager gsm;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		gsm.push(new PlayState());
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.current().render(batch);
		gsm.current().update(Gdx.graphics.getDeltaTime());
	}
}
