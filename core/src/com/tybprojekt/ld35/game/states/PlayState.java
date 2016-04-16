package com.tybprojekt.ld35.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tybprojekt.ld35.game.Collisions;
import com.tybprojekt.ld35.game.entities.Player;

public class PlayState implements State {
	
	private OrthographicCamera cam;
	private World world;
	private Player player;
	private Box2DDebugRenderer b2dr;
	
	private Body body;
	
	public PlayState() {
		cam = new OrthographicCamera();
		cam.setToOrtho(false);
		
		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new Collisions());
		
		player = new Player();
		player.createBody(world);
		
		b2dr = new Box2DDebugRenderer();
		createDebugBody();
	}
	
	private void createDebugBody() {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		body = world.createBody(bdef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(32/2, 32/2);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		Fixture fixture = body.createFixture(fdef);
		fixture.setUserData("interactable");
		shape.dispose();
	}
	
	@Override
	public void update(float dt) {
		cam.update();
		player.update(dt);
		world.step(dt, 6, 2);
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setProjectionMatrix(cam.combined);
		player.render(batch);
		b2dr.render(world, cam.combined);
	}
	
	public void dispose() {
		player.dispose();
	}

}
