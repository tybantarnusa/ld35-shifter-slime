package com.tybprojekt.ld35.game.states;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.tybprojekt.ld35.game.Collisions;
import com.tybprojekt.ld35.game.entities.Chainsaw;
import com.tybprojekt.ld35.game.entities.Drill;
import com.tybprojekt.ld35.game.entities.Entity;
import com.tybprojekt.ld35.game.entities.Player;
import com.tybprojekt.ld35.game.entities.Stone;

public class PlayState implements State {
	
	private GameStateManager gsm;
	
	private Texture hud;
	private Texture grass;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	private World world;
	private Player player;
	private Box2DDebugRenderer b2dr;
	
	private ShapeRenderer hudShaper;
	
	private Array<Entity> entities;
	private Array<Entity> destroyQueue;
	
	public static String INFO_BOX = "find the device!";
	
	private Music bgm;
	private PrintWriter writer;
	
	private BitmapFont font;
	private BitmapFont smallFont;
	private BitmapFont numFont;
	
	public PlayState(GameStateManager gsm) {
		this.gsm = gsm;
		
		try {
			writer = new PrintWriter(new File("kodingan.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		cam = new OrthographicCamera();
		cam.setToOrtho(false);
//		cam.zoom = 0.5f;
		
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
		
		b2dr = new Box2DDebugRenderer();
		
		entities = new Array<Entity>();
		
		destroyQueue = new Array<Entity>();
		createEntities();
		bgm = Gdx.audio.newMusic(new FileHandle("level1.ogg"));
		bgm.setLooping(true);
		bgm.play();
	}
	
	private void createEntities() {
		Drill drill = new Drill();
		drill.createBody(world);
		entities.add(drill);
		
		Chainsaw chainsaw = new Chainsaw();
		chainsaw.createBody(world);
		entities.add(chainsaw);
		
		// Make stones
		Stone stone = new Stone(231, 172);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-231.82f, -559.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-191.82f, -572.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-149.82f, -568.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-105.82f, -589.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-69.82f, -573.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-28.82f, -568.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(11.18f, -575.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(57.18f, -585.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(102.18f, -584.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(141.18f, -568.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(181.18f, -571.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(215.18f, -587.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(252.18f, -573.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(292.18f, -560.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(332.18f, -572.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(374.18f, -576.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(404.94f, -586.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(447.94f, -588.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(485.94f, -586.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(524.94f, -585.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(562.94f, -587.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(597.94f, -573.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(627.94f, -548.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(622.94f, -512.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(621.94f, -463.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(624.94f, -424.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(629.94f, -389.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(612.94f, -341.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(637.94f, -302.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(637.39f, -265.42f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(645.39f, -227.42f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(626.39f, -190.42f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(648.39f, -149.42f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(627.39f, -107.42f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(649.39f, -65.42f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(628.39f, -23.42f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(650.39f, 19.58f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(661.39f, 59.58f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(675.39f, 98.58f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(676.39f, 145.58f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(675.09f, 183.18f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(684.09f, 219.18f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(693.09f, 264.35f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(690.09f, 310.35f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(690.09f, 354.35f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(703.09f, 394.35f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(703.09f, 436.35f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(689.09f, 482.35f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(679.09f, 527.35f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(676.09f, 562.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(669.09f, 599.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(639.09f, 614.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(603.09f, 611.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(561.09f, 606.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(517.09f, 612.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(481.09f, 625.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(438.20f, 622.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(395.20f, 613.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(359.20f, 611.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(321.20f, 616.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(289.20f, 636.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(249.20f, 623.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(209.20f, 623.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(170.20f, 638.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(136.20f, 629.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(99.20f, 616.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(54.20f, 618.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(8.20f, 640.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-33.80f, 627.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-76.80f, 629.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-114.80f, 631.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-153.83f, 632.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-194.83f, 641.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-239.83f, 644.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-277.83f, 641.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-322.83f, 638.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-361.83f, 645.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-403.83f, 642.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-445.83f, 647.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-490.83f, 639.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-539.83f, 655.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-583.83f, 650.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-630.83f, 638.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-649.17f, 603.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-662.17f, 572.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-669.17f, 531.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-666.17f, 485.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-661.17f, 451.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-663.17f, 416.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-675.17f, 384.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-676.17f, 350.08f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-664.17f, 315.19f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-658.17f, 278.19f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-660.17f, 242.19f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-671.17f, 210.19f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-671.17f, 176.19f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-667.17f, 143.19f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-672.17f, 112.19f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-655.17f, 65.97f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-680.17f, 35.97f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-663.17f, -4.03f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-686.17f, -40.03f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-668.17f, -76.03f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-674.17f, -111.81f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-683.17f, -143.81f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-671.17f, -172.81f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-667.17f, -205.90f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-669.17f, -249.90f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-663.17f, -290.90f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-652.17f, -332.90f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-654.17f, -368.14f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-651.17f, -405.14f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-655.17f, -435.14f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-658.17f, -474.14f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-664.17f, -514.11f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-662.17f, -549.11f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-656.17f, -577.11f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-621.17f, -591.11f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-578.17f, -585.11f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-539.17f, -582.11f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-495.17f, -577.11f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-457.17f, -572.11f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-423.17f, -566.11f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-386.17f, -578.11f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-346.17f, -569.11f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-310.17f, -565.11f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-270.17f, -566.11f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-119.19f, -191.77f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-336.32f, -135.67f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-477.37f, 138.86f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-390.91f, 365.58f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-157.54f, 549.82f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-511.02f, 536.82f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-74.98f, 226.19f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-234.70f, -50.35f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-578.00f, -38.16f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(15.24f, -57.23f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(154.44f, 102.51f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(154.52f, 357.32f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-27.87f, 462.90f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(402.73f, 402.39f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(500.59f, 242.22f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(430.81f, 56.20f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(273.87f, -144.01f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(138.15f, -299.55f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-176.52f, -336.52f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-72.18f, 113.14f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-365.33f, 137.15f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-183.00f, 395.67f);
		stone.createBody(world);
		entities.add(stone);
		stone = new Stone(-354.98f, 522.71f);
		stone.createBody(world);
		entities.add(stone);
	}
	
	@Override
	public void update(float dt) {
		cam.position.x = player.getX() + player.getHalfWidth();
		cam.position.y = player.getY() + player.getHalfHeight();
		cam.update();
		player.update(dt);
		for (Entity entity : entities) {
			if (entity.isDestroyed()) {
				destroyQueue.add(entity);
			}
			entity.update(dt);
		}
		
		for (Entity entity : destroyQueue) {
			entities.removeValue(entity, true);
		}
		
		destroyQueue.clear();
		
		world.step(dt, 6, 2);
		
		if (Gdx.input.justTouched()) {
			Vector3 clickPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			clickPos = cam.unproject(clickPos);
			Stone stone = new Stone(clickPos.x, clickPos.y);
			stone.createBody(world);
			entities.add(stone);
			writer.printf("stone = new Stone(%.2ff, %.2ff);%n", clickPos.x, clickPos.y);
			writer.print("stone.createBody(world);\n");
			writer.print("entities.add(stone);\n");
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.NUM_0)) {
			writer.flush();
			System.out.println("New entities codes saved!");
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			gsm.push(new UpgradeState(gsm, player));
		}
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
	}
	
	public OrthographicCamera getCam() {
		return cam;
	}
	
}
