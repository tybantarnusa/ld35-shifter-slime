package com.tybprojekt.ld35.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.tybprojekt.ld35.game.entities.Player;

public class WinState implements State {
	
	private GameStateManager gsm;
	private Texture bg;
	private float elapsedTime;
	
	Player player;
	
	private boolean doneCountingTime;
	private boolean doneCountingEssences;
	private boolean doneCalculatingTotalScore;
	
	private int timeMinute;
	private int timeSecond;
	private int essences;
	private int totalScore;
	private int total;
	
	private BitmapFont font;
	
	public WinState(GameStateManager gsm, Player player) {
		this.gsm = gsm;
		this.player = player;
		bg = new Texture("win.png");
		elapsedTime = 0;
		doneCountingTime = doneCountingEssences = doneCalculatingTotalScore = false;
		timeMinute = timeSecond = essences = total = 0;
		
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fontnum.ttf"));
		FreeTypeFontParameter par = new FreeTypeFontParameter();
		par.color = Color.GREEN;
		par.size = 24;
		font = gen.generateFont(par);
	}
	
	@Override
	public void update(float dt) {
		elapsedTime += dt;
		if (elapsedTime > 1) {
			if (doneCountingEssences && doneCountingTime && doneCalculatingTotalScore) {
				gsm.change(new TitleState(gsm));
			}
			
			if (doneCountingTime && doneCountingEssences && !doneCalculatingTotalScore) {
				totalScore += 5;
				if (totalScore >= total) {
					totalScore = total;
					doneCalculatingTotalScore = true;
					elapsedTime = 0;
				}
			}
			
			if (doneCountingTime && !doneCountingEssences) {
				essences += 1;
				if (essences >= player.getEssencesNum()) {
					essences = player.getEssencesNum();
					total = 500 - timeMinute + essences;
					doneCountingEssences = true;
					elapsedTime = 0;
				}
			}
			
			if (!doneCountingTime) {
				timeSecond += 1;
				timeSecond = timeSecond % 60;
				timeMinute = timeSecond == 0 ? timeMinute + 1 : timeMinute;
				if (timeMinute >= (int) (player.getPlayTime() / 60f)) {
					timeMinute = (int) (player.getPlayTime() / 60f);
					if (timeSecond < player.getPlayTime() % 60){
						timeSecond += 1;
					} else {
						timeSecond = player.getPlayTime() % 60;
						doneCountingTime = true;
						elapsedTime = 0;
					}
				}
			}
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.begin();
		batch.draw(bg, 0, 0);
		font.draw(batch, String.format("%03d:%02d", timeMinute, timeSecond), 210, 195);
		font.draw(batch, "" + essences, 458, 195);
		font.draw(batch, "" + totalScore, Gdx.graphics.getWidth()/2 - 25, 100);
		batch.end();
	}

	@Override
	public void dispose() {
		bg.dispose();
		player.dispose();
	}

}
