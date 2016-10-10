import java.util.Collection;
import java.util.HashSet;

import elder.geometry.Point;

public class Node extends Point
{
	
	public final Collection<Edge> edges = new HashSet<Edge> (); 
	public final Collection<Atom> atoms = new HashSet<Atom> ();
	
	private int wealth=0;
	
	public Node(double x, double y)
	{
		super(x,y);
	}
	
	Collection<Edge> getEdges()
	{
		return edges;
	}
	
	Collection<Atom> getAtoms()
	{
		return atoms;
	}

	public void addEdge(Edge fromTo)
	{
		edges.add(fromTo);
	}
	
	
	
	public Double getBestScore(Element type)
	{
		Double best = Double.NEGATIVE_INFINITY;
		
		for (Edge edge : getEdges())
		{
			best = Math.max(best,edge.getScore(type));
		}
		
		return best;
	}
	
	public void addWealth(int add)
	{
		wealth += add;
	}
	
	public int getWealth()
	{
		return wealth;
	}

}
