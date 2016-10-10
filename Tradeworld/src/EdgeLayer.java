import elder.geometry.Line;
import elder.geometry.Point;
import elder.geometry.Polygon;
import elder.graphics.Layer;

public class EdgeLayer extends Layer
{

	private Economy economy;
	
	public EdgeLayer(Economy economy)
	{
		super("Edges");
		this.economy = economy;
	}
	
	@Override
	public void createDrawables()
	{
		
		for (Node node : economy.getNodes())
		{
			for (Edge edge : node.getEdges())
			{
				createLine(new Line(edge.getFrom(),edge.getTo()), 0, 0, 0, 1, false);
			}
		}
	}

}
