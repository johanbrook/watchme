package event;

/**
 * Contract for an event handler
 * @author hajo
 *
 */
public interface EventHandler {
	/**
	 * A method for handling an event.
	 * 
	 * @param evt The event.
	 * @author lisastenberg
	 */
    public void onEvent(Event evt);
}
