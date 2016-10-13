package elder;
import elder.geometry.Line;

public class Edge extends Line
{
	
	private Edge reverse;
	
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
	
	public void setReverse(Edge reverse)
	{
		this.reverse = reverse;
	}

	public Edge getReverse()
	{
		return reverse;
	}

}
