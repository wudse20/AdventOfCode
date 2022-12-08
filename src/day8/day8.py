def readFile(path):
    file = open(path, mode='r')
    all_of_it = file.read()
    file.close()
    return all_of_it

def part1():
    input = readFile("input.txt").split("\n")
    input = list(map(list, input))

    count = 0
    for y in range(0, len(input)):
        for x in range(0, len(input[y])):
            length = int(input[y][x])
            visible = False

            for i in range(x + 1, len(input[0])):
                if int(input[y][i]) >= length:
                    break
            else:
                visible = True

            for i in range(x - 1, -1, -1):
                if int(input[y][i]) >= length:
                    break
            else:
                visible = True

            for i in range(y + 1, len(input)):
                if int(input[i][x]) >= length:
                    break
            else:
                visible = True

            for i in range(y - 1, -1, -1):
                if int(input[i][x]) >= length:
                    break
            else:
                visible = True

            if visible:
                count += 1

    return count

print("Part 1: " + str(part1()))
