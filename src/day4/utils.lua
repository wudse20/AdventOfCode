function ReadAll(file)
    local f = assert(io.open(file, "rb"))
    local content = f:read("*all")
    f:close()
    return content
end

local function split(str, regex)
    local lines = {}

    for s in str:gmatch(regex) do
        table.insert(lines, s)
    end

    return lines
end

function SplitByNewLine(str)
    return split(str, "[^\r\n]+")
end

function SplitByComma(str)
    return split(str, "[^,]+")
end

function SplitByHyphen(str)
    return split(str, "[^-]+")
end

function TableToString(table)
    local str = "[" -- Start of string.

    for k,v in pairs(table) do
        str = str .. v .. ", " -- Adds each value and a comma and a space.
    end
    str = str:sub(1, -3) -- Removes the last comma and space.
    str = str .. "]"  -- End of the string.
    return str
end
