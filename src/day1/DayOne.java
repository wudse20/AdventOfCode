package day1;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.PriorityQueue;

public class DayOne
{
    public static void main(String[] args) throws IOException
    {
        System.out.printf("Part One: %d%n", partOne());
        System.out.printf("Part Two: %d%n", partTwo());
    }

    static int partOne() throws IOException
    {
        byte[] bytes = getFileContent("./src/day1/input.txt");

        var max = Integer.MIN_VALUE;
        var current = 0;
        var lastNewLine = false;
        for (var i = 0; i < bytes.length; i++)
        {
            var c = (char) bytes[i];

            if (c == '\n' && !lastNewLine)
            {
                lastNewLine = true;

                if ((char) bytes[i + 1] == '\r')
                    i++;

                continue;
            }

            if (c == '\n')
            {
                max = Math.max(current, max);
                current = 0;
                lastNewLine = false;

                if ((char) bytes[i + 1] == '\r')
                    i++;

                continue;
            }

            var v = 0;

            do {
                v = 10 * v + c - '0';
                c = (char) bytes[++i];
            } while (Character.isDigit(c) && i + 1 < bytes.length);

            current += v;
            lastNewLine = false;
        }

        return max;
    }

    static int partTwo() throws IOException
    {
        byte[] bytes = getFileContent("./src/day1/input.txt");

        var max = new PriorityQueue<Integer>();
        var current = 0;
        var lastNewLine = false;
        for (var i = 0; i < bytes.length; i++)
        {
            var c = (char) bytes[i];

            if (c == '\n' && !lastNewLine)
            {
                lastNewLine = true;

                if ((char) bytes[i + 1] == '\r')
                    i++;

                continue;
            }

            if (c == '\n')
            {
                if (max.size() < 3)
                {
                    max.offer(current);
                }
                else if (max.peek() < current)
                {
                    max.offer(current);
                    max.poll();
                }

                current = 0;
                lastNewLine = false;

                if ((char) bytes[i + 1] == '\r')
                    i++;

                continue;
            }

            var v = 0;

            do {
                v = 10 * v + c - '0';
                c = (char) bytes[++i];
            } while (Character.isDigit(c) && i + 1 < bytes.length);

            current += v;
            lastNewLine = false;
        }

        return max.stream().reduce(0, Integer::sum);
    }

    private static byte[] getFileContent(String pathname) throws IOException
    {
        var f = new File(pathname);
        var bis = new BufferedInputStream(new FileInputStream(f));
        var bytes = bis.readAllBytes();
        bis.close();
        return bytes;
    }
}
