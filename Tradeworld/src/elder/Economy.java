package elder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Economy
{

	

	private Network network;
	private Collection<Demand> demands = new HashSet<Demand> ();
	private Collection<Supply> supplys = new HashSet<Supply> ();
	private Collection<Settlement> settlements = new HashSet<Settlement> ();

	private List<Node> nodes; //TODO sloppy
	private List<Node> freeToSettle; 
	
	private final double [] distancesToOwners;
	private final Settlement  [] owners;
	
	private final Collection<Country> countries = new HashSet<Country> ();

	
	Economy(Network network)
	{
		this.network = network;
		nodes = new ArrayList<Node> (network.getNodes());
		freeToSettle = new ArrayList<Node> (network.getNodes());
		
		distancesToOwners = new double[network.getNodeCount()];
		owners = new Settlement [network.getNodeCount()];
		
		for (int n=0; n<network.getNodeCount(); n++)
		{
			distancesToOwners[n] = Double.POSITIVE_INFINITY;
		}
	}
	
	void addSupply(Resource resource, int amount, int depth, Random random)
	{
				
		for (int s=0; s<amount; s++)
		{
			int r = random.nextInt(nodes.size());
			
			for (int d=0; d<depth; d++)
			{
			
				supplys.add(new Supply(resource,nodes.get(r)));
			}
			
			
			nodes.remove(r);
		}
		
	
	}

	
	void matchSupplyAndDemand()
	{
		
		for (Demand demand : demands)
		{
			demand.setSupply(null);
		}
		
		
		Collection<Flow> flows = new HashSet<Flow> ();
		
		for (Supply supply : supplys)
		{
			if (supply.isActive())
			{
				supply.setDemand(null);
				flows.add(new Flow(supply));
			}
		}
		
		boolean running=true;
		
		while (running)
		{
			running = false;
			
			for (Flow flow : flows)
			{
				flow.check();
				running = flow.step()|running;
			}
			
		}
	}
	
	void growPopulation(double birthRate, Random random)
	{
		Collection<Citizen> babies = new HashSet<Citizen> ();
		
		for (Settlement settlement : settlements)
		{
			babies.clear();
			
			for (Citizen citizen : settlement.getCitizens())
			{
				if (random.nextDouble()<birthRate)
				{
					babies.add(new Citizen(citizen.getHome()));
				}
			}
			
			for (Citizen baby : babies)
			{
				//baby.getHome().addWealth(2);
				baby.getHome().getCitizens().add(baby);

			}
		}
	}
	
	void migrate(double migrationRate, Random random)
	{
		
		List<Citizen> migrants = new ArrayList<Citizen> ();
		List<Settlement> newPositions = new ArrayList<Settlement> ();
		
		for (Settlement settlement : settlements)
		{		
			
			if (settlement.getWealth()<0)
			{
				
				int numberOfMigrants = 0;
			
				for (int w=0; w>settlement.getWealth(); w--)
				{
					if (random.nextDouble()<migrationRate)
					{
						numberOfMigrants ++;
					}
				}
				
				for (int c=0; c<Math.min(numberOfMigrants, settlement.getCitizens().size()); c++)
				{
					migrants.add(settlement.getCitizens().get(c));
				}
				
				
			}
			else if (settlement.getWealth()>0)
			{
				for (int w=0; w<settlement.getWealth(); w++)
				{
					if (random.nextDouble()<migrationRate)
					{
						newPositions.add(settlement);
					}
				}
			}
			
		
		}
		
		Collections.shuffle(migrants);
		Collections.shuffle(newPositions);
		
		for (int m=0; m<Math.min(migrants.size(), newPositions.size()); m++)
		{
			Citizen migrant = migrants.get(m);
			Settlement settlement = newPositions.get(m);
			
			migrant.getHome().getCitizens().remove(migrant);
			settlement.getCitizens().add(migrant);
			migrant.setHome(settlement);
			
		}
	}

	
	public void createDemandFromCitizens(Resource food, Resource luxury)
	{
		demands.clear();
		
		for (Settlement settlement : settlements)
		{			
			for (Citizen citizen : settlement.getCitizens())
			{
				demands.add(new Demand(food,citizen.getHome().getNode()));
				demands.add(new Demand(luxury,citizen.getHome().getNode()));

			}
		
		}
		
		
	}
	

	
	void run(Resource food, Resource luxury, double birthRate, double migrationRate, double settlementChance, double settlementStoppingChance, Random random)
	{
		//migrate();
		createDemandFromCitizens(food,luxury);
		matchSupplyAndDemand();
		doWealth();
		growPopulation(birthRate,random);
		settle();
		computeCountries();

		migrate(migrationRate,random);

		//buildNewSettlements(settlementChance,settlementStoppingChance,random);

		
		int population=0;
		int totalWealth = 0;
		
		for (Settlement settlement : settlements)
		{
			population += settlement.getCitizens().size();
		}
		
		System.out.println("Total population is "+population);

	}
	
	public void settle()
	{
		Collection<Settler> settlers = new HashSet<Settler> ();
		
		for (Settlement settlement : settlements)
		{
			Collection<Resource> missingResources = new HashSet<Resource> ();
			
			for (Demand demand : demands)
			{
				if (demand.getNode()==settlement.getNode())
				{
					if (demand.getSupply()==null)
					{
						missingResources.add(demand.getResource());
					}
				}
			}
			
			for (Resource resource : missingResources)
			{
				settlers.add(new Settler(settlement.getNode(),resource));
			}
		}
		
		boolean running=true;
		
		while (running)
		{
			running = false;
			
			for (Settler settler : settlers)
			{
				settler.check();
				running = settler.step()|running;
			}
			
		}
		
		
	}

	public Collection<Demand> getDemands()
	{
		return demands;
	}
	
	public Collection<Supply> getSupplys()
	{
		return supplys;
	}
	
	
	class Flow
	{
		
		private Supply supply;
		private Collection<Node> closed = new HashSet<Node> ();
		private Collection<Node> front = new HashSet<Node> ();

		
		Flow(Supply supply)
		{
			this.supply = supply;
			front.add(supply.getNode());
		}
		
		boolean step()
		{
			if (supply.getDemand()!=null||front.isEmpty())
			{
				return false;
			}
			else
			{
				closed.addAll(front);
				
				Collection<Node> newFront = new HashSet<Node> ();
				
				for (Node node : front)
				{
					for (Edge edge : node.getEdges())
					{
						if (!closed.contains(edge.getTo()))
						{
							newFront.add(edge.getTo());
						}
					}
				}
				
				front = newFront;
			
				return true;
			}
		
			
		}
		
		void check()
		{
			
			for (Demand demand : demands)
			{
				if (supply.getDemand()==null&&demand.getResource()==supply.getResource()&&front.contains(demand.getNode())&&demand.getSupply()==null) //TODO inefficient
				{
					assert(demand.getSupply()==null);
					demand.setSupply(supply);
					assert(supply.getDemand()==null);
					supply.setDemand(demand);
												
					return;
				}
			}
			
		}

	}
	
	class Settler
	{
		
		private Supply supply=null;
		private Resource resource;
		private Collection<Node> closed = new HashSet<Node> ();
		private Collection<Node> front = new HashSet<Node> ();

		
		Settler(Node node, Resource resource)
		{
			this.resource = resource;
			front.add(node);
		}
		
		boolean step()
		{
			if (supply!=null||front.isEmpty())
			{
				return false;
			}
			else
			{
				closed.addAll(front);
				
				Collection<Node> newFront = new HashSet<Node> ();
				
				for (Node node : front)
				{
					for (Edge edge : node.getEdges())
					{
						if (!closed.contains(edge.getTo()))
						{
							newFront.add(edge.getTo());
						}
					}
				}
				
				front = newFront;
			
				return true;
			}
		
			
		}
		
		void check()
		{
			
			for (Supply supply : supplys)
			{
				if (supply.getResource()==resource)
				{
					if (this.supply==null)
					{
						if(front.contains(supply.getNode()))
						{
							
							if(freeToSettle.contains(supply.getNode()))
							{
					
								assert(!supply.isActive());
								
								this.supply = supply;
								addSettlement(supply.getNode());
								
								return;
							}
						}
					}
				}
					
			
			}
			
		}

	}
	
	


	public Network getNetwork()
	{
		return network;
	}
	
	void randomlyPopulate(int settlements, int population, Random random)
	{
		
		for (int i=0; i<settlements; i++)
		{
			int r = random.nextInt(freeToSettle.size());
			
			Settlement settlement = addSettlement(freeToSettle.get(r));
			this.settlements.add(settlement);
			
			for (int p=0; p<population; p++)
			{
				settlement.getCitizens().add(new Citizen(settlement));
			}
						
		}
		
	}
	
	void doWealth()
	{
		for (Demand demand : demands)
		{
			if (demand.getSupply()!=null)
			{
				demand.getNode().addWealth(-1);
				demand.getSupply().getNode().addWealth(1);
			}
		}
		
		for (Settlement settlement : settlements)
		{
			settlement.setWealth(0);
			
			for (Node node : settlement.getLimits())
			{
				settlement.addWealth(node.getWealth());
			}
			
		}
		
		for (Node node : network.getNodes())
		{
			node.setWealth(0);
		}
		
		
	}
	
	Settlement addSettlement(Node node)
	{
		assert(freeToSettle.contains(node));
		
		Map<Node,Double> distances= node.getNeighboursWithinDistance(5);
		Collection<Node> limits = new HashSet<Node> (); 
		
		for (Map.Entry<Node, Double> other : distances.entrySet())
		{
			int index = ((IndexNode) other.getKey()).getIndex();
			
			if (other.getValue()<distancesToOwners[index])
			{
				distancesToOwners[index] = other.getValue();
				
				Settlement currentOwner = owners[index];
				if (currentOwner!=null)
				{
					currentOwner.getLimits().remove(other.getKey());
				}
				
				limits.add(other.getKey());
			}
		}		
		
		Settlement settlement = new Settlement(node,limits);
		settlements.add(settlement);
		freeToSettle.removeAll(distances.keySet());
		
		for (Node other : limits)
		{
			int index = ((IndexNode)other).getIndex();
			owners[index] = settlement;
		}
		
		for (Supply supply : supplys)
		{
			if (limits.contains(supply.getNode()))
			{
				supply.setActive(true);
			}
		}
		
		Country country = new Country(network,settlement);
		countries.add(country);
		
		return settlement;
	}
	
	void buildNewSettlements(double chance, double stoppingChance, Random random)
	{
		
		Collection<Settlement> existingSettlements = new HashSet<Settlement>(settlements); //TODO
		
		for (Settlement settlement : existingSettlements)
		{
			if (random.nextDouble()<=chance)
			{
				
				Node candidate = wander(settlement.getNode(),stoppingChance,random);
				
				if (freeToSettle.contains(candidate))
				{
					addSettlement(candidate);
				}
				
			}
		}
	}
	
	private Node wander(Node origin, double stoppingChance, Random random)
	{
		Collection<Node> closed = new HashSet<Node> ();
		List<Edge> edges = new ArrayList<Edge> ();
		List<Edge> removals = new ArrayList<Edge> ();

		
		Node current = origin;

		while (true)
		{
			closed.add(current);
		
			edges.addAll(current.getEdges());
			removals.clear();
			
			for (Edge edge : edges)
			{
				if (closed.contains(edge.getTo()))
				{
					removals.add(edge);
				}
			}
			
			edges.removeAll(removals);
			
			if (edges.isEmpty())
			{
				return current;
			}
			else if (random.nextDouble()<=stoppingChance)
			{
				return current;
			}
			else
			{
				Collections.shuffle(edges);
				current = edges.get(0).getTo();
			}
			
		}

	}

	
	Collection<Node> getFreeToSettle()
	{
		return freeToSettle;
	}

	public Collection<Settlement> getSettlements()
	{
		return settlements;
	}

	public Collection<Country> getCountries()
	{
		return countries;
	}
	
	void computeCountries()
	{
		
		for (Settlement settlement : settlements)
		{
			for (Settlement other : settlements)
			{
				other.addInfluence(settlement, settlement.getInfluenceAt(other.getNode(), network));
			}
		}
		
		for (Settlement settlement : settlements)
		{
			settlement.computeOwner();
		}

//		for (Country country : countries)
//		{
//			country.getSettlements().clear();
//		}
//		
//		for (Country country : countries)
//		{
//			country.computeWealth();
//		}
//		
//		for (Settlement settlement : settlements)
//		{
//			
//			double maxWealth = Double.NEGATIVE_INFINITY;
//			Country maxCountry = null;
//			
//			for (Country country : countries)
//			{
//				
//				
//				double focusWealth = country.getWealthAt(settlement.getNode());
//				
//				if (country.getWealthAt(settlement.getNode())>maxWealth)
//				{
//					maxWealth = focusWealth;
//					maxCountry = country;
//				}
//				
//				
//				
//			}
//			
//		
//			maxCountry.getSettlements().add(settlement);
//			
//
//		}
		
		
	}
	
}
