import java.util.Collection;
import java.util.HashSet;

class Flow
{
	
	private Demand demand;
	private Source source;
	private Collection<Node> closed = new HashSet<Node> ();
	private Collection<Node> front = new HashSet<Node> ();

	
	Flow(Source source)
	{
		this.source = source;
		front.add(source.getNode());
	}
	
	boolean step()
	{
		if (demand!=null||front.isEmpty())
		{
			return false;
		}
		else
		{
			closed.addAll(front);
			
			Collection<Node> newFront = new HashSet<Node> ();
			
			for (Node node : front)
			{
				for (Edge edge : node.getEdges())
				{
					if (!closed.contains(edge.getTo()))
					{
						newFront.add(edge.getTo());
					}
				}
			}
			
			front = newFront;
		
			return true;
		}
	
		
	}
	
	void check()
	{
		for (Node node : front)
		{
			for (Demand demand : node.getDemand())
			{
				if (demand.getSource()==null)
				{
					demand.setSource(source);
					this.setDemand(demand);
					return;
				}
			}
		}
	}

	public Demand getDemand()
	{
		return demand;
	}

	public void setDemand(Demand demand)
	{
		this.demand = demand;
	}
}
