package event;

/**
 * A single event
 * @author hajo
 *
 */
public class Event {
    // All possible events listed
	//When you input a new tag, please put in alphabetical order.
    public enum Tag {
    	MOVIE_TABLE_CHANGED,
    	TAG_TABLE_CHANGED;
    }
    
    private final Tag tag;
    // The new value 
    private final Object value;
    public Event(Tag tag, Object value){
        this.tag = tag;
        this.value = value;
    }
    public Tag getTag() {
        return tag;
    }
    public Object getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "Event [tag=" + tag + ", value=" + value + "]";
    } 
    
    
}
