import java.util.NoSuchElementException;

/**
 * @author Mason Grant Description: A basic priority queue made to work with the
 *         Path class.
 */
public class GPSPriorityQueue<T extends Comparable<T>> {

	// ===================================================== Properties
	private Node head;
	private int size;

	// ===================================================== Constructors
	public GPSPriorityQueue() {
		super();
		clear();
	}

	public GPSPriorityQueue(T[] entries) {
		this();
		for (T entry : entries)
			add(entry);
	}

	// ===================================================== Inner Node Class
	private class Node {
		private T data;
		private Node next;

		private Node(T data) {
			this(data, null);
		}

		private Node(T data, Node next) {
			this.data = data;
			this.next = next;
		}

	}

	// ===================================================== Methods
	public boolean isEmpty() {
		return size == 0;
	}

	public boolean isFull() {
		return false;
	}

	public void clear() {
		head = null;
		size = 0;
	}

	public int size() {
		return size;
	}

	public void add(T newEntry) {
		Node curr = head;
		Node prev = null;

		while (curr != null && curr.data.compareTo(newEntry) <= 0) {
			prev = curr;
			curr = curr.next;
		}

		if (prev == null)
			head = new Node(newEntry, curr);
		else
			prev.next = new Node(newEntry, curr);

		size++;
	}

	public T remove() {
		if (isEmpty())
			throw new NoSuchElementException();
		T ret = (T) head.data;
		head = head.next;
		size--;
		return ret;
	}

	@Override
	public String toString() {
		String ret = "";
		Node curr = head;
		while (curr != null) {
			ret += ", " + curr.data.toString();
			curr = curr.next;
		}

		return "[" + ret.substring(2) + "]";
	}
}
