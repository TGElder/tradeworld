package elder;
import java.util.HashMap;
import java.util.Map;

import elder.geometry.Polygon;
import elder.graphics.Colour;
import elder.graphics.Layer;

public class OwnershipLayer extends Layer
{

	private Economy economy;
	private Map<Settlement,Colour> colours = new HashMap<Settlement,Colour> ();
	
	public OwnershipLayer(Economy economy)
	{
		super("Ownership");
		this.economy = economy;
	}
	
	@Override
	public void createDrawables()
	{
		for (Settlement settlement : economy.getSettlements())
		{
			if (!colours.containsKey(settlement))
			{
				colours.put(settlement, new Colour());
			}
		}
		
		double size=0.5;
		
		for (Map.Entry<Settlement, Colour> settlement : colours.entrySet())
		{			
					
			for (Node node : settlement.getKey().getLimits())
			{
				Polygon box = new Polygon();
				box.add(new Node(node.x-size,node.y - size));
				box.add(new Node(node.x+size,node.y - size));
				box.add(new Node(node.x+size,node.y + size));
				box.add(new Node(node.x-size,node.y + size));

				Colour colour = settlement.getValue();
				
				createPolygon(box, colour.R, colour.G, colour.B, 0.5f, true);
			}
			
		}
	}

}
