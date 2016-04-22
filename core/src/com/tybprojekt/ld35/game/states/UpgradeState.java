package com.tybprojekt.ld35.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
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
	private BitmapFont sfontRed;
	
	public static int priceStrength = 70;
	public static int priceTime = 50;
	public static int priceLife = 25;
	
	private Sound buy;
	
	public UpgradeState(GameStateManager gsm, Player player) {
		this.gsm = gsm;
		this.player = player;
		
		buy = Gdx.audio.newSound(Gdx.files.internal("kaching.wav"));
		bg = new Texture("upgrade.png");
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fontnum.ttf"));
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = 18;
		param.color = new Color(157/225f, 241/225f, 157/225f, 1);
		font = generator.generateFont(param);
		param.size = 12;
		sfont = generator.generateFont(param);
		param.color = Color.RED;
		sfontRed = generator.generateFont(param);
		generator.dispose();
	}

	@Override
	public void update(float dt) {
		if (Gdx.input.isKeyJustPressed(Keys.NUM_1)) {
			if (player.getEssencesNum() >= priceStrength) {
				player.MOVE_SPEED += 100000;
				player.useEssences(priceStrength);
				priceStrength += 35;
				PlayState.INFO_BOX = "you upgraded your strength!";
				buy.play();
				gsm.pop();
			} else {
				PlayState.howa.play();
			}
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.NUM_2)) {
			if (player.getEssencesNum() >= priceTime) {
				player.SHIFT_TIME += 5;
				player.useEssences(priceTime);
				priceTime += 25;
				PlayState.INFO_BOX = "you upgraded your transormation time!";
				buy.play();
				gsm.pop();
			} else {
				PlayState.howa.play();
			}
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.NUM_3)) {
			if (player.getEssencesNum() >= priceLife) {
				player.restoreLife();
				player.useEssences(priceLife);
				priceLife += 2;
				priceLife = priceLife > 100 ? 100 : priceLife;
				PlayState.INFO_BOX = "because of determination, your lifetime recovered!";
				buy.play();
				gsm.pop();
			} else {
				PlayState.howa.play();
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

		if (player.getEssencesNum() >= priceStrength)
			sfont.draw(batch, "" + priceStrength, 175, 370);
		else
			sfontRed.draw(batch, "" + priceStrength, 175, 370);
		
		if (player.getEssencesNum() >= priceTime)
			sfont.draw(batch, "" + priceTime, 175, 258);
		else
			sfontRed.draw(batch, "" + priceTime, 175, 258);
		
		if (player.getEssencesNum() >= priceLife)
			sfont.draw(batch, "" + priceLife, 175, 145);
		else
			sfontRed.draw(batch, "" + priceLife, 175, 145);
		batch.end();
	}
	
	@Override
	public void dispose() {
		bg.dispose();
		font.dispose();
		sfont.dispose();
		sfontRed.dispose();
		buy.dispose();
	}
	
	public static void reset() {
		priceStrength = 70;
		priceTime = 50;
		priceLife = 25;
	}

}
