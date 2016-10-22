package elder;
import java.util.HashMap;
import java.util.Map;

import elder.geometry.Line;
import elder.graphics.Layer;

public class TradeLayer extends Layer
{

	private Economy economy;
	
	public TradeLayer(Economy economy)
	{
		super("Trade");
		this.economy = economy;
	}
	
	@Override
	public void createDrawables()
	{
		
		Map<Edge,Integer> traffic = new HashMap<Edge,Integer> ();
		
		int maxTraffic=0;
		
		for (Node node : economy.getNetwork().getNodes())
		{
			for (Demand demand : node.getDemand())
			{
				if (demand.getSupply()!=null)
				{
					IndexNode from = (IndexNode)demand.getNode();
					IndexNode to = (IndexNode)demand.getSupply().getNode();
					
					for (Edge edge : economy.getNetwork().getDijkstra().getPath(from, to))
					{
						Integer edgeTraffic = traffic.get(edge);
						
						if (edgeTraffic==null)
						{
							edgeTraffic = 0;
						}
						
						traffic.put(edge, edgeTraffic+1);
	
															
						maxTraffic = Math.max(maxTraffic, edgeTraffic);
	
						
					}
					
				}
			}
		}
		
		for (Map.Entry<Edge, Integer> edge : traffic.entrySet())
		{
			createLine(edge.getKey(),0f,0f,0f,(float)((Math.log(edge.getValue())/Math.log(maxTraffic))*0.5),true);
		}
		
	}
	
	

}
