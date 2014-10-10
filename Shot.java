import java.awt.Graphics;
import java.awt.Color;

public class Shot
{
    public int x_pos;
    public int y_pos;
    private double rotation;
    private final int radius = 10;
    

    public Shot (int x, int y, double playerRotation)
    {
	x_pos = x;
	y_pos = y;
	rotation = playerRotation;
    }


    public int getYPos ()
    {
	return y_pos;
    }


    public int getXPos ()
    {
	return x_pos;
    }


    public void moveShot (int speed)
    {
	x_pos += Math.cos (rotation / 180 * Math.PI) * speed;
	y_pos += Math.sin (rotation / 180 * Math.PI) * speed;
    }


    public void drawShot (Graphics g)
    {
	g.setColor (Color.BLACK);
	g.fillOval (x_pos - radius/2, y_pos - radius/2, radius, radius);
    }
}
