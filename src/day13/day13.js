"use strict";
const fs = require("fs")

function wrapArr(arr) {
    if (Array.isArray(arr)) {
		return arr;
	} else {
		return [arr];
	}
}

function compareSignals(a, b) {
    if (Array.isArray(a) && Array.isArray(b)) {
        let i = 0;
        let res = 0;

        while (res === 0 && i < b.length && i < a.length) {
            res = compareSignals(a[i], b[i]);
            i++;
        }

        if (i < a.length && res === 0) {
            return 1;
        } else if (i < b.length && res === 0) {
            return -1;
        } else {
            return res;
        }
    } else if (typeof a === typeof(+1) && typeof b === typeof(+1)) {
		return a - b;
	} else {
		return compareSignals(wrapArr(a), wrapArr(b));
	}
}

function part1() {
    const input = fs.readFileSync("input.txt")
                    .toString()
                    .replaceAll("\r", "")
                    .split("\n\n")
                    .map(x => x.split("\n"))
                    .map(xs => xs.map(eval));

    let res = 0;
    for (let i = 0; i < input.length; i++) {
        const left = input[i][0];
        const right = input[i][1];
        
        if (compareSignals(left, right) < 0)
            res += i + 1;
    }

    return res;
}

function part2() {
    const input = fs.readFileSync("input.txt")
                    .toString()
                    .replaceAll("\r", "")
                    .split("\n")
                    .filter(x => x !== "")
                    .map(eval)
    const divider1 = [[2]];
    const divider2 = [[6]];
    input.push(divider1);
    input.push(divider2);
    input.sort((a, b) => compareSignals(a, b));
    const a = input.indexOf(divider1) + 1;
    const b = input.indexOf(divider2) + 1;
    return a * b;
}

console.log("Part 1:", part1())
console.log("Part 2:", part2());
