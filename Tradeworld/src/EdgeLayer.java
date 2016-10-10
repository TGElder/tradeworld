import elder.graphics.Layer;

public class EdgeLayer extends Layer
{

	private Network network;
	
	public EdgeLayer(Network network)
	{
		super("Network");
		this.network = network;
	}
	
	@Override
	public void createDrawables()
	{
		
		for (Node node : network.getNodes())
		{
			for (Edge edge : node.getEdges())
			{
				createLine(edge, 0, 0, 0, 1, false);
			}
		}
	}

}
