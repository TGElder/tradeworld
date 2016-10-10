import java.util.ArrayList;
import java.util.List;

public class City

{
	
	private int growthCounter=0;
	private Node location;
	private final Element element;
	
	private final List<Atom> demands = new ArrayList<Atom> ();
	
	City(Node location, Element element)
	{
		this.location = location;
		this.element = element;
	}
	
	public void grow()
	{
		demands.add(null);
	}
	
	
	public void run()
	{
		for (int p=0; p<demands.size(); p++)
		{
			if (demands.get(p)==null)
			{
				demands.set(p, new Atom(location,element));
			}
		}
	}
	
	public void satisfyDemand(Atom demand)
	{
		int index = demands.indexOf(demand);
		assert(index!=-1);
		
		
		
		demands.set(index, null);
		
//		growthCounter++;
//		
//		if (growthCounter==1000)
//		{
//			grow();
//			growthCounter=0;
//		}
	
	}

	
	
	


}
