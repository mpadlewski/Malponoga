import Camera.Camera;
import Entities.*;
import Entities.Ball;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;

public class Board extends BasicGameState{



    Image background;
    Image net;

    List<GameObject> entities = new ArrayList<>();

    CollisionHandler collisionHandler;

    Camera camera;

    int result, recentResult;
    int scoreHome, scoreAway;

    boolean inGoal = false;


    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        background = new Image("/res/textures/board/stadium.png");
        net = new Image("/res/textures/board/net.png");

        entities.add(new Ball(gameContainer.getWidth()/3,gameContainer.getHeight() * 0.4f,"name", gameContainer));
        entities.add(new Player(gameContainer.getWidth()/4,64,"player1",gameContainer, true));
        entities.add(new Player(gameContainer.getWidth()/2,64,"player2",gameContainer,  false));

        collisionHandler
                = new CollisionHandler(new ArrayList<GameObject>() {{ add(entities.get(1)); add(entities.get(2));}},
                new Rectangle(gameContainer.getWidth()/20,686,150,300),new Rectangle(gameContainer.getWidth()/ 1.18f,686,150,300),
                new Line(0,686,gameContainer.getWidth()/20 + 150,686),
                new Line(gameContainer.getWidth()/ 1.18f,686,gameContainer.getWidth() / 1.18f + 300,686),
                (Ball) entities.get(0));


        camera = new Camera();
//      camera.focusOnPoint(Math.abs((entities.get(0).getX() + entities.get(1).getX())) / 2, StaticFields.lowPosition);
         camera.focusOnEntity(entities.get(0));

         scoreAway = 0;
         scoreHome = 0;
         recentResult = 0;


    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

        g.translate( - StaticFields.cameraZoom * camera.camX ,- StaticFields.cameraZoom * camera.camY);

        g.scale(StaticFields.cameraZoom,StaticFields.cameraZoom);


        g.drawImage(background.getScaledCopy(gc.getWidth(),gc.getHeight()),0 ,0);


        entities.forEach(e -> e.render(g));



        g.drawImage(net.getScaledCopy(gc.getWidth(),gc.getHeight()),0 ,0);

    }
    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {



        entities.forEach(e -> e.update(delta,gameContainer));    // UPDATES ALL ENTITIES.

        camera.update(gameContainer);

        collisionHandler.checkForCollisions();

        collisionHandler.checkForBar();

        result =  collisionHandler.checkForGoal(); // RETURNS 0 IF NO GOAL IS SCORED - 1 FOR LEFT - (-1) FOR RIGHT

        if (result == 1 && recentResult == 0) {
            scoreAway++;
        }

        else if (result == -1 && recentResult == 0){
            scoreHome++;
        }
        recentResult = result;

        //System.out.println(scoreHome + " " + scoreAway);



       StaticFields.cameraZoom = 2 - Math.abs(entities.get(2).getX() - entities.get(1).getX()) / 1000;

       if(StaticFields.cameraZoom < 1) StaticFields.cameraZoom = 1;


        camera.focusOnPoint((entities.get(1).getX() + entities.get(2).getX())/2, (entities.get(1).getY() + entities.get(2).getY())/2);


    }

    @Override
    public int getID() {
        return 1;
    }

}
