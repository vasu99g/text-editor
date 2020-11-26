# High Level Approach:
As we know String operations are quite expensive since they are immutable, and we need to iterate over them character
by character. For example, to concatenate two Strings we need to make a new String, and then copy over the 1st String
followed by the 2nd String. Some other examples are getting a substring or deleting some characters. In methods like
cut and paste, we need to frequently insert and delete characters at specific positions which in in O(n) where n is
the length of the String, both worst and average case.

I have tried to decrease the average case time complexity for methods like cut and paste, by constant factors, using
unrolled linked list. An unrolled linked list stores more than one data value per node, which gives it major advantages.
Linked lists support pointer manipulation, will would help us to reduce the time to cut and paste.

## Advantages:
- Lower total overhead than a usual linked list
- Increased performance:
    1) Faster insertions (increases paste performance)
    2) Faster deletions (increases cut performance)
    3) Faster traversals (benefits overall)
- Good cache behavior

## Disadvantages:
- Higher overhead per node (higher space complexity per node)

## Using a unrolled list helps us to reduce the following time complexities: (n is the number of chars in the file)
- Paste: O(n) -> O((i / node_size) + clipboard_size) (typically)
- Cut: O(n) -> O(j / node_size) (typically)

In average use case and as the number of computations increase, i and j would average to n / 2, hence bringing the
time complexities down to:
- Paste: O(n) -> O(n / 2 * node_size) (on average)
- Cut: O(n) -> O(n / 2 * node_size) (on average)

For example, the sample.txt contains 2,162,041 characters. With a node_size = 1500, an unrolled list we can easily store
it in 1442 nodes, therefore on average, we would iterate over 721 nodes for a 2 million character file, giving us a
good boost in performance.

# Run Directions:
- Go to src/test/TextEditorPerformanceTest and run its main method to print the performance of the text editor
- Go to src/test/UnrolledTextListTest and run its main method to run tests for the UnrolledTextList. If UnrolledTextList
  test cases pass, then that means that TextEditor also works well.

# Tradeoffs:
Using unrolled linked list will result in a slight increase in space complexity since we will be storing pointers, but
in return we get a performance boost in time complexity by a constant factor.

# Extensions:
If time permitted, I would like to optimize the misspellings' method to become O(1) by storing it as a class variable,
initializing it in the constructor, and updating it every time the document is modified. Furthermore, I would try to
convert my unrolled linked list to an unrolled skip list to get a asymptotic time complexity boost.