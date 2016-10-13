package elder;

public class Supply
{
	
	private final Resource resource;
	private final Node node;
	private Demand demand;
	private boolean active;

	Supply(Resource resource, Node node)
	{
		this.resource = resource;
		this.node = node;
		this.active = false;
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

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

}
