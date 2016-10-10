import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Edge
{
	
	private final Node from;
	private final Node to;
	
	private final Map<Element,Double> scores = new HashMap<Element,Double> ();
	private final Map<Element,Integer> visits = new HashMap<Element,Integer> ();
	public final Collection<Atom> atoms = new HashSet<Atom> ();
	
	
	Edge(Node from, Node to)
	{
		this.from = from;
		this.to = to;
	}

	public Node getFrom()
	{
		return from;
	}

	public Node getTo()
	{
		return to;
	}
	
	public Double getScore(Element type)
	{
		if (!scores.containsKey(type))
		{
			return new Double(0);
		}
		else
		{
			return scores.get(type);
		}
	}
	
	public void score(Element type, double score)
	{
		
		
		if (!scores.containsKey(type))
		{
			visits.put(type, 1);
			scores.put(type, score);
		}
		else
		{			
			double oldScore = scores.get(type);
			int oldVisits = visits.get(type);
			double newScore = ((oldScore*oldVisits)+score)/(oldVisits+1);
			
			visits.put(type, oldVisits+1);
			scores.put(type, newScore);

		}
		
		scores.put(type,score);
	}
	
	public void learn(Element type, double reward, double learningRate)
	{
		double discountFactor=0.99;
		
		
		
		double oldValue = getScore(type);
		
		double r;
		
		
				
		double newValue = oldValue + learningRate*(reward + discountFactor*getTo().getBestScore(type) - oldValue);
		
		scores.put(type, newValue);
		
	}
	
	
	
	Collection<Atom> getAtoms()
	{
		return atoms;
	}


}
