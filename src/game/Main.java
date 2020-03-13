package game;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import game.ChessBoard.Result;

public class Main
{

	public static void main(String[] args)
	{

		ImageLoader.loadImages();

		while (true)
		{

			MainMenu menu = MainMenu.createAndShowGUI();

			while (!menu.readyToPlay())
			{
				try
				{
					Thread.sleep(10);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

			menu.dispose();

			boolean flipped = false;
			if (menu.getWhitePlayer() != null && menu.getBlackPlayer() == null)
			{
				flipped = true;
			}
			ChessBoard board = new ChessBoard(menu.getWhitePlayer(), menu.getBlackPlayer());
			BoardPanel boardPanel = new BoardPanel(board, flipped);

			JFrame frame = new JFrame();
			frame.setTitle("Chess");

			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			int frameSize = (int) (screen.height * .9);
			frame.setPreferredSize(new Dimension(frameSize, frameSize));
			frame.setLocation(screen.width / 2 - frameSize / 2, screen.height / 2 - frameSize / 2);
			frame.add(boardPanel);
			frame.toFront();
			frame.requestFocus();
			frame.pack();
			frame.setResizable(false);
			frame.setVisible(true);
			frame.setVisible(false);
			frame.setVisible(true);
			frame.toFront();

			board.playGame();

			String messageTitle = "";
			String popupMessage = "";
			Icon popupIcon = null;

			System.out.println(board.getGameResult());
			if (board.getGameResult() == Result.WHITE_WIN)
			{
				messageTitle = "Game Over";
				popupMessage = "Checkmate! White wins!";
				popupIcon = new ImageIcon(ImageLoader.whiteWinIcon);
			} else if (board.getGameResult() == Result.BLACK_WIN)
			{
				messageTitle = "Game Over";
				popupMessage = "Checkmate! Black wins!";
				popupIcon = new ImageIcon(ImageLoader.blackWinIcon);
			} else if (board.getGameResult() == Result.DRAW)
			{
				messageTitle = "Game Over";
				popupMessage = "It's a draw!";
				popupIcon = new ImageIcon(ImageLoader.drawIcon);
			}
			JOptionPane.showConfirmDialog(null, popupMessage, messageTitle, JOptionPane.PLAIN_MESSAGE, 0, popupIcon);
			frame.dispose();
		}
	}
}
