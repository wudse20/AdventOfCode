package day15;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DayFifteen
{
    public static final List<Sensor> SENSORS = new ArrayList<>();
    public static final HashSet<Pos> BEACONS = new HashSet<>();
    public static final boolean TEST = false;

    public static void main(String[] args) throws IOException
    {
        setupData(TEST ? "./src/day15/test.txt" : "./src/day15/input.txt");
        System.out.printf("Part 1: %d%n", part1(TEST ? 10 : 2_000_000).size());
        System.out.printf("Part 2: %d%n", part2(TEST ? 20 : 4_000_000, TEST ? 20 : 4_000_000));
    }

    static Set<Pos> part1(int y)
    {
        var set = new HashSet<Pos>();
        for (var s : SENSORS)
        {
            var distY = s.distanceToY(y);
            var distSens = s.distance();

            if (distY <= distSens)
            {
                var delta = distSens - distY;

                for (int i = -delta; i <= delta; i++)
                    set.add(new Pos(i + s.position.x, y));
            }
        }

        set.removeAll(BEACONS);
        set.removeAll(SENSORS.stream().map(Sensor::position).collect(Collectors.toSet()));
//        set.stream().sorted(Comparator.comparingInt(a -> a.x)).forEachOrdered(System.out::println);
        return set;
    }

    static long part2(int xMax, int yMax)
    {
        for (var y = 0; y < yMax; y++)
        {
            var ranges = new ArrayList<Range>();
            for (var s : SENSORS)
            {
                var dx = s.distance() - Math.abs(y - s.position.y);
                if (dx >= 0) {
                    ranges.add(new Range(s.position.x - dx, s.position.x + dx));
                }
            }

            for (var x = 0; x < xMax; )
            {
                var xx = x;
                var range =
                    ranges.stream()
                          .filter(r -> r.contains(xx))
                          .findFirst();

                if (range.isPresent()) {
                    x = range.get().high + 1;
                } else {
                    return (long) x * 4_000_000 + y;
                }
            }
        }

        return -1;
    }

    private static void setupData(String path) throws IOException
    {
        var input =
            getFileContent(path).replaceAll("\r", "")
                                .split("\n");

        for (var line : input)
        {
            var split = line.split(":");
            var sLine = split[0].split("\\s+");
            var xSensor = Integer.parseInt(sLine[2].substring(2, sLine[2].length() - 1));
            var ySensor = Integer.parseInt(sLine[3].substring(2));

            var bLine = split[1].trim().split("\\s+");
            var xBeacon = Integer.parseInt(bLine[4].substring(2, bLine[4].length() - 1));
            var yBeacon = Integer.parseInt(bLine[5].substring(2));
            SENSORS.add(new Sensor(new Pos(xSensor, ySensor), new Pos(xBeacon, yBeacon)));
            BEACONS.add(new Pos(xBeacon, yBeacon));
        }
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

    private record Pos(int x, int y)
    {
        int distance(Pos p)
        {
            return Math.abs(x - p.x) + Math.abs(y - p.y);
        }
    }

    private record Sensor(Pos position, Pos closestBeacon)
    {
        int distance()
        {
            return position.distance(closestBeacon);
        }

        int distanceToY(int y)
        {
            return position.distance(new Pos(position.x, y));
        }
    }

    private record Range(int low, int high)
    {
        boolean contains(int num)
        {
            return num >= low && num <= high;
        }
    }
}
