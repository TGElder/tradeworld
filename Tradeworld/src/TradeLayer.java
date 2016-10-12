import elder.geometry.Line;
import elder.graphics.Layer;

public class TradeLayer extends Layer
{

	private Economy economy;
	
	public TradeLayer(Economy economy)
	{
		super("Trade");
		this.economy = economy;
	}
	
	@Override
	public void createDrawables()
	{
		
		
		for (Demand demand : economy.getDemands())
		{
			if (demand.getSupply()!=null)
			{
				createLine(new Line(demand.getNode(),demand.getSupply().getNode()), demand.getResource().getR(), demand.getResource().getG(), demand.getResource().getB(), 1, false);
			}
		}
		
	}

}
