package sweeper;

public class Game {

    private Bomb bomb;
    private Flag flag;
    private int countOfLeftPress;
    public GameState getState() {
        return state;
    }
    private GameState state;

    public Game(Complexity complexity) {
        switch (complexity) {
            case EASY:
                Ranges.setSize(new Coord(9, 9));
                bomb = new Bomb(10);
                flag = new Flag();
                return;
            case NORMAL:
                Ranges.setSize(new Coord(16, 16));
                bomb = new Bomb(40);
                flag = new Flag();
                return;
            case HARD:
                Ranges.setSize(new Coord(32, 16));
                bomb = new Bomb(110);
                flag = new Flag();
        }
    }

    public void start() {
        bomb.start();
        flag.start();
        state = GameState.PLAYED;
        countOfLeftPress = 0;
    }

    public Box getBox(Coord coord) {
        if(flag.get(coord) == Box.OPENED) {
            return bomb.get(coord);
        } else {
            return flag.get(coord);
        }
    }

    public void pressLeftButton(Coord coord) {
        countOfLeftPress++;
        if(countOfLeftPress == 1 && bomb.get(coord) == Box.BOMB) {
            start();
            return;
        }
        if(gameOver()) return;
        openBox(coord);
        checkWinner();
    }

    private void checkWinner() {
        if(state == GameState.PLAYED) {
            if(flag.getCountOfClosedBoxes() == bomb.getTotalBombs()) {
                state = GameState.WINNER;
            }
        }
    }

    private void openBox(Coord coord) {
        switch (flag.get(coord)) {
            case OPENED : setOpenedToClosedBoxesAroundNumber(coord); return;
            case FLAGED : return;
            case CLOSED :
                switch (bomb.get(coord)) {
                    case ZERO : openBoxesAround(coord); return;
                    case BOMB : openBombs(coord); return;
                    default : flag.setOpenedToBox(coord); return;
                 }
        }
    }

    private void setOpenedToClosedBoxesAroundNumber(Coord coord) {
        if(bomb.get(coord) != Box.BOMB) {
            if (flag.getCountOfFlagedBoxesAround(coord) == bomb.get(coord).getNumber()) {
                for(Coord around : Ranges.getCoordsAround(coord)) {
                    if(flag.get(around) == Box.CLOSED) {
                        openBox(around);
                    }
                }
            }
        }
    }

    private void openBombs(Coord bombed) {
        state = GameState.BOMBED;
        flag.setBombedToBox(bombed);
        for(Coord coord : Ranges.getAllCoords()) {
            if(bomb.get(coord) == Box.BOMB) {
                flag.setOpenedToClosedBombBox(coord);
            } else {
                flag.setNoBombToFlagedSafeBox(coord);
            }
        }
    }

    private void openBoxesAround(Coord coord) {
        flag.setOpenedToBox(coord);
        for (Coord around : Ranges.getCoordsAround(coord)) {
            openBox(around);
        }
    }

    public void pressRightButton(Coord coord) {
        if(gameOver()) return;
        flag.toggleFlagedToBox(coord);
    }

    private boolean gameOver() {
        if (state == GameState.PLAYED) {
            return false;
        }
        start();
        return true;
    }

//    public void setComplexity(String item) {
//        switch (item) {
//            case "Easy":
//                complexity = Complexity.EASY;
//                start();
//                return;
//            case "Normal":
//                complexity = Complexity.NORMAL;
//                start();
//                return;
//            case "Hard":
//                complexity = Complexity.HARD;
//                start();
//                return;
//        }
//    }
}
