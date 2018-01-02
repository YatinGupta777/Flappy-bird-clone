package com.yatin.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
    Texture gameOver;
   // ShapeRenderer shapeRenderer ;

    Texture Bottomtube;
    Texture Toptube;
    float gap = 400;

    Texture[] birds;
    int flapState = 0;
    float birdY = 0;
    float velocity = 0;

    Circle birdCircle;
    Rectangle topTubesRectangles[];
    Rectangle bottomTubesRectangles[];

    int gameState = 0;
    float gravity = (float) 0.8;

    float maxTubeOffset;
    Random randomGenerator;
    float tubeVelocity = 4;
    int numberOfTubes = 4;

    float tubeX[] = new float[numberOfTubes] ;
    float tubeOffset[] = new float[numberOfTubes];

    float distanceBetweenTubes;

    int score = 0;
    int scoringTube =0 ;

    BitmapFont font;
    BitmapFont font2;



	@Override
	public void create () {
        batch = new SpriteBatch();

        background = new Texture("bg1.jpg");
        gameOver = new Texture("over.jpg");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird.png");//not needed since ball not doing any animation
        birdCircle = new Circle();
        topTubesRectangles = new Rectangle[numberOfTubes];
        bottomTubesRectangles = new Rectangle[numberOfTubes];
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(8);
        //   shapeRenderer = new ShapeRenderer();
        font2 = new BitmapFont();
        font2.setColor(Color.BLACK);
        font2.getData().setScale(4);


        Bottomtube = new Texture("tube2.png");
        Toptube = new Texture("tube1.png");
        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        randomGenerator = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() / 2 + 15;

        startGame();

    }

    public void startGame(){
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

        for (int i = 0; i < numberOfTubes; i++) {
            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
            tubeX[i] = Gdx.graphics.getWidth() / 2 - Toptube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
            topTubesRectangles[i] = new Rectangle();
            bottomTubesRectangles[i] = new Rectangle();
        }
    }

	@Override
	public void render() {


        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if(gameState == 1) {

            if(tubeX[scoringTube] < Gdx.graphics.getWidth()/2){
                score++;

                if(scoringTube < numberOfTubes-1)
                {
                    scoringTube++;
                }else{
                    scoringTube =0;
                }
            }
            if (Gdx.input.justTouched()) {
                velocity = -15;
            }
            for(int i = 0 ; i < numberOfTubes;i++) {

                if(tubeX[i] < -Toptube.getWidth()){
                    tubeX[i] +=numberOfTubes *distanceBetweenTubes;
                    tubeOffset[i] = (randomGenerator.nextFloat()- 0.5f) * (Gdx.graphics.getHeight()- gap-200);

                }else {
                    tubeX[i] -= tubeVelocity;
                }

                batch.draw(Toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(Bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - Bottomtube.getHeight() + tubeOffset[i]);

                topTubesRectangles[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],Toptube.getWidth(),Toptube.getHeight());
                bottomTubesRectangles[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - Bottomtube.getHeight() + tubeOffset[i],Bottomtube.getWidth(),Bottomtube.getHeight());

            }
            if(birdY > 0 ) {
                velocity += gravity;
                birdY -= velocity;
            }else{
                gameState = 2;
            }
        }
        else if(gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        }
        else if(gameState == 2){


            batch.draw(gameOver,Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2,Gdx.graphics.getHeight()/2 - gameOver.getHeight()/2);
            font2.draw(batch,"TAP ANYWHERE TO START",0 ,Gdx.graphics.getHeight()/2 - gameOver.getHeight());

            if (Gdx.input.justTouched()) {
                startGame();
                score = 0;
                velocity = 0;
                scoringTube = 0;
               gameState = 1;
            }
        }




        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
        font.draw(batch,String.valueOf(score),100,200);

        batch.end();

        birdCircle.set(Gdx.graphics.getWidth()/2,birdY+birds[flapState].getHeight()/2,birds[flapState].getWidth()/2);

/*
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

        for(int  i =0 ; i < numberOfTubes;i++){
            shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],Toptube.getWidth(),Toptube.getHeight() );
            shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - Bottomtube.getHeight() + tubeOffset[i],Bottomtube.getWidth(),Bottomtube.getHeight());
        if(topTubesRectangles[i] != null && bottomTubesRectangles[i] !=null) {
            if (Intersector.overlaps(birdCircle, topTubesRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubesRectangles[i])) {

                Gdx.app.log("AA", "AA");
            }
        }
        }
       shapeRenderer.end();
*/
        for(int  i =0 ; i < numberOfTubes;i++){
            if(topTubesRectangles[i] != null && bottomTubesRectangles[i] !=null) {
                if (Intersector.overlaps(birdCircle, topTubesRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubesRectangles[i])) {
                   gameState = 2;
                }
            }
        }
        if (flapState == 0) {
            flapState = 1;
        } else{
            flapState = 0;
        }

	}

	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
