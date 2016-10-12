import elder.geometry.Polygon;
import elder.graphics.Layer;

public class SupplyLayer extends Layer
{

	private Economy economy;
	
	public SupplyLayer(Economy economy)
	{
		super("Supply");
		this.economy = economy;
	}
	
	@Override
	public void createDrawables()
	{
		int size=3;
		
		for (Supply supply : economy.getSupplys())
		{
			Node node = supply.getNode();
			
			Polygon box = new Polygon();
			box.add(new Node(node.x-size,node.y - size));
			box.add(new Node(node.x+size,node.y - size));
			box.add(new Node(node.x+size,node.y + size));
			box.add(new Node(node.x-size,node.y + size));

			createPolygon(box, supply.getResource().getR(), supply.getResource().getG(), supply.getResource().getB(), 1f, true);
		}
	}

}
