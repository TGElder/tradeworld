package elder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import elder.geometry.Point;

public class Node extends Point
{
	
	private final Collection<Edge> edges = new HashSet<Edge> ();
		
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
	
	public Map<Node,Double> getNeighboursWithinDistance(double threshold)
	{
		
		Map<Node,Double> closed = new HashMap<Node,Double> ();
		List<Node> open = new ArrayList<Node> ();
		List<Double> openDistances = new ArrayList<Double> ();
		
		open.add(this);
		openDistances.add(0.0);
		
		while (!open.isEmpty())
		{
			Node focus = open.get(0);
			double focusDistance = openDistances.get(0);
			
			open.remove(0);
			openDistances.remove(0);
			
			closed.put(focus,focusDistance);
			
			for (Edge edge : focus.getEdges())
			{
				Node to = edge.getTo();
				double toDistance = edge.length;
				
				if ((focusDistance+toDistance)<=threshold&&!closed.containsKey(to))
				{
					int openIndex = open.indexOf(to);
					
					if (openIndex!=-1)
					{
						
						double currentDistance = openDistances.get(openIndex);
						
						if (focusDistance + toDistance < currentDistance)
						{
							openDistances.set(openIndex, focusDistance + toDistance);
						}
					}
					else
					{
						open.add(to);
						openDistances.add(focusDistance + toDistance);
					}
				}
			}
		}
		
		return closed;
		
	}
	
	public boolean containsEdgeTo(Node to)
	{
		for (Edge edge : edges)
		{
			if (edge.getTo()==to)
			{
				return true;
			}
		}
		
		return false;
	}
	
	

	
}
