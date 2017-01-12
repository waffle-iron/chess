package communication.internal.command;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuItem;

public abstract class ItemComponent extends BaseComponent{

	protected ItemComponent(JComponent component, JComponent parent) {
		super(component, parent);
	}	
	
	@Override protected final void onInitialize() {
		super.get(JMenuItem.class).addActionListener(new AbstractAction(super.toString()) {
			@Override public void actionPerformed(ActionEvent actionEvent) {
				onExecute(actionEvent);
			}
		});
	}
	
	
}