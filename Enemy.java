import java.awt.Graphics;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.*;
import java.applet.*;

public class Enemy extends Applet
{    
    //declares all the variables
    public int x_pos;
    public int y_pos;
    private double rotation = 0;
    private int radius = 10;
    public int speed;
    private int targetX, targetY;
    Image m_image;
    AffineTransform m_affineTransform;
    
    public Enemy (int x, int y, int mySpeed,int tX, int tY)
    {
	//gets all the stats from the main class and assigns it to the apporpraite variable
	x_pos = x;
	y_pos = y;
	rotation = facePlayer();
	speed = mySpeed;
	targetX = tX;
	targetY = tY;
	m_image = getToolkit().getImage ("enemy.jpg");
    }
    private double facePlayer(){//returns the proper rotation for the enemy
	    int dx = (targetX - 25) - (x_pos -13);
	    int dy = (targetY - 25) - (y_pos -13);
	    return Math.atan2(dy,dx)/Math.PI * 180;
    }

    public void moveEnemy(){//moves the enmey
	x_pos += Math.cos (rotation / 180 * Math.PI) * speed;
	y_pos += Math.sin (rotation / 180 * Math.PI) * speed;
    }

    public void drawEnemy (Graphics g)
    {
	rotation = facePlayer();//fixes the rotation of the enemy
	
	//draws the enemy, rotates the enemy, scales the enemy, and moves it to the apporopriate spot
	m_affineTransform = new AffineTransform ();
	Graphics2D g2d = (Graphics2D) g;
	m_affineTransform.rotate (Math.toRadians (rotation+90), x_pos, y_pos);
	m_affineTransform.translate(x_pos - 20, y_pos - 20);
	m_affineTransform.scale(0.5,0.5);
	g2d.drawImage (m_image, m_affineTransform, this);
    }
}
