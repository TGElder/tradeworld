import java.util.ArrayList;
import java.util.List;

public class Element
{
	
	private float R,G,B;
		
	public Element(float R, float G, float B)
	{
		this.setR(R);
		this.setG(G);
		this.setB(B);
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
