package elder.graphics;


import org.lwjgl.input.Mouse;

import elder.geometry.Point;

public class Navigator implements MouseListener
{

	private final Canvas canvas;
	
	public Navigator(Canvas canvas)
	{
		this.canvas = canvas;
		canvas.addMouseListener(this);
	}
	
	@Override
	public void onMove(Point point)
	{
		
	}

	@Override
	public void onLeftClick(Point point)
	{
		
	}

	@Override
	public void onMiddleClick(Point point)
	{
		
	}

	@Override
	public void onRightClick(Point point)
	{
		
	}

	@Override
	public void onLeftDrag(Point offset)
	{
		canvas.translate(offset.x/canvas.getZoom(),offset.y/canvas.getZoom());
	}

	@Override
	public void onMiddleDrag(Point offset)
	{
		
	}

	@Override
	public void onRightDrag(Point offset)
	{
		
	}

	@Override
	public void onWheelUp()
	{
		canvas.adjustZoom(2,Mouse.getEventX(),Mouse.getEventY());
	}

	@Override
	public void onWheelDown()
	{
		canvas.adjustZoom(0.5,Mouse.getEventX(),Mouse.getEventY());
	}

}
