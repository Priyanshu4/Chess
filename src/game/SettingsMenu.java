package game;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class SettingsMenu extends JFrame
{
	private static final long serialVersionUID = 4059237024172553449L;

	public static boolean flipAfterEachMove = false;
	public static int whiteDepth = 5;
	public static int blackDepth = 5;

	private int minDepth = 1;
	private int maxDepth = 8;

	public SettingsMenu()
	{
		super("Settings");
		setResizable(false);
	}

	public void addComponentsToPane(final Container pane)
	{
		JCheckBox flipCheckBox = new JCheckBox("Flip Board After Each Move", flipAfterEachMove);
		flipCheckBox.setOpaque(false);
		flipCheckBox.setFocusable(false);

		JPanel whiteDepthPanel = new JPanel();
		JLabel whiteDepthSliderTitle = new JLabel("White Minimax Depth: ");
		whiteDepthSliderTitle.setAlignmentY(Component.CENTER_ALIGNMENT);
		JSlider whiteDepthSlider = new JSlider(JSlider.HORIZONTAL, minDepth, maxDepth, whiteDepth);
		whiteDepthSlider.setFocusable(false);
		whiteDepthSlider.setMajorTickSpacing(1);
		whiteDepthSlider.setSnapToTicks(true);
		whiteDepthSlider.setPaintLabels(true);
		whiteDepthSlider.setOpaque(false);
		whiteDepthPanel.add(whiteDepthSliderTitle);
		whiteDepthPanel.add(whiteDepthSlider);
		whiteDepthPanel.setOpaque(false);

		JPanel blackDepthPanel = new JPanel();
		JLabel blackDepthSliderTitle = new JLabel("Black Minimax Depth: ");
		blackDepthSliderTitle.setAlignmentY(Component.CENTER_ALIGNMENT);
		JSlider blackDepthSlider = new JSlider(JSlider.HORIZONTAL, minDepth, maxDepth, blackDepth);
		blackDepthSlider.setFocusable(false);
		blackDepthSlider.setMajorTickSpacing(1);
		blackDepthSlider.setSnapToTicks(true);
		blackDepthSlider.setPaintLabels(true);
		blackDepthSlider.setOpaque(false);
		blackDepthPanel.add(blackDepthSliderTitle);
		blackDepthPanel.add(blackDepthSlider);
		blackDepthPanel.setOpaque(false);

		JFrame thisMenu = this;
		JButton doneButton = new JButton("Save Changes");
		doneButton.setFocusable(false);
		doneButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				flipAfterEachMove = flipCheckBox.isSelected();
				whiteDepth = whiteDepthSlider.getValue();
				blackDepth = blackDepthSlider.getValue();
				thisMenu.dispose();
			}

		});

		pane.setLayout(new GridLayout(0, 1));
		pane.add(flipCheckBox);
		pane.add(whiteDepthPanel);
		pane.add(blackDepthPanel);
		pane.add(doneButton);

	}

	static SettingsMenu createAndShowGUI()
	{

		SettingsMenu frame = new SettingsMenu();

		frame.setContentPane(new JPanel()
		{

			private static final long serialVersionUID = -6262313256751052077L;

			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				Graphics2D g2D = (Graphics2D) g;
				this.setOpaque(true);
				int squaresInARow = MainMenu.backgroundSquaresInARow;
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
}
