package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import pieces.ChessPiece;

public class BoardPanel extends JPanel
{
	private static final long serialVersionUID = -7970671225683234991L;

	private ChessBoard board;
	private Tile[][] tiles;
	private ChessPiece selectedPiece = null;
	private boolean flipped = false;

	public static final Color lightTile = new Color(240, 217, 181);
	public static final Color darkTile = new Color(181, 136, 99);
	public Color legalMovesHighlight = new Color(214, 244, 36); // yellow
	public Color latestMoveHighlight = new Color(0, 255, 0); // green
	public Color checkHighlight = new Color(252, 34, 3); // red
	int tileSize = 0;

	public BoardPanel(ChessBoard board, boolean flip)
	{
		this.board = board;
		board.boardPanel = this;
		this.flipped = flip;

		tileSize = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / board.getRows());
		this.setPreferredSize(new Dimension(board.getColumns() * tileSize, board.getRows() * tileSize));
		tiles = new Tile[board.getColumns()][board.getRows()];
		this.setLayout(new GridLayout(board.getColumns(), board.getRows()));

		boolean light = !flip;

		for (int y = tiles[0].length - 1; y >= 0; y--)
		{
			for (int x = 0; x < tiles.length; x++)
			{

				Color tileColor = lightTile;
				if (light)
				{
					tileColor = (lightTile);
				} else
				{
					tileColor = (darkTile);
				}

				Tile tile = new Tile(x + 1, y + 1, tileColor);
				if (flip)
				{
					tile = new Tile(x + 1, board.getColumns() - y, tileColor);
				}
				tile.setPreferredSize(new Dimension(tileSize, tileSize));
				tiles[x][y] = tile;
				this.add(tiles[x][y]);

				if (x != tiles.length - 1)
				{
					light = !light;
				}
			}
		}

		this.setSize(new Dimension(board.getColumns() * tileSize, board.getRows() * tileSize));

		this.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent arg0)
			{
				if (arg0.getKeyCode() == KeyEvent.VK_F)
				{
					flip();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0)
			{
			}

			@Override
			public void keyTyped(KeyEvent arg0)
			{
			}
		});

		this.setFocusable(true);
		this.requestFocus();
	}

	public void flip()
	{
		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		BoardPanel flippedPanel = new BoardPanel(board, !flipped);
		topFrame.add(flippedPanel);
		topFrame.getContentPane().remove(this);
		topFrame.setVisible(false);
		topFrame.setVisible(true);
		flippedPanel.colorAndHighlightTiles();
		topFrame.repaint();
	}

	public void colorTileAtPosition(Point position, Color color)
	{
		getTileAtPosition(position).setBackground(color);
	}

	public Tile getTileAtPosition(Point position)
	{
		if (!flipped)
			return tiles[position.x - 1][position.y - 1];
		else
			return tiles[position.x - 1][board.getColumns() - position.y];
	}

	public void colorAndHighlightTiles()
	{
		resetAllTileColors();

		if (selectedPiece != null)
		{
			colorTileAtPosition(selectedPiece.getPosition(), legalMovesHighlight);
			ArrayList<Point> moves = selectedPiece.getAllLegalMoves(board);
			for (Point move : moves)
			{
				colorTileAtPosition(move, legalMovesHighlight);
			}
		}

		Point latestMove = board.getLatestMovePosition();
		if (latestMove != null)
		{
			if (getTileAtPosition(latestMove).getBackground() != legalMovesHighlight)
			{
				colorTileAtPosition(latestMove, latestMoveHighlight);
			}
		}

		Point latestMove2 = board.getLatestMovePosition2();
		if (latestMove2 != null)
		{
			if (getTileAtPosition(latestMove2).getBackground() != legalMovesHighlight)
			{
				colorTileAtPosition(latestMove2, latestMoveHighlight);
			}
		}

		if (board.whiteInCheck())
		{
			Point whiteKing = board.getWhiteKing().getPosition();
			if (getTileAtPosition(whiteKing).getBackground() != legalMovesHighlight)
			{
				colorTileAtPosition(whiteKing, checkHighlight);
			}
		}

		if (board.blackInCheck())
		{
			Point blackKing = board.getBlackKing().getPosition();
			if (getTileAtPosition(blackKing).getBackground() != legalMovesHighlight)
			{
				colorTileAtPosition(blackKing, checkHighlight);
			}
		}
	}

	public void resetAllTileColors()
	{
		if (tiles != null && tiles.length > 0)
		{
			for (int y = tiles[0].length - 1; y >= 0; y--)
			{
				for (int x = 0; x < tiles.length; x++)
				{
					tiles[x][y].setBackground(tiles[x][y].defaultColor);
				}
			}
		}
	}

	public class Tile extends JPanel
	{
		private static final long serialVersionUID = 2139672007742323489L;

		int x;
		int y;
		public Color defaultColor;

		public Tile(int x, int y, Color defaultColor)
		{
			super();
			this.x = x;
			this.y = y;
			this.defaultColor = defaultColor;
			this.setBackground(defaultColor);

			this.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					if (board.currentPlayerIsHuman())
					{
						Point position = new Point(x, y);
						if (board.containsPieceOfColor(position, board.isWhiteMove()))
						{
							ChessPiece piece = board.getPieceAt(position);
							selectedPiece = piece;

						} else if (selectedPiece != null)
						{
							ArrayList<Point> moves = selectedPiece.getAllLegalMoves(board);
							for (Point move : moves)
							{
								if (move.equals(position))
								{
									board.makeLegalMove(selectedPiece.getPosition(), move);
								}
							}
							selectedPiece = null;
						}

						colorAndHighlightTiles();
					}
				}
			});

		}

		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Point position = new Point(x, y);
			if (!board.isEmpty(position))
			{
				g.drawImage(board.getPieceAt(x, y).getImage(), 0, 0, this.getWidth(), this.getWidth(), null);
			}
		}

	}
}
