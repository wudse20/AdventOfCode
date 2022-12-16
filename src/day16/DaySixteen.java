package day16;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class DaySixteen
{
    public static void main(String[] args) throws IOException
    {
        System.out.printf("Part 1: %d%n", part1());
    }

    static Set<String> openableValves(Map<String,Valve> valves)
    {
        var result = new HashSet<String>();

        for (var valve : valves.values())
        {
            if (valve.flow > 0)
                result.add(valve.name());

        }

        return result;
    }

    static HashMap<String, Integer> findShortest(Map<String,Valve> valves, Set<String> openable)
    {
        var shortestPath = new HashMap<String, Integer>();
        var startingValves = new HashSet<>(openable);
        startingValves.add("AA");

        for (var valveName: startingValves)
        {
            var valve = valves.get(valveName);
            for (var dest: valves.values())
                shortestPath.put(valveName + dest.name(), 999);

            shortestPath.put(valveName + valveName, 0);
            var pq = new PriorityQueue<>(Comparator.comparingInt(Journey::time));
            pq.add(new Journey(0, valveName, null, 0));

            while(pq.size() >0)
            {
                var here = pq.remove();
                for (var destName: valves.get(here.location()).neighbours)
                {
                    var routeName = valveName + destName;

                    if (here.time() + 1 < 30 && shortestPath.get(routeName) > here.time() + 1)
                    {
                        shortestPath.put(routeName, here.time() + 1);
                        pq.add(new Journey(here.time() + 1, destName, null, 0));
                    }
                }
            }
        }

        return shortestPath;
    }

    static int part1() throws IOException
    {
        var lines = getFileContent("./src/day16/input.txt");
        var valves = new HashMap<String, Valve>();

        for (var l : lines)
        {
            var middle = l.split(";");
            var first = middle[0].split(" ");
            var name = first[1];
            var flowRate = Integer.parseInt(first[4].split("=")[1]);
            var neighbours =
                middle[1].contains("valves") ?
                middle[1].split("valves")[1].trim().split(", ") :
                new String[] { middle[1].trim().split("\\s+")[4] };

            valves.put(name, new Valve(name, flowRate, neighbours));
        }

        var openable = openableValves(valves);
        var shortest = findShortest(valves, openable);

        var res = 0;
        var journeys = new LinkedList<Journey>();
        journeys.add(new Journey(0, "AA", new HashSet<>(), 0));
        while (journeys.size() > 0)
        {
            var journey = journeys.remove();
            var openableHere = new HashSet<>(openable);
            openableHere.removeAll(journey.opened);
            var fpm = 0;

            for (var flowing : journey.opened)
                fpm += valves.get(flowing).flow;

            for (var dest : openableHere)
            {
                var tt = shortest.get(journey.location + dest) + 1;

                if (journey.time + tt < 30)
                {
                    var nowOpen = new HashSet<>(journey.opened);
                    nowOpen.add(dest);
                    journeys.add(new Journey(
                        journey.time() + tt, dest,
                        nowOpen, journey.score() + tt * fpm
                    ));
                }
            }

            var doNothing = journey.score + (30 - journey.time) * fpm;
            res = Math.max(doNothing, res);
        }

        return res;
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

    record Journey(int time, String location, Set<String> opened, int score) {}
    private record Valve(String name, int flow, String[] neighbours)
    {
        @Override
        public String toString()
        {
            var sb = new StringBuilder();
            for (var n : neighbours)
                sb.append(n).append(", ");

            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);

            return "Valve[name=%s, flow=%d, neighbours=[%s]]".formatted(name, flow, sb);
        }
    }
}
