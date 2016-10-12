import java.util.Collection;
import java.util.HashSet;

public class Settlement
{
	
	private final Collection<Citizen> citizens = new HashSet<Citizen> ();
	private final Node node;
	private final Collection<Node> limits;
	private int wealth=0;
	
	public Settlement(Node node, Collection<Node> limits)
	{
		this.node = node;
		this.limits = limits;
	}

	public Collection<Citizen> getCitizens()
	{
		return citizens;
	}

	public Node getNode()
	{
		return node;
	}

	public Collection<Node> getLimits()
	{
		return limits;
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

	

}
