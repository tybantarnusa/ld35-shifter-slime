package com.tybprojekt.ld35.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tybprojekt.ld35.game.Animator;
import com.tybprojekt.ld35.game.Control;

public class Player extends Entity {

	private final int MOVE_SPEED = 150;
	private boolean facingLeft;
	
	private Animator animator;
	
	private Body body;
	
	public Player() {
		facingLeft = true;
		animator = new Animator("slime_guy_idle.png");
		sprite = new Sprite(animator.getCurrentFrame());
	}
	
	public void createBody(World world) {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.DynamicBody;
		bdef.position.set(getX(), getY());
		body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		body.createFixture(fdef);
		shape.dispose();
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
		
		body.setTransform(getX() + sprite.getWidth()/2, getY() + sprite.getHeight()/2, body.getAngle());
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
