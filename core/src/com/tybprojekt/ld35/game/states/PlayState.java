package com.tybprojekt.ld35.game.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.tybprojekt.ld35.game.Collisions;
import com.tybprojekt.ld35.game.entities.Drill;
import com.tybprojekt.ld35.game.entities.Entity;
import com.tybprojekt.ld35.game.entities.Player;

public class PlayState implements State {
	
	private OrthographicCamera cam;
	private World world;
	private Player player;
	private Box2DDebugRenderer b2dr;
	
	private Array<Entity> entities;
	
	public PlayState() {
		cam = new OrthographicCamera();
		cam.setToOrtho(false);
		
		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new Collisions());
		
		player = new Player();
		player.createBody(world);
		
		b2dr = new Box2DDebugRenderer();
		
		entities = new Array<Entity>();
		createDebugEntity();
	}
	
	private void createDebugEntity() {
		Drill drill = new Drill();
		drill.createBody(world);
		entities.add(drill);
	}
	
	@Override
	public void update(float dt) {
		cam.position.x = player.getX() + player.getHalfWidth();
		cam.position.y = player.getY() + player.getHalfHeight();
		cam.update();
		player.update(dt);
		for (Entity entity : entities) entity.update(dt);
		world.step(dt, 6, 2);
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setProjectionMatrix(cam.combined);
		for (Entity entity : entities) entity.render(batch);
		player.render(batch);
		if (player.getNextEntity() != null) player.getNextEntity().drawBubble(batch);
//		b2dr.render(world, cam.combined);
	}
	
	public void dispose() {
		player.dispose();
	}

}
