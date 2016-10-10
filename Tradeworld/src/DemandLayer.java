import elder.geometry.Polygon;
import elder.graphics.Layer;

public class DemandLayer extends Layer
{

	private Network network;
	
	public DemandLayer(Network network)
	{
		super("Demand");
		this.network = network;
	}
	
	@Override
	public void createDrawables()
	{
		int size=3;
		
		for (Node node : network.getNodes())
		{
			if (!node.getDemand().isEmpty())
			{				
				Polygon box = new Polygon();
				box.add(new Node(node.x-size,node.y - size));
				box.add(new Node(node.x+size,node.y - size));
				box.add(new Node(node.x+size,node.y + size));
				box.add(new Node(node.x-size,node.y + size));
	
				createPolygon(box, 1f, 0, 0f, 1f, true);
				
			}
		}
	}

}
