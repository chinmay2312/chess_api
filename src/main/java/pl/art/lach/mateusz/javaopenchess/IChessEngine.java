package pl.art.lach.mateusz.javaopenchess;

import pl.art.lach.mateusz.javaopenchess.core.Game;
import pl.art.lach.mateusz.javaopenchess.core.Square;

public interface IChessEngine {
    Game newGame(boolean firstMove);
    void move(Square begin, Square end);
    void quit();
}
