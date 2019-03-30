package pl.art.lach.mateusz.javaopenchess;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.art.lach.mateusz.javaopenchess.core.Chessboard;
import pl.art.lach.mateusz.javaopenchess.core.Colors;
import pl.art.lach.mateusz.javaopenchess.core.Game;
import pl.art.lach.mateusz.javaopenchess.core.Square;
import pl.art.lach.mateusz.javaopenchess.core.ai.AI;
import pl.art.lach.mateusz.javaopenchess.core.ai.AIFactory;
import pl.art.lach.mateusz.javaopenchess.core.moves.Move;
import pl.art.lach.mateusz.javaopenchess.core.pieces.KingState;
import pl.art.lach.mateusz.javaopenchess.core.pieces.implementation.King;
import pl.art.lach.mateusz.javaopenchess.core.players.Player;
import pl.art.lach.mateusz.javaopenchess.core.players.PlayerFactory;
import pl.art.lach.mateusz.javaopenchess.core.players.PlayerType;
import pl.art.lach.mateusz.javaopenchess.utils.GameModes;
import pl.art.lach.mateusz.javaopenchess.utils.GameTypes;
import pl.art.lach.mateusz.javaopenchess.utils.Settings;

@RestController
public class ChessController// implements IChessEngine
 {
    Game game;
    MovePOJO mp;
    King king;

    /*
     *@param firstMove if true, it means controller is black and will play second
     */
    @PostMapping(path = "/new_game/{isWhite}", produces = "application/json")
    public MovePOJO newGame(@PathVariable("isWhite") boolean firstMove) {

       String playerName1 = "Chinmay", playerName2 = "CPU";
       String whiteName, blackName;
       PlayerType whiteType, blackType;

       if(firstMove)
       {
          whiteName = playerName1;
          blackName = playerName2;
          whiteType = PlayerType.LOCAL_USER;
                /*blackType = (this.oponentComp.isSelected())
                        ? PlayerType.COMPUTER :  PlayerType.LOCAL_USER;*/
          blackType = PlayerType.COMPUTER;
       }
       else
       {
          blackName = playerName1;
          whiteName = playerName2;
          blackType = PlayerType.LOCAL_USER;
                /*whiteType = (this.oponentComp.isSelected())
                        ? PlayerType.COMPUTER :  PlayerType.LOCAL_USER;*/
          whiteType = PlayerType.COMPUTER;
       }
       Player playerWhite = PlayerFactory.getInstance(whiteName, Colors.WHITE, whiteType);
       Player playerBlack = PlayerFactory.getInstance(blackName, Colors.BLACK, blackType);

       game = new Game();
       Settings sett = game.getSettings();
       sett.setPlayerWhite(playerWhite);
       sett.setPlayerBlack(playerBlack);
       sett.setGameMode(GameModes.NEW_GAME);
       sett.setGameType(GameTypes.LOCAL);
       sett.setUpsideDown(false);
       game.setActivePlayer(playerWhite);
       game.newGame();

       AI ai = AIFactory.getAI(1);
       game.setAi(ai);
       if(!firstMove)
          game.doComputerMove();


       //Game g = ChessInvoker.beginGame("Chinmay", 'B');
       //if(firstMove)
       //  return new ResponseEntity<>("New game created", HttpStatus.CREATED);
       mp = new MovePOJO();
       mp.setMessage("New game created");

       if(!firstMove) {
          Move lastMove = game.getMoves().getLastMoveFromHistory();
          System.out.println(lastMove.getMovedPiece().getName() + " moved from " + lastMove.getFrom().getAlgebraicNotation() + " to " + lastMove.getTo().getAlgebraicNotation());

          mp.setStartSq(lastMove.getFrom().getAlgebraicNotation());
          mp.setEndSq(lastMove.getTo().getAlgebraicNotation());
          mp.setMovedPiece(lastMove.getMovedPiece().getName());
          mp.setTakenPiece("");
       }
       return mp;
    }

    //@RequestMapping(value = "/error", method= RequestMethod.GET)
    @GetMapping("/error")
    //@Override
    public ResponseEntity<Object> error() {
       return new ResponseEntity<>("you got an error!!", HttpStatus.ACCEPTED);
    }

    //@Override
    @PostMapping(path = "/move", produces = "application/json")
    public MovePOJO move(@RequestBody MovePOJO repojo) {

       String begin = repojo.getStartSq();
       String end = repojo.getEndSq();

       mp = new MovePOJO();

       //TODO: check if valid move
       Square beginSq = getSquareFromAlgebraic(begin);
       Square endSq = getSquareFromAlgebraic(end);

       game.getChessboard().move(beginSq, endSq);
       game.getChessboard().unselect();
       game.nextMove();
       if(game.getSettings().getPlayerWhite() == game.getActivePlayer())
          king = game.getChessboard().getKingWhite();
       else
          king = game.getChessboard().getKingBlack();

       if(king.getKingState()!= KingState.FINE) {
          mp.setMessage("I am " + king.getKingState());
          game.endGame(game.getActivePlayer().getName()+ " loses");
       }
       else {
          game.doComputerMove();
          if (game.getSettings().getPlayerWhite() == game.getActivePlayer())
             king = game.getChessboard().getKingWhite();
          else
             king = game.getChessboard().getKingBlack();

          if(king.getKingState()!= KingState.FINE) {
             mp.setMessage("You are " + king.getKingState());
          }
          else {
             Move lastMove = game.getMoves().getLastMoveFromHistory();
             System.out.println(lastMove.getMovedPiece().getName() + " moved from " + lastMove.getFrom().getAlgebraicNotation() + " to " + lastMove.getTo().getAlgebraicNotation());

             mp.setStartSq(lastMove.getFrom().getAlgebraicNotation());
             mp.setEndSq(lastMove.getTo().getAlgebraicNotation());
             mp.setMovedPiece(lastMove.getMovedPiece().getName());
             if(lastMove.getTakenPiece()!=null)
               mp.setTakenPiece(lastMove.getTakenPiece().getName());
             else mp.setTakenPiece("");
          }
       }

       return mp;
    }

    //@Override
    @DeleteMapping(value = "/quit", produces = "application/json")
    public MovePOJO quit() {
       game.endGame("You have ended the game");
       mp = new MovePOJO();
       mp.setMessage("You have ended the game");
       return mp;
    }

    private Square getSquareFromAlgebraic(String notation)   {
       int pozX = (int)notation.charAt(0) - Square.ASCII_OFFSET;
       int pozY = Chessboard.LAST_SQUARE - Character.getNumericValue(notation.charAt(1))+1;
       Square sq = game.getChessboard().getSquare(pozX, pozY);
       return sq;
    }
}
