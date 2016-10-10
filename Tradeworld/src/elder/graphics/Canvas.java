package elder.graphics;
import java.awt.Font;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import bobjob.TrueTypeFont;
import elder.geometry.Line;
import elder.geometry.Point;
import elder.geometry.Polygon;

public class Canvas
{
	public static double smallestSide = 10;
	public static double longestUnsplit = 100;
	
	private int width;
	
	private int height;

	private final List<MouseListener> mouseListeners = new ArrayList<MouseListener> ();

	private final List<Layer> layers = new ArrayList<Layer> ();
			
	private double zoom=1;
	
	private Point absTranslate;
	private Double absZoom=null;
	
	final Polygon border = new Polygon();
	
	private final Map<Integer,List<DrawnText>> text = new HashMap<Integer,List<DrawnText>> ();
	
	
	public Canvas(String title, int width, int height)
	{
		setWidth(width);
		setHeight(height);
		init(title);
	  
	}
	
	public void addMouseListener(MouseListener listener)
	{
		mouseListeners.add(listener);
	}
	
	public void removeMouseListener(MouseListener listener)
	{
		mouseListeners.remove(listener);
	}
	
	public List<Layer> getLayers()
	{
		return layers;
	}

	
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
    public void init(String title) 
    {
    	 try 
         {
         	Display.setDisplayMode(new DisplayMode(width,height));
         	Display.setTitle(title);
         	Display.setResizable(true);
         	Display.create();

         	
         } 
         catch (LWJGLException e)
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

		//adjustZoom(Math.max(width/(sim.getCity().getWidth()*sim.getCity().getScale()), height/(sim.getCity().getWidth()*sim.getCity().getScale())),0,0);
    }
    
    public void addLayer(Layer layer)
    {
    	layers.add(layer);
    }
    
    public void refresh()
    {
    	
    	
    	
    	setWidth(Display.getWidth());
    	setHeight(Display.getHeight());

        GL11.glViewport(0, 0, width, height);

    	GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, 0, height, 1, -1);
		
    	DoubleBuffer buf = BufferUtils.createDoubleBuffer(16);

		GL11.glGetDouble(GL11.GL_PROJECTION_MATRIX, buf);
    	
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glClearColor(1f, 1f, 1f, 1f);
		    	
		if (absZoom!=null)
		{			
			zoom = 1;
			GL11.glLoadIdentity();
			adjustZoom(absZoom,0,0);
			translate(absTranslate.x,absTranslate.y);
			absZoom = null;
			absTranslate = null;
		}
		
	    // Clear the screen and depth buffer
	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	
	    	    
	    Point topLeft = getMousePosition(0,0);
		Point bottomRight = getMousePosition(width,height);
		
		border.clear();
		border.add(topLeft);
		border.add(new Point(bottomRight.x,topLeft.y));
		border.add(bottomRight);
		border.add(new Point(topLeft.x,bottomRight.y));
		
		//System.out.println(border);
		
	    
	
		
	    for (Layer layer : layers)
	    {
	    	layer.drawToCanvas(this);
		
	    }
	    
  	  	drawText();

