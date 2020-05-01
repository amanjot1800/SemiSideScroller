package sidescroller.scene;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.Canvas;
import sidescroller.animator.AnimatorInterface;
import sidescroller.entity.property.Entity;
import sidescroller.entity.property.HitBox;
import sidescroller.entity.sprite.tile.*;
import utility.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;



/**
 *
 * @author Amanjot Singh
 * @version April 20, 2020
 */
public class MapScene implements MapSceneInterface {

    private Tuple count;
    private Tuple size;
    private double scale;
    private AnimatorInterface animator;
    private List<Entity> players;
    private List<Entity> staticShapes;
    private Entity background;
    private BooleanProperty drawBounds;
    private BooleanProperty drawFPS;
    private BooleanProperty drawGrid;

    public MapScene(){
        players = new ArrayList<>();
        staticShapes = new ArrayList<>();
        drawBounds = new SimpleBooleanProperty();
        drawFPS = new SimpleBooleanProperty();
        drawGrid = new SimpleBooleanProperty();
    }


    @Override
    public BooleanProperty drawFPSProperty() {
        return drawFPS;
    }

    @Override
    public boolean getDrawFPS() {
        return drawFPS.get();
    }

    @Override
    public BooleanProperty drawBoundsProperty() {
        return drawBounds;
    }

    @Override
    public boolean getDrawBounds() {
        return drawBounds.get();
    }

    @Override
    public BooleanProperty drawGridProperty() {
        return drawGrid;
    }

    @Override
    public boolean getDrawGrid() {
        return drawGrid.get();
    }

    @Override
    public MapSceneInterface setRowAndCol(Tuple count, Tuple size, double scale) {
        this.count = count;
        this.size = size;
        this.scale = scale;
        return this;
    }

    @Override
    public Tuple getGridCount() {
        return count;
    }

    @Override
    public Tuple getGridSize() {
        return size;
    }

    @Override
    public double getScale() {
        return scale;
    }

    @Override
    public void start() {

        if (animator!=null)
            animator.start();
    }

    @Override
    public void stop() {

        if (animator!=null)
            animator.stop();
    }

    @Override
    public List<Entity> staticShapes() {
        return staticShapes;
    }

    @Override
    public List<Entity> players() {
        return players;
    }

    @Override
    public MapSceneInterface createScene(Canvas canvas) {

        MapBuilder mb = MapBuilder.createBuilder();
        mb.setCanvas(canvas);
        mb.setGrid(count, size);
        mb.setGridScale(scale);

        BiFunction<Integer, Integer, Tile> bg = (row, col) -> BackgroundTile.NIGHT;
        mb.buildBackground(bg);
        background = mb.getBackground();

        mb.buildLandMass(13,0, 2, 5);
        mb.buildPlatform(11,6,4, PlatformTile.STONE);
        mb.buildLandMass(13,11, 2, 5);
        mb.buildLandMass(11,16, 4, 5);
        mb.buildLandMass(9,21, 6, 5);
        mb.buildLandMass(7,26, 8, 9);

        mb.buildPlatform(11,6,4, PlatformTile.STONE);

        mb.buildTree(0,30, FloraTile.TREE);

        staticShapes.addAll(mb.getEntities(staticShapes));
        return this;
    }

    @Override
    public boolean inMap(HitBox hitbox) {

        return background.getHitBox().containsBounds(hitbox);

    }

    @Override
    public MapSceneInterface setAnimator(AnimatorInterface newAnimator) {

        if (animator != null)
            animator.stop();

        if (newAnimator==null)
            throw new NullPointerException();

        this.animator = newAnimator;
        return this;
    }

    @Override
    public Entity getBackground() {
        return background;
    }


}
