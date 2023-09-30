# Analysis of a Simple Greedy Algorithm

## Overview
This project analyzes a simple greedy algorithm designed to format a text containing `n` words such that it occupies the minimal number of lines, each of a maximum length `len`. The algorithm processes each word sequentially to decide if it can be placed on the current line without exceeding the maximum length, `len`. The goal of this analysis is to provide an insight into the working of the algorithm and to prove its correctness and efficiency.

## Problem Statement
Given a list of `n` words, with lengths `w1, ..., wn`, and a maximum line length `len`, the objective is to format the text using the minimal number of lines without exceeding the maximum line length, maintaining the order of words. The problem emphasizes developing a greedy algorithm to efficiently solve the task and prove its optimality and correctness.

## Greedy Algorithm
The algorithm is defined as greedy since it makes the locally optimal choice at each step, aiming to produce the globally optimal solution. It attempts to place as many words as possible on the current line, thus minimizing the total number of lines used.

### Algorithm Description
1. It initializes `numberOfRows` and `currentLength` to keep track of the number of rows and the length of the current row.
2. For each word in the list, it decides whether the word can be placed on the current row without exceeding `len`, updating `currentLength` and `numberOfRows` accordingly.

```java
public static int calculateMinimalNumberOfRows(List<String> words, int len) {
    int currentLength = 0;
    int numberOfRows = 1;

    for (String word : words) {
        int space = (currentLength == 0) ? 0 : 1;
        if (currentLength + word.length() + space <= len) {
            currentLength += word.length() + space;
        } else {
            numberOfRows++;
            currentLength = word.length();
        }
    }
    return numberOfRows;
}
``` 

## Time Complexity

The time complexity of the algorithm is O(n), where n is the number of words, as each word in the list is processed exactly once, and each operation within has a constant time complexity.

### Overview
Each basic operation is considered to have a unit cost, including assignment, comparison, and arithmetic operations. Since the algorithm goes through each word in the list exactly once and performs operations with constant time costs, the time complexity of the algorithm is \(O(n)\), where \(n\) is the number of words in the list.

### Breakdown
To break this down further, assuming we have a list with \(n\) words, the following operations are performed for each word:
- Comparing `currentLength` with `len` to determine if the word can be added to the current line.
- Assignment to update `currentLength` if the word is added to the current line.
- Assignment to update `numberOfRows` and reset `currentLength` if the word cannot be added to the current line.

### Final Statement
Therefore, with \(n\) words and a constant number of operations per word, the total time complexity of the algorithm is \(O(n)\) under the unit cost model.

## General Correctness Proof
To prove the correctness of such an algorithm, a loop invariant is used, demonstrating its truth during three conditions: Initialization, Maintenance, and Termination. 

### Loop Invariant
At each iteration's start, `numberOfRows` holds the minimum number of rows required to accommodate all processed words, each row with a maximum length `len`, and `currentLength` holds the length of the currently processed row.

### Loop Invariant Breakdown
- **Initialization:** Before the first iteration, `numberOfRows` is 1, and `currentLength` is 0, consistent with our loop invariant as no word has been processed yet.
  
- **Maintenance:** Each iteration processes a word from the list and checks whether adding the word to the current row would exceed the maximum length `len`. The loop invariant is maintained in both cases: adding the word to the current row or starting a new row.
  
- **Termination:** Post loop, every word is processed once, and `numberOfRows` represents the minimum number of rows needed to place all words within the maximum row length `len`.


## Conclusion
This analysis provides insights into a simple greedy algorithm applied to optimize text formatting. By understanding the mechanics and implications of such algorithms, developers and analysts can better design and implement more complex and efficient solutions in related domains.
