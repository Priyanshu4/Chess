package pieces;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.ChessBoard;
import game.ImageLoader;

public class Bishop extends ChessPiece
{

	public Bishop(Point position, boolean white)
	{
		super(position, white);
	}

	public Bishop(int x, int y, boolean white)
	{
		super(x, y, white);
	}

	@Override
	public ArrayList<Point> getAllMoves(ChessBoard board)
	{
		ArrayList<Point> moves = new ArrayList<Point>();

		// top right
		int i = 1;
		boolean pieceCaptured = false;
		while (board.isEnterable(new Point(position.x + i, position.y + i), white) && !pieceCaptured)
		{
			Point move = new Point(position.x + i, position.y + i);
			if (board.containsPieceOfColor(move, !white))
			{
				pieceCaptured = true;
			}
			moves.add(move);
			i++;
		}

		// bottom left
		i = 1;
		pieceCaptured = false;
		while (board.isEnterable(new Point(position.x - i, position.y - i), white) && !pieceCaptured)
		{
			Point move = new Point(position.x - i, position.y - i);
			if (board.containsPieceOfColor(move, !white))
			{
				pieceCaptured = true;
			}
			moves.add(move);
			i++;
		}

		// top left
		i = 1;
		pieceCaptured = false;
		while (board.isEnterable(new Point(position.x - i, position.y + i), white) && !pieceCaptured)
		{
			Point move = new Point(position.x - i, position.y + i);
			if (board.containsPieceOfColor(move, !white))
			{
				pieceCaptured = true;
			}
			moves.add(move);
			i++;
		}

		// bottom right
		i = 1;
		pieceCaptured = false;
		while (board.isEnterable(new Point(position.x + i, position.y - i), white) && !pieceCaptured)
		{
			Point move = new Point(position.x + i, position.y - i);
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
	public Bishop clone()
	{
		return new Bishop(position.x, position.y, white);
	}

	@Override
	public BufferedImage getWhiteImage()
	{
		return ImageLoader.whiteBishop;
	}

	@Override
	public BufferedImage getBlackImage()
	{
		return ImageLoader.blackBishop;
	}

	@Override
	public int getValue()
	{
		return 3;
	}

}
