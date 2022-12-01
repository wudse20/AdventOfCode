package day1;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DayOne
{
    public static void main(String[] args) throws IOException
    {
        System.out.printf("Part One: %d%n", partOne());
    }

    static int partOne() throws IOException
    {
        var f = new File("./src/day1/input.txt");
        var bis = new BufferedInputStream(new FileInputStream(f));
        var bytes = bis.readAllBytes();
        bis.close();

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

            if (c == '\n' && lastNewLine)
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
}
