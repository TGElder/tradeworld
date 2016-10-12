import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public class Network
{
	
	private final Collection<Node> nodes;

	Network(Collection<Node> nodes)
	{
		this.nodes = nodes;
	}
	
	Collection<Node> getNodes()
	{
		return nodes;
	}
	
	public static Network generateRandomNetwork(int width, int height, Random random, double edgeChance)
	{		
		Collection<Node> nodes = new HashSet<Node> ();
		
		Node [][] nodeMatrix = new Node[width][width];
		int nx[] = {-1,0,1,0,0};
		int ny[] = {0,1,0,-1,0};
		
		for (int x=0; x<width; x+=1)
		{
			for (int y=0; y<height; y+=1)
			{
				Node node = new Node(100 + x*(800/width),100 + y*(800/width));
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
						if (random.nextDouble()<=edgeChance)
						{
							Node from = nodeMatrix[x][y];
							Node to = nodeMatrix[dx][dy];
							
							Edge fromTo = new Edge(from,to);
							from.addEdge(fromTo);
							Edge toFrom = new Edge(to,from);
							to.addEdge(toFrom);

						}
					}
					
				}
			}
		}
		
		return new Network(nodes);
	}

}
