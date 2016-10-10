import elder.geometry.Line;
import elder.graphics.Layer;

public class TradeLayer extends Layer
{

	private Network network;
	
	public TradeLayer(Network network)
	{
		super("Trade");
		this.network = network;
	}
	
	@Override
	public void createDrawables()
	{
		
		for (Node node : network.getNodes())
		{
			for (Demand demand : node.getDemand())
			{
				if (demand.getSource()!=null)
				{
					createLine(new Line(node,demand.getSource().getNode()), 0, 1, 0, 1, false);
				}
			}
		}
	}

}
