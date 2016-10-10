package elder.graphics;

import elder.geometry.Point;
import elder.geometry.Polygon;

public class DrawnPolygon extends Drawable
{
	Polygon polygon;
	float R;
	float G;
	float B;
	float alpha;
	boolean fill;
	final Point min;
	final Point max;
	
	public DrawnPolygon(Polygon polygon, float R, float G, float B, float alpha, boolean fill)
	{
		this.polygon = polygon;
		this.min = polygon.getMin();
		this.max = polygon.getMax();
		this.R = R;
		this.G = G;
		this.B = B;
		this.alpha = alpha;
		this.fill = fill;
	}

	@Override
	public void draw2(Canvas canvas)
	{
		canvas.setColor(R, G, B, alpha);
		canvas.drawPolygon(polygon, fill);
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
