package com.badlogicgames.superjumper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MultiplayerScreen implements Screen {
	public static final int PORT = 10000;
	public static final String IPTOCONNECT = "192.168.1.130";
	Game game;
	public final List<Button> buttons;
	OrthographicCamera guiCam;
	SpriteBatch batcher;
	Rectangle backBounds;
	Rectangle ClientBounds;
	Rectangle ServerBounds;
	Vector3 touchPoint;
	static int seed = 10000;
	String[] highScores;
	float xOffset = 0;
	static String str = "MULTIPLAYER LAN";
	static String client = "PARTECIPA";
	static String server = "OSPITA";
	String message="";

	public MultiplayerScreen (Game game) {
		this.game = game;
		this.buttons=new ArrayList<Button>();
		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);
		backBounds = new Rectangle(0, 0, 64, 64);
		//ClientBounds = new Rectangle(200, 100, 300, 36);
		//ServerBounds = new Rectangle(280, 180, 300, 36);
		ClientBounds = new Rectangle(100, 210, 300, 20);
		ServerBounds = new Rectangle(100, 260, 300, 20);
		touchPoint = new Vector3();
		batcher = new SpriteBatch();
		Button button = new Button(90,230,Assets.resume);
		buttons.add(button);
		Button buttones = new Button(90,180,Assets.quit);
		buttons.add(buttones);
	}

	public void update (float deltaTime) {
		int len = buttons.size();
		for (int i = 0; i < len; i++) {
			Button button=buttons.get(i);
			button.update(deltaTime);
		}
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (OverlapTester.pointInRectangle(backBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new MainMenuScreen(game));
				return;
			} else if (OverlapTester.pointInRectangle(ClientBounds, touchPoint.x, touchPoint.y)) {
				Semaphore sem = new Semaphore(0,true);
				Assets.playSound(Assets.clickSound);
				str = "CONNECTING";
				Gdx.app.debug("PHTEST", "BUFFER STATUS = " + MultiWorld.buffer.selfTest());
				ConnectThread thr = new ConnectThread(IPTOCONNECT,PORT,MultiWorld.buffer,sem);
				thr.start();
				Gdx.app.debug("PHTEST", "started connect thread");
				try {
					sem.acquire();
				} catch (InterruptedException e) {
					str = "ERROR.";
					return;
				}
				str = "CONNECTED";
				game.setScreen(new MultiGameScreen(game,seed));
			} else if (OverlapTester.pointInRectangle(ServerBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				str = "ACCEPTING";
				Semaphore sem = new Semaphore(0,true);
				if (MultiWorld.buffer.selfTest()) Gdx.app.debug("PHTEST", "BUFFER OK");
				else Gdx.app.debug("PHTEST", "BUFFER KO");
				Gdx.app.debug("PHTEST", "starto accept thread");
				AcceptThread thr = new AcceptThread(PORT,MultiWorld.buffer,sem);
				thr.start();
				Gdx.app.debug("PHTEST", "started accept thread");
				try {
					sem.acquire();
				} catch (InterruptedException e) {
					str = "ERROR.";
					return;
				}
				str = "CONNECTED";
				game.setScreen(new MultiGameScreen(game,seed));

			}
		}
	}
	public void draw (float deltaTime) {
		GLCommon gl = Gdx.gl;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.update();

		batcher.setProjectionMatrix(guiCam.combined);
		batcher.disableBlending();
		batcher.begin();
		//MainMenuScreen.drawGradient(batcher, Assets.rect, 0, 0, 320, 480,Color.BLACK,Color.BLUE, false);
		batcher.end();
		batcher.enableBlending();
		batcher.begin();
		batcher.draw(Assets.welcomemulti, 0,0, 512, 512);
		//Assets.font.draw(batcher, client, 100,230);
		//Assets.font.draw(batcher, server, 120,280);
		batcher.draw(Assets.icontextback, 3,10, 54, 54);

		int len = buttons.size();
		for (int i = 0; i < len; i++) {
			Button button = buttons.get(i);
			Texture keyFrame =Assets.ospita;
			if(i==1)keyFrame=Assets.partecipa;
			batcher.draw(keyFrame,button.position.x,button.position.y,145,145);
		}

		batcher.end();
	}

	@Override
	public void render (float delta) {
		update(delta);
		draw(delta);
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void show () {
	}

	@Override
	public void hide () {
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}

	public void test() {


		Socket smtpSocket = null;  
		DataOutputStream os = null;
		DataInputStream is = null;

		try {
			smtpSocket = new Socket("localhost", 9999);
			os = new DataOutputStream(smtpSocket.getOutputStream());
			is = new DataInputStream(smtpSocket.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: hostname");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: hostname");
		}

		if (smtpSocket != null && os != null && is != null) {
			try {

				os.writeBytes("HELLO\n"); 
				os.close();
				is.close();
				smtpSocket.close();   
			} catch (UnknownHostException e) {
				System.err.println("Trying to connect to unknown host: " + e);
			} catch (IOException e) {
				System.err.println("IOException:  " + e);
			}
		}

	}
}