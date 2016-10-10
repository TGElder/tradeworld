
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import elder.graphics.Canvas;
import elder.graphics.GUI;
import elder.graphics.Layer;
import elder.graphics.Navigator;



public class Simulation implements Runnable
{
	private Network network;
	private List<Layer> layers = new ArrayList<Layer> ();
	

	public Simulation()
	{		
		network = Network.generateRandomNetwork(100, 100, 1987, 0.33);
		setupSourcesAndDemand(10,100,1987);
		
		Canvas canvas = new Canvas("Tradeworld",1000,1000);
		
		EdgeLayer edgeLayer = new EdgeLayer(network);
		canvas.addLayer(edgeLayer);
		layers.add(edgeLayer);
		
		SourceLayer sourceLayer = new SourceLayer(network);
		canvas.addLayer(sourceLayer);
		layers.add(sourceLayer);
		
		DemandLayer demandLayer = new DemandLayer(network);
		canvas.addLayer(demandLayer);
		layers.add(demandLayer);
		
		TradeLayer tradeLayer = new TradeLayer(network);
		canvas.addLayer(tradeLayer);
		layers.add(tradeLayer);
		
		GUI gui = new GUI(canvas);
		
		Navigator navigator = new Navigator(canvas);
		canvas.addMouseListener(navigator);
		
	 	Thread thread = new Thread(this);
		thread.start();
		
		canvas.run();
	}
	
	private void setupSourcesAndDemand(int sources, int demand, int seed)
	{
		List<Node> nodes = new ArrayList<Node> (network.getNodes());
		
		Random random = new Random(seed);
		
		for (int s=0; s<sources; s++)
		{
			int r = random.nextInt(nodes.size());
			
			for (int i=0; i<10; i++)
			{
				network.getSources().add(new Source(nodes.get(r)));
			}
			
			nodes.remove(r);
		}
		
		for (int d=0; d<demand; d++)
		{
			int r = random.nextInt(nodes.size());
			
			nodes.get(r).addDemand(new Demand());;
			
			nodes.remove(r);
		}
		
		
	}
	
	private void resolve()
	{
		for (Node node : network.getNodes())
		{
			for (Demand demand : node.getDemand())
			{
				demand.setSource(null);
			}
		}
		
		Collection<Flow> flows = new HashSet<Flow> ();
		
		for (Source source : network.getSources())
		{
			flows.add(new Flow(source));
		}
		
		boolean running=true;
		
		while (running)
		{
			running = false;
			
			for (Flow flow : flows)
			{
				running = flow.step()|running;
				flow.check();
			}
			
		}
	}
	
	
	@Override
	public void run()
	{
		while (true)
		{
			resolve();
			for (Layer layer : layers)
			{
				layer.updateDrawables();
			}
		}
	}
	
	public static void main(String[] args)
	{
		
	
		Simulation sim = new Simulation();
		
	}
	
}
