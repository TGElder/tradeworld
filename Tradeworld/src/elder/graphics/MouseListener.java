package elder.graphics;

import elder.geometry.Point;

public interface MouseListener
{
	
	public void onMove(Point cityPoint);
	
	public void onLeftClick(Point cityPoint);
	
	public void onMiddleClick(Point cityPoint);
	
	public void onRightClick(Point cityPoint);
	
	public void onLeftDrag(Point screenOffset);
	
	public void onMiddleDrag(Point screenOffset);
	
	public void onRightDrag(Point screenOffset);
	
	public void onWheelUp();
	
	public void onWheelDown();
	

}
