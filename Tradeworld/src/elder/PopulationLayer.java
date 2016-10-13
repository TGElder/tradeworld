package elder;
import elder.geometry.Polygon;
import elder.graphics.Layer;

public class PopulationLayer extends Layer
{

	private Economy economy;
	
	public PopulationLayer(Economy economy)
	{
		super("Population");
		this.economy = economy;
	}
	
	@Override
	public void createDrawables()
	{
		double size=0.3;
		
		double maxPopulation = 0;
		
		for (Settlement settlement : economy.getSettlements())
		{
			
			maxPopulation = Math.max(maxPopulation, settlement.getCitizens().size());
		}
		
		
		
		for (Settlement settlement : economy.getSettlements())
		{			
			Node node = settlement.getNode();
			
			Polygon box = new Polygon();
			box.add(new Node(node.x-size,node.y - size));
			box.add(new Node(node.x+size,node.y - size));
			box.add(new Node(node.x+size,node.y + size));
			box.add(new Node(node.x-size,node.y + size));

			
			float colour = (float)(Math.log(settlement.getCitizens().size())/Math.log(maxPopulation*1f));
			
			
			
			createPolygon(box, 1-(colour/2f),1-(colour/2f) ,1-colour,1f, true);

			

			
		}
	}

}
