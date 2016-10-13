package elder;



public class IndexNode extends Node
{
	
	private Integer index;
		
	public IndexNode(double x, double y, Integer integer)
	{
		super(x, y);
		this.setIndex(integer);
	}

	public Integer getIndex()
	{
		return index;
	}

	public void setIndex(Integer index)
	{
		this.index = index;
	}
	
	@Override
	public boolean equals(Object other)
	{
		return this==other;
	}

}
