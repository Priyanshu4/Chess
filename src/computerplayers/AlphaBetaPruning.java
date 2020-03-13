package computerplayers;

import java.util.ArrayList;

import game.ChessBoard;

public class AlphaBetaPruning extends Minimax
{
	public AlphaBetaPruning()
	{
		super();
	}

	public AlphaBetaPruning(int depth)
	{
		super(depth);
	}

	@Override
	protected double searchFunction(ChessBoard board, int depth)
	{
		return alphaBetaPruning(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	private double alphaBetaPruning(ChessBoard board, int depth, double alpha, double beta)
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

				value = Math.max(value, alphaBetaPruning(child, depth - 1, alpha, beta));
				alpha = Math.max(alpha, value);
				if (alpha >= beta)
				{
					return value;
				}
			}
			return value;

		} else
		{
			double value = Integer.MAX_VALUE;
			ArrayList<ChessBoard> futureBoardStates = getFutureBoardStates(board);
			for (ChessBoard child : futureBoardStates)
			{

				value = Math.min(value, alphaBetaPruning(child, depth - 1, alpha, beta));
				beta = Math.min(beta, value);
				if (alpha >= beta)
				{
					return value;
				}
			}
			return value;
		}
	}
}
