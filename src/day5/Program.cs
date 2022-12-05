using System.Text;

namespace solution
{
    class Solution
    {
        static readonly List<Instruction> instructions = new List<Instruction>();
        static readonly List<Queue<String>> stacks = new List<Queue<String>>();

        public static void Main(String[] args)
        {
            ParseInput();
            Console.WriteLine("Part 1: {0}", Part1());
        }

        static String Part1()
        {
            // Converting form queues to stacks,
            // since a deque is not in the standard
            // library, and was to lazy to implement
            // myself.
            var piles = stacks.Select(q => q.Reverse())
                              .Select(q => new Stack<string>(q))
                              .ToList();

            foreach (var inst in instructions)
            {
                var amount = inst.amount;
                var to = inst.to;
                var from = inst.from;

                for (int i = 0; i < amount; i++) 
                {
                    var item = piles[from].Pop();
                    piles[to].Push(item);
                }
            }

            var sb = new StringBuilder();
            
            foreach (var stack in piles)
            {
                sb.Append(stack.Peek());
            }

            return sb.ToString();
        }

        static void ParseInput()
        {
            var input = File.ReadAllText(@"./input.txt").Split("\r\n");
            var instructionsStarted = false;
            var isFirstLine = true;

            foreach (var line in input)
            {
                if (!instructionsStarted)
                {
                    var currentStack = 0;
                    for (var i = 0; i < line.Length; i++)
                    {
                        var c = line[i];

                        if (c == '[') // Start of item
                        {
                            if (isFirstLine)
                                stacks.Add(new Queue<string>());
                            
                            if (currentStack >= stacks.Count())
                                throw new Exception(String.Format(
                                    "Line: {0}, currentStack: {1}, stacks.Count(): {2}", 
                                    line, currentStack, stacks.Count()
                                ));

                            c = line[++i];
                            stacks[currentStack].Enqueue(c.ToString());
                            i++; // Eating the ']'
                            currentStack++;
                        }
                        else if (c == ' ' && i + 1 < line.Length && line[i + 1] == ' ') // Empty item
                        {
                            if (isFirstLine)
                                stacks.Add(new Queue<string>());
                            
                            i += 3; // Eat the 4 ' ' that are for this
                            currentStack++;
                        }
                        else if (c == ' ') // Between items.
                        {
                            continue;
                        }
                        else if (c == '1') // Stack number, finished with stack parsing.
                        {
                            instructionsStarted = true;
                            break;
                        }
                    }

                    isFirstLine = false;
                    continue;
                }

                if (line == "")
                    continue;

                var data = line.Split(" ");
                instructions.Add(new Instruction(Int32.Parse(data[1]), Int32.Parse(data[3]) - 1, Int32.Parse(data[5]) - 1));
            }
        }

        private class Instruction
        {
            public int amount;
            public int from;
            public int to;

            public Instruction(int amount, int from, int to)
            {
                this.amount = amount;
                this.from = from;
                this.to = to;
            }

            public override string ToString()
            {
                return String.Format("Move: {0}, From: {1} ({2}), To: {3} ({4})", amount, from + 1, from, to + 1, to);
            }
        }
    }
}