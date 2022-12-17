package day17;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DaySeventeen
{
    public static void main(String[] args) throws IOException
    {
        System.out.printf("Part 1: %d%n", part1());
    }

    static int part1() throws IOException
    {
        var streams = parse("./src/day17/input.txt");
        var used = new HashSet<Pos>();
        var figure = new HashSet<Pos>();
        var startY = 4;
        var figCount = 0;

        for (int i = 0; i < 7; i++)
            used.add(new Pos(i, 0));

        for (int i = 0; i < 2022; i++)
        {
            addFigure(figure, figCount++ % 5, startY);

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

            startY = used.stream().mapToInt(Pos::y).max().orElse(Integer.MAX_VALUE) + 4;
        }

        return used.stream().mapToInt(Pos::y).max().orElse(Integer.MAX_VALUE);
    }

    static void visualize(Set<Pos> used, Set<Pos> fig)
    {
        var maxY = used.stream().mapToInt(Pos::y).reduce(Integer.MIN_VALUE, Math::max);
        maxY = Math.max(maxY, fig.stream().mapToInt(Pos::y).reduce(Integer.MIN_VALUE, Math::max));
        var sb = new StringBuilder();

        for (int y = maxY; y >= 0; y--)
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

    static void addFigure(Set<Pos> figure, int index, int y)
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

    private record Pos(int x, int y)
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
            return stream.get(count++ % stream.size());
        }
    }
}
