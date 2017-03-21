/*
 * (C) Copyright 1999-2016, by Nathan Fiedler and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * This program and the accompanying materials are dual-licensed under
 * either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation, or (at your option) any
 * later version.
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation.
 */
package dataStructures.fibonacciHeap;

public class FibonacciHeapNode<T>
{
    private T data = null;
    private FibonacciHeapNode<T> child = null;
    private FibonacciHeapNode<T> left = null;
    private FibonacciHeapNode<T> parent = null;
    private FibonacciHeapNode<T> right = null;
    private boolean isMarked;
    private int key;
    private int degree;

    public FibonacciHeapNode(T data)
    {
        right = this;
        left = this;
        this.data = data;
    }

    public final T getData() {
        return data;
    }

    public FibonacciHeapNode<T> getChild() {
        return child;
    }

    public FibonacciHeapNode<T> getLeft() {
        return left;
    }

    public FibonacciHeapNode<T> getParent() {
        return parent;
    }

    public FibonacciHeapNode<T> getRight() {
        return right;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public int getKey() {
        return key;
    }

    public int getDegree() {
        return degree;
    }

    public void setChild(FibonacciHeapNode<T> child) {
        this.child = child;
    }

    public void setLeft(FibonacciHeapNode<T> left) {
        this.left = left;
    }

    public void setParent(FibonacciHeapNode<T> parent) {
        this.parent = parent;
    }

    public void setRight(FibonacciHeapNode<T> right) {
        this.right = right;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

}
