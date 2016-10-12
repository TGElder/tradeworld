import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Economy
{

	

	private Network network;
	private Collection<Demand> demands = new HashSet<Demand> ();
	private Collection<Supply> supplys = new HashSet<Supply> ();
	private Collection<Settlement> settlements = new HashSet<Settlement> ();

	private List<Node> nodes; //TODO sloppy
	private List<Node> freeToSettle; 

	
	Economy(Network network)
	{
		this.network = network;
		nodes = new ArrayList<Node> (network.getNodes());
		freeToSettle = new ArrayList<Node> (network.getNodes());
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
			supply.setDemand(null);
			flows.add(new Flow(supply));
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
		Collection<Citizen> babies = new HashSet<Citizen> ();
		
		for (Settlement settlement : settlements)
		{
			babies.clear();
			
			for (int w=0; w<settlement.getWealth(); w++)
			{
				if (random.nextDouble()<migrationRate)
				{
					babies.add(new Citizen(settlement));
				}
			}
			
			for (Citizen baby : babies)
			{
				//baby.getHome().addWealth(2);
				baby.getHome().getCitizens().add(baby);

			}
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
		migrate(migrationRate,random);

		buildNewSettlements(settlementChance,settlementStoppingChance,random);

		
		int population=0;
		int totalWealth = 0;
		
		for (Settlement settlement : settlements)
		{
			population += settlement.getCitizens().size();
		}
		
		System.out.println("Total population is "+population);

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
		Collection<Node> limits = node.getNeighboursWithinDistance(3);
		Settlement settlement = new Settlement(node,limits);
		settlements.add(settlement);
		freeToSettle.removeAll(limits);
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
	
	
}
