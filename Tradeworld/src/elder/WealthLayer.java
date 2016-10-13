package elder;
import elder.geometry.Polygon;
import elder.graphics.Layer;

public class WealthLayer extends Layer
{

	private Economy economy;
	
	public WealthLayer(Economy economy)
	{
		super("Wealth");
		this.economy = economy;
	}
	
	@Override
	public void createDrawables()
	{
		double size=0.5;
		
		double maxAbsWealth = 0;
		
		for (Settlement settlement : economy.getSettlements())
		{
			
			
			maxAbsWealth = Math.max(maxAbsWealth, Math.abs(settlement.getWealth()));
			

			
			
		}
		
		if (maxAbsWealth>0)
		{
		
		
			for (Settlement settlement : economy.getSettlements())
			{			
				Node node = settlement.getNode();
				
				Polygon box = new Polygon();
				box.add(new Node(node.x-size,node.y - size));
				box.add(new Node(node.x+size,node.y - size));
				box.add(new Node(node.x+size,node.y + size));
				box.add(new Node(node.x-size,node.y + size));
	
				
				float colour = (float)(Math.log(Math.abs(settlement.getWealth()))/Math.log(maxAbsWealth*1f));
				
				if (settlement.getWealth()<0)
				{
					createPolygon(box, 1-(colour/2f),1-colour ,1-colour,1f, true);
	
				}
				else
				{
					createPolygon(box, 1-(colour/2f),1-(colour/2f) ,1-colour,1f, true);
	
				}
	
				
			}
			
		}
	}

}
