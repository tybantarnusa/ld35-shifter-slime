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
import com.tybprojekt.ld35.game.states.PlayState;

public class Gem extends BubbledEntity {
	
	private Body body;
	private float x, y;
	private final int WINNING_CONDITION = 250;
	
	public Gem() {
		super();
		sprite = new Sprite(new Texture("crystal.png"));
		bubble = new Animator("bubbles/bubble_interact.png", 0.1f);
		x = MathUtils.random(-1500, 1500);
		y = MathUtils.random(-1500, 1500);
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
		if (player.getEssencesNum() >= WINNING_CONDITION)
			player.win();
		else {
			PlayState.INFO_BOX = "you need at least " + WINNING_CONDITION + " essences!";
			PlayState.howa.play();
		}
	}
	
	public Body getBody() {
		return body;
	}
	
	public void createBody(World world) {
		BodyDef bdef = new BodyDef();
		bdef.position.set(x, y);
		bdef.type = BodyDef.BodyType.StaticBody;
		body = world.createBody(bdef);
		body.setLinearDamping(2);
		body.setUserData(this);
		body.setFixedRotation(true);
		PolygonShape shape = new PolygonShape();
		shape.set(new float[]{getX() - 11, getY(), getX(), getY() + getHalfHeight(), getX() + 11, getY(), getX() + 11, getY() - 7, getX() + 7, getY() - getHalfHeight(), getX() - 7, getY() - getHalfHeight()});
		FixtureDef fdef = new FixtureDef();
		fdef.density = 7;
		fdef.shape = shape;
		Fixture fixture = body.createFixture(fdef);
		fixture.setUserData("interactable");
	}
	
	public void dispose() {
		super.dispose();
	}
}
