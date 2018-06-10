package Entities;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

public class Ball extends GameObject{

    Image ballImage;
    Image portal;
    Image sandals;
    boolean port;
    boolean sand;
    Shape s_sandals;
    Shape s_portals;
    float velY = 0.4f;
    float velX;
    float ballX;
    float ballY;

    boolean collisionFromRight = false;
    boolean collisionFromLeft = false;




    public Ball(float x, float y, String name, GameContainer gameContainer) {
        super(name, new Circle(x, y, 32));
        init(gameContainer);

    }

    public void update (int delta, GameContainer gameContainer){
        //if (ballImage != null)
        collisionBox = new Circle(ballX + 32, ballY + 32, 32);
        if(collisionBox.intersects(s_sandals)){
            sand=true;
        }
        if(collisionBox.intersects(s_portals)){
            port=true;
        }
        move(delta, gameContainer);

    }

    public void callBack(int flag){
        switch (flag){
            case 0:
                collisionFromRight = false;
                collisionFromLeft = false;
                return;
            case 1:
                collisionFromRight = true;
                velX = -8f;
                velY = -15f;
                break;
            case -1:
                collisionFromLeft = true;
                velX = 8f;
                velY = -15f;
                break;
            default:

                return;
        }
    }

    @Override
    public void init(GameContainer gameContainer) {
        try {
            ballImage = new Image("res/textures/ball.png").getScaledCopy(64,64);
            portal = new Image("res/textures/bonus/portal.png").getScaledCopy(70,70);
            sandals = new Image("res/textures/bonus/sandals.png").getScaledCopy(70,70);
            s_portals = new Circle(900+35,135+500,35);
            s_sandals = new Circle(800+33,179+400,20);
        } catch (SlickException e) {
            e.printStackTrace();
        }
        ballX = gameContainer.getWidth()/3;
        ballY = gameContainer.getHeight() * 0.4f;
        StaticFields.lowPosition = gameContainer.getHeight() * 0.8f;


    }

    @Override
    public float getX() {
        return ballX;
    }

    @Override
    public float getY() {
        return ballY;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void render(Graphics g){

        if (ballImage != null)
            g.drawImage(ballImage, ballX, ballY);
            if(sand!=true) {g.drawImage(sandals, 800, 150+400); g.draw(s_sandals);}

            if(port!=true) {g.drawImage(portal, 900, 100+500); g.draw(s_portals);}
        g.setColor(Color.blue);
   //        g.fill(collisionBox);

    }


    private void move(int delta, GameContainer gameContainer){

        if(ballX <= 0 && velX < 0) velX *= -1;

        if(ballX >= gameContainer.getWidth() - 32 && velX > 0) velX *= -1;

        if(ballY < StaticFields.lowPosition)
        ballY += velY;

        if(ballY >= StaticFields.lowPosition && velY > 0) {
            velY *= -0.65f;
            ballY = StaticFields.lowPosition - 1;
        }


        velY += 0.04f * delta;


        ballX += velX;

        velX -= Math.signum(velX) * 0.001f * delta;

        ballImage.rotate(velX);




    }




}
