package pl.art.lach.mateusz.javaopenchess.core;

import pl.art.lach.mateusz.javaopenchess.JChessApp;
import pl.art.lach.mateusz.javaopenchess.JChessView;
import pl.art.lach.mateusz.javaopenchess.core.ai.AI;
import pl.art.lach.mateusz.javaopenchess.core.ai.AIFactory;
import pl.art.lach.mateusz.javaopenchess.core.pieces.KingState;
import pl.art.lach.mateusz.javaopenchess.core.pieces.implementation.King;
import pl.art.lach.mateusz.javaopenchess.core.pieces.implementation.Pawn;
import pl.art.lach.mateusz.javaopenchess.core.players.Player;
import pl.art.lach.mateusz.javaopenchess.core.players.PlayerFactory;
import pl.art.lach.mateusz.javaopenchess.core.players.PlayerType;
import pl.art.lach.mateusz.javaopenchess.display.windows.DrawLocalSettings;
import pl.art.lach.mateusz.javaopenchess.utils.GameModes;
import pl.art.lach.mateusz.javaopenchess.utils.GameTypes;
import pl.art.lach.mateusz.javaopenchess.utils.Settings;

import java.util.Arrays;
import java.util.Scanner;

import static org.jdesktop.application.Application.launch;

public class ChessInvoker {
    public static void main(String[] args)   {
        System.out.println("Starting Chess");

        Scanner sc = new Scanner(System.in);

        char color = 'B';
        String playerHuman = "Chinmay";
        //launch(JChessApp.class, args);
        Game currGame = beginGame(playerHuman, color);
        //AI ai = AIFactory.getAI(1);
        //currGame.setAi(ai);
        King king;

        if(currGame==null)
            System.out.println("Empty game !!!!!!!!!!!");

        //while(king.getKingState()== KingState.FINE) {
        do {
            System.out.print("Enter source:\t");
            Square beginSq = getSquareFromAlgebraic(currGame, sc.nextLine());
            System.out.print("Enter dest:\t");
            Square endSq = getSquareFromAlgebraic(currGame, sc.nextLine());

            currGame.getChessboard().move(beginSq, endSq);
            //currGame.getChessboard().move(getSquareFromAlgebraic(currGame, "g1"), getSquareFromAlgebraic(currGame, "f3"));
            //Game game = new Game();
            /*Square[][] squares = currGame.getChessboard().getSquares();
            for(Square[] sqRow: squares) {
                for(Square sq: sqRow)   {
                    System.out.print(sq.getAlgebraicNotation()+" : ");
                    if(sq.getPiece()!=null)
                        System.out.println(sq.getPiece().getName());
                }
                System.out.println();
            }*/
            currGame.getChessboard().unselect();
            currGame.nextMove();
            if (currGame.getSettings().getPlayerWhite() == currGame.getActivePlayer())
            {
                king = currGame.getChessboard().getKingWhite();
            } else
            {
                king = currGame.getChessboard().getKingBlack();
            }
            if(king.getKingState()!= KingState.FINE)
                break;


            if (shouldDoComputerMove(currGame))
            {
                currGame.doComputerMove();
            }

            /*squares = currGame.getChessboard().getSquares();
            for(Square[] sqRow: squares) {
                for(Square sq: sqRow)   {
                    System.out.print(sq.getAlgebraicNotation()+" : ");
                    if(sq.getPiece()!=null)
                        System.out.println(sq.getPiece().getName());
                }
                System.out.println();
            }*/

            if (currGame.getSettings().getPlayerWhite() == currGame.getActivePlayer())
            {
                king = currGame.getChessboard().getKingWhite();
            } else
            {
                king = currGame.getChessboard().getKingBlack();
            }
        } while(king.getKingState()== KingState.FINE);

        System.out.println("Game state: "+king.getKingState());
        //System.out.println(currGame.getActivePlayer().getName()+" loses");
        currGame.endGame(currGame.getActivePlayer().getName()+" loses");
    }

