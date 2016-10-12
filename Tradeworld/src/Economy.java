import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Economy
{

	

	private Network network;
	private Collection<Demand> demands = new HashSet<Demand> ();
	private Collection<Supply> supplys = new HashSet<Supply> ();

	private List<Node> nodes; //TODO sloppy
	
	Economy(Network network)
	{
		this.network = network;
		nodes = new ArrayList<Node> (network.getNodes());
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
		
		for (Node node : network.getNodes())
		{
			babies.clear();
			
			for (Citizen citizen : node.getCitizens())
			{
				if (random.nextDouble()<birthRate)
				{
					babies.add(new Citizen(citizen.getNode()));
				}
			}
			
			for (Citizen baby : babies)
			{
				baby.getNode().addWealth(2);
				baby.getNode().getCitizens().add(baby);

			}
		}
	}
	
	void migrate()
	{
		Collection<Migration> migrations = new HashSet<Migration> ();
		
		for (Node node : network.getNodes())
		{			
			while (node.getWealth()<node.getCitizens().size())
			{
				Citizen migrant = node.getCitizens().get(0);
				migrant.setHome(null);
				migrations.add(new Migration(migrant));
				node.getCitizens().remove(0);
			}
		}
		
		boolean running=true;
		
		while (running)
		{
			running = false;
			
			for (Migration migration : migrations)
			{
				migration.check();
				running = migration.step()|running;
			}
			
		}
	}
	
	public void createDemandFromCitizens(Resource food, Resource luxury)
	{
		demands.clear();
		
		int totalFoodAmount=0;
		int totalLuxuryAmount=0;
		
		for (Node node : network.getNodes())
		{
			assert(node.getCitizens().size()<=node.getWealth());
			
			int foodAmount = node.getCitizens().size();
			
			int luxuryAmount;
			
			if (node.getCitizens().size()>0)
			{
				luxuryAmount = node.getWealth() - node.getCitizens().size();
			}
			else
			{
				luxuryAmount = 0;
			}
			
			totalFoodAmount += foodAmount;
			
			for (int d=0; d<foodAmount; d++)
			{
				demands.add(new Demand(food,node));
			}
			
			totalLuxuryAmount += luxuryAmount;
						
			for (int d=0; d<luxuryAmount; d++)
			{
				demands.add(new Demand(luxury,node));
			}
		}
		
		System.out.println("Created "+totalFoodAmount+" food demand and "+totalLuxuryAmount+" luxury demand");
		
		
	}
	

	
	public void addWealth()
	{
		for (Supply supply : supplys)
		{
			if (supply.getDemand()!=null)
			{
				assert(supply.getDemand().getNode().getWealth()>=1);
				supply.getDemand().getNode().addWealth(-1);
				supply.getNode().addWealth(1);
			}
		}
	}
	
	
	void run(Resource food, Resource luxury, double birthRate, Random random)
	{
		migrate();
		createDemandFromCitizens(food,luxury);
		matchSupplyAndDemand();
		addWealth();
		growPopulation(birthRate,random);
		
		int population=0;
		int totalWealth = 0;
		
		for (Node node : network.getNodes())
		{
			population += node.getCitizens().size();
			totalWealth += node.getWealth();
		}
		
		System.out.println("Total wealth is "+totalWealth);
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
							
					System.out.println("Matched "+demand.getNode()+"to"+supply.getNode()+" "+demand.getResource().getName());
					
					return;
				}
			}
			
		}

	}
	
	class Migration
	{
		
		private Citizen citizen;
		private Collection<Node> closed = new HashSet<Node> ();
		private Collection<Node> front = new HashSet<Node> ();

		
		Migration(Citizen citizen)
		{
			this.citizen = citizen;
			front.add(citizen.getNode());
		}
		
		boolean step()
		{
			if (citizen.getHome()!=null||front.isEmpty())
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
			if (citizen.getHome()==null)
			{
				for (Node node : front)
				{
					if (node.getCitizens().size()<node.getWealth())
					{
						node.getCitizens().add(citizen);
						citizen.setHome(node);
					}
				}
				
			}
			
		}

	}


	public Network getNetwork()
	{
		return network;
	}
	
	void randomlyPopulate(int amount, Random random)
	{
		
		for (int i=0; i<10; i++)
		{
			int r = random.nextInt(nodes.size());
			
			nodes.get(r).getCitizens().add(new Citizen(nodes.get(r)));
			nodes.get(r).addWealth(2);
			
			nodes.remove(r);
		}
		
	}
	
	
}
