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
		int size=3;
		
		for (Demand demand : economy.getDemands())
		{
				
			Node node = demand.getNode();
			
			Polygon box = new Polygon();
			box.add(new Node(node.x-size,node.y - size));
			box.add(new Node(node.x+size,node.y - size));
			box.add(new Node(node.x+size,node.y + size));
			box.add(new Node(node.x-size,node.y + size));

			createPolygon(box, demand.getResource().getR(), demand.getResource().getG(), demand.getResource().getB(), 1f, false);

		}
	}

}
