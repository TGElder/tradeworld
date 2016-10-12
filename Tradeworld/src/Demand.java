
public class Demand
{
	
	private final Resource resource;
	private Supply supply;
	private final Node node;
	
	Demand(Resource resource, Node node)
	{
		this.resource = resource;
		this.node = node;
	}

	public Node getNode()
	{
		return node;
	}
	
	public Supply getSupply()
	{
		return supply;
	}

	public void setSupply(Supply supply)
	{
		this.supply = supply;
	}

	public Resource getResource()
	{
		return resource;
	}

}
