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
		body.setUserData(this);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(getHalfWidth(), getHalfHeight());
		FixtureDef fdef = new FixtureDef();
		fdef.density = 100;
		fdef.shape = shape;
		Fixture fixture = body.createFixture(fdef);
		fixture.setUserData("interactable");
	}
}
