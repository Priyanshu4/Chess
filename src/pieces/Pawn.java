package pieces;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.ChessBoard;
import game.ImageLoader;

public class Pawn extends ChessPiece
{

	private int direction = 1;
	private boolean hasDoubleJumped = false; // moved 2 squares in one turn

	public Pawn(Point position, boolean white)
	{
		super(position, white);
		if (!white)
		{
			direction = -1;
		} else
		{
			direction = 1;
		}
	}

	public Pawn(int x, int y, boolean white)
	{
		super(x, y, white);
		if (!white)
		{
			direction = -1;
		} else
		{
			direction = 1;
		}
	}

	@Override
	public ArrayList<Point> getAllMoves(ChessBoard board)
	{

		ArrayList<Point> moves = new ArrayList<Point>();

		Point singleForward = new Point(position.x, position.y + 1 * direction);
		if (board.isEmpty(singleForward))
		{
			moves.add(singleForward);

			if (!hasMoved)
			{
				Point doubleForward = new Point(position.x, position.y + 2 * direction);
				if (board.isEmpty(doubleForward))
				{
					moves.add(doubleForward);
				}
			}
		}

		Point latestMove = board.getLatestMovePosition();

		Point leftAttack = new Point(position.x - 1, position.y + 1 * direction);
		if (board.containsPieceOfColor(leftAttack, !white))
		{
			moves.add(leftAttack);
		} else if (latestMove != null)
		{
			Point leftSide = new Point(position.x - 1, position.y);
			if (latestMove.equals(leftSide))
			{
				ChessPiece toTheLeft = board.getPieceAt(leftSide);
				if (toTheLeft != null && toTheLeft.isWhite() != white && toTheLeft instanceof Pawn)
				{
					if (((Pawn) toTheLeft).hasDoubleJumped())
					{
						moves.add(leftAttack);
					}
				}
			}
		}

		Point rightAttack = new Point(position.x + 1, position.y + 1 * direction);
		if (board.containsPieceOfColor(rightAttack, !white))
		{
			moves.add(rightAttack);
		} else if (latestMove != null)
		{
			Point rightSide = new Point(position.x + 1, position.y);
			if (latestMove.equals(rightSide))
			{
				ChessPiece toTheRight = board.getPieceAt(rightSide);
				if (toTheRight != null && toTheRight.isWhite() != white && toTheRight instanceof Pawn)
				{
					if (((Pawn) toTheRight).hasDoubleJumped())
					{
						moves.add(rightAttack);
					}
				}
			}
		}

		return moves;

	}

	@Override
	public ArrayList<Point> getAllAttackableSquares(ChessBoard board)
	{
		ArrayList<Point> moves = new ArrayList<Point>();

		Point leftAttack = new Point(position.x - 1, position.y + 1 * direction);
		moves.add(leftAttack);

		Point rightAttack = new Point(position.x + 1, position.y + 1 * direction);
		moves.add(rightAttack);

		return moves;
	}

	@Override
	public boolean move(Point move, ChessBoard board)
	{
		Point originalPosition = new Point(position.x, position.y);

		if (move.x != originalPosition.x && board.isEmpty(move))
		{
			// en passant is happening
			ChessPiece toCapture = board.getPieceAt(new Point(move.x, originalPosition.y));
			if (toCapture != null)
			{
				toCapture.capture();
			}
		}

		boolean success = super.move(move, board);
		if (Math.abs(position.y - originalPosition.y) == 2)
		{
			this.hasDoubleJumped = true;
		}
		return success;
	}

	@Override
	public Pawn clone()
	{
		return new Pawn(position.x, position.y, white);
	}

	@Override
	public BufferedImage getWhiteImage()
	{
		return ImageLoader.whitePawn;
	}

	@Override
	public BufferedImage getBlackImage()
	{
		return ImageLoader.blackPawn;
	}

	@Override
	public int getValue()
	{
		return 1;
	}

	public boolean hasDoubleJumped()
	{
		return hasDoubleJumped;
	}

}
