
public class Resource
{
	
	private String name;
	private float R,G,B;
	
	Resource(String name, float R, float G, float B)
	{
		this.name = name;
		this.setR(R);
		this.setG(G);
		this.setB(B);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public float getR()
	{
		return R;
	}

	public void setR(float r)
	{
		R = r;
	}

	public float getG()
	{
		return G;
	}

	public void setG(float g)
	{
		G = g;
	}

	public float getB()
	{
		return B;
	}

	public void setB(float b)
	{
		B = b;
	}
	
	

}
