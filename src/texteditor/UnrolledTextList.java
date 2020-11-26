/*
 * Copyright 2020 <Copyright Vasu Gupta>
 * Vasu Gupta
 * Email: vasu99g@gmail.com
 */

package texteditor;

/**
 * <b>UnrolledTextList</b> represents an mutable Unrolled Linked List which stores text.
 */
public class UnrolledTextList {
    /**
     * Node class representing a Linked List Node.
     */
    private class Node {
        StringBuilder text; // the String that the node stores
        Node next; // next pointer

        /**
         * Constructs an isolated node with the given text
         *
         * @param text the String to be added to the node
         * @throws IllegalArgumentException if text is null
         */
        public Node(StringBuilder text) {
            if (text == null) {
                throw new IllegalArgumentException("Text cannot be negative");
            }

            this.text = text;
        }
    }

    private Node head; // head of the list
    private Node tail; // tail of the list
    private final int nodeSize; // max number of characters per node

    /**
     * Constructs an empty list with the given node capacity
     *
     * @param nodeSize number of characters per node
     * @throws IllegalArgumentException if nodeSize is negative
     */
    public UnrolledTextList(int nodeSize) {
        if (nodeSize < 0) {
            throw new IllegalArgumentException("Node capacity cannot be negative");
        }

        this.nodeSize = nodeSize;
    }

