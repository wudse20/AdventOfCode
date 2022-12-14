#include <stdio.h>
#include <malloc.h>
#include <ctype.h>
#include <string.h>

#define LIT_PIXEL '#'
#define DARK_PIXEL '.'

long long part1(char *path, int lines);
void part2(char* path, int lines);
int drawPixel(int cycle, int x);

int main()
{
    printf("Part 1: %lld\n", part1("input.txt", 144));
    printf("Part 2: \n");
    part2("input.txt", 144);
}

long long part1(char *path, int lines)
{
    FILE *file = fopen(path, "r");

    if (!file)
    {
        printf("\n Unable to open : %s ", path);
        return -1;
    }

    char line[500];
    int cycle = 0;
    int indexCount = 0;
    int waitNext = 0;
    long long x = 1;
    long long addNext = 0;
    long long *store = malloc(sizeof(long long) * 6);

    while (fgets(line, sizeof(line), file))
    {
        int len = strlen(line);
        int i = 0;
        char c = line[i];

        if (waitNext)
        {
            cycle++;
            switch (cycle)
            {
                case 20:
                case 60:
                case 100:
                case 140:
                case 180:
                case 220:
                    store[indexCount++] = cycle * x;
                    printf("store[%d] = %d * %lld = %lld\n", indexCount - 1, cycle, x, store[indexCount - 1]);
            }

            x += addNext;
            addNext = 0;
            waitNext = 0;
        }

        if (c == 'n')
        {
            waitNext = 0;
        }
        else
        {
            int sign = 1;
            int value = 0;

            i += 5;
            if (line[i] == '-')
                sign = -1;
            else
                i--;

            while (i + 1 < len && isdigit(line[i + 1]))
                value = value * 10 + line[++i] - '0';

            value *= sign;
            addNext = value;
            waitNext = 1;
        }

        cycle++;
        switch (cycle)
        {
            case 20:
            case 60:
            case 100:
            case 140:
            case 180:
            case 220:
                store[indexCount++] = cycle * x;
                printf("store[%d] = %d * %lld = %lld\n", indexCount - 1, cycle, x, store[indexCount - 1]);
        }
    }

    long long res = 0;
    for (int i = 0; i < 6; i++)
    {
        res += store[i];
    }

    fclose(file);
    free(store);
    return res;
}

void part2(char* path, int lines)
{
    FILE *file = fopen(path, "r");

    if (!file)
    {
        printf("\n Unable to open : %s ", path);
        return;
    }

    char line[500];
    int cycle = 0;
    int indexCount = 0;
    int waitNext = 0;
    int x = 1;
    int addNext = 0;

    while (fgets(line, sizeof(line), file))
    {
        int len = strlen(line);
        int i = 0;
        char c = line[i];

        if (waitNext)
        {
            cycle++;
            printf("%c", drawPixel(cycle - 1, x) ? LIT_PIXEL : DARK_PIXEL);
            
            if (cycle % 40 == 0)
                printf("\n");
            
            x += addNext;
            addNext = 0;
            waitNext = 0;
        }

        if (c == 'n')
        {
            waitNext = 0;
        }
        else
        {
            int sign = 1;
            int value = 0;

            i += 5;
            if (line[i] == '-')
                sign = -1;
            else
                i--;

            while (i + 1 < len && isdigit(line[i + 1]))
                value = value * 10 + line[++i] - '0';

            value *= sign;
            addNext = value;
            waitNext = 1;
        }

        cycle++;

        printf("%c", drawPixel(cycle - 1, x) ? LIT_PIXEL : DARK_PIXEL);
        if (cycle % 40 == 0)
            printf("\n");
    }

    fclose(file);
}

int drawPixel(int cycle, int pixel)
{
    return cycle % 40 == pixel - 1 ||
           cycle % 40 == pixel     ||
           cycle % 40 == pixel + 1;
}
