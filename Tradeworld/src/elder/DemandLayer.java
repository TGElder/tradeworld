package elder;
import elder.geometry.Polygon;
import elder.graphics.Layer;

public class DemandLayer extends Layer
{

	private Economy economy;
	
	public DemandLayer(Economy economy)
	{
		super("Demand");
		this.economy = economy;
	}
	
	@Override
	public void createDrawables()
	{
		double size=0.3;
		
		for (Node node : economy.getNetwork().getNodes())
		{
			for (Demand demand : node.getDemand())
			{
								
				Polygon box = new Polygon();
				box.add(new Node(node.x-size,node.y - size));
				box.add(new Node(node.x+size,node.y - size));
				box.add(new Node(node.x+size,node.y + size));
				box.add(new Node(node.x-size,node.y + size));
	
				createPolygon(box, demand.getResource().getR(), demand.getResource().getG(), demand.getResource().getB(), 1f, false);
			}

		}
	}

}
