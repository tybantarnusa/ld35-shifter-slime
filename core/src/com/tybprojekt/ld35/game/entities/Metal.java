package com.tybprojekt.ld35.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tybprojekt.ld35.game.Animator;
import com.tybprojekt.ld35.game.entities.Player.Shape;
import com.tybprojekt.ld35.game.states.PlayState;

public class Metal extends BubbledEntity {
	
	private Body body;
	private Animator animation;
	
	private final float DURABILITY = 10;
	private float damageTaken;
	private boolean lasered;
	private float x, y;
	
	private Texture texture;
	
	private ShapeRenderer healthBar;
	
	public Metal(float x, float y) {
		super();
		this.x = x;
		this.y = y;
		texture = new Texture("metal.png");
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		sprite = new Sprite(texture);
		bubble = new Animator("bubbles/bubble_interact.png", 0.3f);
		animation = new Animator("metal_lasered.png", 0.1f);
		damageTaken = 0;
		lasered = false;
		healthBar = new ShapeRenderer();
	}
	
	public void update(float dt) {
		setPosition(body.getPosition().x - getHalfWidth(), body.getPosition().y - getHalfHeight());

		if (damageTaken >= DURABILITY)
			destroy();
		
		if (lasered) {
			animation.play(Gdx.graphics.getDeltaTime());
			sprite.setRegion(animation.getCurrentFrame());
		} else {
			sprite.setRegion(texture);
		}
		playBubble(dt);
	}
	
	private void destroy() {
		lasered = true;
		if (damageTaken < DURABILITY + 0.1f) {
			animation = new Animator("metal_destroyed.png", 0.05f);
			animation.setLoop(false);
			}
		if (animation.isFinished()) {
			destroyed = true;
			player.addLifeTime(15/2f);
			player.addEssences(35/2);
			for (Fixture f : body.getFixtureList()) {
				body.destroyFixture(f);
			}
		}
		PlayState.INFO_BOX = "you broke a metal wall! (+15 lifetime)";
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.begin();
		sprite.draw(batch);
		batch.end();
		if (lasered) {
			healthBar.setProjectionMatrix(batch.getProjectionMatrix());
			if (damageTaken / DURABILITY < 0.5f)
				healthBar.setColor(Color.GREEN);
			else if (damageTaken / DURABILITY < 0.7f)
				healthBar.setColor(Color.YELLOW);
			else
				healthBar.setColor(Color.RED);
			
			healthBar.begin(ShapeType.Filled);
			healthBar.rect(getX() + 2, getY() + getHeight() - 5, MathUtils.clamp(30 - (30 * damageTaken / DURABILITY), 0, 30), 5);
			healthBar.end();
		}
		lasered = false;
	}
	
	private Player player;
	public void interactWith(Player player) {
		this.player = player;
		if (player.getShape() != Shape.LASER) {
			PlayState.howa.play();
			PlayState.INFO_BOX = "can't break this wall, it's made of metal!";
		} else {
			damageTaken += Gdx.graphics.getDeltaTime();
			lasered = true;
		}
	}
	
	public void createBody(World world) {
		BodyDef bdef = new BodyDef();
		bdef.position.set(x, y);
		bdef.type = BodyDef.BodyType.StaticBody;
		body = world.createBody(bdef);
		body.setUserData(this);
		body.setFixedRotation(true);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(getHalfWidth(), getHalfHeight());
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		Fixture fixture = body.createFixture(fdef);
		fixture.setUserData("interactable");
	}
	
	public void dispose() {
		super.dispose();
		healthBar.dispose();
		animation.dispose();
		texture.dispose();
	}
	
}
