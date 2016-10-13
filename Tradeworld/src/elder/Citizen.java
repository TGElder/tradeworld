package elder;

public class Citizen
{
	
	private Settlement home;

	public Citizen(Settlement home)
	{
		this.home = home;
	}
	
	public Settlement getHome()
	{
		return home;
	}

	public void setHome(Settlement home)
	{
		this.home = home;
	}
	
}
