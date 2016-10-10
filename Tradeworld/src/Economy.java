import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Economy
{
	
	private final Collection<Node> nodes = new HashSet<Node> ();
	private final List<Atom> atoms = new ArrayList<Atom> ();
	private final List<Integer> lastJournies = new ArrayList<Integer> ();
		
	private final Random random = new Random();
	
	private int step=0;
	
	private Collection<Atom> rewarded = new HashSet<Atom> ();
	
	private final Element demand = new Element(1,0,0);
	private final Element supply = new Element(0,0,1);
	private final Element green = new Element(0,1,0);
	private final Element yellow = new Element(1,1,0);
	
	private final Map<Node,Factory> factories = new HashMap<Node,Factory> ();
	private final Element factoryElement = new Element(0,1,0);
		
	private final Map<Node,City> cities = new HashMap<Node,City> ();

	
	private final List<Element> elements = new ArrayList<Element> ();
	
	private final List<TransactionType> transactionTypes = new ArrayList<TransactionType>();
	
	public Economy()
	{
		elements.add(demand);
		elements.add(supply);
		

		
		
		List<Element> requirements = new ArrayList<Element> ();
		requirements.add(demand);
		requirements.add(factoryElement);
		
		transactionTypes.add(new TransactionType(requirements)
		{

			@Override
			public void onTransact(Transaction transaction)
			{
				Atom demand = transaction.getAtoms()[0];
				
				Node node = transaction.getNode();
	
				factories.get(node).increaseLevel();
				
				
				
				Atom supply = factories.get(node).getLastProduct();

				node.getAtoms().remove(demand);
				node.getAtoms().remove(supply);
								
				factories.get(supply.getOrigin()).removeProduct(supply);
				cities.get(demand.getOrigin()).satisfyDemand(demand);

				
				node.addWealth(4);
				supply.getOrigin().addWealth(7);
				
				addReward(demand,100);
				addReward(supply,100);
				
				if (lastJournies.size()==1000)
				{
					lastJournies.remove(0);
					
				}
				
				lastJournies.add(demand.getTrace().size());
				
				if (lastJournies.size()==1000)
				{
					lastJournies.remove(0);
					
				}
				
				lastJournies.add(supply.getTrace().size());
			}
		});
		
		
		
		requirements = new ArrayList<Element> ();
		requirements.add(demand);
		requirements.add(supply);
		
		transactionTypes.add(new TransactionType(requirements)
		{

			@Override
			public void onTransact(Transaction transaction)
			{
				Atom demand = transaction.getAtoms()[0];
				Atom supply = transaction.getAtoms()[1];
				
				Node node = transaction.getNode();
				
				node.getAtoms().remove(demand);
				node.getAtoms().remove(supply);
				
				
				factories.get(supply.getOrigin()).removeProduct(supply);
				cities.get(demand.getOrigin()).satisfyDemand(demand);
				
				node.addWealth(4);
				supply.getOrigin().addWealth(7);
				
				addReward(demand,100);
				addReward(supply,100);
				
				if (lastJournies.size()==1000)
				{
					lastJournies.remove(0);
					
				}
				
				lastJournies.add(demand.getTrace().size());
				
				if (lastJournies.size()==1000)
				{
					lastJournies.remove(0);
					
				}
				
				lastJournies.add(supply.getTrace().size());
				
			}
		});
		
		//elements.add(green);
		//green.getPreference().add(blue);
		//green.getPreference().add(yellow);
		//elements.add(yellow);

		
		//blue.getPreference().add(red);
		
				
		
	}
	
	public Element getDemand()
	{
		return demand;
	}
	
	public Element getSupply()
	{
		return supply;
	}
	
	public Element getGreen()
	{
		return green;
	}
	
	public Element getYellow()
	{
		return yellow;
	}
	
	List<Atom> getAtoms()
	{
		return atoms;
	}

	Collection<Node> getNodes()
	{
		return nodes;
	}
	
	
	public void moveOffEdges(Element type)
	{
		Map<Atom,Edge> movements = new HashMap<Atom,Edge> ();
		
		for (Node node : nodes)
		{
			for (Edge edge : node.getEdges())
			{
				for (Atom atom : edge.getAtoms())
				{
					if (atom.getType().equals(type))
					{
						movements.put(atom, edge);
					}
				}
			}
		}
		
		
		for (Map.Entry<Atom, Edge> movement : movements.entrySet())
		{
			Atom atom = movement.getKey();
			Edge edge = movement.getValue();
			
			edge.getAtoms().remove(atom);
			edge.getTo().getAtoms().add(atom);
			
			atom.getTrace().add(edge);
			
//			if (edge.getFrom()!=edge.getTo())
//			{
				addReward(atom,-1);
//			}
			
			//edge.learn(atom.getType(),-1, learningRate);
			
			//this.movements.put(atom, edge);
		}
		
	}
	
	public void moveOntoEdges(Element type, double explorationRate)
	{
		Map<Atom,Edge> movements = new HashMap<Atom,Edge> ();
		
		movements.clear();
		
		
		
		for (Node node : nodes)
		{
			for (Atom atom : node.getAtoms())
			{
				if (atom.getType().equals(type))
				{
					List<Edge> candidates = new ArrayList<> ();
					
					if (random.nextDouble()>explorationRate)
					{
				
						Double topScore = Double.NEGATIVE_INFINITY;
						
						for (Edge edge : node.getEdges())
						{
							topScore = Math.max(edge.getScore(atom.getType()), topScore);
						}
						
						
						for (Edge edge : node.getEdges())
						{
							if (topScore.equals(edge.getScore(atom.getType())))
							{
								candidates.add(edge);
							}
						}
		
					}
					else
					{
						candidates.addAll(node.getEdges());
					}
						
					if (candidates.size()>0)
					{
						Collections.shuffle(candidates);
	
						movements.put(atom, candidates.get(0));
					}
						
					
					
				}
				
			}
		}
		
		for (Map.Entry<Atom, Edge> movement : movements.entrySet())
		{
			Atom atom = movement.getKey();
			Edge edge = movement.getValue();
			
			edge.getFrom().getAtoms().remove(atom);
			edge.getAtoms().add(atom);
		}
	}
	
	
	public void matchAtoms()
	{
		for (TransactionType transactionType : transactionTypes)
		{
			for (Node node : getNodes())
			{
				transactionType.makeTransactions(node);
			}
		}
		
		
		
	}
	
	
	public Double getAvgJournyLength()
	{
		if (lastJournies.size()==1000)
		{
			double total=0;
			for (int i=0; i<1000; i++)
			{
				total += lastJournies.get(i);
			}
			
			return total/1000;
		}
		
		return null;
	}
	
	
	public void learn(double learningRate)
	{
		
		
		for (Atom atom : rewarded)
		{
			
			if (atom.getTrace().size()>0)
			{
			
				atom.getLastMovement().learn(atom.getType(),atom.getReward(), learningRate);
				
				atom.setReward(0);
				
			}
			

		}
		
		rewarded.clear();

		
	}
	
	public void addReward(Atom atom, int reward)
	{
		atom.addReward(reward);
		rewarded.add(atom);
	}
	
	void addNode(Node node)
	{
		nodes.add(node);
	}
	
	void addAtom(Atom atom)
	{
		atoms.add(atom);
	}

	
	public void buildFactory(Node node)
	{
		Atom atom = new Atom(node,factoryElement);
		Factory factory = new Factory(node,supply,atom);
		
		factories.put(node, factory);
	}
	
	public void buildCity(Node node)
	{
		City city = new City(node,demand);
		
		cities.put(node, city);
	}
	
	public void runFactories()
	{
		for (Factory factory : factories.values())
		{
			factory.run();
		}
	}
	
	public void runCities()
	{
		for (City city : cities.values())
		{
			city.run();
		}
	}

	public Map<Node,Factory> getFactories()
	{
		return factories;
	}
	
	public Map<Node,City> getCities()
	{
		return cities;
	}
	
//	public void createDemand()
//	{
//		for (Node node : nodes)
//		{
//			while (node.getWealth()>=10)
//			{
//				node.addWealth(-10);
//
//				new Atom(node,demand);
//				
//				
//			}
//		}
//	}



}
