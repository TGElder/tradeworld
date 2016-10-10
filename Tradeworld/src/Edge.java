import elder.geometry.Line;

public class Edge extends Line
{
	

	Edge(Node from, Node to)
	{
		super(from,to);
	}

	public Node getFrom()
	{
		return (Node)a;
	}

	public Node getTo()
	{
		return (Node)b;
	}

}
