package com.tybprojekt.ld35.game.entities;

import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tybprojekt.ld35.game.Animator;
import com.tybprojekt.ld35.game.Control;

public class Player extends Entity {
	
	public enum Shape {
		NORMAL,
		SHIFTING,
		DRILL,
		KEY,
		TURRET
	}

	private final int MOVE_SPEED = 30000;
	private boolean facingLeft;
	private BubbledEntity nextTo;
	private TreeMap<String, Animator> animators;
	private Animator animator;
	private Body body;
	private Shape currentShape;
	private final float SHIFT_TIME = 3;
	
	private float timer;
	
	public Player() {
		facingLeft = true;
		
		animators = new TreeMap<String, Animator>();
		animators.put("normal", new Animator("slime_guy_idle.png", 0.3f));
		animators.put("shift drill", new Animator("slime_transform_drill.png", 0.1f));
		animator = animators.get("normal");
		
		sprite = new Sprite(animator.getCurrentFrame(), 0, 0, 2 * animator.getCurrentFrame().getRegionWidth(), 2 * animator.getCurrentFrame().getRegionHeight());
		currentShape = Shape.NORMAL;
	}
	
	public void createBody(World world) {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.DynamicBody;
		bdef.position.set(300, 300);
		body = world.createBody(bdef);
		body.setUserData(this);
		
		createNormalFixture();
	}
	
	private void createNormalFixture() {
		body.getFixtureList().clear();
		
		// Collider
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(getHalfWidth(), getHalfHeight()/2);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		body.createFixture(fdef);
		
		// Interacting range
		shape.setAsBox(getHalfWidth() + 10, getHalfHeight()/2 + 10);
		fdef.shape = shape;
		fdef.isSensor = true;
		Fixture fixture = body.createFixture(fdef);
		fixture.setUserData("interactor");
		
		shape.dispose();
	}
	
	private void createDrillFixture() {
		body.getFixtureList().clear();
		
		// Collider
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(getHalfWidth()/2 - 3, getHalfHeight()/2, new Vector2(-15, 0), 0);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		body.createFixture(fdef);
		
		// Interacting range
		shape.setAsBox(getHalfWidth() + 10, getHalfHeight()/2 + 10);
		fdef.shape = shape;
		fdef.isSensor = true;
		Fixture fixture = body.createFixture(fdef);
		fixture.setUserData("interactor");
		
		shape.dispose();
	}
	
	private void backToNormalShape(float dt) {
		if (timer < SHIFT_TIME + 0.1f) {
			switch (currentShape) {
			case DRILL:
				animator = new Animator("slime_transform_drill.png", 0.1f, true);
				animator.setLoop(false);
				break;
			default:
				break;
			}
			currentShape = Shape.SHIFTING;
		}
		
		animator.playBackward(dt);
		
		if (animator.isIndexZero()) {
			timer = 0;
			currentShape = Shape.NORMAL;
			animator = animators.get("normal");
			animator.setLoop(true);
			createNormalFixture();
		}
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.begin();
		sprite.draw(batch);
		batch.end();
	}
	
	@Override
	public void update(float dt) {
		if (currentShape != Shape.NORMAL) {
			timer += dt;
			if (timer > SHIFT_TIME) {
				backToNormalShape(dt);
			}
		}
		
		handleInput(dt);
		
		if (currentShape != Shape.SHIFTING) animator.play(dt);
		
		sprite.setRegion(animator.getCurrentFrame());
		
		if (!facingLeft)
			sprite.setScale(-1, 1);
		else
			sprite.setScale(1, 1);
		
		// Switch facing
		switch (currentShape) {
		case DRILL:
			if (!facingLeft) {
				((PolygonShape) body.getFixtureList().get(0).getShape()).setAsBox(getHalfWidth()/2 - 3, getHalfHeight()/2, new Vector2(-15, 0), 0);
			} else {
				((PolygonShape) body.getFixtureList().get(0).getShape()).setAsBox(getHalfWidth()/2 - 3, getHalfHeight()/2, new Vector2(15, 0), 0);
			}
			break;
		default:
			break;
		}
		setPosition(body.getPosition().x - getHalfWidth(), body.getPosition().y - getHalfHeight()/2);
		
		if (nextTo != null)
			nextTo.setBubbleShown(true);
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
			nextTo.interactWith();
			if (nextTo instanceof Drill && currentShape == Shape.NORMAL) {
				currentShape = Shape.DRILL;
				animator = animators.get("shift drill");
				animator.setLoop(false);
				animator.reset();
				createDrillFixture();
			}
		}
		
	}
	
	public void setNextTo(BubbledEntity what) {
		nextTo = what;
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
	
	public BubbledEntity getNextEntity() {
		return nextTo;
	}
}
