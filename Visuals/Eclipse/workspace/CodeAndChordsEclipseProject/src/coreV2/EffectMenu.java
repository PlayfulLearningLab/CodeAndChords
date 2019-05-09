/**
 * 
 */
package coreV2;

import java.awt.Color;

import controlP5.ControlEvent;
import controlP5.Controller;
import controlP5.ScrollableList;
import coreV2_visuals.PitchColorVisual;

/**
 * @author Danny Mahota
 *
 */
public class EffectMenu extends MenuTemplate {


	public EffectMenu(ModuleDriver driver) 
	{
		super("Effect Menu", driver, true);


	}

	private void formatControllers(Visual visual)
	{

	}

	private void formatLabels(Visual visual)
	{

	}

	public void controlEvent(ControlEvent theEvent)
	{

	}
	
	
	

	/* (non-Javadoc)
	 * @see coreV2.MenuTemplate#sliderEvent(int, float)
	 */
	@Override
	public void sliderEvent(int id, float val) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see coreV2.MenuTemplate#buttonEvent(int)
	 */
	@Override
	public void buttonEvent(int id) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see coreV2.MenuTemplate#colorWheelEvent(int, java.awt.Color)
	 */
	@Override
	public void colorWheelEvent(int id, Color color) {
		// TODO Auto-generated method stub

	}

}
