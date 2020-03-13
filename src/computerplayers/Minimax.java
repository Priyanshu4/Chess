package computerplayers;

import java.awt.Point;
import java.util.ArrayList;

import game.ChessBoard;
import game.ChessBoard.Phase;
import game.ChessBoard.Result;
import pieces.ChessPiece;
import pieces.Pawn;

public class Minimax extends ComputerPlayer
{

	protected int depth = 5;

	public Minimax()
	{
		super();
	}

	public Minimax(int depth)
	{
		super();
		this.depth = depth;
	}

	@Override
	public boolean move(ChessBoard board)
	{
		Point bestMovePiece = new Point();
		Point bestMoveDestination = new Point();

		double preMoveValue = evaluateBoardState(board);
		preMoveValue = Math.round(preMoveValue * 100.0) / 100.0;
		System.out.println("Pre-Move Value: " + preMoveValue);
		System.out.println(board.getCurrentPhase());

		ArrayList<ChessPiece> whitePieces = board.getWhitePieces();
		ArrayList<ChessPiece> blackPieces = board.getBlackPieces();

		if (board.isWhiteMove())
		{
			double bestMoveScore = Integer.MIN_VALUE;

			for (ChessPiece c : whitePieces)
			{
				ArrayList<Point> legalMoves = c.getAllLegalMoves(board);
				for (Point move : legalMoves)
				{
					ChessBoard boardClone = board.clone();
					if (boardClone.makeLegalMove(c.getPosition(), move))
					{
						double moveScore = searchFunction(boardClone, depth - 1);
						if (moveScore >= bestMoveScore)
						{
							bestMovePiece = new Point(c.getPosition().x, c.getPosition().y);
							bestMoveDestination = new Point(move.x, move.y);
							bestMoveScore = moveScore;
						}
					}
				}
			}
		} else
		{
			double bestMoveScore = Integer.MAX_VALUE;

			for (ChessPiece c : blackPieces)
			{
				ArrayList<Point> legalMoves = c.getAllLegalMoves(board);
				for (Point move : legalMoves)
				{
					ChessBoard boardClone = board.clone();
					if (boardClone.makeLegalMove(c.getPosition(), move))
					{
						double moveScore = searchFunction(boardClone, depth - 1);
						if (moveScore <= bestMoveScore)
						{
							bestMovePiece = new Point(c.getPosition().x, c.getPosition().y);
							bestMoveDestination = new Point(move.x, move.y);
							bestMoveScore = moveScore;
						}
					}
				}
			}
		}
		return board.makeLegalMove(bestMovePiece, bestMoveDestination);
	}

	protected double searchFunction(ChessBoard board, int depth)
	{
		return minimax(board, depth);
	}

	private double minimax(ChessBoard board, int depth)
	{
		if (depth <= 0 || board.gameOver())
		{
			return evaluateBoardState(board);
		}

		if (board.isWhiteMove())
		{
			double value = Integer.MIN_VALUE;
			ArrayList<ChessBoard> futureBoardStates = getFutureBoardStates(board);
			for (ChessBoard child : futureBoardStates)
			{
				value = Math.max(value, minimax(child, depth - 1));
			}
			return value;
		} else
		{
			double value = Integer.MAX_VALUE;
			ArrayList<ChessBoard> futureBoardStates = getFutureBoardStates(board);
			for (ChessBoard child : futureBoardStates)
			{
				value = Math.min(value, minimax(child, depth - 1));
			}
			return value;
		}

	}

	/**
	 * Heuristic for the minimax algorithim.
	 */
	protected double evaluateBoardState(ChessBoard board)
	{
		ArrayList<ChessPiece> whitePieces = board.getWhitePieces();
		ArrayList<ChessPiece> blackPieces = board.getBlackPieces();
		Phase currentPhase = board.getCurrentPhase();
		int rows = board.getRows();

		double value = 0;
		if (!board.gameOver())
		{
			for (ChessPiece c : whitePieces)
			{
				double pieceValue = c.getValue();

				// for the first move and endgame, this increases value of pawns that are closer
				// to promotion
				if (currentPhase == Phase.FIRST_MOVE || currentPhase == Phase.ENDGAME)
				{
					if (c instanceof Pawn)
					{
						Point position = c.getPosition();
						double maxIncrease = 0.2;
						double adjustment = 0;
						double slope = maxIncrease / (rows - 3);
						adjustment = slope * (position.y - 2);
						pieceValue += adjustment;
					}
				}

				value += pieceValue;
			}

			for (ChessPiece c : blackPieces)
			{
				double pieceValue = c.getValue();

				if (currentPhase == Phase.FIRST_MOVE || currentPhase == Phase.ENDGAME)
				{
					if (c instanceof Pawn)
					{
						Point position = c.getPosition();
						double maxIncrease = 0.2;
						double adjustment = 0;
						double slope = -maxIncrease / (rows - 3);
						adjustment = slope * (position.y - 2) + maxIncrease;
						pieceValue += adjustment;
					}
				}

				value -= pieceValue;
			}
		} else
		{
			Result gameResult = board.getGameResult();
			if (gameResult == Result.WHITE_WIN)
			{
				value = Integer.MAX_VALUE;
			} else if (gameResult == Result.BLACK_WIN)
			{
				value = Integer.MIN_VALUE;
			} else if (gameResult == Result.DRAW)
			{
				value = 0;
			}
		}

		return value;
	}

	protected ArrayList<ChessBoard> getFutureBoardStates(ChessBoard board)
	{
		ArrayList<ChessBoard> futureBoardStates = new ArrayList<ChessBoard>();
		if (board.isWhiteMove())
		{
			for (ChessPiece c : board.getWhitePieces())
			{
				ArrayList<Point> allMoves = c.getAllMoves(board);
				for (Point move : allMoves)
				{
					ChessBoard boardClone = board.clone();
					if (boardClone.makeAnyMove(c.getPosition(), move))
					{
						futureBoardStates.add(boardClone);
					}
				}
			}
		} else
		{
			for (ChessPiece c : board.getBlackPieces())
			{
				ArrayList<Point> allMoves = c.getAllMoves(board);
				for (Point move : allMoves)
				{
					ChessBoard boardClone = board.clone();
					if (boardClone.makeAnyMove(c.getPosition(), move))
					{
						futureBoardStates.add(boardClone);
					}
				}
			}
		}
		return futureBoardStates;

	}

	public int getDepth()
	{
		return depth;
	}

	public void setDepth(int depth)
	{
		this.depth = depth;
	}

}
