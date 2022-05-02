import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Mason Grant Description: A class that represents the entire map of
 *         vertices and edges - - and gets that information from a text file.
 */
public class Graph {

	public static boolean useDistanceCost = true;
	public static boolean returnAddress = false;
	public static boolean useHighways = false;

	public HashMap<String, Vertex> vertices;
	public ArrayList<Vertex> verticesIterable;
	public ArrayList<Edge> edges;

	public Graph(String fileName) {
		vertices = new HashMap<String, Vertex>();
		verticesIterable = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
		String[] parts;

		try (Scanner fin = new Scanner(new File(fileName))) {
			while (fin.hasNextLine()) {
				parts = fin.nextLine().split("\t");
				if (parts[0].equals("<Nodes>")) {
					fin.nextLine();
					while (true) {
						parts = fin.nextLine().split("\t");
						if (parts[0].equals("</Nodes>"))
							break;
						vertices.put(parts[0], new Vertex(parts[0], parts[1]));

						verticesIterable.add(new Vertex(parts[0], parts[1]));
					}
				} else if (parts[0].equals("<Edges>")) {
					fin.nextLine();
					while (true) {
						parts = fin.nextLine().split("\t");
						if (parts[0].equals("</Edges>"))
							break;
						edges.add(new Edge(vertices.get(parts[0]), vertices.get(parts[1]), Integer.parseInt(parts[2]),
								Integer.parseInt(parts[3]), Integer.parseInt(parts[4])));
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Edge e : edges) {
			s.append(e).append("\n");
		}
		return s.toString();
	}

}
