package com.tybprojekt.ld35.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tybprojekt.ld35.game.Animator;

public class Drill extends BubbledEntity {
	
	private Body body;
	
	public Drill() {
		sprite = new Sprite(new Texture("drill.png"));
		bubble = new Animator("bubbles/bubble_transform.png", 0.1f);
		bubbleShown = false;
	}
	
	@Override
	public void update(float dt) {
		setPosition(body.getPosition().x - getHalfWidth(), body.getPosition().y - getHalfHeight());
		sprite.setRotation(90 * body.getAngle());
		playBubble(dt);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.begin();
		sprite.draw(batch);
		batch.end();
	}

	@Override
	public void interactWith() {
		System.out.println("You feel this drill inside your body.");
	}
	
	public void createBody(World world) {
		BodyDef bdef = new BodyDef();
		bdef.position.set(500, 500);
		bdef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bdef);
		body.setLinearDamping(2);
		body.setUserData(this);
		body.setFixedRotation(true);
		PolygonShape shape = new PolygonShape();
		shape.set(new float[]{getX() - getHalfWidth(), getY(), getX() + getHalfWidth(), getY() + getHalfHeight(), getX(), getY() - getHalfHeight(), getX() - 5, getY() - getHalfHeight(), getX() - getHalfWidth(), getY() - 5});
		FixtureDef fdef = new FixtureDef();
		fdef.density = 5;
		fdef.shape = shape;
		Fixture fixture = body.createFixture(fdef);
		fixture.setUserData("interactable");
	}
}
