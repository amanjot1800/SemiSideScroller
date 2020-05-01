package sidescroller.scene;

import javafx.scene.canvas.Canvas;
import sidescroller.entity.GenericEntity;
import sidescroller.entity.property.Entity;
import sidescroller.entity.property.HitBox;
import sidescroller.entity.sprite.*;
import sidescroller.entity.sprite.tile.Tile;
import utility.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;


/**
 *
 * @author Amanjot Singh
 * @version April 20, 2020
 */
public class MapBuilder implements MapBuilderInterface{

    private Tuple rowColCount;
    private Tuple dimension;
    private double scale;
    private Canvas canvas;
    private Entity background;
    private List<Entity> landMass;
    private List<Entity> other;

    protected MapBuilder(){
        landMass = new ArrayList<>();
        other = new ArrayList<>();
    }

    public static MapBuilder createBuilder(){
        return new MapBuilder();
    }

    @Override
    public MapBuilderInterface setCanvas(Canvas canvas) {
        this.canvas = canvas;
        return this;
    }

    @Override
    public MapBuilderInterface setGrid(Tuple rowColCount, Tuple dimension) {
        this.rowColCount = rowColCount;
        this.dimension = dimension;
        return this;
    }

    @Override
    public MapBuilderInterface setGridScale(double scale) {
        this.scale = scale;
        return this;
    }

    @Override
    public MapBuilderInterface buildBackground(BiFunction<Integer, Integer, Tile> callback) {

        BackgroundSprite bks = SpriteFactory.get("Background");
        bks.init(scale, dimension, Tuple.pair(0,0));
        bks.createSnapshot(canvas, rowColCount, callback);
        HitBox hb = HitBox.build(0,0,scale * dimension.x() * rowColCount.y(),  scale * dimension.y() * rowColCount.x());
        background = new GenericEntity(bks, hb);
        return this;

    }

    @Override
    public MapBuilderInterface buildLandMass(int rowPos, int colPos, int rowConut, int colCount) {

        LandSprite landSprite = SpriteFactory.get("Land");
        landSprite.init(scale, dimension, Tuple.pair(colPos, rowPos));
        landSprite.createSnapshot(canvas, rowConut, colCount);
        HitBox hb = HitBox.build(colPos * dimension.x() * scale, rowPos * dimension.y() * scale, scale * dimension.x() * colCount, scale * dimension.y() * rowConut);
        landMass.add(new GenericEntity(landSprite, hb));
        return this;
    }

    @Override
    public MapBuilderInterface buildTree(int rowPos, int colPos, Tile tile) {

        TreeSprite treeSprite = SpriteFactory.get("Tree");
        treeSprite.init(scale, dimension, Tuple.pair(colPos, rowPos));
        treeSprite.createSnapshot(canvas, tile);
        other.add(new GenericEntity(treeSprite, null));
        return this;
    }

    @Override
    public MapBuilderInterface buildPlatform(int rowPos, int colPos, int length, Tile tile) {

        PlatformSprite platformSprite = SpriteFactory.get("Platform");
        platformSprite.init(scale, dimension, Tuple.pair(colPos, rowPos));
        platformSprite.createSnapshot(canvas, tile, length);
        HitBox hb = HitBox.build((colPos + .5) * dimension.x() * scale, rowPos * dimension.y() * scale, scale * dimension.x() * (length - 1), scale * dimension.y() / 2);
        other.add(new GenericEntity(platformSprite, hb));
        return this;

    }

    @Override
    public Entity getBackground() {
        return background;
    }

    @Override
    public List<Entity> getEntities(List<Entity> list) throws NullPointerException {

        list.addAll(landMass);
        list.addAll(other);
        return list;
    }

}
