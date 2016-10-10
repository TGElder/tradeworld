import elder.geometry.Point;
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
		int atomSize = 4;
		
		int maxWealth = 0;
		
		for (Node node : economy.getNodes())
		{
			
			maxWealth = Math.max(node.getWealth(), maxWealth);
			
		}
		
		if (maxWealth>0)
		{
		
			for (Node node : economy.getNodes())
			{
				
				maxWealth = Math.max(node.getWealth(), maxWealth);
				
					
				Polygon box = new Polygon();
				box.add(new Node(node.x-atomSize,node.y - atomSize));
				box.add(new Node(node.x+atomSize,node.y - atomSize));
				box.add(new Node(node.x+atomSize,node.y + atomSize));
				box.add(new Node(node.x-atomSize,node.y + atomSize));
				
				float colour = (float)(Math.log(node.getWealth())/Math.log(maxWealth*1f));
	
				createPolygon(box, 1-(colour/2f),1-(colour/2f) ,1-colour,1f, true);
				
			}
			
		}
	}

}
