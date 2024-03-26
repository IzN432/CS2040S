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
	private static class Pair<T, U> {
		public T first;
		public U second;
		public Pair(T first, U second) {
			this.first = first;
			this.second = second;
		}

	}
	private static class RoomValue extends Pair<Room, Pair<Integer, Integer>> {
		public RoomValue(Room room, int row, int col) {
			super(room, new Pair<>(row, col));
		}
		public int getRow() {
			return super.second.first;
		}
		public int getCol() {
			return super.second.second;
		}
		public Room getRoom() {
			return super.first;
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
        return switch (direction) {
            case NORTH -> room.hasNorthWall();
            case SOUTH -> room.hasSouthWall();
            case EAST -> room.hasEastWall();
            case WEST -> room.hasWestWall();
            default -> throw new RuntimeException("this should never happen");
        };
    }
	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (maze == null) throw new Exception();

		if (shortestPath != null) {
			PairList<RoomValue> temp = shortestPath;
			while (temp.isNotEmpty()) {
				temp.get().getRoom().onPath = false;
				temp = temp.tail();
			}
		}

        int[][] roomDistances = new int[maze.getColumns()][maze.getRows()];
		for (int[] roomDistance : roomDistances) {
			Arrays.fill(roomDistance, INF);
		}
		this.reachableRooms = new HashMap<>();

		Room room = maze.getRoom(startRow, startCol);
		Queue<PairList<RoomValue>> roomQueue = new LinkedList<>();
		Queue<PairList<RoomValue>> holding = new LinkedList<>();
		roomQueue.add(PairList.of(new RoomValue(room, startRow, startCol)));

		int totalPathLength = 0;
        do {
            while (!roomQueue.isEmpty()) {
                PairList<RoomValue> current = roomQueue.remove();
                RoomValue latestValue = current.get();
                int currRow = latestValue.getRow();
                int currCol = latestValue.getCol();

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
						temp.get().getRoom().onPath = true;
						temp = temp.tail();
					}
				}

                Room latestRoom = latestValue.getRoom();

				for (int direction = 0; direction < 4; direction++) {
					if (!hasWallInDirection(direction, latestRoom)) {
						int row = latestValue.getRow() + DELTAS[direction][0];
						int col = latestValue.getCol() + DELTAS[direction][1];
						holding.add(current.append(new RoomValue(maze.getRoom(row, col), row, col)));
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
		return reachableRooms.getOrDefault(k, 0);
	}

	public static void main(String[] args) {
		// Do remember to remove any references to ImprovedMazePrinter before submitting
		// your code!
		try {
			Maze maze = Maze.readMaze("maze-empty.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);
			System.out.println(solver.pathSearch(1, 1, 2, 2));
			MazePrinter.printMaze(maze);
			System.out.println(solver.pathSearch(1, 0, 2, 2));
			MazePrinter.printMaze(maze);
			System.out.println(solver.pathSearch(0, 0, 3, 2));
			MazePrinter.printMaze(maze);
			System.out.println(solver.pathSearch(2, 2, 2, 2));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
