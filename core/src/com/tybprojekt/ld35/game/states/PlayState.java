package com.tybprojekt.ld35.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
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
	
	private Texture grass;
	private OrthographicCamera cam;
	private World world;
	private Player player;
	private Box2DDebugRenderer b2dr;
	
	private Array<Entity> entities;
	
	private Music bgm;
	
	public PlayState() {
		cam = new OrthographicCamera();
		cam.setToOrtho(false);
		
		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new Collisions());
		
		player = new Player();
		player.createBody(world);
		
		grass = new Texture("grass.png");
		grass.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		b2dr = new Box2DDebugRenderer();
		
		entities = new Array<Entity>();
		createDebugEntity();
//		bgm = Gdx.audio.newMusic(new FileHandle("level1.mp3"));
//		bgm.play();
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
		batch.begin();
		for (int i = -50; i < 50; i++) {
			for (int j = -50; j < 50; j++) {
				batch.draw(grass, i * grass.getWidth() * 2, j * grass.getHeight() * 2, 2 * grass.getWidth(), 2 * grass.getHeight());
			}
		}
		batch.end();
		for (Entity entity : entities) entity.render(batch);
		player.render(batch);
		if (player.getNextEntity() != null) player.getNextEntity().drawBubble(batch);
//		b2dr.render(world, cam.combined);
	}
	
	public void dispose() {
		player.dispose();
	}

}
