package lab1;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;

/**
 *  Initially places a snake head and a cherry. When a cherry is picked up, a new one appears and a snake forms after the snake head.
 *  The game ends when the snake tries to enter itself, tries to enter the wall, or has filled the entire board.
 */
public class SnakeModel extends GameModel {
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

    /** The dynamic array containing the body of the snake */
    private ArrayDeque<Position> deque;

    /** The position of the cherry*/
    private Position cherryPos;

    /** The position of the snake head*/
    private Position snakeHeadPos;

    /** The number of cherries picked */
    private int score = 0;

    /** The direction of the snake. */
    private Directions direction = Directions.NORTH;


    /*
	 * The following GameTile objects are used only
	 * to describe how to paint the specified item.
	 *
	 * This means that they should only be used in
	 * conjunction with the get/setGameboardState()
	 * methods.
	 */

    /** Graphical representation of a snake body. */
    private static final GameTile bodyTile = new SquareTile(Color.BLACK, Color.GREEN);

    /** Graphical representation of a snake head. */
    private static final GameTile headTile = new SquareTile(Color.BLACK);

    /** Graphical representation of a blank tile. */
    private static final GameTile blankTile = new GameTile();

    /** Graphical representation of a cherry. */
    private static final GameTile CHERRY_TILE=new RoundTile(Color.BLACK,Color.RED);

    /**
     * Create a new model for the snake game.
     */
    public SnakeModel() {
        // Blank out the whole game board
        for (int i = 0; i < getGameboardSize().width; i++) {
            for (int j = 0; j < getGameboardSize().height; j++) {
                setGameboardState(i, j, blankTile);
            }
        }

        // Initialise deque containing the snake body
        deque = new ArrayDeque<>(10);

        // Place the snake head
        snakeHeadPos = new Position(getGameboardSize().width/2, getGameboardSize().height/2);
        setGameboardState(snakeHeadPos, headTile);


        // Place a cherry
        try {
            moveCherry();
        } catch (GameOverException e){}
    }

    /**
     * Update the direction of the collector
     * according to the user's keypress.
     */
    private void updateDirection(final int key) {

        Directions newDirection = direction;

        switch (key) {
            case KeyEvent.VK_LEFT:
                newDirection = Directions.WEST;
                break;
            case KeyEvent.VK_UP:
                newDirection = Directions.NORTH;
                break;
            case KeyEvent.VK_RIGHT:
                newDirection = Directions.EAST;
                break;
            case KeyEvent.VK_DOWN:
                newDirection = Directions.SOUTH;
                break;
            default:
                // Don't change direction if another key is pressed
                break;
        }

        // Allow all directions if the snake has no body, but otherwise don't allow going backwards
        if (deque.isEmpty() || (direction.xDelta + newDirection.xDelta != 0 && direction.yDelta + newDirection.yDelta != 0)){
            direction = newDirection;
        }

    }

    /**
     * Get next position of the snake head.
     */
    private Position getNextSnakePos() {
        return new Position(
                this.snakeHeadPos.getX() + this.direction.getXDelta(),
                this.snakeHeadPos.getY() + this.direction.getYDelta());
    }

    private void moveSnake() throws GameOverException {

        deque.addFirst(snakeHeadPos);
        setGameboardState(snakeHeadPos, bodyTile);

        snakeHeadPos = getNextSnakePos();


        if ( snakeHeadPos.getX() >= getGameboardSize().width || snakeHeadPos.getY() >= getGameboardSize().height ||
                snakeHeadPos.getX() < 0 || snakeHeadPos.getY() < 0 || getGameboardState(snakeHeadPos).equals(bodyTile)) {
            throw new GameOverException(score);
        }

        setGameboardState(snakeHeadPos, headTile);

    }

    /**
     * Return whether the specified position is empty.
     *
     * @param pos
     *            The position to test.
     * @return true if position is empty.
     */
    private boolean isPositionEmpty(final Position pos) {
        return (getGameboardState(pos) == blankTile);
    }


    private void moveCherry() throws GameOverException{
        Position tmp;
        boolean hasEmpty=false;
        Dimension size = getGameboardSize();

        // Loop until a blank position is found and ...
        for (int i =0;i<size.width && !hasEmpty;i++){
            for (int j=0;j<size.height  && !hasEmpty ;j++){
                tmp =new Position(i,j);
                if (isPositionEmpty(tmp)){
                    hasEmpty = true  ;
                }
            }
        }

        if (!hasEmpty){
            throw new GameOverException(score + 20);
        }

        do {
            tmp = new Position((int) (Math.random() * size.width),
                    (int) (Math.random() * size.height));
        } while (!isPositionEmpty(tmp));

        // ... add a new coin to the empty tile.
        setGameboardState(tmp, CHERRY_TILE);
        cherryPos = tmp;

    }

    /**
     * This method is called repeatedly so that the
     * game can update its state.
     *
     * @param lastKey
     *            The most recent keystroke.
     */
    @Override
    public void gameUpdate(int lastKey) throws GameOverException {
        updateDirection(lastKey);

        // Move the snake forward
        moveSnake();

        // Check if the cherry is at the snake
        if (snakeHeadPos.equals(cherryPos)) {
            moveCherry(); // Make a new cherry
            score++;
        }
        else { // Remove the last piece of the body if no cherry was found
            setGameboardState(deque.peekLast(), blankTile);
            deque.removeLast();
        }
    }
}