package computerplayers;

import java.awt.Point;
import java.util.ArrayList;

import game.ChessBoard;
import pieces.ChessPiece;

public class Random extends ComputerPlayer
{

	private long moveDelay = 0;

	public Random()
	{
		super();
	}

	public Random(long moveDelay)
	{
		super();
		this.moveDelay = moveDelay;
	}

	@Override
	public boolean move(ChessBoard board)
	{
		ArrayList<Point> moveStarts = new ArrayList<Point>();
		ArrayList<Point> moveDestinations = new ArrayList<Point>();

		if (board.isWhiteMove())
		{
			for (ChessPiece c : board.getWhitePieces())
			{
				ArrayList<Point> legalMoves = c.getAllLegalMoves(board);
				for (Point move : legalMoves)
				{
					moveStarts.add(new Point(c.getPosition().x, c.getPosition().y));
					moveDestinations.add(new Point(move.x, move.y));
				}
			}
		} else if (!board.isWhiteMove())
		{
			for (ChessPiece c : board.getBlackPieces())
			{
				ArrayList<Point> legalMoves = c.getAllLegalMoves(board);
				for (Point move : legalMoves)
				{
					moveStarts.add(new Point(c.getPosition().x, c.getPosition().y));
					moveDestinations.add(new Point(move.x, move.y));
				}
			}
		}

		int random = (int) (Math.random() * moveStarts.size());
		if (moveDelay > 0)
		{
			try
			{
				Thread.sleep(moveDelay);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		return board.makeLegalMove(moveStarts.get(random), moveDestinations.get(random));
	}

}
