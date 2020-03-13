package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;

import computerplayers.ComputerPlayer;
import pieces.Bishop;
import pieces.ChessPiece;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Queen;
import pieces.Rook;

public class ChessBoard
{

	private ArrayList<ChessPiece> whitePieces = new ArrayList<ChessPiece>();
	private ArrayList<ChessPiece> blackPieces = new ArrayList<ChessPiece>();

	private ComputerPlayer whitePlayer = null; // leave null for human player
	private ComputerPlayer blackPlayer = null; // leave null for human player

	private int rows = 8;
	private int columns = 8;

	private boolean whiteMove = true;
	private int movesMade = 0;
	private boolean gameOver = false;

	private Point latestMovePosition = null;
	private Point latestMovePosition2 = null; // only used when castling, as 2 pieces are moved

	public enum Result
	{
		WHITE_WIN, DRAW, BLACK_WIN;
	}

	private Result gameResult = null;

	public enum Phase
	{
		FIRST_MOVE, OPENING, MIDGAME, ENDGAME,
	}

	public Phase currentPhase = Phase.FIRST_MOVE;

	public BoardPanel boardPanel = null;

	public ChessBoard()
	{
		SetupClassicChess();
	}

	public ChessBoard(ComputerPlayer whitePlayer, ComputerPlayer blackPlayer)
	{
		SetupClassicChess();
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
	}

	public ChessBoard(ArrayList<ChessPiece> whitePieces, ArrayList<ChessPiece> blackPieces, int rows, int columns)
	{
		super();
		this.whitePieces = whitePieces;
		this.blackPieces = blackPieces;
		this.rows = rows;
		this.columns = columns;
	}

	public ChessBoard(ComputerPlayer whitePlayer, ComputerPlayer blackPlayer, ArrayList<ChessPiece> whitePieces,
			ArrayList<ChessPiece> blackPieces, int rows, int columns)
	{
		super();
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
		this.whitePieces = whitePieces;
		this.blackPieces = blackPieces;
		this.rows = rows;
		this.columns = columns;
	}

	public ChessBoard(ArrayList<ChessPiece> whitePieces, ArrayList<ChessPiece> blackPieces, int rows, int columns,
			boolean whiteMove, int movesMade, Phase currentPhase, boolean gameOver, Result gameResult)
	{
		super();
		this.whitePieces = whitePieces;
		this.blackPieces = blackPieces;
		this.rows = rows;
		this.columns = columns;
		this.whiteMove = whiteMove;
		this.movesMade = movesMade;
		this.currentPhase = currentPhase;
		this.gameOver = gameOver;
		this.gameResult = gameResult;
	}

	public void SetupClassicChess()
	{

		// add pawns
		for (int i = 1; i <= columns; i++)
		{
			whitePieces.add(new Pawn(i, 2, true));
			blackPieces.add(new Pawn(i, rows - 1, false));
		}

		// add main pieces
		if (rows == 8 && columns == 8)
		{
			whitePieces.add(new Rook(1, 1, true));
			blackPieces.add(new Rook(1, rows, false));

			whitePieces.add(new Knight(2, 1, true));
			blackPieces.add(new Knight(2, rows, false));

			whitePieces.add(new Bishop(3, 1, true));
			blackPieces.add(new Bishop(3, rows, false));

			whitePieces.add(new Queen(4, 1, true));
			blackPieces.add(new Queen(4, rows, false));

			whitePieces.add(new King(5, 1, true));
			blackPieces.add(new King(5, rows, false));

			whitePieces.add(new Bishop(6, 1, true));
			blackPieces.add(new Bishop(6, rows, false));

			whitePieces.add(new Knight(7, 1, true));
			blackPieces.add(new Knight(7, rows, false));

			whitePieces.add(new Rook(8, 1, true));
			blackPieces.add(new Rook(8, rows, false));
		}

		Collections.shuffle(whitePieces);
		Collections.shuffle(blackPieces);

	}

