package elder.graphics;

import elder.geometry.Line;
import elder.geometry.Point;

public class DrawnLine extends Drawable
{
	final Line line;
	final float R;
	final float G;
	final float B;
	final float thickness;
	final boolean scaled;
	final Point min;
	final Point max;
	
	public DrawnLine(Line line, float R, float G, float B, float thickness, boolean scaled)
	{
		this.line = line;
		min = line.getMin();
		max = line.getMax();
		this.R = R;
		this.G = G;
		this.B = B;
		this.thickness = thickness;
		this.scaled = scaled;
	}

	@Override
	public void draw2(Canvas canvas)
	{
		
			canvas.setColor(R, G, B, 1);
			canvas.drawLine(line, thickness, scaled);
		
		
		
	}

	@Override
	public Point getMin()
	{
		return min;
	}

	@Override
	public Point getMax()
	{
		return max;
	}
}
