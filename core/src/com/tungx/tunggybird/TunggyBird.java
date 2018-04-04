package com.tungx.tunggybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Random;

public class TunggyBird extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture background;
	private Texture[] birds;
	private Texture bottomTube;
	private Texture topTube;
	private Texture base;
	private Texture scoreBox;
	private Texture secondScore;
	private Texture gameOver;
	private Texture message;
    private Texture medal_bronze;
    private Texture medal_gold;
    private Texture medal_platinum;
    private Texture birdUpdate;
    private Texture replayTexture;
    private TextureRegion replayTextureRegion;
    private TextureRegionDrawable replayTextureRegionDrawable;
    private ImageButton replayButton;
    private Stage stage;
	private Circle birdCircle;
	private Rectangle[] bottomTubeRec;
	private Rectangle[] topTubeRec;
//    ShapeRenderer shapeRenderer;
    private float birdY = 0;
    private float birdX = 0;
	private float velocity = 0;
    private float gravity = 0;
    private float space = 0;
    private float baseHeight = 0;
    private float scoreWidth = 0;
    private float scoreHeight = 0;
    private float messageWidth = 0;
    private float messageHeight = 0;
    private float gap = 0;
    private float tubeWidth = 0;
    private float tubeHeight = 0;
    private float birdWidth = 0;
    private float birdHeight = 0;
    private float scoreBoardWidth = 0;
    private float scoreBoardHeight = 0;
    private float tubeVelocity = 0;
    private float topTubeY = 0;
    private float botTubeY = 0;
    private float distanceBetweenTubes;

    private int scoringTube = 0;
    private int numberOfTubes = 4;
    private int firstE = 0;
    private int secondE = -1;
    private int flapState = 0;
    private int gameState = 0;
    private int score = 0;
    private int random_background;

    private float[] tubeX = new float[numberOfTubes];
    private float[] baseX = new float[numberOfTubes];
    private float[] tubeOffset = new float[numberOfTubes];

    private Random randomGenerator;
    private Random randomBg;

    private Sound die;
    private Sound wing;
    private Sound swooshing;
    private Sound point;
    private Sound hit;

    private boolean replayIsPressed = false;

	@Override
	public void create () {

		batch = new SpriteBatch();

        randomBg = new Random();

        //Background
        random_background = randomBg.nextInt(2);

        if (random_background == 0) {

            background = new Texture("background-day.png");

        } else {

            background = new Texture("background-night.png");

        }

        //Sound
        die = Gdx.audio.newSound(Gdx.files.internal("hurt.wav"));
        wing = Gdx.audio.newSound(Gdx.files.internal("sfx_wing.wav"));
        swooshing = Gdx.audio.newSound(Gdx.files.internal("sfx_swooshing.wav"));
        point = Gdx.audio.newSound(Gdx.files.internal("sfx_point.wav"));
        hit = Gdx.audio.newSound(Gdx.files.internal("sfx_hit.wav"));
        
        //Bird texture
		birds = new Texture[7];

		birds[0] = new Texture("bird-midflap.png");
		birds[1] = new Texture("bird-upflap.png");
        birds[2] = new Texture("bird-downflap.png");
        birds[3] = new Texture("bird-midtilt.png");
        birds[4] = new Texture("bird-uptilt.png");
        birds[5] = new Texture("bird-downtilt.png");
        birds[6] = new Texture("bird-down.png");

        birdUpdate = birds[0];

        //Measurement
        birdWidth = Gdx.graphics.getWidth() * birds[0].getWidth() / 1080f;
        birdHeight = Gdx.graphics.getHeight() * birds[0].getHeight() / 1776f;

        //birds[birds.length - 1] = new Texture("bird-down.png");

		bottomTube = new Texture("bottomtube.png");
		topTube = new Texture("toptube.png");
		base = new Texture("base.png");
		gameOver = new Texture("scoreboard.png");
		secondScore = new Texture("0.png");
        medal_bronze = new Texture("medal_bronze.png");
        medal_gold = new Texture("medal_gold.png");
        medal_platinum = new Texture("medal_platinum.png");

        birdCircle = new Circle();

        gravity = (float) Gdx.graphics.getHeight() * 2f / 1776f;
        tubeVelocity = (float) Gdx.graphics.getWidth() * 6f / 1080f;
        baseHeight = (Gdx.graphics.getWidth() * base.getHeight() / base.getWidth()) * 2f / 3f;

        tubeWidth = Gdx.graphics.getWidth() * topTube.getWidth() / 1080f;
        tubeHeight = Gdx.graphics.getHeight() * topTube.getHeight() / 1776f;

        scoreBoardWidth = Gdx.graphics.getWidth() * 3 / 4;
        scoreBoardHeight = scoreBoardWidth * gameOver.getHeight() / gameOver.getWidth();

        gap = Gdx.graphics.getWidth() * 450f / 1080f;

        bottomTubeRec = new Rectangle[numberOfTubes];
        topTubeRec = new Rectangle[numberOfTubes];

//        shapeRenderer = new ShapeRenderer();

		distanceBetweenTubes = Gdx.graphics.getWidth() * 0.7f;

		randomGenerator = new Random();

		setup();

        setAnimation(0, 1, 2, 0.15f);

        messageWidth = Gdx.graphics.getWidth() / 2;
        messageHeight = messageWidth * message.getHeight() / message.getWidth();

        scoreWidth = Gdx.graphics.getWidth() * scoreBox.getWidth() / 1080f;
        scoreHeight = Gdx.graphics.getHeight() * scoreBox.getHeight() / 1776f;
        space = scoreWidth / Gdx.graphics.getWidth() * 100f;
	}

	@Override
	public void render () {

		batch.begin();

		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        birdCircle.set(birdX + birdWidth / 2, birdY + birdHeight / 2, birdHeight / 2);

		if(gameState == 1) {

            if (Gdx.input.justTouched()) {

                //Bird flapping velocity
                velocity = - Gdx.graphics.getHeight() * 30 / 1776;

                wing.play();

            }

            //Move bird to center of the screen

            new com.badlogic.gdx.utils.Timer().scheduleTask(new com.badlogic.gdx.utils.Timer.Task() {

                @Override
                public void run() {

                    if (birdX <= Gdx.graphics.getWidth() / 2 - birdWidth / 2) {

                        birdX += 5;

                    }

                }

            }, 0f,0.5f);

            //Update score if earned
            if (tubeX[scoringTube] < birdX) {

                point.play();

                score++;

                if (score <= 9) {

                    scoreBox = new Texture(score + ".png");

                } else {

					firstE = score / 10;
                    scoreBox = new Texture(firstE + ".png");
					secondE = score - firstE * 10;
					secondScore = new Texture(secondE + ".png");

                }

                if (scoringTube < numberOfTubes - 1) {

                    scoringTube++;

                } else {

                    scoringTube = 0;

                }

            }

            //Increase difficulty
            float smallIncrease = 0;

            if (score >= 0 && score <=9) {

                tubeVelocity = Gdx.graphics.getWidth() * 6f / 1080f;

            } else if (score >= 10 && score <= 22) {

                tubeVelocity = Gdx.graphics.getWidth() * 6f / 1080f + Gdx.graphics.getWidth() * 2f / 1080f;

            } else if (score >= 23 && score <= 37) {

                tubeVelocity = Gdx.graphics.getWidth() * 6f / 1080f + Gdx.graphics.getWidth() * 4f / 1080f;

            } else if (score >= 38 && score <= 54) {

                tubeVelocity = Gdx.graphics.getWidth() * 6f / 1080f + Gdx.graphics.getWidth() * 6f / 1080f;

            } else {

                smallIncrease += Gdx.graphics.getWidth() * 0.2f / 1080f;

                tubeVelocity = Gdx.graphics.getWidth() * 4f / 1080f + Gdx.graphics.getWidth() * 8f / 1080f + smallIncrease;

            }

			for (int i = 0; i < numberOfTubes; i++) {

				if (tubeX[i] < - tubeWidth) {

					tubeX[i] += numberOfTubes * distanceBetweenTubes;

                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

                } else {

					tubeX[i] -= tubeVelocity;

				}

                    //Draw tubes and base
                    topTubeY = Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i] + baseHeight;

                    botTubeY = Gdx.graphics.getHeight() / 2 - gap / 2 - tubeHeight + tubeOffset[i] + baseHeight;

                    batch.draw(base, baseX[i], 0, Gdx.graphics.getWidth(), baseHeight);

					batch.draw(topTube, tubeX[i], topTubeY, tubeWidth, tubeHeight);

                    batch.draw(bottomTube, tubeX[i], botTubeY, tubeWidth, tubeHeight);

                    //Set up Rectangle on tubes for detecting collision
                    topTubeRec[i].set(tubeX[i], topTubeY, tubeWidth, tubeHeight);

                    bottomTubeRec[i].set(tubeX[i], botTubeY, tubeWidth, tubeHeight);

//                    shapeRenderer.rect(bottomTubeRec[i].x, bottomTubeRec[i].y, bottomTubeRec[i].width, bottomTubeRec[i].height);
//                    shapeRenderer.rect(topTubeRec[i].x, topTubeRec[i].y, topTubeRec[i].width, topTubeRec[i].height);

                //Collision Detector

                if (Intersector.overlaps(birdCircle, topTubeRec[i]) || Intersector.overlaps(birdCircle, bottomTubeRec[i])) {

                    hit.play();

                    die.play();

                    gameState = 2;

                }

			}

			if (birdY > baseHeight && birdY < Gdx.graphics.getHeight()) {

                velocity += gravity;
                birdY -= velocity;

			} else {

                hit.play();

                die.play();

				gameState = 2;

			}

		} else if (gameState == 0){

            //Display game title and introduction
            batch.draw(message, Gdx.graphics.getWidth() / 2 - messageWidth / 2, birdY - birdHeight / 2, messageWidth, messageHeight);

			if(Gdx.input.justTouched()) {

				gameState = 1;
                wing.play();
                velocity = - Gdx.graphics.getHeight() * 30 / 1776;

			}

		} else if (gameState == 2) {

            flapState = birds.length - 1;

            for (int i = 0; i < numberOfTubes; i++) {

                topTubeY = Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i] + baseHeight;

                botTubeY = Gdx.graphics.getHeight() / 2 - gap / 2 - tubeHeight + tubeOffset[i] + baseHeight;

                batch.draw(topTube, tubeX[i], topTubeY, tubeWidth, tubeHeight);

                batch.draw(bottomTube, tubeX[i], botTubeY, tubeWidth, tubeHeight);

            }

            if (birdY > baseHeight) {

                birdY -= Gdx.graphics.getHeight() * 15f / 1776f;

            }

            //Game over screen
            float gameOverX = Gdx.graphics.getWidth() / 2 - scoreBoardWidth / 2;

            float gameOverY = Gdx.graphics.getHeight() / 2 - scoreBoardHeight / 4;

			batch.draw(gameOver, gameOverX, gameOverY, scoreBoardWidth, scoreBoardHeight);

            //Activate medal
            float medalX = gameOverX + scoreBoardWidth * 0.14f;

            float medalY = gameOverY + scoreBoardHeight * 0.165f;

            float medalWidth = Gdx.graphics.getWidth() * 66 / 480;

            float medalHeight = Gdx.graphics.getHeight() * 66 / 800;

            if (score >= 5 && score < 20) {

                batch.draw(medal_bronze, medalX, medalY, medalWidth, medalHeight);

            } else if (score >= 20 && score < 40) {

                batch.draw(medal_gold, medalX, medalY, medalWidth, medalHeight);

            } else if (score >= 40){

                batch.draw(medal_platinum, medalX, medalY, medalWidth, medalHeight);

            }

            float scoreDisplayX = Gdx.graphics.getWidth() * 0.67f + scoreBoardWidth * 0.11f;
            float scoreDisplayY = Gdx.graphics.getHeight() / 2 + gameOverY * 0.08f;
            float bestScoreDisplayY = Gdx.graphics.getHeight() / 2 - gameOverY * 0.11f;

            if(secondE != -1) {

                //Your current score
                batch.draw(scoreBox, scoreDisplayX - scoreWidth + space * 0.8f, scoreDisplayY, scoreWidth*0.8f, scoreHeight*0.8f);
                batch.draw(secondScore, scoreDisplayX, scoreDisplayY, scoreWidth * 0.8f, scoreHeight*0.8f);

                //Best score
                batch.draw(scoreBox, scoreDisplayX - scoreWidth + space * 0.8f, bestScoreDisplayY, scoreWidth*0.8f, scoreHeight*0.8f);
                batch.draw(secondScore, scoreDisplayX, bestScoreDisplayY, scoreWidth * 0.8f, scoreHeight*0.8f);

            } else {

                //Your current score
                batch.draw(scoreBox, scoreDisplayX, scoreDisplayY, scoreWidth*0.8f, scoreHeight*0.8f);

                //Best score
                batch.draw(scoreBox, scoreDisplayX, bestScoreDisplayY, scoreWidth*0.8f, scoreHeight*0.8f);

            }

            //Replay image button
            replayTexture = new Texture("replay.png");

            replayTextureRegion = new TextureRegion(replayTexture);

            replayTextureRegionDrawable = new TextureRegionDrawable(replayTextureRegion);

            replayButton = new ImageButton(replayTextureRegionDrawable);

            stage = new Stage(new ScreenViewport());

            replayButton.setX(Gdx.graphics.getWidth() / 2 - scoreBoardWidth / 2 + scoreBoardWidth * 0.275f);

            replayButton.setY(Gdx.graphics.getHeight() / 2 - scoreBoardHeight / 2 - gameOverY * 0.1f);

            replayButton.setWidth(scoreBoardWidth * 0.45f);

            replayButton.setHeight(scoreBoardHeight / 3);

            batch.draw(replayTexture, replayButton.getX(), replayButton.getY(), replayButton.getWidth(), replayButton.getHeight());

            stage.addActor(replayButton);

            Gdx.input.setInputProcessor(stage);

            replayButton.addListener(new EventListener() {

                @Override
                public boolean handle(Event event) {

                    replayIsPressed = true;

                    gameState = 1;

                    setup();

                    return false;

                }

            });

		}

        //Display and update score
        batch.draw(scoreBox, Gdx.graphics.getWidth() / 2 - scoreWidth / 2, Gdx.graphics.getHeight() * 5 / 6, scoreWidth, scoreHeight);

        if(secondE != -1) {

            batch.draw(secondScore, Gdx.graphics.getWidth() / 2 + scoreWidth / 2 + space, Gdx.graphics.getHeight() * 5 / 6, scoreWidth, scoreHeight);

        }

        //Draw and update bird position
        batch.draw(birds[flapState], birdX, birdY, birdWidth, birdHeight);

        for (int i = 0; i < numberOfTubes; i++) {

            if (baseX[i] < - Gdx.graphics.getWidth()) {

                baseX[i] += Gdx.graphics.getWidth();

            } else {

                baseX[i] -= tubeVelocity;

            }

            batch.draw(base, baseX[i], 0, Gdx.graphics.getWidth(), baseHeight);

            batch.draw(base, baseX[i] + Gdx.graphics.getWidth(), 0, Gdx.graphics.getWidth(), baseHeight);

        }

		batch.end();

