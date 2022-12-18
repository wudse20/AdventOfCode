package day18;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DayEighteen
{
    public static void main(String[] args) throws IOException
    {
        System.out.printf("Part 1: %d%n", part1("./src/day18/input.txt"));
        System.out.printf("Part 2: %d%n", part2("./src/day18/input.txt"));
    }

    static int part1(String path) throws IOException
    {
        var pos = parseData(path);

        var cubes = new HashSet<>(pos);
        var res = new AtomicInteger();
        cubes.forEach(cube -> {
            cube.getAdjacentPositions().forEach(adj -> {
                if (!cubes.contains(adj))
                    res.getAndIncrement();
            });
        });

        return res.get();
    }

    static int part2(String path) throws IOException
    {
        var minX = Integer.MAX_VALUE;
        var maxX = Integer.MIN_VALUE;
        var minY = Integer.MAX_VALUE;
        var maxY = Integer.MIN_VALUE;
        var minZ = Integer.MAX_VALUE;
        var maxZ = Integer.MIN_VALUE;
        var points = new HashSet<Pos>();
        var data = getFileContent(path);

        for (var l : data)
        {
            var split = l.split(",");
            var point = new Pos(
                Integer.parseInt(split[0]),
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2])
            );

            points.add(point);

            minX = Math.min(minX, point.x);
            maxX = Math.max(maxX, point.x);
            minY = Math.min(minY, point.y);
            maxY = Math.max(maxY, point.y);
            minZ = Math.min(minZ, point.z);
            maxZ = Math.max(maxZ, point.z);
        }

        minX--;
        maxX++;
        minY--;
        maxY++;
        minZ--;
        maxZ++;

        var current = new Pos(minX, minY, minZ);
        var external = new HashSet<>(List.of(current));
        var q = new LinkedList<>(List.of(current));

        while (!q.isEmpty())
        {
            current = q.poll();
            for (var adj : current.getAdjacentPositions())
            {
                if (
                    adj.x < minX || adj.x > maxX ||
                    adj.y < minY || adj.y > maxY ||
                    adj.z < minZ || adj.z > maxZ
                )
                {
                    continue;
                }

                if (!external.contains(adj) && !points.contains(adj))
                {
                    q.offer(adj);
                    external.add(adj);
                }
            }
        }

        var res = 0;
        for (var point : points)
        {
            for (var adj : point.getAdjacentPositions())
            {
                if (external.contains(adj))
                    res++;
            }
        }

        return res;
    }

    static List<Pos> parseData(String path) throws IOException
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

        return al;
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
