package elder;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;

import elder.geometry.Point;
import elder.graphics.Canvas;
import elder.graphics.GUI;
import elder.graphics.Layer;
import elder.graphics.Navigator;



public class Simulation implements Runnable
{
	private Economy economy;
	private List<Layer> layers = new ArrayList<Layer> ();
	Resource food = new Resource("Food",0,1,0);
	Resource wine = new Resource("Wine",1,0,0);
	Random random = new Random(1986);
	
	public Simulation()
	{		
		
		

		
		economy = new Economy(Network.generateRandomNetwork(50, 50, random, 0.75));
		economy.addSupply(food, 1600, 3, random);
		economy.addSupply(wine, 16, 25, random);
		
		economy.randomlyPopulate(3,3, random);
		
		
		Canvas canvas = new Canvas("Tradeworld",1000,1000);
		canvas.zoom(new Point(0,0), new Point(50,50));
		
		EdgeLayer edgeLayer = new EdgeLayer(economy.getNetwork());
		canvas.addLayer(edgeLayer);
		layers.add(edgeLayer);
		
		SupplyLayer supplyLayer = new SupplyLayer(economy);
		canvas.addLayer(supplyLayer);
		layers.add(supplyLayer);
		
		DemandLayer demandLayer = new DemandLayer(economy);
		canvas.addLayer(demandLayer);
		layers.add(demandLayer);
		
		TradeLayer tradeLayer = new TradeLayer(economy);
		canvas.addLayer(tradeLayer);
		layers.add(tradeLayer);

		SettlementLayer settlementLayer = new SettlementLayer(economy);
		canvas.addLayer(settlementLayer);
		layers.add(settlementLayer);
		
		WealthLayer wealthLayer = new WealthLayer(economy);
		wealthLayer.disable();
		canvas.addLayer(wealthLayer);
		layers.add(wealthLayer);
		
		PopulationLayer populationLayer = new PopulationLayer(economy);
		populationLayer.disable();
		canvas.addLayer(populationLayer);
		layers.add(populationLayer);
		
		
		FreeToSettleLayer freeToSettleLayer = new FreeToSettleLayer(economy);
		freeToSettleLayer.disable();
		canvas.addLayer(freeToSettleLayer);
		layers.add(freeToSettleLayer);
		
		OwnershipLayer ownershipLayer = new OwnershipLayer(economy);
		ownershipLayer.disable();
		canvas.addLayer(ownershipLayer);
		layers.add(ownershipLayer);
		
		CountryLayer countryLayer = new CountryLayer(economy,ownershipLayer);
		countryLayer.disable();
		canvas.addLayer(countryLayer);
		layers.add(countryLayer);
		
		
		GUI gui = new GUI(canvas,this);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridwidth=1;
		constraints.gridheight=1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		
		
		Navigator navigator = new Navigator(canvas);
		canvas.addMouseListener(navigator);
		
	 	Thread thread = new Thread(this);
		thread.start();
		
		canvas.run();
	}
	
	
	
	@Override
	public void run()
	{
		
		double birthRate = 0.1;
		
		while (true)
		{
			
			int pop=0;
			
			for (Settlement settlement : economy.getSettlements())
			{
				pop += settlement.getCitizens().size();
			}
			
			if (pop>=10000)
			{
				birthRate = 0;
			}
				
			economy.run(food, wine, birthRate, 0.25, 0.05, 0.01, random);


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
	
	// Only allow resources from controlled nodes
	// Settlements expand when some settlement has no food
	// Have death/anti-migration
	
}
