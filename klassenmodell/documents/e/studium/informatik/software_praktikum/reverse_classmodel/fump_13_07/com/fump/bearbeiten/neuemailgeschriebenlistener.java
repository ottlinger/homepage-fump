package com.fump.bearbeiten;

/**
*  @author Tobias Becker (tbecker), Joerg Dieckmann (dieck)
*  @version $Id: NeueMailGeschriebenListener.java,v 1.3 2001/06/13 16:29:00 tbecker Exp $
*
*
*/

import com.fump.bearbeiten.*;
import java.util.*;

public interface NeueMailGeschriebenListener extends EventListener
{
	public void geschriebeneMailAbholen(NeueMailGeschriebenEvent e);
}
