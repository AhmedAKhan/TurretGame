import java.awt.Graphics;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.*;
import java.applet.*;

public class Player extends Applet
{

    // Variables
    private int x_pos;
    private int y_pos;
    public double rotation;

    Image m_image;
    private AffineTransform m_affineTransform;

    public Player (int x, int y)
    {
	//gets the x and y variables of the player
	x_pos = x;
	y_pos = y;

	//draws the player
	m_image = getToolkit().getImage ("Turret.jpg");
	prepareImage (m_image, this);
    }

    //creates a shot object and returns the shot object to the asteroids class.
    public Shot generateShot (double rotation)
    {
	Shot shot = new Shot (x_pos, y_pos, rotation);
	return shot;//creates a bullet
    }

    //draws the player
    public void drawPlayer (Graphics g)
    {
	//draws the player, rotates it, and transtates it, and scales it.
	m_affineTransform = new AffineTransform ();
	Graphics2D g2d = (Graphics2D) g;
	m_affineTransform.rotate (Math.toRadians (rotation), x_pos, y_pos);
	m_affineTransform.translate(x_pos - 18, y_pos - 18);
	m_affineTransform.scale(0.2,0.2);
	g2d.drawImage (m_image, m_affineTransform, this);
    }
}
