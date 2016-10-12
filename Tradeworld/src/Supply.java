
public class Supply
{
	
	private final Resource resource;
	private final Node node;
	private Demand demand;

	Supply(Resource resource, Node node)
	{
		this.resource = resource;
		this.node = node;
	}

	public Node getNode()
	{
		return node;
	}

	public Demand getDemand()
	{
		return demand;
	}

	public void setDemand(Demand demand)
	{
		this.demand = demand;
	}

	public Resource getResource()
	{
		return resource;
	}

}
