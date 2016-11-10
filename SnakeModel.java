package lab1;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;

/**
 * Created 2016-11-10.
 */
public class SnakeModel extends GameModel {

    private ArrayDeque<Position> deque;
    private Position cherryPos;
    private Position snakeHeadPos;
    private static final GameTile bodyTile = new SquareTile(Color.BLACK, Color.GREEN);
    private static final GameTile headTile = new SquareTile(Color.BLACK);
    private static final GameTile blankTile = new GameTile();
    private static final GameTile CHERRY_TILE=new RoundTile(Color.BLACK,Color.RED);
    private int score = 0;


    /**
     * The direction of the collector.
     */
    private Directions direction = Directions.NORTH;

    public SnakeModel() {
        // Blank out the whole gameboard
        for (int i = 0; i < getGameboardSize().width; i++) {
            for (int j = 0; j < getGameboardSize().height; j++) {
                setGameboardState(i, j, blankTile);
            }
        }

        deque = new ArrayDeque<>(10);
        snakeHeadPos = new Position(getGameboardSize().width/2, getGameboardSize().height/2);
        setGameboardState(snakeHeadPos, headTile);

        try {
            moveCherry();
        } catch (GameOverException e){}
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

        if (deque.isEmpty() || (direction.xDelta + newDirection.xDelta != 0 && direction.yDelta + newDirection.yDelta != 0)){
            direction = newDirection;
        }

    }

    private Position getNextSnakePos() {
        return new Position(
                this.snakeHeadPos.getX() + this.direction.getXDelta(),
                this.snakeHeadPos.getY() + this.direction.getYDelta());
    }

    /**
     * Get Next Positon For The Snake.
     * @throws GameOverException
     */

    private void moveSnake() throws GameOverException {
         //  added the the SnakeHead position.
        deque.addFirst(snakeHeadPos);

        setGameboardState(snakeHeadPos, bodyTile);

        snakeHeadPos = getNextSnakePos();

        // IT will Checke  The Conditions Of If We Are Out Of The Bord Or The Snake Crashing With It Self Then Throw Exception To End The Game.
        if ( snakeHeadPos.getX() >= getGameboardSize().width || snakeHeadPos.getY() >= getGameboardSize().height ||
                snakeHeadPos.getX() < 0 || snakeHeadPos.getY() < 0 || getGameboardState(snakeHeadPos).equals(bodyTile)) {
            throw new GameOverException(score);
        }

        setGameboardState(snakeHeadPos, headTile);

    }

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

    @Override
    public void gameUpdate(int lastKey) throws GameOverException {
        updateDirection(lastKey);

        moveSnake();

        if (snakeHeadPos.equals(cherryPos)) {
            moveCherry();
            score++;
        } else {
            setGameboardState(deque.peekLast(), blankTile);
            deque.removeLast();

        }
    }
}