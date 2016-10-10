

public class Transaction
{
	private final Node node;
	private final Atom[] atoms;
	
	Transaction(Node node, Atom[] atoms)
	{
		this.node = node;
		this.atoms = atoms;
	}

	public Node getNode()
	{
		return node;
	}
	
	public Atom[] getAtoms()
	{
		return atoms;
	}


}
