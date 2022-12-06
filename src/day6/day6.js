"use strict";
const fs = require("fs");

function solution(size) {
    const input = fs.readFileSync("input.txt").toString();

    for (let i = 0; i < input.length; i++) {
        let tmp = "";
        let set = new Set();
        for (let ii = 0; i < input.length && ii < +size; ii++) {
            if (set.has(input[i + ii])) {
                break;
            } else {
                tmp += input[i + ii];
                set.add(input[i + ii]);
            }
        }

        if (set.size === size) {
            return i + size;
        }
    }

    return -1;
}

console.log("Part 1: ", solution(4));
console.log("Part 2: ", solution(14))
