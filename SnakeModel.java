package lab1;

/**
 * Created by Kraft on 2016-11-10.
 */
public class SnakeModel extends GameModel {



    private void updateDirection(int key){

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

