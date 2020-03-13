package game;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import computerplayers.AlphaBetaPruning;
import computerplayers.ComputerPlayer;
import computerplayers.Minimax;
import computerplayers.Random;

public class MainMenu extends JFrame
{

	private static final long serialVersionUID = -3418108967904499584L;

	String[] playerStrings =
	{ "Human", "Random Moves", "AlphaBeta" };
	ComputerPlayer[] whitePlayers =
	{ null, new Random(100), new AlphaBetaPruning() };
	ComputerPlayer[] blackPlayers =
	{ null, new Random(100), new AlphaBetaPruning() };

	private static int selectedWhiteIndex = 0;
	private static int selectedBlackIndex = 0;
	private boolean readyToPlay = false;

	public static int backgroundSquaresInARow = 4;

	public MainMenu(String name)
	{
		super(name);
		setResizable(false);
	}

	public void addComponentsToPane(final Container pane)
	{
		final JPanel gameTitle = new JPanel();
		gameTitle.setLayout(new BoxLayout(gameTitle, BoxLayout.Y_AXIS));
		gameTitle.setOpaque(false);

		JLabel titleLabel = new JLabel();
		titleLabel.setOpaque(false);
		ImageIcon titleIcon = new ImageIcon(ImageLoader.titleScreenImage);
		titleLabel.setIcon(titleIcon);
		titleLabel.setAlignmentX(CENTER_ALIGNMENT);
		gameTitle.add(titleLabel);

		JLabel chessIconLabel = new JLabel();
		chessIconLabel.setOpaque(false);
		ImageIcon chessIcon = new ImageIcon(ImageLoader.blackKing);
		chessIconLabel.setIcon(chessIcon);
		chessIconLabel.setAlignmentX(CENTER_ALIGNMENT);
		gameTitle.add(chessIconLabel);

		final JPanel menuOptions = new JPanel();
		GridLayout optionsLayout = new GridLayout(0, 1);
		menuOptions.setLayout(optionsLayout);
		menuOptions.setOpaque(false);

		JLabel whiteLabel = new JLabel("White Player: ");
		JComboBox<String> whiteCB = new JComboBox<String>(playerStrings);
		JLabel blackLabel = new JLabel("Black Player: ");
		JComboBox<String> blackCB = new JComboBox<String>(playerStrings);
		whiteLabel.setFont(new Font(whiteLabel.getFont().getName(), Font.BOLD, whiteLabel.getFont().getSize()));
		blackLabel.setFont(new Font(blackLabel.getFont().getName(), Font.BOLD, blackLabel.getFont().getSize()));
		whiteLabel.setOpaque(false);
		blackLabel.setOpaque(false);
		whiteCB.setOpaque(false);
		whiteCB.setSelectedIndex(selectedWhiteIndex);
		blackCB.setOpaque(false);
		blackCB.setSelectedIndex(selectedBlackIndex);

		JPanel whitePlayerContainer = new JPanel();
		JPanel blackPlayerContainer = new JPanel();

		whitePlayerContainer.setOpaque(false);
		blackPlayerContainer.setOpaque(false);

		whitePlayerContainer.add(whiteLabel);
		whitePlayerContainer.add(whiteCB);

		blackPlayerContainer.add(blackLabel);
		blackPlayerContainer.add(blackCB);

		menuOptions.add(whitePlayerContainer);
		menuOptions.add(blackPlayerContainer);

		JButton playButton = new JButton("Play");
		playButton.setFocusable(false);
		playButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				selectedWhiteIndex = whiteCB.getSelectedIndex();
				selectedBlackIndex = blackCB.getSelectedIndex();
				readyToPlay = true;
			}

		});
		menuOptions.add(playButton);

		JButton settingsButton = new JButton("Settings");
		settingsButton.setFocusable(false);
		settingsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SettingsMenu.createAndShowGUI();
			}

		});
		menuOptions.add(settingsButton);

		pane.setLayout(new GridLayout(0, 1));
		pane.add(gameTitle);
		pane.add(menuOptions);

	}

	static MainMenu createAndShowGUI()
	{

		MainMenu frame = new MainMenu("Chess");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setContentPane(new JPanel()
		{

			private static final long serialVersionUID = -6262313256751052077L;

			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				Graphics2D g2D = (Graphics2D) g;
				this.setOpaque(true);
				int squaresInARow = backgroundSquaresInARow;
				int squareSize = frame.getWidth() / squaresInARow;
				int rows = (frame.getHeight() / squareSize) + 1;
				boolean light = true;
				for (int r = 0; r < rows; r++)
				{
					for (int s = 0; s < squaresInARow; s++)
					{
						if (light)
						{
							g2D.setColor(BoardPanel.lightTile);
						} else
						{
							g2D.setColor(BoardPanel.darkTile);
						}

						g2D.fillRect(s * squareSize, r * squareSize, squareSize, squareSize);

						if (s != squaresInARow - 1)
						{
							light = !light;
						}
					}
				}
			}

		});

		frame.addComponentsToPane(frame.getContentPane());

		frame.setResizable(false);
		frame.pack();

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		frame.setLocation(screen.width / 2 - frame.getSize().width / 2, screen.height / 2 - frame.getSize().height / 2);
		frame.setVisible(true);
		return frame;
	}

	public ComputerPlayer getWhitePlayer()
	{
		ComputerPlayer whitePlayer = whitePlayers[selectedWhiteIndex];
		if (whitePlayer instanceof Minimax)
		{
			((Minimax) whitePlayer).setDepth(SettingsMenu.whiteDepth);
		}
		return whitePlayer;
	}

	public ComputerPlayer getBlackPlayer()
	{
		ComputerPlayer blackPlayer = blackPlayers[selectedBlackIndex];
		if (blackPlayer instanceof Minimax)
		{
			((Minimax) blackPlayer).setDepth(SettingsMenu.blackDepth);
		}
		return blackPlayer;
	}

	public boolean readyToPlay()
	{
		return readyToPlay;
	}

}
