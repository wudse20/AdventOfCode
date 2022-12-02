module Main where
    main :: IO ()
    main = do
        file <- readFile "input.txt"
        putStrLn . show . part1 . prepareData $ file -- Part 1 answer
        putStrLn . show . part2 . prepareData $ file -- Part 2 answer
    
    prepareData :: String -> [[String]]
    prepareData = map words . lines

    part1 :: [[String]] -> Int
    part1 [] = 0
    part1 (xs:xss) = line xs + part1 xss
        where
            line :: [String] -> Int
            line xs
                | win xs = 6 + score xs
                | draw xs = 3 + score xs
                | otherwise = score xs
                    where
                        win :: [String] -> Bool
                        win xs = 
                            ((xs !! 0) == "A" && (xs !! 1) == "Y") || 
                            ((xs !! 0) == "B" && (xs !! 1) == "Z") || 
                            ((xs !! 0) == "C" && (xs !! 1) == "X")
                        draw :: [String] -> Bool
                        draw xs =
                            ((xs !! 0) == "A" && (xs !! 1) == "X") || 
                            ((xs !! 0) == "B" && (xs !! 1) == "Y") || 
                            ((xs !! 0) == "C" && (xs !! 1) == "Z")
                        score :: [String] -> Int
                        score xs
                            | xs !! 1 == "X" = 1
                            | xs !! 1 == "Y" = 2
                            | xs !! 1 == "Z" = 3
                            | otherwise = 0

    part2 :: [[String]] -> Int
    part2 [] = 0
    part2 (xs:xss) = line xs + part2 xss
        where
            line :: [String] -> Int
            line xs
                | xs !! 1 == "X" = loose $ xs !! 0
                | xs !! 1 == "Y" = (3+) . draw $ xs !! 0
                | xs !! 1 == "Z" = (6+) . win $ xs !! 0
                | otherwise = 0
                    where
                        loose :: String -> Intq
                        loose x
                            | x == "A" = 3 -- We want to loose and elf played rock so we play scissors = 3
                            | x == "B" = 1 -- We want to loose and elf played paper so we play rock = 1
                            | x == "C" = 2 -- We want to loose and elf played scissors so we play paper = 2
                            | otherwise = 0
                        draw :: String -> Int
                        draw x
                            | x == "A" = 1 -- We want to draw and elf played rock so we play rock = 1
                            | x == "B" = 2 -- We want to draw and elf played paper so we play paper = 2
                            | x == "C" = 3 -- We want to draw and elf played scissors so we play scissors = 3
                            | otherwise = 0
                        win x
                            | x == "A" = 2 -- We want to win and elf played rock so we play paper = 2
                            | x == "B" = 3 -- We want to win and elf played paper so we play scissors = 3
                            | x == "C" = 1 -- We want to win and elf played scissors so we play rock = 1
                            | otherwise = 0
