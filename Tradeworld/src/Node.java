import java.util.Collection;
import java.util.HashSet;

import elder.geometry.Point;

public class Node extends Point
{
	
	public final Collection<Edge> edges = new HashSet<Edge> (); 
	public final Collection<Demand> demand = new HashSet<Demand> ();
		
	public Node(double x, double y)
	{
		super(x,y);
	}
	
	Collection<Edge> getEdges()
	{
		return edges;
	}
	
	public void addEdge(Edge fromTo)
	{
		edges.add(fromTo);
	}
	
	public Collection<Demand>  getDemand()
	{
		return demand;
	}
	
	public void addDemand(Demand demand)
	{
		this.demand.add(demand);
	}
	
}
