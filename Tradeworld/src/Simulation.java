import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import org.lwjgl.opengl.Display;

import elder.graphics.Canvas;
import elder.graphics.GUI;
import elder.graphics.Layer;
import elder.graphics.Navigator;


public class Simulation implements Runnable
{
	
	private Economy economy;
	private AtomLayer atomLayer;
	private WealthLayer wealthLayer;
	private EdgeLayer edgeLayer;

	
	public Simulation()
	{
		initEconomy();
		
		Canvas canvas = new Canvas("Tradeworld",1000,1000);
		
		
		
		wealthLayer = new WealthLayer(economy);
		canvas.addLayer(wealthLayer);
		
		atomLayer = new AtomLayer(economy);
		canvas.addLayer(atomLayer);
		
		edgeLayer = new EdgeLayer(economy);
		canvas.addLayer(edgeLayer);
		
		GUI gui = new GUI(canvas);
		
		Navigator navigator = new Navigator(canvas);
		canvas.addMouseListener(navigator);
				
	 	Thread thread = new Thread(this);
		thread.start();
		
		canvas.run();
	}
	
	private void step(Element element, double explorationRate, boolean draw)
	{
		economy.runFactories();
		economy.runCities();
		economy.moveOntoEdges(element,explorationRate);
		if (draw)
		{
			edgeLayer.updateDrawables();
			atomLayer.updateDrawables();
			wealthLayer.updateDrawables();
		}
		economy.moveOffEdges(element);
		economy.matchAtoms();
	}
	
	private void initEconomy()
	{
		economy = new Economy();
		
		// Initialise
		
		Random random = new Random(1988);
		
		int width = 20;
		Node [][] nodeMatrix = new Node[width][width];
		int nx[] = {-1,0,1,0,0};
		int ny[] = {0,1,0,-1,0};
		
		for (int x=0; x<width; x+=1)
		{
			for (int y=0; y<width; y+=1)
			{
				Node node = new Node(100 + x*(800/width),100 + y*(800/width));
				economy.addNode(node);
				nodeMatrix[x][y] = node;
			}	
		}
		
		for (int x=0; x<width; x++)
		{
			for (int y=0; y<width; y++)
			{
				for (int n = 0; n<5; n++)
				{
					
					int dx = x + nx[n];
					int dy = y + ny[n];
					
					if (dx>=0 && dx<width && dy>=0 && dy<width)
					{
						if (random.nextDouble()<=1)
						{
							Node from = nodeMatrix[x][y];
							Node to = nodeMatrix[dx][dy];
							
							Edge fromTo = new Edge(from,to);
							from.addEdge(fromTo);
							Edge toFrom = new Edge(to,from);
							to.addEdge(toFrom);

						}
					}
					
				}
			}
		}
		
		
		Collection<Node> used = new HashSet<Node> ();		
		
		for (int i=0; i<1; i++)
		{
			Node node = nodeMatrix[random.nextInt(width)][random.nextInt(width)];
			
			economy.buildCity(node);
		}
		
		for (City city : economy.getCities().values())
		{
			for (int i=0; i<160; i++)
			{
				city.grow();
			}
		}
		
		Map<Node,Factory> factories = economy.getFactories();
		
		while (factories.size()<40)
		{
			Node node = nodeMatrix[random.nextInt(width)][random.nextInt(width)];
			
			if (!used.contains(node))
			{
				economy.buildFactory(node);
				used.add(node);
			}
		}
		
	}

	@Override
	public void run()
	{
		int iterations=100000;
		
		for (int i=0; i<iterations; i++)
		{

			
			if (i%100==0)
			{
				System.out.println(i+"\t"+economy.getAvgJournyLength());
			}
			
			step(economy.getSupply(),(iterations-i)/(iterations*1f),false);
			step(economy.getDemand(),(iterations-i)/(iterations*1f),false);
			//step(economy.getGreen(),(iterations-i)/(iterations*2f),false);
			//step(economy.getYellow(),(iterations-i)/(iterations*2f),false);

			economy.learn(0.1);
		}
		
		while (true)
		{
			
			step(economy.getSupply(),0,true);
			step(economy.getDemand(),0,true);
			//step(economy.getGreen(),0.1,true);
			//step(economy.getYellow(),0.1,true);
			
			economy.learn(0);

			
			System.out.println(economy.getAvgJournyLength());
				
		}
	}
	
	public static void main(String[] args)
	{
		
	
		Simulation sim = new Simulation();
		
		
		
		
		
	}
	
}
