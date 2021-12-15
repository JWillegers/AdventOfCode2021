package Day15;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Solution {
    protected int nOfLines = 100;
    private int[][] arrayp1 = new int[nOfLines][nOfLines];
    private int[][] arrayp2 = new int[5*nOfLines][5*nOfLines];

    public static void main(String[] args) {
        Solution part = new Solution();
        part.setup();
        part.solution(part.arrayp1, 1);
        part.solution(part.arrayp2, 5);
    }

    public int increase(int number) {
        int newNumber = number + 1;
        if (newNumber > 9) {
            return 1;
        } else {
            return newNumber;
        }
    }

    public void setup() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/Day15/input.txt"));
            for (int i = 0; i < nOfLines; i++) {
                String line = reader.readLine();
                for (int j = 0; j < nOfLines; j++) {
                    int number = Integer.parseInt(String.valueOf(line.charAt(j)));
                    arrayp1[i][j] = number;
                    for (int m = 0; m < 5; m++) {
                        int extraNumber = number;
                        for (int n = 0; n < 5; n++) {
                            arrayp2[i + m * nOfLines][j + n * nOfLines] = extraNumber;
                            extraNumber = increase(extraNumber);
                        }
                        number = increase(number);
                    }

                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void solution(int[][] array, int max) {
        int idCounter = 1;
        boolean run = true;
        Map<Integer, Path> allPaths = new HashMap<>();
        boolean[][] start = new boolean[max*nOfLines][max*nOfLines];
        start[0][0] = true;
        allPaths.put(0, new Path(0,0, 0, start));
        while (run) {
            int idOfCurrentLowestRisk = -1;
            int currentLowestRisk = nOfLines*nOfLines*10;
            for (Integer id : allPaths.keySet()) {
                if (allPaths.get(id).risk < currentLowestRisk) {
                    currentLowestRisk = allPaths.get(id).risk;
                    idOfCurrentLowestRisk = id;
                }
            }

            for (int i = 0; i < 4; i++) {
                Path myPath = allPaths.get(idOfCurrentLowestRisk);
                Path newPath = new Path();
                newPath.risk = myPath.risk;
                newPath.x = myPath.x;
                newPath.y = myPath.y;
                newPath.visited = new boolean[max * nOfLines][max * nOfLines];
                for (int j = 0; j < max * nOfLines; j++) {
                    for (int k = 0; k < max * nOfLines; k++) {
                        newPath.visited = myPath.visited;
                    }
                }
                int number = -1;
                if (i == 0) {
                    try {
                        if (!myPath.visited[myPath.y][myPath.x - 1]) {
                            number = array[myPath.y][myPath.x - 1];
                            newPath.risk += number;
                            newPath.x--;
                            newPath.visited[myPath.y][myPath.x - 1] = true;
                        }
                    } catch (IndexOutOfBoundsException ignore) {}
                } else if (i == 1) {
                    try {
                        if (!myPath.visited[myPath.y ][myPath.x + 1]) {
                            number = array[myPath.y][myPath.x + 1];
                            newPath.risk += number;
                            newPath.visited[myPath.y ][myPath.x + 1] = true;
                            newPath.x++;
                        }
                    } catch (IndexOutOfBoundsException ignore) {}
                } else if (i == 2) {
                    try {
                        if (!myPath.visited[myPath.y + 1][myPath.x]) {
                            number = array[myPath.y + 1][myPath.x];
                            newPath.risk += number;
                            newPath.visited[myPath.y + 1][myPath.x] = true;
                            newPath.y++;
                        }
                    } catch (IndexOutOfBoundsException ignore) {}
                } else {
                    try {
                        if (!myPath.visited[myPath.y - 1][myPath.x]) {
                            number = array[myPath.y - 1][myPath.x];
                            newPath.risk += number;
                            newPath.visited[myPath.y - 1][myPath.x] = true;
                            newPath.y--;
                        }
                    } catch (IndexOutOfBoundsException ignore) {}
                }

                if (number != -1) {
                    allPaths.put(idCounter, newPath);
                    idCounter++;
                }
                if (newPath.x == max * nOfLines - 1 && newPath.y == max * nOfLines - 1) {
                    System.out.println(newPath.risk);
                    run = false;
                }
            }
            allPaths.remove(idOfCurrentLowestRisk);
        }


    }
}
