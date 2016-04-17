package com.tybprojekt.ld35.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tybprojekt.ld35.game.Animator;

public class Chainsaw extends BubbledEntity {
	
	private Body body;
	
	public Chainsaw() {
		super();
		sprite = new Sprite(new Texture("chainsaw.png"));
		bubble = new Animator("bubbles/bubble_transform.png", 0.1f);
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
	public void interactWith(Player player) {
	}
	
	public void createBody(World world) {
		BodyDef bdef = new BodyDef();
//		bdef.position.set(MathUtils.random(1000, 2000), MathUtils.random(1000, 2000));
		bdef.position.set(100, 100);
		bdef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bdef);
		body.setLinearDamping(2);
		body.setUserData(this);
		body.setFixedRotation(true);
		PolygonShape shape = new PolygonShape();
		shape.set(new float[]{getX() - getHalfWidth(), getY() - getHalfHeight() + 7, getX() + getHalfWidth() - 9, getY() + getHalfHeight(), getX() + getHalfWidth(), getY() + getHalfHeight(), getX() + getHalfWidth(), getY() + getHalfHeight() - 10, getX() - 5, getY() - getHalfHeight()});
		FixtureDef fdef = new FixtureDef();
		fdef.density = 7;
		fdef.shape = shape;
		Fixture fixture = body.createFixture(fdef);
		fixture.setUserData("interactable");
	}
}
