/**
 * @author Mason Grant Description: A class that represents the path from one
 *         vertex to another through edges.
 */
public class Path implements Comparable<Path> {

	public String path = "";
	public int totalCost = 0;

	public Path(String startVertex, int cost, String endVertex) {
		path = startVertex + endVertex;
		totalCost = cost;
	}

	public String getLast() {
		return (path.length() == 0) ? "" : path.charAt(path.length() - 1) + "";
	}

	@Override
	public int compareTo(Path o) {
		return totalCost - o.totalCost;
	}
}
