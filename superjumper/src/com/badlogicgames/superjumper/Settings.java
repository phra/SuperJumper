/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogicgames.superjumper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;

public abstract class Settings {
	public static boolean soundEnabled = true;
	public final static int[] highscores = new int[] {100, 80, 50, 30, 10};
	public final static String file = ".superjumper";


	public static void load () {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(Gdx.files.external(file).read()));
			soundEnabled = Boolean.parseBoolean(in.readLine());
			for (int i = 0; i < 5; i++) {
				highscores[i] = Integer.parseInt(in.readLine());
			}
		} catch (IOException e) {
			Gdx.app.debug("LOAD", "I/O exception");
			// :( It's ok we have defaults
		} catch (GdxRuntimeException e){
			Gdx.app.debug("LOAD", "GdxRuntimeException");
		}

		finally {
			try {
				if (in != null) in.close();
			} catch (IOException e) {
				Gdx.app.debug("LOAD", "I/O exception");
			}
		}
	}

	public static void save () {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(Gdx.files.external(file).write(false)));
			out.write(Boolean.toString(soundEnabled));
			out.write("\n");
			for (int i = 0; i < 5; i++) {
				out.write(Integer.toString(highscores[i]));
				out.write("\n");
			}

		} catch (IOException e) {
			Gdx.app.debug("SAVE", "I/O exception");
		} catch (GdxRuntimeException e){
			Gdx.app.debug("SAVE", "GdxRuntimeException");
		} finally {
			try {
				if (out != null) out.close();
			} catch (IOException e) {
				Gdx.app.debug("SAVE", "I/O exception");
			}
		}
	}


	public static void saveFloat (float i) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(Gdx.files.external(file).write(false)));

			out.write(Float.toString(i));
			out.write("\n");


		} catch (IOException e) {
		} finally {
			try {
				if (out != null) out.close();
			} catch (IOException e) {
			}
		}
	}



	public static float readFloat () {
		BufferedReader in = null;
		float h = 0 ;
		try {
			in = new BufferedReader(new InputStreamReader(Gdx.files.external(file).read()));

			h = Float.parseFloat(in.readLine());

		} catch (Throwable e) {
			// :( It's ok we have defaults
		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException e) {
			}
		}
		return h;
	}
	
	/*
	public static void addScore (int score) {
		for (int i = 0; i < 5; i++) {
			if (highscores[i] < score) {
				for (int j = 4; j > i; j--)
					highscores[j] = highscores[j - 1];
				highscores[i] = score;
				break;
			}
		}
	}*/
	
	public static void addScore(int score) {
		if (score <= highscores[4]) return;
		for (int i = 0; i < 5; i++) {
			if (score > highscores[i]) {
				int temp1 = highscores[i];
				highscores[i++] = score;
				for (; i < 5; i++) {
					int temp2 = highscores[i];
					highscores[i] = temp1;
					temp1 = temp2;
				}
				break;
			}
		}
	}
	
public static int firstScore(){
	int maxscore=highscores[0];
	return maxscore;
}
}
