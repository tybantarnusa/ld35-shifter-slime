package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tybprojekt.ld35.game.Animator;
import com.tybprojekt.ld35.game.Control;

public class Player extends Entity {

	private final int MOVE_SPEED = 150;
	private boolean facingLeft;
	
	private Animator animator;
	
	public Player() {
		facingLeft = true;
		animator = new Animator("slime_guy_idle.png");
		sprite = new Sprite(animator.getCurrentFrame());
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.begin();
		sprite.draw(batch);
		batch.end();
	}
	
	@Override
	public void update(float dt) {
		handleInput(dt);
		
		animator.play(dt);
		sprite.setRegion(animator.getCurrentFrame());
		
		if (!facingLeft)
			sprite.setScale(-1, 1);
		else
			sprite.setScale(1, 1);
	}
	
	private void handleInput(float dt) {
		
		if (Gdx.input.isKeyPressed(Control.LEFT_KEY)) {
			translate(-MOVE_SPEED * dt, 0);
			facingLeft = true;
		}
		
		if (Gdx.input.isKeyPressed(Control.RIGHT_KEY)) {
			translate(MOVE_SPEED * dt, 0);
			facingLeft = false;
		}
		
		if (Gdx.input.isKeyPressed(Control.UP_KEY)) {
			translate(0, MOVE_SPEED * dt);
		}
		
		if (Gdx.input.isKeyPressed(Control.DOWN_KEY)) {
			translate(0, -MOVE_SPEED * dt);
		}
		
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
	}
}
