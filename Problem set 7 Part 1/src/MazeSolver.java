import java.util.NoSuchElementException;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Arrays;

public class MazeSolver implements IMazeSolver {

	private static class PairList<T> {
		private final PairList<T> tail;
		private final T head;
		private static final PairList<?> EMPTY = new EmptyPairList();

		private PairList(T head, PairList<T> tail) {
			this.head = head;
			this.tail = tail;
		}
		public boolean isNotEmpty() {
			return this != EMPTY;
		}
		public T get() {
			return head;
		}
		public PairList<T> tail() {
			return tail;
		}
		public PairList<T> append(T t) {
			return new PairList<>(t, this);
		}
		public static <T> PairList<T> of(T t) {
			// this is okay because EMPTY is empty
			@SuppressWarnings("unchecked")
			PairList<T> emptyTail = (PairList<T>) EMPTY;
			return new PairList<>(t, emptyTail);
		}
		private static class EmptyPairList extends PairList<Object> {
			private EmptyPairList() {
				super(null, null);
			}
			@Override
			public Object get() {
				throw new NoSuchElementException();
			}
			@Override
			public PairList<Object> tail() {
				throw new NoSuchElementException();
			}

		}
	}
	private static class RoomValue {
		private final int row;
		private final int col;
		private final Maze maze;
		public RoomValue(int row, int col, Maze maze) {
			this.row = row;
			this.col = col;
			this.maze = maze;
		}
		public int row() {
			return row;
		}
		public int col() {
			return col;
		}
		public Room room() {
			return maze.getRoom(row, col);
		}
	}

	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static final int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};

	private static final int INF = Integer.MAX_VALUE;

    private HashMap<Integer, Integer> reachableRooms;

	private PairList<RoomValue> shortestPath;

	private Maze maze;

	public MazeSolver() {
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
	}

	private boolean hasWallInDirection(int direction, Room room) {
        switch (direction) {
			case NORTH:
				return room.hasNorthWall();
			case SOUTH:
				return room.hasSouthWall();
			case EAST:
				return room.hasEastWall();
			case WEST:
				return room.hasWestWall();
			default:
				throw new RuntimeException("this should never happen");
        }
    }
	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (maze == null) throw new Exception();

		if (shortestPath != null) {
			PairList<RoomValue> temp = shortestPath;
			while (temp.isNotEmpty()) {
				temp.get().room().onPath = false;
				temp = temp.tail();
			}
		}

        int[][] roomDistances = new int[maze.getColumns()][maze.getRows()];
		for (int[] roomDistance : roomDistances) {
			Arrays.fill(roomDistance, INF);
		}
		this.reachableRooms = new HashMap<>();

		Queue<PairList<RoomValue>> roomQueue = new LinkedList<>();
		Queue<PairList<RoomValue>> holding = new LinkedList<>();
		roomQueue.add(PairList.of(new RoomValue(startRow, startCol, maze)));

		int totalPathLength = 0;
        do {
            while (!roomQueue.isEmpty()) {
                PairList<RoomValue> current = roomQueue.remove();
                RoomValue latestValue = current.get();
                int currRow = latestValue.row();
                int currCol = latestValue.col();

				if (roomDistances[currRow][currCol] != INF) {
					continue;
				} else {
					roomDistances[currRow][currCol] = totalPathLength;
					reachableRooms.put(totalPathLength, reachableRooms.getOrDefault(totalPathLength, 0) + 1);
				}

				if (currRow == endRow && currCol == endCol) {
					shortestPath = current;
					PairList<RoomValue> temp = current;
					while (temp.isNotEmpty()) {
						temp.get().room().onPath = true;
						temp = temp.tail();
					}
				}

                Room latestRoom = latestValue.room();

				for (int direction = 0; direction < 4; direction++) {
					if (!hasWallInDirection(direction, latestRoom)) {
						int row = latestValue.row() + DELTAS[direction][0];
						int col = latestValue.col() + DELTAS[direction][1];
						holding.add(current.append(new RoomValue(row, col, maze)));
					}
				}
            }
            totalPathLength++;

            // swap my roomQueue and my holding
            Queue<PairList<RoomValue>> temp = roomQueue;
            roomQueue = holding;
            holding = temp;

        } while (!roomQueue.isEmpty());

		return roomDistances[endRow][endCol] == INF ? null : roomDistances[endRow][endCol];
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		if (maze == null) throw new Exception();
		return reachableRooms.get(k);
	}

	public static void main(String[] args) {
		// Do remember to remove any references to ImprovedMazePrinter before submitting
		// your code!
		try {
			Maze maze = MazeGenerator.generateMaze(20, 20);
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);
			System.out.println(solver.pathSearch(0, 0, 19, 19));
			ImprovedMazePrinter.printMaze(maze, 0, 0);

			maze = MazeGenerator.generateMaze(5, 5);
			solver.initialize(maze);
			System.out.println(solver.pathSearch(0, 0, 4, 4));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
