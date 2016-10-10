import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import elder.geometry.Line;
import elder.geometry.Point;
import elder.geometry.Polygon;

public class View
{

	private float zoom = 1f;
	private int width;
	private int height;

	public void init(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		try
		{
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("View");
			Display.setResizable(true);
			Display.create();
		} catch (LWJGLException e)
		{
			e.printStackTrace();
			System.exit(0);
		}

		// init OpenGL
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, 0, height, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glClearColor(1f, 1f, 1f, 1f);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}

    public void clear()
    {
    	setWidth(Display.getWidth());
    	setHeight(Display.getHeight());

        GL11.glViewport(0, 0, width, height);

    	GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, 0, height, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glClearColor(1f, 1f, 1f, 1f);
    	
	    // Clear the screen and depth buffer
	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	

    }
    
    public void update()
    {
    	Display.update();
    }

	public void destroy()
	{
		Display.destroy();
	}

	

	public void setColor(float R, float G, float B, float alpha)
	{
		GL11.glColor4f(R, G, B, alpha);
	}

	public void drawLine(Line line, float lineWidth, boolean scale)
	{
		if (scale)
		{
			Line a = line.getParallelLine(lineWidth);

			Polygon polygon = new Polygon();
			polygon.add(a.a);
			polygon.add(a.b);
			polygon.add(line.b);
			polygon.add(line.a);

			drawPolygon(polygon, true);
		} 
		else
		{
			GL11.glLineWidth(lineWidth);
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d((line.a.x), (line.a.y));
			GL11.glVertex2d((line.b.x), (line.b.y));
			GL11.glEnd();
		}

	}

	public void drawPoint(Point point, float size)
	{

		GL11.glLineWidth(size);

		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d((point.x), (point.y) + ((size / 2) / zoom));
		GL11.glVertex2d((point.x), (point.y) - ((size / 2) / zoom));
		GL11.glEnd();

	}

	public void drawPolygon(Polygon polygon, boolean filled)
	{
		GL11.glLineWidth(1f);

		if (filled)
		{
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		} 
		else
		{
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);

		}
		GL11.glBegin(GL11.GL_POLYGON);
		for (Line line : polygon)
		{
			GL11.glVertex2d((line.a.x), (line.a.y));
			GL11.glVertex2d((line.b.x), (line.b.y));
		}
		GL11.glEnd();

	}

}
