package com.tybprojekt.ld35.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tybprojekt.ld35.game.Animator;
import com.tybprojekt.ld35.game.Control;

public class Player extends Entity {

	private final int MOVE_SPEED = 10000;
	private boolean facingLeft;
	
	private Object nextTo;
	
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
		body.setUserData(this);
		
		// Collider
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.friction = 1;
		body.createFixture(fdef);
		
		// Interacting range
		CircleShape shape2 = new CircleShape();
		shape2.setRadius(sprite.getWidth() - 5);
		fdef.shape = shape2;
		fdef.isSensor = true;
		Fixture fixture = body.createFixture(fdef);
		fixture.setUserData("interactor");
		
		shape.dispose();
		shape2.dispose();
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
		
		sprite.setPosition(body.getPosition().x - sprite.getWidth()/2, body.getPosition().y - sprite.getHeight()/2);
	}
	
	@Override
	public void translate(float xAmount, float yAmount) {
		body.setLinearVelocity(xAmount, yAmount);
	}
	
	private void handleInput(float dt) {
		body.setLinearVelocity(0, 0);
		
		if (Gdx.input.isKeyPressed(Control.LEFT_KEY)) {
			body.setLinearVelocity(-MOVE_SPEED * dt, body.getLinearVelocity().y);
			facingLeft = true;
		}
		
		if (Gdx.input.isKeyPressed(Control.RIGHT_KEY)) {
			body.setLinearVelocity(MOVE_SPEED * dt, body.getLinearVelocity().y);
			facingLeft = false;
		}
		
		if (Gdx.input.isKeyPressed(Control.UP_KEY)) {
			body.setLinearVelocity(body.getLinearVelocity().x, MOVE_SPEED * dt);
		}
		
		if (Gdx.input.isKeyPressed(Control.DOWN_KEY)) {
			body.setLinearVelocity(body.getLinearVelocity().x, -MOVE_SPEED * dt);
		}
		
		// Interact
		if (Gdx.input.isKeyJustPressed(Control.CONFIRM_BUTTON) && nextTo != null) {
			System.out.println("Howdy!");
		}
		
	}
	
	public void setNextTo(Object what) {
		nextTo = what;
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
}
