import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * @author Mason Grant Description: A class that represents a vertex (a
 *         location).
 */
public class Vertex {
	private static int longestAddress = 1;
	public String symbol;
	public String address;

	public Vertex(String symbol, String address) {
		super();
		this.symbol = symbol;
		this.address = address;
		longestAddress = Math.max(longestAddress, address.length());
	}

	@Override
	public String toString() {
		return String.format("%-" + (Graph.returnAddress ? longestAddress : 1) + "s",
				Graph.returnAddress ? address : symbol);
	}

}
