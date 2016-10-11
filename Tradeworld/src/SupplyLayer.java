import elder.geometry.Polygon;
import elder.graphics.Layer;

public class SourceLayer extends Layer
{

	private Network network;
	
	public SourceLayer(Network network)
	{
		super("Sources");
		this.network = network;
	}
	
	@Override
	public void createDrawables()
	{
		int size=3;
		
		for (Source source : network.getSources())
		{
			Node node = source.getNode();
			
			Polygon box = new Polygon();
			box.add(new Node(node.x-size,node.y - size));
			box.add(new Node(node.x+size,node.y - size));
			box.add(new Node(node.x+size,node.y + size));
			box.add(new Node(node.x-size,node.y + size));

			createPolygon(box, 0, 0, 1f, 1f, true);
		}
	}

}
