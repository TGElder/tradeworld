package elder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Network
{
	
	private final List<IndexNode> nodes;

	private final Dijkstra dijkstra;
	
	Network(List<IndexNode> nodes)
	{
		this.nodes = nodes;
		dijkstra = new Dijkstra(nodes);
		dijkstra.compute();
		
		System.out.println(dijkstra.getPath(nodes.get(200), nodes.get(150)));
	}
	
	Collection<IndexNode> getNodes()
	{
		return nodes;
	}
	
	public static Network generateRandomNetwork(int width, int height, Random random, double edgeChance)
	{		
		List<IndexNode> nodes = new ArrayList<IndexNode> ();
		
		IndexNode [][] nodeMatrix = new IndexNode[width][width];
		int nx[] = {-1,0,1,0,0};
		int ny[] = {0,1,0,-1,0};
		
		for (int y=0; y<height; y++)
		{
			for (int x=0; x<width; x++)
			{
				IndexNode node = new IndexNode(x,y,(y*width)+x);
				nodes.add(node);
				nodeMatrix[x][y] = node;
			}	
		}
		
		for (int x=0; x<width; x++)
		{
			for (int y=0; y<height; y++)
			{
				for (int n = 0; n<5; n++)
				{
					
					int dx = x + nx[n];
					int dy = y + ny[n];
					
					if (dx>=0 && dx<width && dy>=0 && dy<width)
					{
						IndexNode from = nodeMatrix[x][y];
						IndexNode to = nodeMatrix[dx][dy];
						
						if (from.getIndex()<=to.getIndex())
						{
						
							if (random.nextDouble()<=edgeChance)
							{
			
								assert(!to.containsEdgeTo(from));
								assert(!from.containsEdgeTo(to));
								
								Edge fromTo = new Edge(from,to);
								from.addEdge(fromTo);
								Edge toFrom = new Edge(to,from);
								to.addEdge(toFrom);
								
								fromTo.setReverse(toFrom);
								toFrom.setReverse(fromTo);
	
							}
							
						}
							
						
					}
					
				}
			}
		}
		
		return new Network(nodes);
	}
	
	public Dijkstra getDijkstra()
	{
		return dijkstra;
	}

}
