package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.values.RectangleSpawnShapeValue;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import com.badlogic.gdx.math.Rectangle;
import java.util.Iterator;

public class MyGame extends ApplicationAdapter {
	OrthographicCamera camera;
	SpriteBatch batch;
	Texture cosImage;
	Texture fructImage;
	Sound dropSound;
	Music fonMusic;
	Rectangle bucket;
	Vector3 touchPos;
	Array<Rectangle> fructeDrops;
	long lastDropTime;

	@Override
	public void create() {

		touchPos = new Vector3();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();

		cosImage = new Texture("cos.png");
		fructImage = new Texture("banana.png");

		dropSound = Gdx.audio.newSound(Gdx.files.internal("cadere.wav"));
		fonMusic = Gdx.audio.newMusic(Gdx.files.internal("fon.mp3"));

		fonMusic.setLooping(true);
		fonMusic.play();

		bucket = new Rectangle();
		bucket.x = 800/2-65/2;
		bucket.y=20;
		bucket.width = 65;
		bucket.height = 65;

		fructeDrops = new Array<Rectangle>();
		spawnFructDrop();
	}

	private void spawnFructDrop(){
		Rectangle fructeDrop = new Rectangle();
		fructeDrop.x = MathUtils.random(0,800-50);
		fructeDrop.y = 480;
		fructeDrop.width = 50;
		fructeDrop.height = 50;
		fructeDrops.add(fructeDrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(cosImage, bucket.x, bucket.y);
		for (Rectangle fructeDrop:fructeDrops){
			batch.draw(fructImage, fructeDrop.x, fructeDrop.y);
		}
		batch.end();

		if(Gdx.input.isTouched()){
			touchPos.set (Gdx.input.getX(), Gdx.input.getY(),0);
			camera.unproject(touchPos);
			bucket.x = (int) (touchPos.x - 65/2);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800-65) bucket.x = 800-65;

		if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnFructDrop();
		Iterator<Rectangle> iter = fructeDrops.iterator();
		while (iter.hasNext()){
			Rectangle fructeDrop = iter.next();
			fructeDrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (fructeDrop.y + 50 < 0) iter.remove();
			if (fructeDrop.overlaps(bucket)){
				dropSound.play();
				iter.remove();
			}
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		fructImage.dispose();
		cosImage.dispose();
		dropSound.dispose();
		fonMusic.dispose();
		batch.dispose();
	}
}
