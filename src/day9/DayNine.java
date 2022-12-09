package day9;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DayNine
{
    public static void main(String[] args) throws IOException
    {
        System.out.printf("Part 1: %d", part1());
    }

    private static int part1() throws IOException
    {
        var input = getFileContent("./src/day9/input.txt").split("\n");
        var positions = new HashSet<Pos>();
        var head = Pos.ORIGIN;
        var tail = Pos.ORIGIN;

        for (var line : input)
        {
            var dir = line.charAt(0);
            var count = Integer.parseInt(line.split(" ")[1]);
            positions.add(tail);

            for (int i = 1; i <= count; i++)
            {
                switch (dir) {
                    case 'R' -> head = head.right();
                    case 'L' -> head = head.left();
                    case 'U' -> head = head.up();
                    case 'D' -> head = head.down();
                }

                tail = checkAndMoveTail(head, tail);
                positions.add(tail);
            }
        }

        return positions.size();
    }

    private static Pos checkAndMoveTail(Pos head, Pos tail)
    {
        var allNeighbours = head.allNeighbors();
        for (Pos neighbour : allNeighbours)
        {
            if (neighbour.x == tail.x && neighbour.y == tail.y)
                return tail;
        }

        if ((head.y > tail.y + 1 && head.x > tail.x) || (head.x > tail.x + 1 && head.y > tail.y)) // Up, right
        {
            var newTail = tail.up();
            return newTail.right();
        }
        else if ((head.y > tail.y + 1 && head.x < tail.x) || (head.x < tail.x - 1 && head.y > tail.y)) // Up, left
        {
            var newTail = tail.up();
            return newTail.left();
        }
        else if ((head.y < tail.y - 1 && head.x > tail.x) || (head.x > tail.x + 1 && head.y < tail.y)) // Down, right
        {
            var newTail = tail.down();
            return newTail.right();
        }
        else if ((head.y < tail.y - 1 && head.x < tail.x) || (head.x < tail.x - 1 && head.y < tail.y)) // Down, left
        {
            var newTail = tail.down();
            return newTail.left();
        }

        if (head.x > tail.x + 1)
            return tail.right();
        else if (head.x < tail.x - 1)
            return tail.left();
        else if (head.y > tail.y + 1)
            return tail.up();
        else if (head.y < tail.y - 1)
            return tail.down();

        return tail;
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
        return sb.toString();
    }

    private record Pos(int x, int y)
    {
        public static final Pos UP = new Pos(0, 1);
        public static final Pos DOWN = new Pos(0, -1);
        public static final Pos LEFT = new Pos(-1, 0);
        public static final Pos RIGHT = new Pos(1, 0);
        public static final Pos ORIGIN = new Pos(0, 0);

        private Pos add(Pos p)
        {
            return new Pos(x + p.x, y + p.y);
        }

        public Pos up()
        {
            return add(UP);
        }

        public Pos down()
        {
            return add(DOWN);
        }

        public Pos left()
        {
            return add(LEFT);
        }

        public Pos right()
        {
            return add(RIGHT);
        }

        public List<Pos> allNeighbors()
        {
            var list = new ArrayList<Pos>();

            for (int dy = -1; dy < 2; dy++)
            {
                for (int dx = -1; dx < 2; dx++)
                {
                    if (!(dx == 0 && dy == 0))
                        list.add(new Pos(x + dx,y + dy));
                }
            }

            return list;
        }
    }
}
