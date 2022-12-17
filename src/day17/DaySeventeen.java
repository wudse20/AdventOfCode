package day17;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DaySeventeen
{
    public static void main(String[] args) throws IOException
    {
        System.out.printf("Part 1: %d%n", part1(2022));
        System.out.printf("Part 2: %d%n", part2());
    }

    static long part1(long sims) throws IOException
    {
        var streams = parse("./src/day17/input.txt");
        var used = new HashSet<Pos>();
        var figure = new HashSet<Pos>();
        var startY = 4L;
        var figCount = 0L;

        for (int i = 0; i < 7; i++)
            used.add(new Pos(i, 0));

        for (int i = 0; i < sims; i++)
        {
            addFigure(figure, (int) (figCount++ % 5), startY);

            while (true)
            {
//                visualize(used, figure);
                // Jets
                var jets = streams.next();
                var allowed =
                    figure.parallelStream()
                          .map(b -> jets == '<' ? b.moveLeft() : b.moveRight())
                          .allMatch(p -> p.x >= 0 && p.x < 7);

                allowed &= figure.parallelStream()
                                 .map(b -> jets == '<' ? b.moveLeft() : b.moveRight())
                                 .noneMatch(used::contains);

                if (allowed)
                {
                    figure = figure.parallelStream()
                                   .map(b -> jets == '<' ? b.moveLeft() : b.moveRight())
                                   .collect(Collectors.toCollection(HashSet::new));
                }

//                visualize(used, figure);

                // Gravity
                allowed = figure.parallelStream()
                                .map(Pos::moveDown)
                                .noneMatch(used::contains);

                if (allowed)
                {
                    figure = figure.parallelStream()
                                   .map(Pos::moveDown)
                                   .collect(Collectors.toCollection(HashSet::new));
                }
                else
                {
                    used.addAll(figure);
                    break;
                }
            }

            startY = used.parallelStream().mapToLong(Pos::y).max().orElse(Integer.MAX_VALUE) + 4;
        }

        return used.parallelStream().mapToLong(Pos::y).max().orElse(Integer.MAX_VALUE);
    }

    static void visualize(Set<Pos> used, Set<Pos> fig)
    {
        var maxY = used.parallelStream().mapToLong(Pos::y).reduce(Integer.MIN_VALUE, Math::max);
        maxY = Math.max(maxY, fig.parallelStream().mapToLong(Pos::y).reduce(Integer.MIN_VALUE, Math::max));
        var sb = new StringBuilder();

        for (var y = maxY; y >= 0; y--)
        {
            sb.append(y != 0 ? '|' : '+');
            for (int x = 0; x < 7; x++)
            {
                var p = new Pos(x, y);
                if (used.contains(p))
                    sb.append(y != 0 ? '#' : '-');
                else if (fig.contains(p))
                    sb.append('@');
                else
                    sb.append('.');
            }
            sb.append(y != 0 ? '|' : '+').append('\n');
        }

        System.out.println(sb);
    }

    static void addFigure(Set<Pos> figure, int index, long y)
    {
        figure.clear();

        switch (index)
        {
            // XXXX
            case 0 ->
            {
                figure.add(new Pos(2, y));
                figure.add(new Pos(3, y));
                figure.add(new Pos(4, y));
                figure.add(new Pos(5, y));
            }
            //  X
            // XXX
            //  X
            case 1 ->
            {
                figure.add(new Pos(3, y + 2));
                figure.add(new Pos(2, y + 1));
                figure.add(new Pos(3, y + 1));
                figure.add(new Pos(4, y + 1));
                figure.add(new Pos(3, y));
            }
            //   X
            //   X
            // XXX
            case 2 ->
            {
                figure.add(new Pos(4, y + 2));
                figure.add(new Pos(4, y + 1));
                figure.add(new Pos(2, y));
                figure.add(new Pos(3, y));
                figure.add(new Pos(4, y));
            }
            // X
            // X
            // X
            // X
            case 3 ->
            {
                figure.add(new Pos(2, y + 3));
                figure.add(new Pos(2, y + 2));
                figure.add(new Pos(2, y + 1));
                figure.add(new Pos(2, y));
            }
            // XX
            // XX
            case 4 ->
            {
                figure.add(new Pos(2, y + 1));
                figure.add(new Pos(3, y + 1));
                figure.add(new Pos(2, y));
                figure.add(new Pos(3, y));
            }
            default -> System.err.printf("Unexpected index: %d%n", index);
        }
    }

    static JetStream parse(String pathname) throws IOException
    {
        var f = new File(pathname);
        var bis = new BufferedInputStream(new FileInputStream(f));
        var bytes = bis.readAllBytes();
        bis.close();

        var al = new ArrayList<Character>();

        for (var b : bytes)
        {
            if (b == '<' || b == '>')
                al.add((char) b);
        }

        return new JetStream(al);
    }

    public static long part2() throws IOException
    {
        var chars =
            parse("./src/day17/input.txt").stream
                                                   .toString()
                                                   .replaceAll(",", "")
                                                   .replaceAll(" ", "")
                                                   .replaceAll("\\[", "")
                                                   .replaceAll("]", "")
                                                   .toCharArray();

        var numberOfRocks = 1000000000000L;
        var cycleStart = sim(numberOfRocks, chars, s -> s.cycleReset && s.fallenRocks != 0);
        var nextCycle = sim(numberOfRocks, chars, s -> s.cycleReset && s.fallenRocks > cycleStart.fallenRocks);
        var rocksPerCycle = nextCycle.fallenRocks - cycleStart.fallenRocks;
        var numberOfCycles = numberOfRocks / rocksPerCycle;
        var totalRocks = rocksPerCycle * numberOfCycles + cycleStart.fallenRocks;
        var heightPerCycle = nextCycle.height - cycleStart.height;
        var totalHeight = heightPerCycle * numberOfCycles + cycleStart.height;
        var overshoot = totalRocks - numberOfRocks;
        var atOvershoot = sim(numberOfRocks, chars, s -> s.fallenRocks == cycleStart.fallenRocks - overshoot);
        return totalHeight - (cycleStart.height - atOvershoot.height);
    }

    private static State sim(long iterations, char[] chars, Predicate<State> exitCondition)
    {
        var shapes = new Grid[] {
            new Grid(new char[][]{ "####".toCharArray()} ),
            new Grid(new HashMap<>(Map.of(
                new Pos(1, 0), '#', new Pos(0, 1), '#',
                new Pos(1, 1), '#', new Pos(2, 1), '#',
                new Pos(1, 2), '#'
            ))),
            new Grid(new HashMap<>(Map.of(
                new Pos(2, 0), '#', new Pos(0, 2), '#',
                new Pos(1, 2), '#', new Pos(2, 2), '#',
                new Pos(2, 1), '#'
            ))),
            new Grid(new char[][] { { '#' }, { '#' }, { '#' }, { '#' } }),
            new Grid(new char[][] { { '#', '#' }, { '#', '#' } })
        };

        var g = new Grid(new char[][]{"+-------+".toCharArray()});
        addWall(g, 4);

        var highest = 0L;
        var fallenRocks = 0L;
        var s = shapes[0].move(3, highest - 4);

        for(int i = 0, shapeIndex = 0; fallenRocks < iterations; i++)
        {
            if(i >= chars.length) i = 0;

            var state = new State(Math.abs(highest), fallenRocks, i==0);
            if (exitCondition.test(state))
                return state;

            var c = chars[i];
            var moved = s.move(c == '>' ? 1 : -1, 0);
            if (g.canPlace(moved))
                s = moved;

            moved = s.move(0, 1);
            if (g.canPlace(moved))
            {
                s = moved;
            }
            else
            {
                g.place(s);
                shapeIndex = (shapeIndex + 1) % shapes.length;
                var oldHighest = highest;
                highest = Math.min(s.minY(), highest);
                addWall(g, Math.toIntExact((oldHighest - highest) + shapes[shapeIndex].height()));
                s = shapes[shapeIndex].move(3, highest - 3 - shapes[shapeIndex].height());
                fallenRocks++;
            }
        }

        return new State(Math.abs(highest), iterations, false);
    }

    private static void addWall(Grid g, int n)
    {
        var lowestY = g.minY()-1;
        for (int i = 0; i<n; i++)
        {
            g.set(new Pos(0, lowestY-i), '|');
            g.set(new Pos(8, lowestY-i), '|');
        }
    }

    public record State(long height, long fallenRocks, boolean cycleReset) {}
    private record Pair<A, B>(A a, B b) {}

    private record Pos(long x, long y)
    {
        Pos moveDown()
        {
            return new Pos(x, y - 1);
        }

        Pos moveLeft()
        {
            return new Pos(x - 1, y);
        }

        Pos moveRight()
        {
            return new Pos(x + 1, y);
        }

        Pos move(long x, long y)
        {
            return new Pos(x() + x, y() + y);
        }
    }

    private static class JetStream
    {
        private int count;
        private final List<Character> stream;

        private JetStream(List<Character> stream)
        {
            this.stream = stream;
            this.count = 0;
        }

        private char next()
        {
            count = count % stream.size();
            var val = stream.get((int) count);
            count++;
            return val;
        }
    }

    public static class Grid
    {
        private final Map<Pos, Character> grid;

        public Grid(char[][] g)
        {
            this();
            for (int i = 0; i < g.length; i++)
            {
                for (int ii = 0; ii < g[0].length; ii++)
                    grid.put(new Pos(ii, i), g[i][ii]);
            }
        }

        public Grid(Map<Pos, Character> grid)
        {
            this.grid = grid;
        }

        public Grid()
        {
            this.grid = new HashMap<>();
        }

        public void set(Pos p, char c)
        {
            grid.put(p, c);
        }

        public boolean canPlace(Grid g)
        {
            return g.grid
                    .keySet()
                    .parallelStream()
                    .noneMatch(grid::containsKey);
        }

        public long minY()
        {
            return grid.keySet()
                       .parallelStream()
                       .mapToLong(e -> e.y)
                       .min()
                       .orElse(0);
        }

        public long maxY()
        {
            return grid.keySet()
                       .parallelStream()
                       .mapToLong(e -> e.y)
                       .max()
                       .orElse(0);
        }

        public Grid move(long x, long y)
        {
            return new Grid(
                grid.entrySet()
                    .parallelStream()
                    .map(e -> new Pair<>(e.getKey().move(x, y), e.getValue()))
                    .collect(Collectors.toMap(Pair::a, Pair::b)));
        }

        public void place(Grid s)
        {
            s.grid.forEach(this::set);
        }

        public long height()
        {
            return maxY() + 1 - minY();
        }

    }
}
