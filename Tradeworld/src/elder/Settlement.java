package elder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Settlement
{
	
	private final List<Citizen> citizens = new ArrayList<Citizen> ();
	private final Node node;
	private final Collection<Node> limits;
	private int wealth=0;
	private final Map<Settlement,Double> influence = new HashMap<Settlement,Double> ();
	private Settlement owner;
	
	public Settlement(Node node, Collection<Node> limits)
	{
		this.node = node;
		this.limits = limits;
		owner = this;
	}

	public List<Citizen> getCitizens()
	{
		return citizens;
	}

	public Node getNode()
	{
		return node;
	}

	public Collection<Node> getLimits()
	{
		return limits;
	}
	
	public int getWealth()
	{
		return wealth;
	}
	
	public void addWealth(int wealth)
	{
		this.wealth += wealth;
	}

	public void setWealth(int wealth)
	{
		this.wealth = wealth;
	}
	
	void addInfluence(Settlement settlement, Double amount)
	{
		Double current = influence.get(settlement);	
		
		if (current==null)
		{
			influence.put(settlement, amount);
		}
		else
		{
			influence.put(settlement,current+amount);
		}
	}
	
	public void computeOwner()
	{
		Settlement owner = null;
		Double out = null;
		
		for (Map.Entry<Settlement,Double> settlement : influence.entrySet())
		{
			if (owner==null||out<settlement.getValue())
			{
				owner=settlement.getKey();
				out=settlement.getValue();
			}
		}
		
		this.owner = owner;
	}
	
	public Settlement getOwner()
	{
		return owner;
	}
	
	public Settlement getTopOwner()
	{
		if (owner==this)
		{
			return this;
		}
		else
		{
			return owner.getTopOwner();
		}
	}
	
	public double getInfluenceAt(Node node, Network network)
	{
		int from = ((IndexNode)getNode()).getIndex();
		int to = ((IndexNode)node).getIndex();
		
				
		double distance = network.getDijkstra().getDistances()[from][to];
		
		if (distance==Double.POSITIVE_INFINITY)
		{
			return Double.NEGATIVE_INFINITY;
		}
		else
		{
			return wealth/Math.sqrt((distance+1));
		}
	}

	

}
