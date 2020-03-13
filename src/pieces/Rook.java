package pieces;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.ChessBoard;
import game.ImageLoader;

public class Rook extends ChessPiece
{

	public Rook(Point position, boolean white)
	{
		super(position, white);

	}

	public Rook(int x, int y, boolean white)
	{
		super(x, y, white);
	}

	@Override
	public ArrayList<Point> getAllMoves(ChessBoard board)
	{
		ArrayList<Point> moves = new ArrayList<Point>();

		// left direction
		int i = 1;
		boolean pieceCaptured = false;
		while (board.isEnterable(new Point(position.x - i, position.y), white) && !pieceCaptured)
		{
			Point move = new Point(position.x - i, position.y);
			if (board.containsPieceOfColor(move, !white))
			{
				pieceCaptured = true;
			}
			moves.add(move);
			i++;
		}

		// right direction
		i = 1;
		pieceCaptured = false;
		while (board.isEnterable(new Point(position.x + i, position.y), white) && !pieceCaptured)
		{
			Point move = new Point(position.x + i, position.y);
			if (board.containsPieceOfColor(move, !white))
			{
				pieceCaptured = true;
			}
			moves.add(move);
			i++;
		}

		// up direction
		i = 1;
		pieceCaptured = false;
		while (board.isEnterable(new Point(position.x, position.y + i), white) && !pieceCaptured)
		{
			Point move = new Point(position.x, position.y + i);
			if (board.containsPieceOfColor(move, !white))
			{
				pieceCaptured = true;
			}
			moves.add(move);
			i++;
		}

		// down direction
		i = 1;
		pieceCaptured = false;
		while (board.isEnterable(new Point(position.x, position.y - i), white) && !pieceCaptured)
		{
			Point move = new Point(position.x, position.y - i);
			if (board.containsPieceOfColor(move, !white))
			{
				pieceCaptured = true;
			}
			moves.add(move);
			i++;
		}

		return moves;
	}

	@Override
	public Rook clone()
	{
		return new Rook(position.x, position.y, white);
	}

	@Override
	public BufferedImage getWhiteImage()
	{
		return ImageLoader.whiteRook;
	}

	@Override
	public BufferedImage getBlackImage()
	{
		return ImageLoader.blackRook;
	}

	@Override
	public int getValue()
	{
		return 5;
	}

}
