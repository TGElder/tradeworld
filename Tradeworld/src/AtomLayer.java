import elder.geometry.Point;
import elder.geometry.Polygon;
import elder.graphics.Layer;

public class AtomLayer extends Layer
{

	private Economy economy;
	private double p;
	
	public AtomLayer(Economy economy)
	{
		super("Atoms");
		this.economy = economy;
	}
	
	@Override
	public void updateDrawables()
	{
		long startTime = System.currentTimeMillis();
		
		long targetTime = startTime + (long)(250);
				
		long currentTime;
				
		while ((currentTime=System.currentTimeMillis())<targetTime)
		{
			
			
			p = (currentTime-startTime)/((targetTime-startTime)*1f);
			

			
			super.updateDrawables();
		}
	}
	
	@Override
	public void createDrawables()
	{
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

				createPolygon(box, atom.getType().getR(), atom.getType().getG(), atom.getType().getB(), 1f, true);
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
					
					createPolygon(box, atom.getType().getR(), atom.getType().getG(), atom.getType().getB(), 1f, true);
				}
			}
		}
	}

}
