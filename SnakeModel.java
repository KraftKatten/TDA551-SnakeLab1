package lab1;

import java.awt.event.KeyEvent;
import java.util.ArrayDeque;

/**
 * Created 2016-11-10.
 *
 *
 */
public class SnakeModel extends GameModel {

    private ArrayDeque<Position> deque;

    /** The direction of the collector. */
    private Directions direction = Directions.NORTH;

    public SnakeModel(){
        deque = new ArrayDeque<>(10);
        deque.addFirst(new Position(getGameboardSize().width/2, getGameboardSize().height/2));
    }

    public enum Directions {
        EAST(1, 0),
        WEST(-1, 0),
        NORTH(0, -1),
        SOUTH(0, 1),
        NONE(0, 0);

        private final int xDelta;
        private final int yDelta;

        Directions(final int xDelta, final int yDelta) {
            this.xDelta = xDelta;
            this.yDelta = yDelta;
        }

        public int getXDelta() {
            return this.xDelta;
        }

        public int getYDelta() {
            return this.yDelta;
        }
    }

    /**
     * Update the direction of the collector
     * according to the user's keypress.
     */
    private void updateDirection(final int key) {
        switch (key) {
            case KeyEvent.VK_LEFT:
                this.direction = Directions.WEST;
                break;
            case KeyEvent.VK_UP:
                this.direction = Directions.NORTH;
                break;
            case KeyEvent.VK_RIGHT:
                this.direction = Directions.EAST;
                break;
            case KeyEvent.VK_DOWN:
                this.direction = Directions.SOUTH;
                break;
            default:
                // Don't change direction if another key is pressed
                break;
        }
    }

    private void moveSnake(){

    }

    private void moveCherry(){

    }

    @Override
    public void gameUpdate(int lastKey) throws GameOverException {
        updateDirection(lastKey);

        moveSnake();

        //If ätit cherry, flytta cherry, lägg till score,
        //Annars ta bort sista i deque

        // if utanför throw exception


    }
}

