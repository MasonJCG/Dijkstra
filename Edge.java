import java.awt.Color;
import java.awt.Graphics;

/**
 * @author Mason Grant Description: A class that represents the edges between
 *         vertices.
 */
public class Edge {
	public Vertex fromVertex;
	public Vertex toVertex;
	public int timeCost;
	public int distanceCost;
	public int highways;

	public Edge(Vertex fromVertex, Vertex toVertex, int timeCost, int distanceCost, int highways) {
		super();
		this.fromVertex = fromVertex;
		this.toVertex = toVertex;
		this.timeCost = timeCost;
		this.distanceCost = distanceCost;
		this.highways = highways;
	}

	@Override
	public String toString() {
		return String.format("%s -> %s (%d %s)\n", fromVertex, toVertex,
				Graph.useDistanceCost ? distanceCost : (Graph.useHighways ? highways : timeCost),
				Graph.useDistanceCost ? "miles" : (Graph.useHighways ? "highways" : "minutes"));

	}

	// A toString method, but without providing the cost of the edge.
	public String toStringNoCost() {
		return String.format("%s -> %s ", fromVertex, toVertex);
	}

}
