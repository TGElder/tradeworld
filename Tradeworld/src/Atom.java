import java.util.ArrayList;
import java.util.Comparator;

public class Atom
{
	
	private Node origin;
	private final ArrayList<Edge> trace = new ArrayList<Edge> ();
	
	private final Element type;
	
	private int reward=0;
		
	Atom(Node origin, Element type)
	{
		this.origin = origin;
		this.type = type;
		origin.getAtoms().add(this);
	}


	public Element getType()
	{
		return type;
	}
	
	public Node getOrigin()
	{
		return origin;
	}

	public ArrayList<Edge> getTrace()
	{
		return trace;
	}
	
	public Edge getLastMovement()
	{
		return trace.get(trace.size()-1);
	}

	public int getReward()
	{
		return reward;
	}

	public void addReward(int delta)
	{
		this.reward += delta;
	}
	
	public void setReward(int reward)
	{
		this.reward = reward;
	}

}
