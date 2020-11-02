package com.fump.bearbeiten;

/**
*  @author Tobias Becker (tbecker), Joerg Dieckmann (dieck)
*  @version $Id: WillAntwortMailSchreibenListener.java,v 1.2 2001/06/20 14:14:32 tbecker Exp $ 
*
*
*/

import java.util.*;
import com.fump.bearbeiten.*;

public interface WillAntwortMailSchreibenListener extends EventListener
{
	public void antwortMailSchreiben(WillAntwortMailSchreibenEvent e);
}