	    Display.update();

    
    }
    
    static public Point getMousePosition(int mouseX, int mouseY)
    {
    	IntBuffer viewport = BufferUtils.createIntBuffer(16);
    	FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
    	FloatBuffer projection = BufferUtils.createFloatBuffer(16);
    	FloatBuffer position = BufferUtils.createFloatBuffer(3);
    	
    	float winX, winY;
    	
       GL11.glGetFloat( GL11.GL_MODELVIEW_MATRIX, modelview );
       GL11.glGetFloat( GL11.GL_PROJECTION_MATRIX, projection );
       GL11.glGetInteger( GL11.GL_VIEWPORT, viewport );

       winX = (float)mouseX;
       winY = (float)mouseY;

       GLU.gluUnProject(winX, winY, 1, modelview, projection, viewport, position);
       
       return new Point(position.get(0), position.get(1));
    }
 
    
    public void setColor(float R, float G, float B, float alpha)
    {
    	 GL11.glColor4f(R,G,B,alpha);
    }
    
    public void drawLine(Line line, float lineWidth, boolean scale)
    {
        if (scale)
        {
        	Line a = line.getParallelLine(lineWidth);
        	//Line b = line.getParallelLine(-lineWidth/2);
        	
        	Polygon polygon = new Polygon();
        	polygon.add(a.a);
        	polygon.add(a.b);
        	polygon.add(line.b);
        	polygon.add(line.a);
        	
        	drawPolygon(polygon,true);
        }
        else
        {
        	GL11.glLineWidth(lineWidth);

        }
    	GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d((line.a.x),(line.a.y));
        GL11.glVertex2d((line.b.x),(line.b.y));
        GL11.glEnd();
    
        //GL11.glLineWidth(1f);
    }
    
    public void drawPoint(Point point, float size)
    {
      
        GL11.glLineWidth(size);

    
    	GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d((point.x),(point.y) + ((size/2)/zoom));
        GL11.glVertex2d((point.x),(point.y) - ((size/2)/zoom));
        GL11.glEnd();
    
        //GL11.glLineWidth(1f);
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
    		GL11.glVertex2d((line.a.x),(line.a.y));
            GL11.glVertex2d((line.b.x),(line.b.y));
    	}
        GL11.glEnd();
        
 
    }
    

    
    public void destroy()
    {
    	Display.destroy();
    }
    

    public void adjustZoom(double delta, int x, int y)
    {

    	zoom=zoom*delta;
    	
    	Point a = getMousePosition((int)x,(int)y);
    	
    	
    	GL11.glScaled(delta,delta,delta);

    	Point b = getMousePosition((int)x,(int)y);
    	
    	translate(b.x-a.x,b.y-a.y);
    	   	
    	//translate(x*((1/zoom) - (1/oldZoom)),y*((1/zoom) - (1/oldZoom)));
    }
    
    public void zoom(Point topLeft, Point bottomRight)
    {   
    	double xZoom = Display.getWidth()/((bottomRight.x - topLeft.x)+200);
    	double yZoom = Display.getWidth()/((bottomRight.y - topLeft.y)+200);
    	
    	absZoom = Math.max(xZoom,yZoom);
    	
    	absTranslate = new Point(-(topLeft.x - 100),-(topLeft.y - 100));
    	
    	
//    	translate(-(topLeft.x - 100),-(topLeft.y - 100));
//    	
////    	xOffset = -(topLeft.x - 100);
////    	yOffset = -(topLeft.y - 100);
//    	
//    	double xZoom = Display.getWidth()/((bottomRight.x - topLeft.x)+200);
//    	double yZoom = Display.getWidth()/((bottomRight.y - topLeft.y)+200);
//    	
//    	adjustZoom(Math.max(xZoom,yZoom)/zoom,0,0);
    }
    
    public void translate(double xDelta, double yDelta)
    {
 
    	GL11.glTranslated(xDelta,yDelta,0);	

    	
    }


    
    public Point getCityCoordinate(int x, int y)
    {
    	return getMousePosition(x,y);
    	
//    	System.out.println(x+","+y);
//    	System.out.println(((x/zoom) - xOffset2)+","+((y/zoom) - yOffset2));
//    	return new Point((x - xOffset2)/zoom,(y - yOffset2)/zoom);
    }

    public void run()
    {
    	boolean dragging[] = new boolean[3];
    	int xMove[] = new int[3];
    	int yMove[] = new int[3];
    	
    	while(!Display.isCloseRequested())
    	{
  
    		refresh();
    		
    		while (Mouse.next())
    		{
    			Point cityPoint = getCityCoordinate(Mouse.getX(),Mouse.getY());

    			Point offset = new Point(Mouse.getDX(),Mouse.getDY());
    			
    			if (Mouse.getEventButton()==-1)
    			{
    				for (MouseListener listener : mouseListeners)
    				{
    					listener.onMove(cityPoint);
    				}
    				
    				for (int b=0;b<3;b++)
    				{
    					if (dragging[b])
    					{
    						for (MouseListener listener : mouseListeners)
    	    				{
    							switch(b)
    							{
    							case 0:
    								listener.onLeftDrag(offset);
    								break;
    							case 1:
    								listener.onRightDrag(offset);
    								break;
    							case 2:
    								listener.onMiddleDrag(offset);
    								break;
    							}
    								
    	    				}
    						
    						xMove[b]+=offset.x;
    						yMove[b]+=offset.y;
    					}
    				}
    			}
    			
    			else if (Mouse.getEventButton()<3)
    			{
    				int b = Mouse.getEventButton();
    				
    				if (Mouse.getEventButtonState())
    				{
    					dragging[b] = true;
    					xMove[b] = 0;
    					yMove[b] = 0;
    				}
    				else
    				{
    					
    					dragging[b] = false;
    					
    					if (xMove[b]==0&&yMove[b]==0)
    					{
    						for (MouseListener listener : mouseListeners)
    	    				{
	    						switch(b)
								{
								case 0:
									listener.onLeftClick(offset);
									break;
								case 1:
									listener.onRightClick(offset);
									break;
								case 2:
									listener.onMiddleClick(offset);
									break;
								}
    	    				}
    					}
    					
    				}
    			}
    			
    			for (MouseListener listener : mouseListeners)
    			{
    				if (Mouse.getEventDWheel()>0)
    				{
    					listener.onWheelUp();
    				}
    				if (Mouse.getEventDWheel()<0)
    				{
    					listener.onWheelDown();
    				}
    			}
    		}
    
    	}
    	
    	destroy();
    }
    
 


	public double getZoom()
	{
		return zoom;
	}
	
	public Point getTopLeft()
	{
		return getMousePosition(0,0);
	}
	
	public Point getBottomRight()
	{
		return getMousePosition(width,height);
	}

	public Polygon getBorder()
	{
		return border;
	}
	
	public void write(DrawnText drawnText)
	{
		if (!text.containsKey(drawnText.size))
		{
			text.put(drawnText.size, new ArrayList<DrawnText> ());
		}
		
		text.get(drawnText.size).add(drawnText);
	}
	
	private void drawText()
	{
   		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

		
		List<Integer> sizes = new ArrayList<Integer> ();
		

		sizes.add(24);
		sizes.add(22);
		sizes.add(20);
		sizes.add(18);
		sizes.add(16);
		sizes.add(14);
		sizes.add(12);

		
		
		Collection<Polygon> boxes = new HashSet<Polygon> ();
		
		setColor(1,1,1,1);

		
		for (Integer size : sizes)
		{
			if (this.text.containsKey(size))
			{
				
				Font font = new Font("Sans Serif", Font.BOLD, size);
				TrueTypeFont trueTypeFont = new TrueTypeFont(font, false);
				
				for (DrawnText text : this.text.get(size))
				{
				
					
					double width = trueTypeFont.getWidth(text.text)/zoom;
					double height = trueTypeFont.getHeight()/zoom;
					
					Polygon box = new Polygon();
					
					box.add(new Point(text.centre.x - width/2,text.centre.y - height/2));
					box.add(new Point(text.centre.x + width/2,text.centre.y - height/2));
					box.add(new Point(text.centre.x + width/2,text.centre.y + height/2));
					box.add(new Point(text.centre.x - width/2,text.centre.y + height/2));
					
					if (getBorder().containsPoint(text.centre))
					{
						
					
						if (!overlaps(box,boxes))
						{
							
								try
								{

									trueTypeFont.drawString((float)(text.centre.x),(float)(text.centre.y - height/2),text.text,(float)(1/zoom),(float)(1/zoom), TrueTypeFont.ALIGN_CENTER);
								}
								catch (NullPointerException e)
								{
								}
								boxes.add(box);
							
						}
						
					}
				    	
				}
				
				trueTypeFont.destroy();
			
			}
			
			
		}
		
		text.clear();
		

	}
	
	private boolean overlaps(Polygon polygon, Collection<Polygon> polygons)
	{
		for (Line line : polygon)
		{
			for (Polygon otherPolygon : polygons)
			{
				for (Line otherLine : otherPolygon)
				{
					if (line.getIntersectionInBothSegments(otherLine)!=null)
					{
						return true;
					}
				}
			}
		}
		
		for (Line line : polygon)
		{
			for (Polygon otherPolygon : polygons)
			{
				if (otherPolygon.containsPoint(line.a))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	
}
