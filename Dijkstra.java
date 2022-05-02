import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * @author Mason Grant Description: Using Dijkstra's algorithm to get the lowest
 *         cost path.
 */
public class Dijkstra {
	private static int totalCost;

	protected static Path shortestPath(Graph graph, String startVertexName, String endVertexName) {
		GPSPriorityQueue<Path> pq = new GPSPriorityQueue<Path>();

		// Priming the pump
		pq.add(new Path(startVertexName, 0, startVertexName));

		ArrayList<String> visited = new ArrayList<String>();

		// While the priority queue isn't empty we keep doing the algorithm.
		while (!pq.isEmpty()) {
			Path nextEntry = pq.remove();
			if (!(visited.contains(nextEntry.getLast()))) {
				// We add locations to visited ArrayList so that we don't continuously use them.
				visited.add(nextEntry.getLast());
			}
			if (nextEntry.getLast().equals(endVertexName)) {
				// Once we pull a path from the priority queue that matches our end location-
				// - we return it.
				return nextEntry;

			} else {
				// Preparing whatever we pulled from the priority queue
				Vertex v = graph.vertices.get(nextEntry.getLast());
				totalCost = nextEntry.totalCost;
				Path currPath = nextEntry;

				// For loop goes through all edges, but only continues if the item -
				// - from the priority queue is at the front of the edge and we -
				// - haven't visited the neighbor yet.
				for (Edge e : graph.edges) {
					if (e.fromVertex.symbol.equals(nextEntry.getLast()) && !visited.contains(e.toVertex.symbol)) {
						// Setting up the neighbor vertex to be the next path put into the priority
						// queue.
						int nextCost;
						if (graph.useHighways == true) {
							nextCost = totalCost + e.highways;
						} else {
							if (graph.useDistanceCost == false)
								nextCost = totalCost + e.timeCost;
							else
								nextCost = totalCost + e.distanceCost;
						}
						String nextPath = currPath.getLast() + e.toVertex.symbol;
						Path nextState = new Path(nextEntry.path, nextCost, nextPath);
						pq.add(nextState);
					}
				}
			}

		}
		// We return null if there is no path.
		return null;

	}
}
