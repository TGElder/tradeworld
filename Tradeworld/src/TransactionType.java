import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public abstract class TransactionType
{
	
	private List<Element> requiredElements;

	public TransactionType(List<Element> requiredElements)
	{
		this.requiredElements = requiredElements;
	}
	
	public void makeTransactions(Node node)
	{		
		Transaction focus;
		
		while ((focus=getTransaction(node))!=null)
		{
			onTransact(focus);
		}
	}
	
	public Transaction getTransaction(Node node)
	{
		Atom [] match = new Atom[requiredElements.size()];
		
		for (Atom atom : node.getAtoms())
		{
			for (int e=0; e<requiredElements.size(); e++)
			{
				Element requirement = requiredElements.get(e);
				
				if (match[e]==null&&atom.getType().equals(requirement))
				{
					match[e] = atom;
				}
			}
		}
				
		boolean complete = true;
		
		for (Atom atom : match)
		{
			if (atom==null)
			{
				complete = false;
			}
		}
			
		if (complete)
		{
			return new Transaction(node,match);
		}
		else
		{
			return null;
		}
	}
	
	public void completeTransactions(Collection<Transaction> transactions)
	{
		for (Transaction transaction : transactions)
		{
			onTransact(transaction);
		}
	}
	
	public abstract void onTransact(Transaction transaction);

}
