package elder.graphics;
import java.awt.Checkbox;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JFrame
{
	public GUI(Canvas canvas)
	{		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth=2;
		constraints.gridheight=1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		
		
		JPanel layerPanel = new JPanel();
		layerPanel.setName("Layers");
		layerPanel.setBorder(BorderFactory.createTitledBorder(layerPanel.getName()));
		layerPanel.setLayout(new BoxLayout(layerPanel,BoxLayout.Y_AXIS));
		
		for (final Layer layer : canvas.getLayers())
		{
			Checkbox checkbox = new Checkbox(layer.toString(),layer.enabled());
			
			checkbox.addItemListener(new ItemListener() 
			{

				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==1)
					{
						layer.enable();
					}
					else
					{
						layer.disable();
					}
				}
		         
			});
			layerPanel.add(checkbox);
		}
		
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth=1;
		constraints.gridheight=9;
		
		add(layerPanel,constraints);
				
		pack();
		setVisible(true);
	}
	


	
}
