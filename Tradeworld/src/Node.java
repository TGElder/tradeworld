import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import elder.geometry.Point;

public class Node extends Point
{
	
	private final Collection<Edge> edges = new HashSet<Edge> ();
	
	private final List<Citizen> citizens = new ArrayList<Citizen> ();
	
	private int wealth=0;
		
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

	public List<Citizen> getCitizens()
	{
		return citizens;
	}

	
}
