package com.tybprojekt.ld35.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.tybprojekt.ld35.game.entities.Player;

public class UpgradeState implements State {
	
	private GameStateManager gsm;
	private Texture bg;
	
	private Player player;
	
	private BitmapFont font;
	private BitmapFont sfont;
	
	public static int priceStrength = 70;
	public static int priceTime = 50;
	public static int priceLife = 25;
	
	private boolean once;
	
	public UpgradeState(GameStateManager gsm, Player player) {
		this.gsm = gsm;
		this.player = player;
		
		once = false;
		
		bg = new Texture("upgrade.png");
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fontnum.ttf"));
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = 18;
		param.color = new Color(157/225f, 241/225f, 157/225f, 1);
		font = generator.generateFont(param);
		param.size = 12;
		sfont = generator.generateFont(param);
		generator.dispose();
	}

	@Override
	public void update(float dt) {
		if (Gdx.input.isKeyJustPressed(Keys.NUM_1)) {
			if (player.getEssencesNum() >= priceStrength) {
				player.MOVE_SPEED += 100000;
				player.useEssences(priceStrength);
				priceStrength += 35;
				gsm.pop();
			} else {
				
			}
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.NUM_2)) {
			if (player.getEssencesNum() >= priceTime) {
				player.SHIFT_TIME += 5;
				player.useEssences(priceTime);
				priceTime += 25;
				gsm.pop();
			} else {
				
			}
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.NUM_3)) {
			if (player.getEssencesNum() >= priceLife) {
				player.restoreLife();
				player.useEssences(priceLife);
				gsm.pop();
			} else {
			
			}
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			gsm.pop();
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.begin();
		batch.draw(bg, 0, 0);
		font.draw(batch, String.format("%03d", player.getEssencesNum()), 553, 423);
		sfont.draw(batch, "" + priceStrength, 175, 370);
		sfont.draw(batch, "" + priceTime, 175, 258);
		sfont.draw(batch, "" + priceLife, 175, 145);
		batch.end();
	}

	@Override
	public void dispose() {
		bg.dispose();
		font.dispose();
		sfont.dispose();
	}

}
