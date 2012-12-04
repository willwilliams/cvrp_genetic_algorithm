import java.awt.Point;

public class CVRPData {
	private static final int X_COORDINATE = 0;
	private static final int Y_COORDINATE = 1;

	/** The capacity that all vehicles in fruitybun-data.vrp have. */
	public static final int VEHICLE_CAPACITY = 220;

	/**
	 * The number of nodes in the fruitybun CVRP i.e. the depot and the
	 * customers
	 */
	public static final int NUM_NODES = 76;

	/** Return the demand for a given node. */
	static int getDemand(int node) {
		node += 2;
		if (!nodeIsValid(node)) {
			System.err.println("Error: demand for node " + node
					+ " was requested from getDemand() but only nodes 1.."
					+ NUM_NODES + " exist");
			System.exit(-1);
		}
		return demand[node];
	}

	/** Return the Euclidean distance between the two given nodes */
	public static double getDistance(int node1, int node2) {
		node1 += 2;
		node2 += 2;
		if (!nodeIsValid(node1)) {
			System.err.println("Error: distance for node " + node1
					+ " was requested from getDistance() but only nodes 1.."
					+ NUM_NODES + " exist");
			System.exit(-1);
		}

		if (!nodeIsValid(node2)) {
			System.err.println("Error: distance for node " + node2
					+ " was requested from getDistance() but only nodes 1.."
					+ NUM_NODES + " exist");
			System.exit(-1);
		}
		int x1 = coords[node1][X_COORDINATE];
		int y1 = coords[node1][Y_COORDINATE];
		int x2 = coords[node2][X_COORDINATE];
		int y2 = coords[node2][Y_COORDINATE];

		return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}

	public static Point getLocation(int node){
		if (!nodeIsValid(node)) {
			System.err
					.println("Error: Request for location of non-existent node "
							+ node + ".");
			System.exit(-1);
		}
		return new Point(coords[node][X_COORDINATE], coords[node][Y_COORDINATE]);
	}

	/**
	 * Return true if the given node is within the valid range (1..NUM_NODES),
	 * false otherwise
	 */
	private static boolean nodeIsValid(int node) {
		if (node < 1 || node > NUM_NODES)
			return false;
		else
			return true;
	}

	/** Return customer location */
	public static int[] getCustomer(int customerNumber) {
		return coords[customerNumber + 2];
	}

	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	// DATA
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------

	public static int[][] coords = new int[][] {
			{ -1, -1 }, // dummy entry to make index of array match indexing of
						// nodes in fruitybun-data.vrp
			{ 40, 40 }, // the coordinates of node 1 (the depot)
			{ 22, 22 }, { 36, 26 }, { 21, 45 }, { 45, 35 }, { 55, 20 },
			{ 33, 34 }, { 50, 50 }, { 55, 45 }, { 26, 59 }, { 40, 66 },
			{ 55, 65 }, { 35, 51 }, { 62, 35 }, { 62, 57 }, { 62, 24 },
			{ 21, 36 }, { 33, 44 }, { 9, 56 }, { 62, 48 }, { 66, 14 },
			{ 44, 13 }, { 26, 13 }, { 11, 28 }, { 7, 43 }, { 17, 64 },
			{ 41, 46 }, { 55, 34 }, { 35, 16 }, { 52, 26 }, { 43, 26 },
			{ 31, 76 }, { 22, 53 }, { 26, 29 }, { 50, 40 }, { 55, 50 },
			{ 54, 10 }, { 60, 15 }, { 47, 66 }, { 30, 60 }, { 30, 50 },
			{ 12, 17 }, { 15, 14 }, { 16, 19 }, { 21, 48 }, { 50, 30 },
			{ 51, 42 }, { 50, 15 }, { 48, 21 }, { 12, 38 }, { 15, 56 },
			{ 29, 39 }, { 54, 38 }, { 55, 57 }, { 67, 41 }, { 10, 70 },
			{ 6, 25 }, { 65, 27 }, { 40, 60 }, { 70, 64 }, { 64, 4 },
			{ 36, 6 }, { 30, 20 }, { 20, 30 }, { 15, 5 }, { 50, 70 },
			{ 57, 72 }, { 45, 42 }, { 38, 33 }, { 50, 4 }, { 66, 8 },
			{ 59, 5 }, { 35, 60 }, { 27, 24 }, { 40, 20 }, { 40, 37 } };

	private static int[] demand = new int[] { 9999999, // dummy entry to make
														// index of array match
														// indexing of nodes in
														// fruitybun-data.vrp
			0, // node 1
			18, // node 2
			26, // node 3
			11, // node 4
			30, // node 5
			21, // node 6
			19, // node 7
			15, // node 8
			16, // node 9
			29, // node 10
			26, // node 11
			37, // node 12
			16, // node 13
			12, // node 14
			31, // node 15
			8, // node 16
			19, // node 17
			20, // node 18
			13, // node 19
			15, // node 20
			22, // node 21
			28, // node 22
			12, // node 23
			6, // node 24
			27, // node 25
			14, // node 26
			18, // node 27
			17, // node 28
			29, // node 29
			13, // node 30
			22, // node 31
			25, // node 32
			28, // node 33
			27, // node 34
			19, // node 35
			10, // node 36
			12, // node 37
			14, // node 38
			24, // node 39
			16, // node 40
			33, // node 41
			15, // node 42
			11, // node 43
			18, // node 44
			17, // node 45
			21, // node 46
			27, // node 47
			19, // node 48
			20, // node 49
			5, // node 50
			22, // node 51
			12, // node 52
			19, // node 53
			22, // node 54
			16, // node 55
			7, // node 56
			26, // node 57
			14, // node 58
			21, // node 59
			24, // node 60
			13, // node 61
			15, // node 62
			18, // node 63
			11, // node 64
			28, // node 65
			9, // node 66
			37, // node 67
			30, // node 68
			10, // node 69
			8, // node 70
			11, // node 71
			3, // node 72
			1, // node 73
			6, // node 74
			10, // node 75
			20 };// node 76

}
