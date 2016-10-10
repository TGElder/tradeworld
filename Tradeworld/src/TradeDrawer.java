import org.lwjgl.opengl.Display;

import elder.geometry.Line;
import elder.geometry.Point;
import elder.geometry.Polygon;

public class TradeDrawer
{
	
	
	private Economy economy;
	private final View view;
	
	public TradeDrawer(Economy economy)
	{
		setEconomy(economy);
		this.view = new View();
		
		View view = new View();

		view.init(1000, 1000);

	}

	public Economy getEconomy()
	{
		return economy;
	}

	void setEconomy(Economy economy)
	{
		this.economy = economy;
	}
	
	void update(double time)
	{
		
		
		long startTime = System.currentTimeMillis();
		
		double targetTime = startTime + (time*1000);

		
		int ticks=100;
		
		long currentTime;
		
		while ((currentTime=System.currentTimeMillis())<targetTime)
		{
			
			double p = (currentTime-startTime)/(targetTime-startTime);
		
			view.clear();
			
			// Draw Line
			
			float lineWidth = 1f;
			view.setColor(0f, 0f, 0f, 1f);
	
			
			for (Node node : economy.getNodes())
			{
				for (Edge edge : node.getEdges())
				{
					Line line = new Line(edge.getFrom(),edge.getTo());
					view.drawLine(line,lineWidth,false);
				}
	
			}
			
		
			
			// Draw Node
			
			int nodeSize = 5;
			view.setColor(0f, 0f, 0f, 1f);
			
			for (Node node : economy.getNodes())
			{
				Polygon box = new Polygon();
				box.add(new Node(node.x-nodeSize,node.y - nodeSize));
				box.add(new Node(node.x+nodeSize,node.y - nodeSize));
				box.add(new Node(node.x+nodeSize,node.y + nodeSize));
				box.add(new Node(node.x-nodeSize,node.y + nodeSize));
				
				//view.drawPolygon(box,true);
	
			}
			
			// Draw atoms on edges
			
			int atomSize = 2;
			
			for (Node node : economy.getNodes())
			{
				for (Atom atom : node.getAtoms())
				{
					
					Polygon box = new Polygon();
					box.add(new Node(node.x-atomSize,node.y - atomSize));
					box.add(new Node(node.x+atomSize,node.y - atomSize));
					box.add(new Node(node.x+atomSize,node.y + atomSize));
					box.add(new Node(node.x-atomSize,node.y + atomSize));
					
					view.setColor(atom.getType().getR(), atom.getType().getG(), atom.getType().getB(), 1f);
						
					view.drawPolygon(box,true);
				}
				
				for (Edge edge : node.getEdges())
				{
					for (Atom atom : edge.getAtoms())
					{
						int focusX = (int)(edge.getFrom().x + (edge.getTo().x - edge.getFrom().x)*p);
						int focusY = (int)(edge.getFrom().y + (edge.getTo().y - edge.getFrom().y)*p);
						
						Point focus = new Point(focusX,focusY);
						
						Polygon box = new Polygon();
						box.add(new Node(focus.x-atomSize,focus.y - atomSize));
						box.add(new Node(focus.x+atomSize,focus.y - atomSize));
						box.add(new Node(focus.x+atomSize,focus.y + atomSize));
						box.add(new Node(focus.x-atomSize,focus.y + atomSize));
						
						view.setColor(atom.getType().getR(), atom.getType().getG(), atom.getType().getB(), 1f);
						
						view.drawPolygon(box,true);
					}
				}
			}
			
			view.update();
			
		}
	}

}
