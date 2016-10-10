import java.util.ArrayList;
import java.util.List;

public class Factory
{
	
	private final Node location;
	private final Element element;
	private final Atom factoryAtom;
	private int level=0;
	private final List<Atom> products = new ArrayList<Atom> ();
	
	public Factory(Node location, Element element, Atom factoryElement)
	{
		this.location = location;
		this.element = element;
		this.factoryAtom = factoryElement;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public void increaseLevel()
	{
		
		level ++;
		products.add(new Atom(location,element));
		
		if (level==2)
		{
			location.getAtoms().remove(factoryAtom);
		}
	
		
	}

	public Node getLocation()
	{
		return location;
	}
	
	public void run()
	{
		for (int p=0; p<products.size(); p++)
		{
			if (products.get(p)==null)
			{
				products.set(p, new Atom(location,element));
			}
		}
	}
	
	public void removeProduct(Atom product)
	{
		int index = products.indexOf(product);
		assert(index!=-1);
		
		products.set(index, null);
	
	}
	
	public Atom getLastProduct()
	{
		return products.get(products.size()-1);
	}
	
	

}
