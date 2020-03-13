package pieces;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.ChessBoard;

public abstract class ChessPiece
{
	protected Point position = new Point();
	protected Point startPosition = new Point();
	protected boolean white;
	protected boolean hasMoved = false;
	protected boolean captured = false;

	public ChessPiece(Point position, boolean white)
	{
		super();
		this.position = position;
		this.startPosition = new Point(position.x, position.y);
		this.white = white;
	}

	public ChessPiece(int x, int y, boolean white)
	{
		super();
		this.position = new Point(x, y);
		this.startPosition = new Point(x, y);
		this.white = white;
	}

	/**
	 * Finds all squares this piece can move too on the board. Exactly the same as
	 * getAllLegalMoves, except this allows moves that leave the king in danger.
	 * 
	 * @param ChessBoard
	 *            the chess board
	 * @return an array of points containing moves
	 */
	public abstract ArrayList<Point> getAllMoves(ChessBoard board);

	/**
	 * Finds all squares this piece can move too on the board to attack a piece at
	 * that square. Exactly the same as getAllMoves for most pieces (except pawn and
	 * king).
	 * 
	 * @param ChessBoard
	 *            the chess board
	 * @return an array of points containing attacking moves
	 */
	public ArrayList<Point> getAllAttackableSquares(ChessBoard board)
	{
		return getAllMoves(board);
	}

	/**
	 * Finds all legal squares this piece can move too on the board.
	 * 
	 * @param ChessBoard
	 *            the chess board
	 * @return an array of points containing legal moves
	 */
	public ArrayList<Point> getAllLegalMoves(ChessBoard board)
	{
		ArrayList<Point> moves = getAllMoves(board);

		for (int i = 0; i < moves.size(); i++)
		{
			ChessBoard boardClone = board.clone();
			if (boardClone.makeAnyMove(position, moves.get(i)))
			{
				if (white && boardClone.whiteInCheck())
				{
					moves.remove(i);
					i--;
				}
				if (!white && boardClone.blackInCheck())
				{
					moves.remove(i);
					i--;
				}
			}
		}

		return moves;
	}

	/**
	 * Moves the chess piece to a new position. Doesn't check for legality of the
	 * move, but the move will only be executed if: an allied piece is not already
	 * on the move destination square, and the move is in bounds. If an enemy piece
	 * is on the move destination square, it will be captured.
	 * 
	 * @param move
	 *            the destination of the move
	 * @param board
	 *            the chess board
	 * @return whether or not the move went through
	 */
	public boolean move(Point move, ChessBoard board)
	{
		ChessPiece atMoveLocation = board.getPieceAt(move);

		if (board.inBounds(move))
		{
			if (atMoveLocation != null)
			{
				if (atMoveLocation.isWhite() == white)
				{
					return false; // move destination has ally piece
				} else if (atMoveLocation.isWhite() != white)
				{
					atMoveLocation.capture();
				}
			}
			position = move;
			hasMoved = true;
			return true;
		}
		return false;
	}

	public boolean isUnderAttack(ChessBoard board)
	{
		return board.isUnderAttack(position, !white);
	}

	/**
	 * Makes a copy of this ChessPiece.
	 * 
	 * @return the copy
	 */
	public abstract ChessPiece clone();

	public abstract BufferedImage getWhiteImage();

	public abstract BufferedImage getBlackImage();

	/**
	 * Returns the value of the chess piece as states by normal chess rules.
	 * 
	 * @return the value of the piece
	 */
	public abstract int getValue();

	public Point getStartPosition()
	{
		return startPosition;
	}

	public Point getPosition()
	{
		return position;
	}

	public boolean isWhite()
	{
		return white;
	}

	public boolean hasMoved()
	{
		return hasMoved;
	}

	public boolean isCaptured()
	{
		return captured;
	}

	public void capture()
	{
		captured = true;
	}

	public BufferedImage getImage()
	{
		if (white)
		{
			return getWhiteImage();
		} else
		{
			return getBlackImage();
		}
	}
}
