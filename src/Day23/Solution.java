package Day23;

import java.util.Arrays;
import java.util.HashMap;

public class Solution {
    private HashMap<Character, Integer> costs = new HashMap<>();
    private HashMap<Character, Integer> column = new HashMap<>();
    private int lowestCost = 1000000000;

    public static void main(String[] args) {
        Solution sol = new Solution();

        sol.setup();
    }

    public void setup() {
        costs.put('A', 1);
        costs.put('B', 10);
        costs.put('C', 100);
        costs.put('D', 1000);
        column.put('A', 2);
        column.put('B', 4);
        column.put('C', 6);
        column.put('D', 8);

        /*
            00000000001
            01234567890
            ...........
            ##D#B#B#A##
            ##C#C#D#A##
         */
        char[][] burrow = new char[3][11];
        burrow[1][8] = 'A';
        burrow[2][8] = 'A';
        burrow[1][4] = 'B';
        burrow[1][6] = 'B';
        burrow[2][2] = 'C';
        burrow[2][4] = 'C';
        burrow[1][2] = 'D';
        burrow[2][6] = 'D';

        for (int j = 1; j <= 2; j++){
            for (int i = 0; i <= 10; i++) {
                if (burrow[j][i] == '\0') {
                    burrow[j][i] = '#';
                }
                burrow[0][i] = '.';

            }
        }
        solution(burrow, 0);
        System.out.println(lowestCost);
    }

    /*
        step stop it from running infinitely
     */
    public void solution(char[][] burrow, int cost) {
        if (burrow[1][2] == 'A' && burrow[2][2] == 'A'
        && burrow[1][4] == 'B' && burrow[2][4] == 'B'
        && burrow[1][6] == 'C' && burrow[2][6] == 'C'
        && burrow[1][8] == 'D' && burrow[2][8] == 'D') {
            if (cost < lowestCost) {
                lowestCost = cost;
            }
        } else {
            for (int i = 0; i < burrow.length; i++) {
                for (int j = 0; j < burrow[0].length; j++) {
                    char current = burrow[i][j];
                    if (current != '#' && current != '.') {
                        if ((!(j == 2 && current == 'A')
                            && !(j == 4 && current == 'B')
                            && !(j == 6 && current == 'C')
                            && !(j == 8 && current == 'D'))
                            || (i == 1 && burrow[2][j] != current)) {
                            if (i == 0) {
                                toFinalDestination(i, j, current, burrow, cost);
                            } else {
                                if (i == 1 || burrow[1][j] == '.') {
                                    if (!toFinalDestination(i, j, current, burrow, cost)) {
                                        for (int m = 0; m < burrow[0].length; m++) {
                                            if ((m % 2 == 1 || m == 0 || m == burrow[0].length - 1) && burrow[0][m] == '.') {
                                                if (noObstacle(burrow, m, j)) {
                                                    int newCost = cost;
                                                    char[][] newBurrow = new char[burrow.length][];
                                                    for (int p = 0; p < burrow.length; p++) {
                                                        newBurrow[p] = Arrays.copyOf(burrow[p], burrow[p].length);
                                                    }
                                                    newBurrow[i][j] = '.';
                                                    newBurrow[0][m] = current;
                                                    newCost += costs.get(current) * (Math.abs(m - j) + i);
                                                    solution(newBurrow, newCost);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean noObstacle(char[][] burrow, int dest, int start) {
        if (start - dest > 0) {
            for (int i = start - 1; i >= dest; i--) {
                if (burrow[0][i] != '.') {
                    return false;
                }
            }
        } else {
            for (int i = start + 1; i <= dest; i++) {
                if (burrow[0][i] != '.') {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean toFinalDestination(int i, int j, char current, char[][] burrow, int cost) {
        char[][] newBurrow = new char[burrow.length][];
        for (int p = 0; p < burrow.length; p++) {
            newBurrow[p] = Arrays.copyOf(burrow[p], burrow[p].length);
        }
        int newCost = cost;
        newBurrow[i][j] = '.';

        int destColumn = column.get(current);
        if (noObstacle(burrow, destColumn, j)){
            if (burrow[2][destColumn] == '.') {
                newBurrow[2][destColumn] = current;
                newCost += costs.get(current) * (2 + i + Math.abs(j - destColumn));
                solution(newBurrow, newCost);
                return true;
            } else if (burrow[2][destColumn] == current && burrow[1][destColumn] == '.') {
                newBurrow[1][destColumn] = current;
                newCost += costs.get(current) * (1 + i + Math.abs(j - destColumn));
                solution(newBurrow, newCost);
                return true;
            }
        }
        return false;
    }
}
