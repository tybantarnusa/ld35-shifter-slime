package com.tybprojekt.ld35.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.Player;

public class PlayState implements State {
	private Player player;
	
	public PlayState() {
		player = new Player();
	}
	
	@Override
	public void update(float dt) {
		player.update(dt);
	}

	@Override
	public void render(SpriteBatch batch) {
		player.render(batch);
	}
	
	public void dispose() {
		player.dispose();
	}

}
