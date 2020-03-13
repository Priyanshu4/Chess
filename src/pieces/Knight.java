package pieces;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.ChessBoard;
import game.ImageLoader;

public class Knight extends ChessPiece
{

	public Knight(Point position, boolean white)
	{
		super(position, white);
	}

	public Knight(int x, int y, boolean white)
	{
		super(x, y, white);
	}

	@Override
	public ArrayList<Point> getAllMoves(ChessBoard board)
	{
		Point upLeft = new Point(position.x - 1, position.y + 2);
		Point upRight = new Point(position.x + 1, position.y + 2);
		Point leftUp = new Point(position.x - 2, position.y + 1);
		Point leftDown = new Point(position.x - 2, position.y - 1);
		Point downLeft = new Point(position.x - 1, position.y - 2);
		Point downRight = new Point(position.x + 1, position.y - 2);
		Point rightDown = new Point(position.x + 2, position.y - 1);
		Point rightUp = new Point(position.x + 2, position.y + 1);

		ArrayList<Point> moves = new ArrayList<Point>();
		moves.add(upLeft);
		moves.add(upRight);
		moves.add(leftUp);
		moves.add(leftDown);
		moves.add(downLeft);
		moves.add(downRight);
		moves.add(rightDown);
		moves.add(rightUp);

		ArrayList<Point> movesToRemove = new ArrayList<Point>();
		for (Point p : moves)
		{

			if (!board.isEnterable(p, white))
			{
				movesToRemove.add(p);
			}
		}

		for (Point p : movesToRemove)
		{
			moves.remove(p);
		}

		return moves;
	}

	@Override
	public Knight clone()
	{
		return new Knight(position.x, position.y, white);
	}

	@Override
	public BufferedImage getWhiteImage()
	{
		return ImageLoader.whiteKnight;
	}

	@Override
	public BufferedImage getBlackImage()
	{
		return ImageLoader.blackKnight;
	}

	@Override
	public int getValue()
	{
		return 3;
	}

}
