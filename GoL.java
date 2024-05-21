import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

public class GoL {
    private final int rows;
    private final int cols;
    private int[] world;    
    int cellSize;

    public GoL(int rows, int cols) {
        this.cellSize = 10;
        this.rows = rows;
        this.cols = cols;
        this.world = new int[rows * cols];
        
    }

    
    public GoL set(int row, int col) {
        world[row * cols + col] = 1;
        return this;
    }

    // Methode, um den Zustand der Welt zu berechnen
    public void nextGeneration() {
        int[] newWorld = new int[rows * cols];

        IntUnaryOperator nextState = index -> {
            int row = index / cols;
            int col = index % cols;
            int aliveNeighbors = countAliveNeighbors(row, col);

            if (world[index] == 1) {
                return (aliveNeighbors < 2 || aliveNeighbors > 3) ? 0 : 1;
            } else {
                return (aliveNeighbors == 3) ? 1 : 0;
            }
        };

        IntStream.range(0, world.length).forEach(index -> newWorld[index] = nextState.applyAsInt(index));

        world = newWorld;
    }

    // Methode, um die Anzahl der lebenden Nachbarn zu zählen
    private int countAliveNeighbors(int row, int col) {
        return (int) IntStream.rangeClosed(-1, 1).boxed()
                .flatMap(i -> IntStream.rangeClosed(-1, 1).mapToObj(j -> new int[] { i, j }))
                .filter(offset -> !(offset[0] == 0 && offset[1] == 0))
                .map(offset -> new int[] { row + offset[0], col + offset[1] })
                .filter(neighbor -> neighbor[0] >= 0 && neighbor[0] < rows && neighbor[1] >= 0 && neighbor[1] < cols)
                .mapToInt(neighbor -> world[neighbor[0] * cols + neighbor[1]])
                .sum();
    }

    // Methode, um die Welt auszugeben, gedacht für die cmd
    public void printWorld() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                System.out.print(world[row * cols + col] + " ");
            }
            System.out.println();
        }
    }

    public String drawWorldAsString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int state = world[row * cols + col];
                sb.append(state == 1 ? "1  " : "0  "); 
            }
            sb.append("\r\n"); 
        }
        return sb.toString();
    }

    public void run(int iteration){
        Clerk.markdown(drawWorldAsString());
        for (int i = 0; i < iteration; i++) {
            Clerk.markdown("## Gen " + (i+1));
            nextGeneration();
            Clerk.markdown(drawWorldAsString());
    
        }
    }

}


Clerk.clear();
Clerk.markdown("## Start:");
GoL world = new GoL(3,3).set(0,0).set(1,0).set(1,2).set(0,2).set(2,2);
world.run(3);