package com.tybprojekt.ld35.game.entities;

import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
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
import com.tybprojekt.ld35.game.states.PlayState;

public class Player extends Entity {
	
	public enum Shape {
		NORMAL,
		SHIFTING,
		DRILL,
		CHAINSAW,
		LASER
	}

	public float MOVE_SPEED = 10000;
	private float playTime;
	private boolean facingLeft;
	private BubbledEntity nextTo;
	private TreeMap<String, Animator> animators;
	private Animator animator;
	private Body body;
	private Shape currentShape;
	public float SHIFT_TIME = 10;
	private Sound shiftSfx;
	private Sound stepSfx;
	private boolean isWalking;
	private boolean doAction;
	private boolean shifting;
	private boolean win;
	private Sound teleport;
	
	private float timer;
	
	private final float MAX_LIFETIME = 120f;
	private float lifeTime;
	
	private int essences;
	
	public Player() {
		playTime = 0;
		facingLeft = true;
		
		lifeTime = MAX_LIFETIME;
		win = false;
		
		animators = new TreeMap<String, Animator>();
		animators.put("normal", new Animator("slime_guy_idle.png", 0.3f));
		
		// Drill animations
		animators.put("shift drill", new Animator("slime_transform_drill.png", 0.1f));
		animators.put("drill", new Animator("slime_drill.png", 0.3f));
		animators.put("drilling", new Animator("slime_drilling.png", 0.05f));
		
		// Chainsaw animations
		animators.put("shift chainsaw", new Animator("slime_transform_chainsaw.png", 0.1f));
		animators.put("chainsaw", new Animator("slime_chainsaw.png", 0.3f));
		animators.put("chainsawing", new Animator("slime_chainsawing.png", 0.03f));
		
		// Laser animations
		animators.put("shift laser", new Animator("slime_transform_laser.png", 0.1f));
		animators.put("laser", new Animator("slime_laser.png", 0.3f));
		animators.put("lasering", new Animator("slime_lasering.png", 0.03f));
		

		animators.put("win", new Animator("slime_win.png", 0.03f));
		
		animator = animators.get("normal");
		
		sprite = new Sprite(animator.getCurrentFrame(), 0, 0, 2 * animator.getCurrentFrame().getRegionWidth(), 2 * animator.getCurrentFrame().getRegionHeight());
		currentShape = Shape.NORMAL;
		
		shiftSfx = Gdx.audio.newSound(Gdx.files.internal("shapeshift.ogg"));
		stepSfx = Gdx.audio.newSound(Gdx.files.internal("step.ogg"));
		teleport = Gdx.audio.newSound(Gdx.files.internal("dadah.wav"));

		essences = 0;
		
		isWalking = false;
		doAction = false;
		shifting = false;
	}
	
	public int getEssencesNum() {
		return essences;
	}
	
	public void addEssences(int amount) {
		essences += amount;
	}
	
	public void useEssences(int amount) {
		essences -= amount;
	}
	
	public void createBody(World world, boolean normal) {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.DynamicBody;
		bdef.position.set(0, 0);
		body = world.createBody(bdef);
		body.setUserData(this);
		
		if (normal) createNormalFixture();
	}
	
	public Body getBody() {
		return body;
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
	
	private void createLaserFixture() {
		for(int i = 0; i < body.getFixtureList().size; i++)
			body.destroyFixture(body.getFixtureList().get(i));
		body.getFixtureList().clear();
		
		// Collider
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(getHalfWidth() - 12, getHalfHeight()/2 - 2, new Vector2(0, 7), 0);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		body.createFixture(fdef);
		
		// Interacting range
		shape.setAsBox(getHalfWidth()/2-15, getHalfHeight(), new Vector2(0, 15), 0);
		fdef.shape = shape;
		fdef.isSensor = true;
		Fixture fixture = body.createFixture(fdef);
		fixture.setUserData("interactor");
		shape.setAsBox(getHalfWidth(), getHalfHeight()/2-15, new Vector2(0, 16), 0);
		fdef.shape = shape;
		fdef.isSensor = true;
		fixture = body.createFixture(fdef);
		fixture.setUserData("interactor");
		
		shape.dispose();
	}
	
	public float getPercentShiftTime() {
		return MathUtils.clamp((SHIFT_TIME - timer) / SHIFT_TIME, 0, 1);
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
			case LASER:
				animator = new Animator("slime_transform_laser.png", 0.1f, true);
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
			PlayState.INFO_BOX = "you became normal again!";
		}
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.begin();
		sprite.draw(batch);
		batch.end();
	}
	
	public void win() {
		this.win = true;
	}
	
	public boolean isWin() {
		return win;
	}
	
	public Animator getAnimator() {
		return animator;
	}
	
	public int getPlayTime() {
		return (int) playTime;
	}
	
	boolean doneTeleport = false;
	@Override
	public void update(float dt) {
		if (win) {
			if (currentShape != Shape.NORMAL) {
				timer = SHIFT_TIME;
			} else {
				animator = animators.get("win");
				animator.setLoop(false);
				animator.play(dt);
				if (!doneTeleport) {
					teleport.play();
					doneTeleport = true;
				}
				sprite.setRegion(animator.getCurrentFrame());
				
				return;
			}
		}
		
		playTime += dt;
		
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
			case LASER:
				animator = animators.get("laser");
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
				if (!PlayState.hwaa.isPlaying())
					PlayState.hwaa.play();
				animator = animators.get("drilling");
				break;
			case CHAINSAW:
				if (!PlayState.hwaa.isPlaying())
					PlayState.hwaa.play();
				animator = animators.get("chainsawing");
				break;
			case LASER:
				PlayState.cyu.play();
				animator = animators.get("lasering");
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
		
		lifeTime -= dt;
	}
	
	public float getPercentLife() {
		return MathUtils.clamp(lifeTime / 120f, 0, 1);
	}
	
	public void restoreLife() {
		lifeTime = MAX_LIFETIME;
	}
	
	public void  addLifeTime(float amount) {
		lifeTime = lifeTime + amount > MAX_LIFETIME ? MAX_LIFETIME : lifeTime + amount;
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
		if ((Gdx.input.isKeyJustPressed(Control.CONFIRM_BUTTON) || Gdx.input.isKeyJustPressed(Keys.Z)) && nextTo != null) {
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
				PlayState.INFO_BOX = "you turned into a drill!";
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
				PlayState.INFO_BOX = "you turned into a chainsaw!";
			} else if (nextTo instanceof Laser && currentShape == Shape.NORMAL) {
				currentShape = Shape.LASER;
				shifting = true;
				shiftSfx.play();
				animator = animators.get("shift laser");
				animator.reset();
				animator.setLoop(false);
				world.destroyBody(body);
				createBody(world, x, y);
				createLaserFixture();
				PlayState.INFO_BOX = "you turned into a... laser thing?";
			}
		}
		
		if ((Gdx.input.isKeyJustPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyJustPressed(Keys.SHIFT_RIGHT)) && currentShape != Shape.NORMAL) {
			PlayState.INFO_BOX = "you tried to turn into normal by force!";
			timer = SHIFT_TIME;
		}
		
		if (Gdx.input.isKeyPressed(Control.CONFIRM_BUTTON) || Gdx.input.isKeyPressed(Keys.Z)) {
			doAction = true;
			if (nextTo != null) {
				if (nextTo instanceof Stone && currentShape == Shape.DRILL) {
					nextTo.interactWith(this);
				} else if (nextTo instanceof Log && currentShape == Shape.CHAINSAW) {
					nextTo.interactWith(this);
				} else if (nextTo instanceof Metal && currentShape == Shape.LASER) {
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
