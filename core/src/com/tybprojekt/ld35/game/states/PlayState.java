package com.tybprojekt.ld35.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.tybprojekt.ld35.game.Collisions;
import com.tybprojekt.ld35.game.entities.Chainsaw;
import com.tybprojekt.ld35.game.entities.Drill;
import com.tybprojekt.ld35.game.entities.Entity;
import com.tybprojekt.ld35.game.entities.Gem;
import com.tybprojekt.ld35.game.entities.Laser;
import com.tybprojekt.ld35.game.entities.Log;
import com.tybprojekt.ld35.game.entities.Metal;
import com.tybprojekt.ld35.game.entities.Player;
import com.tybprojekt.ld35.game.entities.Stone;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class PlayState implements State {
	
	private GameStateManager gsm;
	
	private Texture hud;
	private Texture grass;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	private World world;
	private Player player;
	
	private ShapeRenderer hudShaper;
	
	private Array<Entity> entities;
	private Array<Entity> destroyQueue;
	
	public static String INFO_BOX;
	
	private Music bgm;
	private Music lowLifeBgm;
	public static Sound howa;
	public static Sound cyu;
	public static Music hwaa;
	
	private BitmapFont font;
	private BitmapFont smallFont;
	private BitmapFont numFont;
	
	private RayHandler rayHandler;
	private PointLight winLight;
	
	private Gem diamond;
	private Body limit;
	
	public PlayState(GameStateManager gsm) {
		this.gsm = gsm;
		INFO_BOX = "find a shape-shifter tool!";
		
		howa = Gdx.audio.newSound(Gdx.files.internal("howa.wav"));
		cyu = Gdx.audio.newSound(Gdx.files.internal("ciyu.wav"));
		hwaa = Gdx.audio.newMusic(Gdx.files.internal("ahwaahwaahwa.wav"));
		
		UpgradeState.reset();
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false);
		
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false);
		hud = new Texture("hud.png");
		hudShaper = new ShapeRenderer();
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = 16;
		param.color = Color.WHITE;
		font = generator.generateFont(param);
		param.size = 12;
		smallFont = generator.generateFont(param);
		generator.dispose();
		
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fontnum.ttf"));
		param.size = 18;
		param.color = new Color(157/225f, 241/225f, 157/225f, 1);
		numFont = generator.generateFont(param);
		generator.dispose();
		
		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new Collisions());
		
		player = new Player();
		player.createBody(world, true);
		
		grass = new Texture("grass.png");
		grass.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		entities = new Array<Entity>();
		
		destroyQueue = new Array<Entity>();
		createEntities();
		
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0.85f);
		winLight = new PointLight(rayHandler, 128);
		winLight.setDistance(500);
		winLight.setColor(0, 1, 0, 0.8f);
		winLight.attachToBody(diamond.getBody());
		winLight.setSoftnessLength(0);
		
		bgm = Gdx.audio.newMusic(Gdx.files.internal("level1.ogg"));
		bgm.setLooping(true);
		
		lowLifeBgm = Gdx.audio.newMusic(Gdx.files.internal("panic.ogg"));
		lowLifeBgm.setLooping(true);
		
		createLimit();
	}
	
	private void createLimit() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(0, 0);
		bdef.type = BodyDef.BodyType.StaticBody;
		limit = world.createBody(bdef);
		limit.setUserData("barrier");
		limit.setFixedRotation(true);
		PolygonShape shape = new PolygonShape();
		shape.set(new float[]{-2000, 2000, -2005, 2000, -2005, -2000, -2000, -2000});
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		limit.createFixture(fdef);
		shape.set(new float[]{-2000, 2000, -2000, 2005, 2000, 2005, 2000, 2000});
		fdef.shape = shape;
		limit.createFixture(fdef);
		shape.set(new float[]{2000, 2000, 2005, 2000, 2005, -2000, 2000, -2000});
		fdef.shape = shape;
		limit.createFixture(fdef);
		shape.set(new float[]{2000, -2000, 2000, -2005, -2000, -2005, -2000, -2000});
		fdef.shape = shape;
		limit.createFixture(fdef);
	}

	
	private void createEntities() {
		Drill drill = new Drill();
		drill.createBody(world);
		entities.add(drill);
		
		Chainsaw chainsaw = new Chainsaw();
		chainsaw.createBody(world);
		entities.add(chainsaw);
		
		Laser laser = new Laser();
		laser.createBody(world);
		entities.add(laser);
		
		Gem pendant = new Gem();
		pendant.createBody(world);
		entities.add(pendant);
		diamond = pendant;
		
		for (int i = 0; i < 100; i++) {
			Metal metal = new Metal(MathUtils.random(-1800f, 1800f), MathUtils.random(-1800f, 1800f));
			metal.createBody(world);
			entities.add(metal);
		}
		
		for (int i = 0; i < 200; i++) {
			Log log = new Log(MathUtils.random(-1600f, 1600f), MathUtils.random(-1600f, 1600f));
			log.createBody(world);
			entities.add(log);
		}
		
		for (int i = 0; i < 300; i++) {
			Stone stone = new Stone(MathUtils.random(-1500f, 1500f), MathUtils.random(-1500f, 1500f));
			stone.createBody(world);
			entities.add(stone);
		}
		
	}
	
	private float timer = 0;
	@Override
	public void update(float dt) {
		cam.position.x = player.getX() + player.getHalfWidth();
		cam.position.y = player.getY() + player.getHalfHeight();
		cam.update();

		player.update(dt);
		
		if (player.isWin()) {
			bgm.stop();
			lowLifeBgm.stop();
			
			if (player.getAnimator().isFinished()) {
				timer += dt;
				INFO_BOX = "you win!";
				if (timer > 0.5f) {
					gsm.change(new WinState(gsm, player));
				}
			} else {
				INFO_BOX = "you are going home!";
				timer = 0;
			}
			
			return;
		}
		
		for (Entity entity : entities) {
			if (entity.isDestroyed()) {
				destroyQueue.add(entity);
			}
			entity.update(dt);
		}
		
		for (Entity entity : destroyQueue) {
			entity.dispose();
			entities.removeValue(entity, true);
		}
		
		destroyQueue.clear();
		
		world.step(dt, 6, 2);
		
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			gsm.push(new UpgradeState(gsm, player));
		}
		
		if (!bgm.isPlaying() && player.getPercentLife() > 0.2f) {
			lowLifeBgm.stop();
			bgm.play();
		}
		
		if (!lowLifeBgm.isPlaying() && player.getPercentLife() < 0.2f) {
			bgm.stop();
			lowLifeBgm.play();
		}
		
		if (player.getPercentLife() > 0.2f) {
			cam.zoom =  MathUtils.clamp(cam.zoom - 0.1f, 1f, cam.zoom - 0.1f);;
		}
		
		if (player.getPercentLife() < 0.2f) {
			cam.zoom = MathUtils.clamp(cam.zoom + 0.01f, cam.zoom + 0.01f, 2f);
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.H)) {
			gsm.push(new HelpState(gsm));
		}
		
		if (player.getPercentLife() == 0) {
			gsm.change(new LoseState(gsm));
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setProjectionMatrix(cam.combined);
		rayHandler.setCombinedMatrix(cam);
		
		batch.begin();
		for (int i = -50; i < 50; i++) {
			for (int j = -50; j < 50; j++) {
				batch.draw(grass, i * grass.getWidth() * 2, j * grass.getHeight() * 2, 2 * grass.getWidth(), 2 * grass.getHeight());
			}
		}
		batch.end();
		
		for (Entity entity : entities) entity.render(batch);
		
		player.render(batch);
		rayHandler.updateAndRender();
		
		
		if (player.getNextEntity() != null) player.getNextEntity().drawBubble(batch);
		
		hudShaper.setProjectionMatrix(hudCam.combined);
		hudShaper.begin(ShapeType.Filled);
		hudShaper.setColor(Color.BLACK);
		hudShaper.rect(30, 56, 30, 320);
		if (player.getPercentLife() > 0.6)
			hudShaper.setColor(Color.GREEN);
		else if (player.getPercentLife() > 0.2)
			hudShaper.setColor(Color.YELLOW);
		else
			hudShaper.setColor(Color.RED);
		hudShaper.rect(30, 56, 30, 320 * player.getPercentLife());
		
		hudShaper.setColor(Color.BLACK);
		hudShaper.rect(116, 420, 341, 20);
		if (player.getPercentShiftTime() > 0.6)
			hudShaper.setColor(Color.GREEN);
		else if (player.getPercentShiftTime() > 0.2)
			hudShaper.setColor(Color.YELLOW);
		else
			hudShaper.setColor(Color.RED);
		hudShaper.rect(116, 420, 341 * player.getPercentShiftTime(), 20);
		hudShaper.end();
		
		batch.setProjectionMatrix(hudCam.combined);
		batch.begin();
		batch.draw(hud, 0, 0);
		smallFont.draw(batch, "press ENTER to upgrade", 110, 90);
		smallFont.draw(batch, String.format("position: (%d, %d)", (int) player.getBody().getPosition().x, (int) player.getBody().getPosition().y), 410, 90);
		font.draw(batch, INFO_BOX, 110, 65);
		numFont.draw(batch, String.format("%03d", player.getEssencesNum()), 553, 423);
		batch.end();
	}
	
	public void dispose() {
		bgm.dispose();
		player.dispose();
		hud.dispose();
		hudShaper.dispose();
		smallFont.dispose();
		font.dispose();
		numFont.dispose();
		for (Entity entity : entities) {
			entity.dispose();
		}
		rayHandler.dispose();
		grass.dispose();
		lowLifeBgm.dispose();
		howa.dispose();
		hwaa.dispose();
		cyu.dispose();
	}
	
	public OrthographicCamera getCam() {
		return cam;
	}
	
}