    /**
     * Appends the given text into the list, filling each node to the maximum capacity.
     *
     * @param text the text to be inserted into the list
     * @throws IllegalArgumentException if text is null
     */
    public void insertText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }

        // if head is null, initialize the list
        if (head == null) {
            head = new Node(new StringBuilder());
            tail = head;
        }

        int i = 0; // iterator for the text
        int textLength = text.length();
        // number of characters that can be accommodated in the tail node
        int tailBufferLength = nodeSize - tail.text.length();

        if (tailBufferLength > 0) {
            if (textLength < tailBufferLength) {
                // whole text can be accommodated in the tail node
                tail.text.append(text);
                i += textLength;
            } else {
                // textLength > number of characters that can be accommodated in the tail node
                // fill till node completely
                tail.text.append(text, i, tailBufferLength);
                i += tailBufferLength;
            }
        }

        // breaking text into chucks of node size and appending them to the list
        while (i < textLength) {
            Node node = i + nodeSize < textLength ? new Node(new StringBuilder(text.substring(i, i + nodeSize))) :
                    new Node(new StringBuilder(text.substring(i)));
            tail.next = node;
            tail = node;

            i += nodeSize;
        }
    }

    /**
     * Copies the text from ith character to the jth character in the document (exclusive). If the ending index is
     * greater than the length of the document (in terms of characters), then the copied text ranges from
     * starting index to the end of the document.
     *
     * @param i the index of the starting character that needs to be copied
     * @param j the index of the ending character that needs to be copied
     * @throws IllegalArgumentException if j < i or i < 0 or i > document length (in characters)
     * @return A UnrolledTextList containing the copied text.
     */
    public UnrolledTextList copyText(int i, int j) {
        boundsCheck(i, j);

        // find the node containing i
        // make deep copy
        // return
        Object[] nodeData = findNode(i);
        Node ithNode = (Node) nodeData[0];
        nodeFound(ithNode);
        int ithNodeIndex = (int) nodeData[1];

        UnrolledTextList copyText = new UnrolledTextList(nodeSize);
        int copyLength = j - i;
        int offset = i - ithNodeIndex;
        int nodeTextLength = ithNode.text.length();

        // i and j both in node
        if (offset + copyLength < nodeTextLength) {
            copyText.insertText(ithNode.text.substring(offset, offset + copyLength));
            return copyText;
        }
        // i and j in different nodes: new node(i to end of starting node) -> new node(full nodes in between) ->
        // new node(starting of ending node to j)

        // iterator on characters of the text that needs to be copied
        int iter = 0;

        // new node(i to end of starting node)
        copyText.insertText(ithNode.text.substring(offset));
        iter += nodeTextLength - offset;
        Node curr = ithNode.next;

        // full nodes in between
        while (curr != null && i + iter + curr.text.length() < j) {
            iter += curr.text.length();
            copyText.insertText(curr.text.toString());
            curr = curr.next;
        }

        // new node(starting of ending node to j)
        if (curr != null && iter < copyLength) {
            copyText.insertText(curr.text.substring(0, copyLength - iter));
        }

        return copyText;
    }

    /**
     * Cuts the text from ith character to jth character in the document (exclusive). If the ending index is
     * greater than the length of the document (in terms of characters), then the cut text ranges from
     * starting index to the end of the document.
     *
     * @param i the index of the starting character that needs to be copied
     * @param j the index of the ending character that needs to be copied
     * @throws IllegalArgumentException if j < i or i < 0 or i > document length (in characters)
     * @return A UnrolledTextList containing the cut text.
     */
    public UnrolledTextList cutText(int i, int j) {
        boundsCheck(i, j);

        // find the node containing i
        // make shallow copy
        // update pointers to concatenate
        UnrolledTextList cutText = new UnrolledTextList(nodeSize);
        Object[] nodeData = findNode(i);
        Node ithNode = (Node) nodeData[0];
        nodeFound(ithNode);
        int ithNodeIndex = (int) nodeData[1];

        int cutLength = j - i;
        int offset = i - ithNodeIndex;
        int nodeTextLength = ithNode.text.length();

        // i and j both in node
        if (offset + cutLength < nodeTextLength) {
            cutText.insertText(ithNode.text.substring(offset, offset + cutLength));
            ithNode.text.delete(offset, offset + cutLength);
            return cutText;
        }
        // i and j in different nodes: new node(i to end of starting node) -> full nodes in between ->
        // new node(starting of ending node to j)

        // iterator on characters of the text that needs to be copied
        int iter = 0;

        // new node(i to end of starting node)
        cutText.insertText(ithNode.text.substring(offset));
        ithNode.text.delete(offset, nodeTextLength);
        iter += nodeTextLength - offset;
        Node curr = ithNode.next;

        // full nodes in between
        while (curr != null && i + iter + curr.text.length() <= j) { // j is exclusive
            iter += curr.text.length();
            cutText.tail.next = curr;
            cutText.tail = cutText.tail.next;
            curr = curr.next;
            cutText.tail.next = null;
        }

        // new node(starting of ending node to j)
        if (curr != null && iter < cutLength) {
            cutText.insertText(curr.text.substring(0, cutLength - iter));
            curr.text.delete(0, cutLength - iter);
        }

        // trying to combine nodes
        if (curr != null && ithNode.text.length() + curr.text.length() <= nodeSize) {
            ithNode.text.append(curr.text);
            ithNode.next = curr.next;
            curr = curr.next;
        } else {
            // simple concatenating
            ithNode.next = curr;
        }

        // edge case if part of the list is cut till end
        if (curr == null) {
            tail = ithNode;
        }

        return cutText;
    }

    /**
     * Pastes the text in the clipboard into the document starting from the ith character.
     *
     * @param i the index of the starting character in the document
     * @param pasteText unrolled text list in the clipboard
     * @throws IllegalArgumentException if i > document length or clipboard is null
     */
    public void pasteText(int i, UnrolledTextList pasteText) {
        if (pasteText == null) {
            throw new IllegalArgumentException("clipboard cannot be null");
        }

        // make a deep copy of a clipboard
        // find node that contains i
        // if i is at the last index of a node, we do not split, simply add the list in between the nodes
        // else we have to split the node containing i into 2 parts
        UnrolledTextList clipboard = new UnrolledTextList(nodeSize);
        clipboard.insertText(pasteText.toString());

        Object[] nodeData = findNode(i);
        Node ithNode = (Node) nodeData[0];
        nodeFound(ithNode);
        int ithNodeIndex = (int) nodeData[1];

        if (ithNodeIndex + (ithNode.text.length() - 1) == i) {
            Node temp = ithNode.next;
            ithNode.next = clipboard.head;
            clipboard.tail.next = temp;

            // edge case, if ithNode is the last node
            if (temp == null) {
                tail = clipboard.tail;
            }
        } else {
            int offset = i - ithNodeIndex;
            // right part of the split node
            Node laterHalf = new Node(new StringBuilder(ithNode.text.substring(offset)));
            laterHalf.next = ithNode.next;
            // trimming left part of the split node
            ithNode.text.delete(offset, ithNode.text.length());

            if (offset == 0) {
                ithNode.text.append(clipboard.head.text);
                ithNode.next = clipboard.head.next != null ? clipboard.head.next : laterHalf;
            } else {
                // joining clipboard in middle
                ithNode.next = clipboard.head;
            }

            clipboard.tail.next = laterHalf;

            // edge case, if ithNode is the last node
            if (laterHalf.next == null) {
                tail = laterHalf;
            }
        }
    }

    /**
     * Clears the list
     */
    public void clear() {
        head = null;
        tail = null;
    }

    /**
     * Converts the list into its text (String) representation.
     *
     * @return String representing the text in the list
     */
    public String toString() {
        StringBuilder text = new StringBuilder();
        Node curr = head;

        while (curr != null) {
            text.append(curr.text);
            curr = curr.next;
        }

        return text.toString();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ////  Util methods
    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * Checks the bounds for i and j
     *
     * @param i the index of the starting character in the document
     * @param j the index of the ending character in the document
     * @throws IllegalArgumentException if j < i or i < 0
     */
    private void boundsCheck(int i, int j) {
        if (j < i || i < 0) {
            throw new IllegalArgumentException("Ending index cannot be smaller than starting index");
        }
    }

    /**
     * Returns the node containing the ith character in the text document, and
     * also returns the starting character index of that node.
     *
     * @param i the ith character which needs to be found
     * @return the node containing the ith character and the starting character index of that node,
     * returns <b>special value: </b> null, if the index is out of bounds
     */
    private Object[] findNode(int i) {
        Node curr = head;
        int index = 0;

        while (curr != null && index + curr.text.length() <= i) {
            index += curr.text.length();
            curr = curr.next;
        }

        return new Object[]{curr, index};
    }

    /**
     * Checks if i is in bounds
     *
     * @param ithNode the index of the starting character in the document
     * @throws  IllegalArgumentException if i > document length
     */
    private void nodeFound(Node ithNode) {
        if (ithNode == null) {
            throw new IllegalArgumentException("starting index is greater than the document length");
        }
    }
}
