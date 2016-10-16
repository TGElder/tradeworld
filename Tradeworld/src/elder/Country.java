package elder;

import java.util.Collection;
import java.util.HashSet;

public class Country
{
	
	private Network network;
	private Settlement capital;
	private final Collection<Settlement> settlements = new HashSet<Settlement> ();
	private int wealth=0;
	
	Country(Network network, Settlement capital)
	{
		this.network = network;
		this.capital = capital;
	}

	public Collection<Settlement> getSettlements()
	{
		return settlements;
	}
	
	public void computeWealth()
	{		
		wealth = capital.getWealth();
	}
	
	public double getWealthAt(Node node)
	{
		int from = ((IndexNode)capital.getNode()).getIndex();
		int to = ((IndexNode)node).getIndex();
		
				
		double distance = network.getDijkstra().getDistances()[from][to];
		
		if (distance==Double.POSITIVE_INFINITY)
		{
			return Double.NEGATIVE_INFINITY;
		}
		else
		{
			return wealth/(distance+1);
		}
	}

	public Settlement getCapital()
	{
		return capital;
	}
	
	

}
