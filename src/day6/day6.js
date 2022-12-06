"use strict";
const fs = require("fs");

function part1() {
    const input = fs.readFileSync("input.txt").toString();

    for (let i = 0; i < input.length; i++) {
        let tmp = "";
        let set = new Set();
        for (let ii = 0; i < input.length && ii < 4; ii++) {
            if (set.has(input[i + ii])) {
                break;
            } else {
                tmp += input[i + ii];
                set.add(input[i + ii]);
            }
        }

        if (set.size === 4) {
            return i + 4;
        }
    }

    return -1;
}

console.log("Part 1: ", part1());
