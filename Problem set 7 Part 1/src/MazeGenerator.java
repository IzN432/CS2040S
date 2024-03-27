import java.util.Arrays;
import java.util.Random;

public class MazeGenerator {

	private static final Random rng = new Random();
	private static class InProgressRoom {
		// North South East West
		private boolean visited = false;
		private final boolean[] wallArray = new boolean[4];
		public InProgressRoom() {
			Arrays.fill(wallArray, true);
		}
		public void setWall(int direction, boolean wallPresence) {
			wallArray[direction] = wallPresence;
		}
		public Room getRoom() {
			return new Room(wallArray[0], wallArray[1], wallArray[2], wallArray[3]);
		}
		public boolean visited() {
			return visited;
		}
		public void setVisited() {
			visited = true;
		}
	}
	private MazeGenerator() { }

	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static final int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 1, 0 }, // South
			{ 0, 1 }, // East
			{ 0, -1 } // West
	};

	// TODO: Feel free to modify the method parameters.
	public static Maze generateMaze(int rows, int columns) {
		InProgressRoom[][] roomMatrix = new InProgressRoom[rows][columns];
		for (InProgressRoom[] row : roomMatrix) {
			for (int i = 0; i < row.length; i++) {
				row[i] = new InProgressRoom();
			}
		}
		randomTreeTraversal(0, 0, -1, roomMatrix);
		Room[][] finalRoomArray = new Room[rows][columns];
		for (int row = 0; row < roomMatrix.length; row++) {
			for (int col = 0; col < roomMatrix[0].length; col++) {
				finalRoomArray[row][col] = roomMatrix[row][col].getRoom();
			}
		}
		return new Maze(finalRoomArray);
	}

	private static int flipDirection(int origin) {
		switch(origin) {
			case 0:
				return 1;
			case 1:
				return 0;
			case 2:
				return 3;
			case 3:
				return 2;
		}
		return -1;
	}

	private static void randomTreeTraversal(int row, int col, int originDirection, InProgressRoom[][] roomMatrix) {
		// Pick a random direction
		int random = rng.nextInt(4);
		int direction;

		// If originDirection is not -1, open up the wall
		if (originDirection != -1) {
			roomMatrix[row][col].setWall(originDirection, false);
		}

		// Set this guy to visited
		roomMatrix[row][col].setVisited();

		// Check if the direction is visited already
		// If visited, pick another direction until it is not visited
		for (int i = 0; i < 4; i++) {
			direction = (random + i) % 4;
			int newRow = row + DELTAS[direction][0];
			int newCol = col + DELTAS[direction][1];

			if (newRow < 0 || newRow >= roomMatrix.length || newCol < 0 || newCol >= roomMatrix[0].length ||
					roomMatrix[newRow][newCol].visited()) {
				continue;
			}

			// Open up the wall leading to the next guy
			roomMatrix[row][col].setWall(direction, false);

			// Recurse on the next guy
			randomTreeTraversal(newRow, newCol, flipDirection(direction), roomMatrix);
		}

		// If all of its neighbours have been visited, you have failed
	}
}
