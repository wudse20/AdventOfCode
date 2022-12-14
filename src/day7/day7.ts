import { dir } from "console";
import * as fs from "fs"

class Tree {
    children: Tree[];
    files: File[];
    parrent: Tree;
    name: string;

    constructor(name: string) {
        this.name = name;
        this.children = [];
        this.files = [];
    }

    setParrent(parrent: Tree) {
        this.parrent = parrent;
    }

    size(): number {
        if (this.children.length == 0)
            return this.sizeOfFiles();

        let res = 0;
        for (let i = 0; i < this.children.length; i++)
            res += this.children[i].size();

        return res + this.sizeOfFiles();
    }

    sizeOfFiles(): number {
        let res = 0;

        for (let i = 0; i < this.files.length; i++)
            res += this.files[i].size;

        return res;
    }
}

class File {
    size: number;
    name: string;

    constructor(size: number, name: string) {
        this.size = size;
        this.name = name;
    }
}

const root = new Tree("/");
root.parrent = root;
const directories: Tree[] = [root];

function buildTree() {
    let data: string = fs.readFileSync("./input.txt").toString();
    let commands = data.split("\r\n");
    let current = root;

    for (let i = 0; i < commands.length; i++) {
        let cmd = commands[i];

        if (cmd[0] === "$") { // It is a command
            let instruction = cmd.split(" ");

            switch (instruction[1]) {
                case "ls":
                    while (i + 1 < commands.length && commands[i + 1][0] !== "$") { // Get all the items.
                        i++;
                        cmd = commands[i];
                        let lineData = cmd.split(" ");

                        if (lineData[0] === "dir") {
                            let t = new Tree(lineData[1]);
                            t.parrent = current;
                            current.children.push(t);
                            directories.push(t)
                        } else {
                            current.files.push(new File(+lineData[0], lineData[1]));
                        }
                    }
                    break;
                case "cd":
                    let lineData = cmd.split(" ");

                    if (lineData[2] === '/') {
                        current = root;
                        break;
                    } else if (lineData[2] === '..') {
                        current = current.parrent;
                        break;
                    }

                    for (let i = 0; i < current.children.length; i++) {
                        let currentChild = current.children[i];
                        if (currentChild.name === lineData[2]) {
                            current = currentChild;
                            break;
                        }
                    }
                    break;
            }
        }
    }
}

function part1(): number {
    return directories.map(t => t.size())
                      .filter(s => s < 100_000)
                      .reduce((acc, a) => acc + a);
}

const totalSize = 70000000;
const requiredSpace = 30000000;

function part2(): number {
    let total: number = root.size();
    let needed: number = totalSize - total; // The space we need to free up.
    needed = requiredSpace - needed;
    let res: number = totalSize; // Just starting with something big!

    for (let i = 0; i < directories.length; i++) {
        let current = directories[i];
        let size = current.size();

        if (size >= needed && size < res) {
            res = size;
        }
    }

    return res;
}

buildTree();
console.log("Part 1:", part1());
console.log("Part 2:", part2());