    private static Square getSquareFromAlgebraic(Game g, String notation)   {
        int pozX = (int)notation.charAt(0) - Square.ASCII_OFFSET;
        int pozY = Chessboard.LAST_SQUARE - Character.getNumericValue(notation.charAt(1))+1;
        Square sq = g.getChessboard().getSquare(pozX, pozY);
        //Square sq = new Square(pozX, pozY, new Pawn(g.getChessboard(), g.getActivePlayer()));
        return sq;
    }

    private static Game beginGame(String playerName1, char color) {
        //JChessView jChessView = JChessApp.getJavaChessView();
        //Object target = e.getSource();
        /*if (target == oponentComp)
        {
            computerLevel.setEnabled(true);
            secondName.setEnabled(false);
        }
        else if (target == this.oponentHuman)
        {
            this.computerLevel.setEnabled(false);
            this.secondName.setEnabled(true);
        }
        else if (target == this.okButton)
        {*/
                //this.firstName.setText(this.trimString(firstName, 9));
                playerName1 = playerName1.substring(0, Math.min(9, playerName1.length()));

            if (bothNamesMustBeFilled())
            {
                //JOptionPane.showMessageDialog(this, Settings.lang("fill_names"));
                System.out.println("Fill both names");
                return null;
            }
            if (nameMustBeFilled())
            {
                //JOptionPane.showMessageDialog(this, Settings.lang("fill_name"));
                System.out.println("Fill name");
                return null;
            }
            String playerName2 = "Dell PC";
            String whiteName;
            String blackName;
            PlayerType whiteType;
            PlayerType blackType;

            //if (0 == this.color.getSelectedIndex())
            if(color=='W')
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
            //String tabTitle = playerWhite.getName() + " vs " + playerBlack.getName();
            //Game game = JChessApp.getJavaChessView().createNewGameTab(tabTitle);
            Game game = new Game();
            game.getChat().setEnabled(false);
            Settings sett = game.getSettings();
            sett.setPlayerWhite(playerWhite);
            sett.setPlayerBlack(playerBlack);
            sett.setGameMode(GameModes.NEW_GAME);
            sett.setGameType(GameTypes.LOCAL);
            //sett.setUpsideDown(this.upsideDown.isSelected());
            sett.setUpsideDown(false);
            game.setActivePlayer(playerWhite);
            /*if (this.timeGame.isSelected())
            {
                String value = this.times[this.time4Game.getSelectedIndex()];
                Integer val = new Integer(value);
                sett.setTimeForGame((int) val * SECONDS_IN_ONE_MINUTE);
                GameClock clock = game.getGameClock();
                clock.setTimes(sett.getTimeForGame(), sett.getTimeForGame());
                clock.start();
            }*/
            //createDebugLogInformation(playerWhite, playerBlack, sett);

            game.newGame();
            //parent.setVisible(false);
            Game activeGame = game;
            //activeGame.repaint();
            //jChessView.setActiveTabGame(jChessView.getNumberOfOpenedTabs() - 1);
            //if (oponentComp.isSelected())
            //{
                //AI ai = AIFactory.getAI(computerLevel.getValue());
                AI ai = AIFactory.getAI(1);
                activeGame.setAi(ai);
                if (shouldDoComputerMove(activeGame))
                {
                    activeGame.doComputerMove();
                }
            //}
        //}

        return activeGame;
    }

    /*private static void playerMove(String src, String target)    {
        MouseEvent mouseEvent = new MouseEvent();
    }*/

    private static boolean shouldDoComputerMove(Game activeGame)
    {
        //2nd condition shuould be "computer is active player" instead of "computer is white player"
        return activeGame.getSettings().isGameVersusComputer()
                && activeGame.getSettings().getPlayerWhite().getPlayerType() == PlayerType.COMPUTER;
    }

    private static boolean nameMustBeFilled()
    {
        return false;
        //return this.oponentComp.isSelected() && this.firstName.getText().length() == 0;
    }

    private static boolean bothNamesMustBeFilled()
    {
        return false;
        /*return !this.oponentComp.isSelected()
                && (this.firstName.getText().length() == 0
                || this.secondName.getText().length() == 0);*/
    }
}
