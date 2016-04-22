package com.tybprojekt.ld35.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class TitleState implements State {
	
	private GameStateManager gsm;
	private Texture bg;
	
	private PlayState playState;
	
	private Music bgm;
	private Sound select;
	
	public TitleState(GameStateManager gsm) {
		this.gsm = gsm;
		bg = new Texture("titlescreen.jpg");
		playState = new PlayState(gsm);
		bgm = Gdx.audio.newMusic(Gdx.files.internal("title.ogg"));
		bgm.setLooping(true);
		bgm.play();
		select = Gdx.audio.newSound(Gdx.files.internal("confirm.wav"));
	}
	
	@Override
	public void update(float dt) {
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			select.play();
			Timer.schedule(new Task(){

				@Override
				public void run() {
					gsm.change(playState);
				}
				
			}, 0.3f);
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.H)) {
			select.play();
			gsm.push(new HelpState(gsm));
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
		bgm.dispose();
		select.dispose();
	}

}
