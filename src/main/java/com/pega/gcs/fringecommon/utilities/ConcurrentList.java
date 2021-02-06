/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.utilities;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentList<E> implements List<E> {

    private final ReadWriteLock readWriteLock;

    private final List<E> list;

    public ConcurrentList(List<E> list) {
        this.list = list;
        readWriteLock = new ReentrantReadWriteLock();
    }

    protected ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    @Override
    public int size() {

        Lock lock = readWriteLock.readLock();
        lock.lock();

        try {
            return list.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        Lock lock = readWriteLock.readLock();
        lock.lock();

        try {
            return list.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean contains(Object object) {
        Lock lock = readWriteLock.readLock();
        lock.lock();

        try {
            return list.contains(object);
        } finally {
            lock.unlock();
        }

    }

    @Override
    public Iterator<E> iterator() {

        return new Itr(list.iterator());
    }

    @Override
    public Object[] toArray() {
        Lock lock = readWriteLock.readLock();
        lock.lock();

        try {
            return list.toArray();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T> T[] toArray(T[] array) {
        Lock lock = readWriteLock.readLock();
        lock.lock();

        try {
            return list.toArray(array);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void add(int index, E element) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();

        try {
            list.add(index, element);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean add(E element) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();

        try {
            return list.add(element);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E remove(int index) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();

        try {
            return list.remove(index);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean remove(Object object) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();

        try {
            return list.remove(object);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        Lock lock = readWriteLock.readLock();
        lock.lock();

        try {
            return list.containsAll(collection);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();

        try {
            return list.addAll(collection);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> collection) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();

        try {
            return list.addAll(index, collection);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();

        try {
            return list.removeAll(collection);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();

        try {
            return list.retainAll(collection);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        Lock lock = readWriteLock.writeLock();
        lock.lock();

        try {
            list.clear();
        } finally {
            lock.unlock();
        }

    }

    @Override
    public E get(int index) {
        Lock lock = readWriteLock.readLock();
        lock.lock();

        try {
            return list.get(index);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E set(int index, E element) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();

        try {
            return list.set(index, element);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int indexOf(Object object) {
        Lock lock = readWriteLock.readLock();
        lock.lock();

        try {
            return list.indexOf(object);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int lastIndexOf(Object object) {
        Lock lock = readWriteLock.readLock();
        lock.lock();

        try {
            return list.lastIndexOf(object);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        Lock lock = readWriteLock.readLock();
        lock.lock();

        try {
            return new ConcurrentList<E>(list.subList(fromIndex, toIndex));
        } finally {
            lock.unlock();
        }
    }

    private class Itr implements Iterator<E> {

        private Iterator<E> iterator;

        public Itr(Iterator<E> iterator) {
            super();
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            Lock lock = getReadWriteLock().readLock();
            lock.lock();

            try {
                return iterator.hasNext();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public E next() {
            Lock lock = getReadWriteLock().readLock();
            lock.lock();

            try {
                return iterator.next();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void remove() {
            Lock lock = getReadWriteLock().writeLock();
            lock.lock();

            try {
                iterator.remove();
            } finally {
                lock.unlock();
            }
        }

    }
}
