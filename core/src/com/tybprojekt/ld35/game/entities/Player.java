package com.tybprojekt.ld35.game.entities;

import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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
		CHAINSAW,
		TURRET
	}

	private final float MOVE_SPEED = 10000;
	private boolean facingLeft;
	private BubbledEntity nextTo;
	private TreeMap<String, Animator> animators;
	private Animator animator;
	private Body body;
	private Shape currentShape;
	private final float SHIFT_TIME = 10;
	private Sound shiftSfx;
	private Sound stepSfx;
	private boolean isWalking;
	private boolean doAction;
	private boolean shifting;
	
	private float timer;
	
	public Player() {
		facingLeft = true;
		
		animators = new TreeMap<String, Animator>();
		animators.put("normal", new Animator("slime_guy_idle.png", 0.3f));
		
		// Drill animations
		animators.put("shift drill", new Animator("slime_transform_drill.png", 0.1f));
		animators.put("drill", new Animator("slime_drill.png", 0.3f));
		animators.put("drilling", new Animator("slime_drilling.png", 0.05f));
		
		// Chainsaw animations
		animators.put("shift chainsaw", new Animator("slime_transform_chainsaw.png", 0.1f));
		animators.put("chainsaw", new Animator("slime_chainsaw.png", 0.3f));
		animators.put("chainsawing", new Animator("slime_chainsawing.png", 0.05f));
		
		animator = animators.get("normal");
		
		sprite = new Sprite(animator.getCurrentFrame(), 0, 0, 2 * animator.getCurrentFrame().getRegionWidth(), 2 * animator.getCurrentFrame().getRegionHeight());
		currentShape = Shape.NORMAL;
		
		shiftSfx = Gdx.audio.newSound(new FileHandle("shapeshift.ogg"));
		stepSfx = Gdx.audio.newSound(new FileHandle("step.ogg"));
		
		isWalking = false;
		doAction = false;
		shifting = false;
	}
	
	public void createBody(World world, boolean normal) {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.DynamicBody;
		bdef.position.set(0, 0);
		body = world.createBody(bdef);
		body.setUserData(this);
		
		if (normal) createNormalFixture();
	}
	
	public void createBody(World world) {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.DynamicBody;
		bdef.position.set(0, 0);
		body = world.createBody(bdef);
		body.setUserData(this);
	}
	
	public void createBody(World world, float x, float y) {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.DynamicBody;
		bdef.position.set(x, y);
		body = world.createBody(bdef);
		body.setUserData(this);
	}
	
	private void createNormalFixture() {
		for(int i = 0; i < body.getFixtureList().size; i++)
			body.destroyFixture(body.getFixtureList().get(i));
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
		for(int i = 0; i < body.getFixtureList().size; i++)
			body.destroyFixture(body.getFixtureList().get(i));
		body.getFixtureList().clear();
		
		// Collider
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(getHalfWidth()/2 - 3, getHalfHeight()/2, new Vector2(-15, 0), 0);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		body.createFixture(fdef);
		
		// Interacting range
		shape.setAsBox(getHalfWidth()/2+5, getHalfHeight()/2, new Vector2(5, 15), 0);
		fdef.shape = shape;
		fdef.isSensor = true;
		Fixture fixture = body.createFixture(fdef);
		fixture.setUserData("interactor");
		
		shape.dispose();
	}
	
	private void createChainsawFixture() {
		for(int i = 0; i < body.getFixtureList().size; i++)
			body.destroyFixture(body.getFixtureList().get(i));
		body.getFixtureList().clear();
		
		// Collider
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(getHalfWidth() - 10, getHalfHeight()/2 - 5, new Vector2(0, -5), 0);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		body.createFixture(fdef);
		
		// Interacting range
		shape.setAsBox(getHalfWidth()/2-4, getHalfHeight()/2 + 7, new Vector2(0, 20), 0);
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
			case CHAINSAW:
				animator = new Animator("slime_transform_chainsaw.png", 0.1f, true);
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
		System.out.println(body.getPosition().x + ", " +  body.getPosition().y);
		if (currentShape != Shape.NORMAL) {
			timer += dt;
			if (timer > SHIFT_TIME) {
				backToNormalShape(dt);
			}
		}
		
		handleInput(dt);
		
		if (shifting) {
			animator.play(dt);
			if (animator.isFinished()) {
				shifting = false;
			}
		} 
		
		if (!shifting && currentShape != Shape.SHIFTING) {
			switch(currentShape) {
			case DRILL:
				animator = animators.get("drill");
				break;
			case CHAINSAW:
				animator = animators.get("chainsaw");
				break;
			default:
				break;
			}
			animator.play(dt);
		}
			
		if (doAction && !shifting && currentShape != Shape.SHIFTING) {
			animator.setLoop(true);
			switch (currentShape) {
			case DRILL:
				animator = animators.get("drilling");
				break;
			case CHAINSAW:
				animator = animators.get("chainsawing");
				break;
			default:
				break;
			}
			animator.play(dt);
		}
		
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
				((PolygonShape) body.getFixtureList().get(1).getShape()).setAsBox(getHalfWidth()/2+5, getHalfHeight()/2, new Vector2(5, 15), 0);;
			} else {
				((PolygonShape) body.getFixtureList().get(0).getShape()).setAsBox(getHalfWidth()/2 - 3, getHalfHeight()/2, new Vector2(15, 0), 0);
				((PolygonShape) body.getFixtureList().get(1).getShape()).setAsBox(getHalfWidth()/2+5, getHalfHeight()/2, new Vector2(-10, 15), 0);;
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
		isWalking = false;
		
		if (!(Gdx.input.isKeyPressed(Control.LEFT_KEY)
			|| Gdx.input.isKeyPressed(Control.UP_KEY)
			|| Gdx.input.isKeyPressed(Control.DOWN_KEY)
			|| Gdx.input.isKeyPressed(Control.RIGHT_KEY)))
			stepSfx.stop();
		
		if (Gdx.input.isKeyPressed(Control.LEFT_KEY)) {
			body.setLinearVelocity(-MOVE_SPEED * dt, body.getLinearVelocity().y);
			facingLeft = true;
			if (!isWalking) stepSfx.play(0.2f, MathUtils.random(0.5f, 2), 0);
			isWalking = true;
		}
		
		if (Gdx.input.isKeyPressed(Control.RIGHT_KEY)) {
			body.setLinearVelocity(MOVE_SPEED * dt, body.getLinearVelocity().y);
			facingLeft = false;
			if (!isWalking) stepSfx.play(0.2f, MathUtils.random(0.5f, 2), 0);
			isWalking = true;
		}
		
		if (Gdx.input.isKeyPressed(Control.UP_KEY)) {
			body.setLinearVelocity(body.getLinearVelocity().x, MOVE_SPEED * dt);
			if (!isWalking) stepSfx.play(0.2f, MathUtils.random(0.5f, 2), 0);
			isWalking = true;
		}
		
		if (Gdx.input.isKeyPressed(Control.DOWN_KEY)) {
			body.setLinearVelocity(body.getLinearVelocity().x, -MOVE_SPEED * dt);
			if (!isWalking) stepSfx.play(0.2f, MathUtils.random(0.5f, 2), 0);
			isWalking = true;
		}
		
		// Interact
		if (Gdx.input.isKeyJustPressed(Control.CONFIRM_BUTTON) && nextTo != null) {
			nextTo.interactWith(this);
			World world = body.getWorld();
			float x = body.getPosition().x;
			float y = body.getPosition().y;
			if (nextTo instanceof Drill && currentShape == Shape.NORMAL) {
				currentShape = Shape.DRILL;
				shifting = true;
				shiftSfx.play();
				animator = animators.get("shift drill");
				animator.reset();
				animator.setLoop(false);
				world.destroyBody(body);
				createBody(world, x, y);
				createDrillFixture();
			} else if (nextTo instanceof Chainsaw && currentShape == Shape.NORMAL) {
				currentShape = Shape.CHAINSAW;
				shifting = true;
				shiftSfx.play();
				animator = animators.get("shift chainsaw");
				animator.reset();
				animator.setLoop(false);
				world.destroyBody(body);
				createBody(world, x, y);
				createChainsawFixture();
			}
		}
		
		if (Gdx.input.isKeyPressed(Control.CONFIRM_BUTTON)) {
			doAction = true;
			if (nextTo != null) {
				if (nextTo instanceof Stone && currentShape == Shape.DRILL) {
					nextTo.interactWith(this);
				}
			}
		} else {
			doAction = false;
		}
		
	}
	
	public void setNextTo(BubbledEntity what) {
		nextTo = what;
	}
	
	public Shape getShape() {
		return currentShape;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		shiftSfx.dispose();
		stepSfx.dispose();
	}
	
	public BubbledEntity getNextEntity() {
		return nextTo;
	}
}
