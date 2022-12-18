package day18;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DayEighteen
{
    public static void main(String[] args) throws IOException
    {
        System.out.printf("Part 1: %d%n", part1("./src/day18/input.txt"));
    }

    static int part1(String path) throws IOException
    {
        var lines = getFileContent(path);
        var al = new ArrayList<Pos>();

        for (var l : lines)
        {
            var split = l.split(",");
            al.add(new Pos(
                Integer.parseInt(split[0]),
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2])
            ));
        }

        var cubes = new HashSet<>(al);
        var res = new AtomicInteger();
        cubes.forEach(cube -> {
            cube.getAdjacentPositions().forEach(adj -> {
                if (!cubes.contains(adj))
                    res.getAndIncrement();
            });
        });

        return res.get();
    }

    static List<String> getFileContent(String pathname) throws IOException
    {
        var f = new File(pathname);
        var br = new BufferedReader(new FileReader(f));
        var res = new ArrayList<String>();

        String line;
        while ((line = br.readLine()) != null)
            res.add(line.replaceAll("\r", ""));

        br.close();
        return res;
    }

    private record Pos(int x, int y, int z)
    {
        List<Pos> getAdjacentPositions()
        {
            return List.of(
                new Pos(x + 1, y, z),
                new Pos(x - 1, y, z),
                new Pos(x, y + 1, z),
                new Pos(x, y - 1, z),
                new Pos(x, y, z + 1),
                new Pos(x, y, z - 1)
            );
        }
    }
}
