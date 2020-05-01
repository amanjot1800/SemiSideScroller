package sidescroller.animator;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import sidescroller.entity.FpsCounter;
import sidescroller.entity.Grid;
import sidescroller.entity.property.Drawable;
import sidescroller.scene.MapSceneInterface;
import utility.Tuple;


/**
 *
 * @author Amanjot Singh
 * @version April 20, 2020
 */
public abstract class AbstractAnimator extends AnimationTimer implements AnimatorInterface {

    protected MapSceneInterface map;
    protected Tuple mouse;
    private Canvas canvas;
    private FpsCounter fps;
    private Grid grid;

    public AbstractAnimator(){

        mouse = new Tuple();
        fps = new FpsCounter(10, 25);

        Drawable<?> fpsSprite = fps.getDrawable();

        fpsSprite.setFill(Color.BLACK).setStroke(Color.WHITE).setWidth(1);

    }

    @Override
    public void clearAndFill(GraphicsContext gc, Color background){

        gc.setFill(background);
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

    }

    @Override
    public void handle(long now){

        GraphicsContext gc = canvas.getGraphicsContext2D();

        if (map.getDrawFPS()) {
            fps.calculateFPS(now);
        }

        handle(gc, now);

        if (map.getDrawGrid()){

            if (grid==null){

                grid = new Grid(map.getGridCount(), canvas.getWidth(), canvas.getHeight());
                Drawable<?> gridSprite = grid.getDrawable();

                gridSprite.setStroke(Color.BLACK).setWidth(1).setScale(map.getScale()).setTileSize(map.getGridSize());


            }

            Drawable<?> draw = grid.getDrawable();
            draw.draw(gc);
        }

        if (map.getDrawFPS()){

            Drawable<?> draw = fps.getDrawable();
            draw.draw(gc);
        }

    }


    @Override
    public abstract void handle(GraphicsContext gc, long now);


    @Override
    public void setMapScene(MapSceneInterface map){
        this.map = map;
    }

    @Override
    public void setCanvas(Canvas canvas){
        this.canvas = canvas;
    }



}
