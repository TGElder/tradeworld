package elder.graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import elder.geometry.Line;
import elder.geometry.Point;
import elder.geometry.Polygon;

public abstract class Layer
{
	
	private List<Drawable> next = Collections.emptyList();
	private List<Drawable> ready = Collections.emptyList();
	
	protected boolean display=true;
	
	protected static Random random = new Random();

	private String name;
	
	private boolean drawing=false;
		
	
	public Layer(String name)
	{
		this.name = name;
	}
	
	public void drawToCanvas(Canvas canvas)
	{
		if (display)
		{
			List<Drawable> current = ready;
						
			for (Drawable drawable : current)
			{
				
				drawable.draw(canvas);
				
			}
			
			
		}
	}
	
	protected void createPolygon(Polygon polygon, float R, float G, float B, float alpha, boolean fill)
	{
		createDrawable(new DrawnPolygon(polygon,R,G,B,alpha,fill));
	}
	
	
	protected void createLine(Line line, float R, float G, float B, float thickness, boolean scaled)
	{
		
		createDrawable(new DrawnLine(line,R,G,B,thickness,scaled));
	}
	
	protected void createPoint(Point point, float R, float G, float B, float size)
	{
		
		createDrawable(new DrawnPoint(point,R,G,B,size));
	}
	
	protected void createText(String text, int size, Point centre)
	{
		createDrawable(new DrawnText(text,size,centre));
	}
	
	protected void createDrawable(Drawable drawable)
	{
		next.add(drawable);
	}
	
	public void enable()
	{
		display=true;
	}
	
	public void disable()
	{
		display=false;
	}
	
	public boolean enabled()
	{
		return display;
	}
	
	public abstract void createDrawables();
	
	public void updateDrawables()
	{
		if (!drawing&&enabled())
		{
			drawing=true;
			
			next = new ArrayList<Drawable> ();
	
			createDrawables();
		
			
			ready=next;
			
			assert(!ready.contains(null));
			
			drawing=false;
		}
		
		
	}
	

	public String toString()
	{
		return name;
	}
	
	
}
