package elder;
import java.util.HashMap;
import java.util.Map;

import elder.geometry.Polygon;
import elder.graphics.Colour;
import elder.graphics.Layer;

public class CountryLayer extends Layer
{

	private Economy economy;
	private OwnershipLayer ownershipLayer;
	
	public CountryLayer(Economy economy, OwnershipLayer ownershipLayer)
	{
		super("Countries");
		this.economy = economy;
		this.ownershipLayer = ownershipLayer;
	}
	
	@Override
	public void createDrawables()
	{
		
		for (Settlement settlement : economy.getSettlements())
		{
			if (!ownershipLayer.getColours().containsKey(settlement))
			{
				ownershipLayer.getColours().put(settlement, new Colour());
			}
		}
		
		double size=0.5;
		
		for (Settlement settlement : economy.getSettlements())
		{
			Colour colour = ownershipLayer.getColours().get(settlement.getTopOwner());
			
			for (Node node : settlement.getLimits())
			{
				Polygon box = new Polygon();
				box.add(new Node(node.x-size,node.y - size));
				box.add(new Node(node.x+size,node.y - size));
				box.add(new Node(node.x+size,node.y + size));
				box.add(new Node(node.x-size,node.y + size));
					
				createPolygon(box, colour.R, colour.G, colour.B, 0.5f, true);
			}
		}
		
		
//		for (Country country : economy.getCountries())
//		{		
//			Colour colour = ownershipLayer.getColours().get(country.getCapital());
//						
//			
//			for (Settlement settlement : country.getSettlements())
//			{
//			
//				for (Node node : settlement.getLimits())
//				{
//					Polygon box = new Polygon();
//					box.add(new Node(node.x-size,node.y - size));
//					box.add(new Node(node.x+size,node.y - size));
//					box.add(new Node(node.x+size,node.y + size));
//					box.add(new Node(node.x-size,node.y + size));
//						
//					createPolygon(box, colour.R, colour.G, colour.B, 0.5f, true);
//				}
//								
//			}
//			
//		}
	}
	
}


