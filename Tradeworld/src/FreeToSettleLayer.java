import elder.geometry.Polygon;
import elder.graphics.Layer;

public class FreeToSettleLayer extends Layer
{

	private Economy economy;
	
	public FreeToSettleLayer(Economy economy)
	{
		super("Free To Settle");
		this.economy = economy;
	}
	
	@Override
	public void createDrawables()
	{
		double size=0.3;
		
		for (Node node : economy.getFreeToSettle())
		{			
			Polygon box = new Polygon();
			box.add(new Node(node.x-size,node.y - size));
			box.add(new Node(node.x+size,node.y - size));
			box.add(new Node(node.x+size,node.y + size));
			box.add(new Node(node.x-size,node.y + size));

			createPolygon(box, 0,1,0, 1f, true);
		}
	}

}
