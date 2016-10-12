
public class Citizen
{
	
	private Node node;	
	private Node home;

	public Citizen(Node node)
	{
		this.node = node;
		this.home = node;
	}
	
	public Node getNode()
	{
		return node;
	}
	
	public void setNode(Node node)
	{
		this.node = node;
	}

	public Node getHome()
	{
		return home;
	}

	public void setHome(Node home)
	{
		this.home = home;
	}
	
}
