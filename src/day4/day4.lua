require "utils"

local function part1()
    local input = SplitByNewLine(ReadAll("input.txt"))
    local reductions = 0

    for k, v in pairs(input) do
        local line = SplitByComma(v)
        local elf1 = { tonumber(SplitByHyphen(line[1])[1]), tonumber(SplitByHyphen(line[1])[2]) }
        local elf2 = { tonumber(SplitByHyphen(line[2])[1]), tonumber(SplitByHyphen(line[2])[2]) }

        if elf1[1] <= elf2[1] and elf1[2] >= elf2[2] then
            reductions = reductions + 1
        elseif elf1[1] >= elf2[1] and elf1[2] <= elf2[2] then
            reductions = reductions + 1
        end
    end
    return reductions
end

print("Part 1: " .. part1())
