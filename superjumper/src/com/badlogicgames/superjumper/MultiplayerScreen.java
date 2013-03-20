package com.badlogicgames.superjumper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MultiplayerScreen implements Screen {
   Game game;

   OrthographicCamera guiCam;
   SpriteBatch batcher;
   Rectangle backBounds;
   Rectangle ClientBounds;
   Rectangle ServerBounds;
   Vector3 touchPoint;
   String[] highScores;
   float xOffset = 0;
   static String str = "INIT";

   public MultiplayerScreen (Game game) {
           this.game = game;

           guiCam = new OrthographicCamera(320, 480);
           guiCam.position.set(320 / 2, 480 / 2, 0);
           backBounds = new Rectangle(0, 0, 64, 64);
           ClientBounds = new Rectangle(200, 100, 33, 33);
           ServerBounds = new Rectangle(280, 180, 33, 33);
           touchPoint = new Vector3();
           batcher = new SpriteBatch();
           highScores = new String[5];
           for (int i = 0; i < 5; i++) {
                   highScores[i] = i + 1 + ". " + Settings.highscores[i];
                   xOffset = Math.max(Assets.font.getBounds(highScores[i]).width, xOffset);
           }
           xOffset = 160 - xOffset / 2 + Assets.font.getSpaceWidth() / 2;
   }

   public void update (float deltaTime) {
           if (Gdx.input.justTouched()) {
                   guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

                   if (OverlapTester.pointInRectangle(backBounds, touchPoint.x, touchPoint.y)) {
                           Assets.playSound(Assets.clickSound);
                           game.setScreen(new MainMenuScreen(game));
                           return;
                   }
                   else if (OverlapTester.pointInRectangle(ClientBounds, touchPoint.x, touchPoint.y)) {
                           Assets.playSound(Assets.clickSound);
                           str = "CONNECTING";
                           ConnectThread thr = new ConnectThread("192.168.0.2",9999);
                           thr.start();
                   }
                   else if (OverlapTester.pointInRectangle(ServerBounds, touchPoint.x, touchPoint.y)) {
                           Assets.playSound(Assets.clickSound);
                           str = "ACCEPTING";
                           AcceptThread thr = new AcceptThread(9999);
                           thr.start();
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
           batcher.draw(Assets.backgroundRegion, 0, 0, 320, 480);
           batcher.end();

           batcher.enableBlending();
           batcher.begin();
           batcher.draw(Assets.highScoresRegion, 10, 360 - 16, 300, 33);
           batcher.draw(Assets.life1, 200, 100, 33, 33);
           batcher.draw(Assets.life, 280, 180, 33, 33);
           Assets.font.draw(batcher, str, 16, 460);


           float y = 230;
           for (int i = 4; i >= 0; i--) {
                   Assets.font.draw(batcher, highScores[i], xOffset, y);
                   y += Assets.font.getLineHeight();
           }

           batcher.draw(Assets.arrow, 0, 0, 64, 64);
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