	/**
	 * Plays out a game on this board between the players of this board.
	 */
	public void playGame()
	{
		while (!gameOver)
		{
			if (currentPlayerIsHuman())
			{
				try
				{
					Thread.sleep(10);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			} else
			{
				System.out.println("Waiting for computer");
				long startTime = System.nanoTime();
				boolean success = getCurrentComputerPlayer().move(this);
				if (!success)
				{
					System.out.println("Computer move failed");
					String errorMessage = "The computer tried to make an illegal move and the move was canceled. The game has been stopped. Please restart the game and try again.";
					JOptionPane.showMessageDialog(null, errorMessage, "Error!!!", JOptionPane.ERROR_MESSAGE);
					break;
				} else
				{
					long endTime = System.nanoTime();
					long duration = (endTime - startTime);
					long durationInSeconds = (long) (duration * .000000001);
					System.out.println("Computer move made in " + durationInSeconds + " seconds");
					System.out.println(movesMade + " moves made this game");

				}

			}

		}
	}

	/**
	 * Makes a move on the ChessBoard. The move only goes through if it is legal. If
	 * it is legal, then the move is made and true is returned. Otherwise false is
	 * returned.
	 * 
	 * @param pieceToMove
	 *            the original location of the pieceToMove
	 * @param moveLocation
	 *            the destination of the pieceToMove
	 * @return whether or not the move was succesful
	 */
	public boolean makeLegalMove(Point pieceToMove, Point moveLocation)
	{

		ChessPiece toMove = getPieceAt(pieceToMove);
		if (toMove == null)
		{
			return false;
		}
		if (toMove.isWhite() != whiteMove)
		{
			return false;
		}

		ArrayList<Point> legalMoves = toMove.getAllLegalMoves(this);
		for (Point move : legalMoves)
		{
			if (moveLocation.equals(move))
			{
				boolean success = toMove.move(moveLocation, this);
				latestMovePosition = new Point(moveLocation);
				latestMovePosition2 = null;

				// upgrade pawn
				if (toMove instanceof Pawn)
				{
					if (toMove.isWhite() && toMove.getPosition().y == rows)
					{
						whitePieces.remove(toMove);
						whitePieces.add(new Queen(moveLocation, true));
					} else if (!toMove.isWhite() && toMove.getPosition().y == 1)
					{
						blackPieces.remove(toMove);
						blackPieces.add(new Queen(moveLocation, false));
					}
				}

				// castling
				if (toMove instanceof King)
				{

					// white king side
					if (toMove.isWhite() && pieceToMove.equals(new Point(5, 1)) && moveLocation.equals(new Point(7, 1)))
					{
						ChessPiece rook = getPieceAt(8, 1);
						if (rook != null && rook instanceof Rook)
						{
							rook.move(new Point(6, 1), this);
						}
						latestMovePosition2 = new Point(6, 1);
					}
					// white queen side
					else if (toMove.isWhite() && pieceToMove.equals(new Point(5, 1))
							&& moveLocation.equals(new Point(3, 1)))
					{
						ChessPiece rook = getPieceAt(1, 1);
						if (rook != null && rook instanceof Rook)
						{
							rook.move(new Point(4, 1), this);
						}
						latestMovePosition2 = new Point(4, 1);
					}
					// black king side
					else if (!toMove.isWhite() && pieceToMove.equals(new Point(5, 8))
							&& moveLocation.equals(new Point(7, 8)))
					{
						ChessPiece rook = getPieceAt(8, 8);
						if (rook != null && rook instanceof Rook)
						{
							rook.move(new Point(6, 8), this);
						}
						latestMovePosition2 = new Point(6, 8);
					}
					// black queen side
					else if (!toMove.isWhite() && pieceToMove.equals(new Point(5, 8))
							&& moveLocation.equals(new Point(3, 8)))
					{
						ChessPiece rook = getPieceAt(1, 8);
						if (rook != null && rook instanceof Rook)
						{
							rook.move(new Point(4, 8), this);
						}
						latestMovePosition2 = new Point(4, 8);
					}
				}

				whiteMove = !whiteMove;
				movesMade++;
				removeCapturedPieces();
				updatePhase();
				checkGameOver();

				if (boardPanel != null)
				{
					boardPanel.colorAndHighlightTiles();
					if (SettingsMenu.flipAfterEachMove)
					{
						boardPanel.flip();
					}
					boardPanel.repaint();
				}

				return success;
			}
		}
		return false;
	}

	/**
	 * Makes any move on the board. The move must be legal, with the exception that
	 * moves can be made that leave your king in check. This is used when simulating
	 * games for the computer player.
	 * 
	 * @param pieceToMove
	 * @param moveLocation
	 * @return whether or not the move was succesful
	 */
	public boolean makeAnyMove(Point pieceToMove, Point moveLocation)
	{

		ChessPiece toMove = getPieceAt(pieceToMove);
		if (toMove == null)
		{
			return false;
		}
		if (toMove.isWhite() != whiteMove)
		{
			return false;
		}

		boolean success = toMove.move(moveLocation, this);
		latestMovePosition = new Point(moveLocation);
		latestMovePosition2 = null;

		// upgrade pawn or set hasDoubleJumped
		if (toMove instanceof Pawn)
		{
			if (toMove.isWhite() && toMove.getPosition().y == rows)
			{
				whitePieces.remove(toMove);
				whitePieces.add(new Queen(moveLocation, true));
			} else if (!toMove.isWhite() && toMove.getPosition().y == 1)
			{
				blackPieces.remove(toMove);
				blackPieces.add(new Queen(moveLocation, false));
			}
		}

		// castling
		if (toMove instanceof King)
		{

			// white king side
			if (toMove.isWhite() && pieceToMove.equals(new Point(5, 1)) && moveLocation.equals(new Point(7, 1)))
			{
				ChessPiece rook = getPieceAt(8, 1);
				if (rook != null && rook instanceof Rook)
				{
					rook.move(new Point(6, 1), this);
				}
				latestMovePosition2 = new Point(6, 1);
			}
			// white queen side
			else if (toMove.isWhite() && pieceToMove.equals(new Point(5, 1)) && moveLocation.equals(new Point(3, 1)))
			{
				ChessPiece rook = getPieceAt(1, 1);
				if (rook != null && rook instanceof Rook)
				{
					rook.move(new Point(4, 1), this);
				}
				latestMovePosition2 = new Point(4, 1);
			}
			// black king side
			else if (!toMove.isWhite() && pieceToMove.equals(new Point(5, 8)) && moveLocation.equals(new Point(7, 8)))
			{
				ChessPiece rook = getPieceAt(8, 8);
				if (rook != null && rook instanceof Rook)
				{
					rook.move(new Point(6, 8), this);
				}
				latestMovePosition2 = new Point(6, 8);
			}
			// black queen side
			else if (!toMove.isWhite() && pieceToMove.equals(new Point(5, 8)) && moveLocation.equals(new Point(3, 8)))
			{
				ChessPiece rook = getPieceAt(1, 8);
				if (rook != null && rook instanceof Rook)
				{
					rook.move(new Point(4, 8), this);
				}
				latestMovePosition2 = new Point(4, 8);
			}
		}

		removeCapturedPieces();
		checkKingsDead();

		if (success)
		{
			whiteMove = !whiteMove;
			movesMade++;
		}

		return success;
	}

	public ChessPiece getPieceAt(Point p)
	{
		for (ChessPiece c : whitePieces)
		{
			if (c.getPosition().equals(p))
			{
				return c;
			}
		}

		for (ChessPiece c : blackPieces)
		{
			if (c.getPosition().equals(p))
			{
				return c;
			}
		}

		return null;
	}

	public ChessPiece getPieceAt(int x, int y)
	{
		return getPieceAt(new Point(x, y));
	}

	/**
	 * Checks if a square is empty.
	 * 
	 * @param position
	 * @return Whether it is empty or not.
	 */
	public boolean isEmpty(Point position)
	{
		ChessPiece atMoveLocation = getPieceAt(position);

		return (atMoveLocation == null);
	}

	/**
	 * Checks if a square contains a piece of the same color as the isWhiteColor
	 * parameter.
	 * 
	 * @param position
	 *            the square
	 * @param isWhite
	 *            the color of the piece
	 * @return whether or not the square has an piece of the same color
	 */
	public boolean containsPieceOfColor(Point position, boolean isWhite)
	{
		ChessPiece piece = getPieceAt(position);
		if (piece != null)
		{
			return piece.isWhite() == isWhite;
		} else
		{
			return false;
		}
	}

	public boolean inBounds(int x, int y)
	{
		if (x <= columns && x > 0 && y <= rows && y > 0)
		{
			return true;
		}
		return false;
	}

	public boolean inBounds(Point position)
	{
		return inBounds(position.x, position.y);
	}

	/**
	 * Finds whether a piece of the specified color can enter the specified square
	 * position. A square is said to be enterable if its empty or contains a piece
	 * of the opposite color, and if it is in bounds.
	 * 
	 * @param position
	 * @param color
	 *            The color of the piece: true for white, false for black
	 * @return whether or not the square at the position is enterable
	 */
	public boolean isEnterable(Point position, boolean color)
	{
		if (containsPieceOfColor(position, color) || !inBounds(position))
		{
			return false;
		}
		return true;
	}

	public void removeCapturedPieces()
	{
		ArrayList<ChessPiece> whitePiecesToRemove = new ArrayList<ChessPiece>();
		for (ChessPiece c : whitePieces)
		{
			if (c.isCaptured())
			{
				whitePiecesToRemove.add(c);
			}
		}

		for (ChessPiece c : whitePiecesToRemove)
		{
			whitePieces.remove(c);
		}

		ArrayList<ChessPiece> blackPiecesToRemove = new ArrayList<ChessPiece>();
		for (ChessPiece c : blackPieces)
		{
			if (c.isCaptured())
			{
				blackPiecesToRemove.add(c);
			}
		}

		for (ChessPiece c : blackPiecesToRemove)
		{
			blackPieces.remove(c);
		}
	}

	public King getWhiteKing()
	{
		for (ChessPiece c : whitePieces)
		{
			if (c instanceof King)
			{
				return (King) c;
			}
		}
		return null;
	}

	public King getBlackKing()
	{
		for (ChessPiece c : blackPieces)
		{
			if (c instanceof King)
			{
				return (King) c;
			}
		}
		return null;
	}

	public boolean whiteInCheck()
	{
		return (getWhiteKing().isUnderAttack(this));
	}

	public boolean blackInCheck()
	{
		return (getBlackKing().isUnderAttack(this));
	}

	/**
	 * Checks if a square is under attack.
	 * 
	 * @param position
	 *            the square to check
	 * @param white
	 *            the color of the attackers, true for white, false for black
	 * @return
	 */
	public boolean isUnderAttack(Point position, boolean white)
	{
		ArrayList<ChessPiece> enemyPieces = new ArrayList<ChessPiece>();
		if (!white)
		{
			enemyPieces = getBlackPieces();
		} else
		{
			enemyPieces = getWhitePieces();
		}

		for (ChessPiece piece : enemyPieces)
		{
			ArrayList<Point> moves = piece.getAllAttackableSquares(this);
			for (Point move : moves)
			{
				if (move.equals(position))
				{
					return true;
				}
			}
		}
		return false;
	}

	public void updatePhase()
	{
		if (movesMade <= 2)
		{
			currentPhase = Phase.FIRST_MOVE;
		} else if (movesMade <= 15)
		{
			currentPhase = Phase.OPENING;
		} else if (movesMade <= 50)
		{
			currentPhase = Phase.MIDGAME;
		} else
		{
			currentPhase = Phase.ENDGAME;
		}
	}

	public void checkKingsDead()
	{
		King whiteKing = getWhiteKing();
		King blackKing = getBlackKing();
		if (whiteKing == null && blackKing == null)
		{
			gameResult = Result.DRAW;
			gameOver = true;
		} else if (whiteKing == null)
		{
			gameResult = Result.BLACK_WIN;
			gameOver = true;
		} else if (blackKing == null)
		{
			gameResult = Result.WHITE_WIN;
			gameOver = true;
		}

	}

	public void checkGameOver()
	{
		boolean noMovesAvailable = true;
		if (whiteMove)
		{
			for (ChessPiece c : whitePieces)
			{
				if (c.getAllLegalMoves(this).size() > 0)
				{
					noMovesAvailable = false;
				}
			}
		} else
		{
			for (ChessPiece c : blackPieces)
			{
				if (c.getAllLegalMoves(this).size() > 0)
				{
					noMovesAvailable = false;
				}
			}
		}

		if (noMovesAvailable)
		{
			gameOver = true;
			if (whiteInCheck())
			{
				gameResult = Result.BLACK_WIN;
				System.out.println(gameResult);
			} else if (blackInCheck())
			{
				gameResult = Result.WHITE_WIN;
			} else
			{
				gameResult = Result.DRAW;
			}
		}

		if (whitePieces.size() <= 1 && blackPieces.size() == whitePieces.size())
		{
			gameResult = Result.DRAW;
			gameOver = true;
		}

		checkKingsDead();

	}

	/**
	 * Clones the board, but only aspects of the board that are important for the
	 * simulating games. For example, graphics related things are not cloned.
	 */
	public ChessBoard clone()
	{
		ArrayList<ChessPiece> whitePiecesClone = new ArrayList<ChessPiece>();
		for (ChessPiece c : whitePieces)
		{
			whitePiecesClone.add(c.clone());
		}

		ArrayList<ChessPiece> blackPiecesClone = new ArrayList<ChessPiece>();
		for (ChessPiece c : blackPieces)
		{
			blackPiecesClone.add(c.clone());
		}

		ChessBoard clone = new ChessBoard(whitePiecesClone, blackPiecesClone, rows, columns, whiteMove, movesMade,
				currentPhase, gameOver, gameResult);
		return clone;
	}

	public int getRows()
	{
		return rows;
	}

	public int getColumns()
	{
		return columns;
	}

	public ArrayList<ChessPiece> getWhitePieces()
	{
		return whitePieces;
	}

	public ArrayList<ChessPiece> getBlackPieces()
	{
		return blackPieces;
	}

	public boolean isWhiteMove()
	{
		return whiteMove;
	}

	public int getMovesMade()
	{
		return movesMade;
	}

	public ComputerPlayer getCurrentComputerPlayer()
	{
		if (whiteMove)
		{
			return whitePlayer;
		} else
		{
			return blackPlayer;
		}
	}

	public boolean currentPlayerIsHuman()
	{
		return getCurrentComputerPlayer() == null;
	}

	public boolean gameOver()
	{
		return gameOver;
	}

	public Result getGameResult()
	{
		return gameResult;
	}

	public Phase getCurrentPhase()
	{
		return currentPhase;
	}

	public Point getLatestMovePosition()
	{
		return latestMovePosition;
	}

	public Point getLatestMovePosition2()
	{
		return latestMovePosition2;
	}

}
