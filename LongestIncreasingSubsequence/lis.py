user_input = input("Enter a sequence of numbers separated by spaces: ")

# Split the string into substrings based on spaces
string_numbers = user_input.split()

numbers = [int(num) for num in string_numbers]

n = len(numbers)
memo = [1] * n
longest = 1

for i in range(1, n):
    for j in range(i):
        if numbers[j] < numbers[i]:
            memo[i] = max(memo[i], memo[j] + 1)
            longest = max(longest, memo[i])

print(f"The longest increasing subsequence is {longest}")