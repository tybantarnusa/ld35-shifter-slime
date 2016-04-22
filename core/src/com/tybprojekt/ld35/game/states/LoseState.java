package com.tybprojekt.ld35.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LoseState implements State {
	
	private GameStateManager gsm;
	private Texture bg;
	private float elapsedTime;
	
	private Sound sound;
	
	public LoseState(GameStateManager gsm) {
		this.gsm = gsm;
		bg = new Texture("lose.png");
		elapsedTime = 0;
		sound = Gdx.audio.newSound(Gdx.files.internal("yaudah.wav"));
		sound.play();
	}
	
	@Override
	public void update(float dt) {
		elapsedTime += dt;
		if (elapsedTime > 2) gsm.change(new TitleState(gsm));
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
		sound.dispose();
	}

}