//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
//        shapeRenderer.end();

	}
	
	@Override
	public void dispose () {

	}

    //Method
    public void setAnimation(final int bird0, final int bird1, final int bird2, float timer) {

        new com.badlogic.gdx.utils.Timer().scheduleTask(new com.badlogic.gdx.utils.Timer.Task() {

            @Override
            public void run() {

                birdUpdate = birds[flapState];

                if (flapState == bird0) {

                    flapState = bird1;

                } else if (flapState == bird1) {

                    flapState = bird2;

                } else {

                    flapState = bird0;

                }

            }

        }, 0f,timer);

    }

    public void setup() {

        die.stop();
        wing.stop();
        swooshing.stop();
        point.stop();

        random_background = randomBg.nextInt(2);

        if (random_background == 0) {

            background = new Texture("background-day.png");

        } else {

            background = new Texture("background-night.png");

        }

        if (!replayIsPressed) {

            birdX = Gdx.graphics.getWidth() / 4 - birdWidth / 2;

        } else {

            birdX = Gdx.graphics.getWidth() / 2 - birdWidth / 2;

        }

        birdY = Gdx.graphics.getHeight() / 3 - birdHeight / 2 + baseHeight / 2;

        score = 0;

        scoringTube = 0;

        scoreBox = new Texture(score + ".png");

        velocity = 0;

        secondE = -1;

        tubeVelocity = (float) Gdx.graphics.getWidth() * 6f / 1080f;

        message = new Texture("message.png");

        stage = null;

        Gdx.input.setInputProcessor(stage);

        for (int i = 0; i < numberOfTubes; i++) {

            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

            tubeX[i] = Gdx.graphics.getWidth()* 3 / 2 - tubeWidth / 2 + i * distanceBetweenTubes;

            baseX[i] = 0;

            bottomTubeRec[i] = new Rectangle();

            topTubeRec[i] = new Rectangle();

        }

    }
}

