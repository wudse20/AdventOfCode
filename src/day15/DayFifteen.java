package day15;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class DayFifteen
{
    public static void main(String[] args) throws IOException
    {
        System.out.printf("Part 1: %d%n", part1());
    }

    static int part1() throws IOException
    {
        var sensors = new ArrayList<Sensor>();
        var beacons = new HashSet<Pos>();
        var input =
            getFileContent("./src/day15/input.txt").replaceAll("\r", "")
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
            sensors.add(new Sensor(new Pos(xSensor, ySensor), new Pos(xBeacon, yBeacon)));
            beacons.add(new Pos(xBeacon, yBeacon));
        }

        var targetY = 2_000_000;
        var set = new HashSet<Pos>();
        for (var s : sensors)
        {
            var distY = s.distanceToY(targetY);
            var distSens = s.distance();

            if (distY <= distSens)
            {
                var delta = distSens - distY;

                for (int i = -delta; i <= delta; i++)
                    set.add(new Pos(i + s.position.x, targetY));
            }
        }

        set.removeAll(beacons);
//        set.stream().sorted(Comparator.comparingInt(a -> a.x)).forEachOrdered(System.out::println);
        return set.size();
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
}
