package day12;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class DayTwelve
{
    public static void main(String[] args) throws IOException
    {
        System.out.printf("Part 1: %d%n", part1());
        System.out.printf("Part 2: %d%n", part2());
    }

    private static int part1() throws IOException
    {
        var grid = prepGrid("./src/day12/input.txt");
        return find(grid, find(grid, 'S'));
    }

    private static int part2() throws IOException
    {
        var grid = prepGrid("./src/day12/input.txt");
        var starts = findAll(grid, 'a', 'S');
        return starts.stream()
                     .mapToInt(s -> find(grid, s))
                     .min()
                     .orElse(Integer.MAX_VALUE);
    }

    private static int[][] prepGrid(String pathname) throws IOException
    {
        return Arrays.stream(getFileContent(pathname).split("\n"))
                     .map(s -> s.chars().toArray())
                     .toArray(int[][]::new);
    }


    private static Pos find(int[][] grid, char target)
    {
        for (int i = 0; i < grid.length; i++)
        {
            for (int ii = 0; ii < grid[i].length; ii++)
            {
                if (grid[i][ii] == target)
                    return new Pos(i, ii);
            }
        }

        return null;
    }

    private static List<Pos> findAll(int[][] grid, char... target)
    {
        var res = new ArrayList<Pos>();

        for (int i = 0; i < grid.length; i++)
        {
            for (int ii = 0; ii < grid[i].length; ii++)
            {
                for (var t : target)
                {
                    if (grid[i][ii] == t)
                        res.add(new Pos(i, ii));
                }
            }
        }

        return res;
    }

    private static int find(int[][] grid, Pos start)
    {
        var visited = new HashSet<Pos>();
        var currentLevel = new HashSet<Pos>();
        currentLevel.add(start);

        var steps = 0;
        while (!currentLevel.isEmpty())
        {
            var level = new HashSet<>(currentLevel);
            currentLevel.clear();

            for (var p : level)
            {
                var current = grid[p.a][p.b];
                if (current == 'S')
                    current = 'a';

                if (p.a != 0) // Up
                {
                    var p2 = new Pos(p.a - 1, p.b);
                    if ((current == 'y' || current == 'z') && grid[p2.a][p2.b] == 'E') // Found target, finished!
                        return steps + 1;

                    if (grid[p2.a][p2.b] <= current + 1 && visited.add(p2))
                        currentLevel.add(p2);
                }

                if (p.a < grid.length - 1) // Down
                {
                    var p2 = new Pos(p.a + 1, p.b);
                    if ((current == 'y' || current == 'z') && grid[p2.a][p2.b] == 'E') // Found target, finished!
                        return steps + 1;

                    if (grid[p2.a][p2.b] <= current + 1 && visited.add(p2))
                        currentLevel.add(p2);
                }

                if (p.b != 0) // Left
                {
                    var p2 = new Pos(p.a, p.b - 1);
                    if ((current == 'y' || current == 'z') && grid[p2.a][p2.b] == 'E') // Found target, finished!
                        return steps + 1;

                    if (grid[p2.a][p2.b] <= current + 1 && visited.add(p2))
                        currentLevel.add(p2);
                }

                if (p.b < grid[p.a].length - 1) // Right
                {
                    var p2 = new Pos(p.a, p.b + 1);
                    if ((current == 'y' || current == 'z') && grid[p2.a][p2.b] == 'E') // Found target, finished!
                        return steps + 1;

                    if (grid[p2.a][p2.b] <= current + 1 && visited.add(p2))
                        currentLevel.add(p2);
                }
            }
            steps++;
        }

        return Integer.MAX_VALUE;
    }

    private static String getFileContent(String pathname) throws IOException
    {
        var f = new File(pathname);
        var br = new BufferedReader(new FileReader(f));
        var sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null)
            sb.append(line).append("\n");

        br.close();
        return sb.toString().replace("\r", "");
    }

    private record Pos(int a, int b) {}
}
