package pl.art.lach.mateusz.javaopenchess;

public interface IChessEngine {
    MovePOJO newGame(boolean firstMove);
    MovePOJO move(MovePOJO repojo);
    MovePOJO quit();
}
