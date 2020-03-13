package game;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader
{

	private static String whiteKingPath = "/Images/whiteKing.png";
	private static String blackKingPath = "/Images/blackKing.png";
	private static String whiteQueenPath = "/Images/whiteQueen.png";
	private static String blackQueenPath = "/Images/blackQueen.png";
	private static String whiteRookPath = "/Images/whiteRook.png";
	private static String blackRookPath = "/Images/blackRook.png";
	private static String whiteBishopPath = "/Images/whiteBishop.png";
	private static String blackBishopPath = "/Images/blackBishop.png";
	private static String whiteKnightPath = "/Images/whiteKnight.png";
	private static String blackKnightPath = "/Images/blackKnight.png";
	private static String whitePawnPath = "/Images/whitePawn.png";
	private static String blackPawnPath = "/Images/blackPawn.png";
	private static String whiteWinIconPath = "/Images/whiteKing.png";
	private static String blackWinIconPath = "/Images/blackKing.png";
	private static String drawIconPath = "/Images/drawIcon.png";
	private static String titleScreenImagePath = "/Images/titleImage.png";

	public static BufferedImage whiteKing = null;
	public static BufferedImage blackKing = null;
	public static BufferedImage whiteQueen = null;
	public static BufferedImage blackQueen = null;
	public static BufferedImage whiteRook = null;
	public static BufferedImage blackRook = null;
	public static BufferedImage whiteBishop = null;
	public static BufferedImage blackBishop = null;
	public static BufferedImage whiteKnight = null;
	public static BufferedImage blackKnight = null;
	public static BufferedImage whitePawn = null;
	public static BufferedImage blackPawn = null;
	public static BufferedImage whiteWinIcon = null;
	public static BufferedImage blackWinIcon = null;
	public static Image drawIcon = null;
	public static BufferedImage titleScreenImage = null;

	public static void loadImages()
	{
		try
		{

			whiteKing = ImageIO.read(ImageLoader.class.getResource(whiteKingPath));
			blackKing = ImageIO.read(ImageLoader.class.getResource(blackKingPath));
			whiteQueen = ImageIO.read(ImageLoader.class.getResource(whiteQueenPath));
			blackQueen = ImageIO.read(ImageLoader.class.getResource(blackQueenPath));
			whiteRook = ImageIO.read(ImageLoader.class.getResource(whiteRookPath));
			blackRook = ImageIO.read(ImageLoader.class.getResource(blackRookPath));
			whiteBishop = ImageIO.read(ImageLoader.class.getResource(whiteBishopPath));
			blackBishop = ImageIO.read(ImageLoader.class.getResource(blackBishopPath));
			whiteKnight = ImageIO.read(ImageLoader.class.getResource(whiteKnightPath));
			blackKnight = ImageIO.read(ImageLoader.class.getResource(blackKnightPath));
			whitePawn = ImageIO.read(ImageLoader.class.getResource(whitePawnPath));
			blackPawn = ImageIO.read(ImageLoader.class.getResource(blackPawnPath));
			whiteWinIcon = ImageIO.read(ImageLoader.class.getResource(whiteWinIconPath));
			blackWinIcon = ImageIO.read(ImageLoader.class.getResource(blackWinIconPath));
			drawIcon = ImageIO.read(ImageLoader.class.getResource(drawIconPath));
			drawIcon = drawIcon.getScaledInstance(100, 100, 0);
			titleScreenImage = ImageIO.read(ImageLoader.class.getResource(titleScreenImagePath));

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
