package pieces;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.ChessBoard;
import game.ImageLoader;

public class King extends ChessPiece
{

	public King(Point position, boolean white)
	{
		super(position, white);
	}

	public King(int x, int y, boolean white)
	{
		super(x, y, white);
	}

	@Override
	public ArrayList<Point> getAllMoves(ChessBoard board)
	{
		Point topLeft = new Point(position.x - 1, position.y + 1);
		Point topMiddle = new Point(position.x, position.y + 1);
		Point topRight = new Point(position.x + 1, position.y + 1);
		Point midLeft = new Point(position.x - 1, position.y);
		Point midRight = new Point(position.x + 1, position.y);
		Point bottomLeft = new Point(position.x - 1, position.y - 1);
		Point bottomMiddle = new Point(position.x, position.y - 1);
		Point bottomRight = new Point(position.x + 1, position.y - 1);

		ArrayList<Point> moves = new ArrayList<Point>();
		moves.add(topLeft);
		moves.add(topMiddle);
		moves.add(topRight);
		moves.add(midLeft);
		moves.add(midRight);
		moves.add(bottomLeft);
		moves.add(bottomMiddle);
		moves.add(bottomRight);

		for (int i = 0; i < moves.size(); i++)
		{
			if (!board.isEnterable(moves.get(i), white))
			{
				moves.remove(i);
				i--;
			}
		}

		if (!hasMoved && !isUnderAttack(board))
		{
			ChessPiece rightRook = board.getPieceAt(new Point(position.x + 3, position.y));
			ChessPiece leftRook = board.getPieceAt(new Point(position.x - 4, position.y));

			if (rightRook != null && !rightRook.hasMoved() && rightRook instanceof Rook)
			{
				Point twoRight = new Point(position.x + 2, position.y);
				if (board.isEmpty(midRight) && board.isEmpty(twoRight) && !board.isUnderAttack(midRight, !white))
				{
					moves.add(twoRight);
				}
			}

			if (leftRook != null && !leftRook.hasMoved() && leftRook instanceof Rook)
			{
				Point twoLeft = new Point(position.x - 2, position.y);
				Point threeLeft = new Point(position.x - 3, position.y);
				if (board.isEmpty(midLeft) && board.isEmpty(twoLeft) && board.isEmpty(threeLeft)
						&& !board.isUnderAttack(midLeft, !white))
				{
					moves.add(twoLeft);
				}
			}

		}

		return moves;
	}

	@Override
	public ArrayList<Point> getAllAttackableSquares(ChessBoard board)
	{
		Point topLeft = new Point(position.x - 1, position.y + 1);
		Point topMiddle = new Point(position.x, position.y + 1);
		Point topRight = new Point(position.x + 1, position.y + 1);
		Point midLeft = new Point(position.x - 1, position.y);
		Point midRight = new Point(position.x + 1, position.y);
		Point bottomLeft = new Point(position.x - 1, position.y - 1);
		Point bottomMiddle = new Point(position.x, position.y - 1);
		Point bottomRight = new Point(position.x + 1, position.y - 1);

		ArrayList<Point> moves = new ArrayList<Point>();
		moves.add(topLeft);
		moves.add(topMiddle);
		moves.add(topRight);
		moves.add(midLeft);
		moves.add(midRight);
		moves.add(bottomLeft);
		moves.add(bottomMiddle);
		moves.add(bottomRight);

		return moves;
	}

	@Override
	public King clone()
	{
		return new King(position.x, position.y, white);
	}

	@Override
	public BufferedImage getWhiteImage()
	{
		return ImageLoader.whiteKing;
	}

	@Override
	public BufferedImage getBlackImage()
	{
		return ImageLoader.blackKing;
	}

	@Override
	public int getValue()
	{
		return 100;
	}

}
