package elder;
import elder.geometry.Polygon;
import elder.graphics.Layer;

public class SettlementLayer extends Layer
{

	private Economy economy;
	
	public SettlementLayer(Economy economy)
	{
		super("Settlements");
		this.economy = economy;
	}
	
	@Override
	public void createDrawables()
	{
		double size=0.5;
		
		for (Settlement settlement : economy.getSettlements())
		{			
			Node node = settlement.getNode();
			
			Polygon box = new Polygon();
			box.add(new Node(node.x-size,node.y - size));
			box.add(new Node(node.x+size,node.y - size));
			box.add(new Node(node.x+size,node.y + size));
			box.add(new Node(node.x-size,node.y + size));

			createPolygon(box, 0,0,0, 1f, false);
		}
	}

}
