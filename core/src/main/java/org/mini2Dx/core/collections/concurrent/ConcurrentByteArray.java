/*******************************************************************************
 * Copyright 2020 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.core.collections.concurrent;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.lock.ReadWriteLock;
import org.mini2Dx.gdx.utils.ByteArray;

public class ConcurrentByteArray extends ByteArray implements ConcurrentCollection {

    protected ReadWriteLock lock = Mdx.locks.newReadWriteLock();

    /**
     * Creates an ordered array with a capacity of 16.
     */
    public ConcurrentByteArray() {
    }

    /**
     * Creates an ordered array with the specified capacity.
     *
     * @param capacity
     */
    public ConcurrentByteArray(int capacity) {
        super(capacity);
    }

    /**
     * @param ordered  If false, methods that remove elements may change the order of other elements in the array, which avoids a
     *                 memory copy.
     * @param capacity Any elements added beyond this will cause the backing array to be grown.
     */
    public ConcurrentByteArray(boolean ordered, int capacity) {
        super(ordered, capacity);
    }

    /**
     * Creates a new array containing the elements in the specific array. The new array will be ordered if the specific array is
     * ordered. The capacity is set to the number of elements, so any subsequent elements added will cause the backing array to be
     * grown.
     *
     * NOTE: read access to the other array is not thread-safe
     *
     * @param array
     */
    public ConcurrentByteArray(ByteArray array) {
        super(array);
    }

    /**
     * Creates a new ordered array containing the elements in the specified array. The capacity is set to the number of elements,
     * so any subsequent elements added will cause the backing array to be grown.
     *
     * @param array
     */
    public ConcurrentByteArray(byte[] array) {
        super(array);
    }

    /**
     * Creates a new array containing the elements in the specified array. The capacity is set to the number of elements, so any
     * subsequent elements added will cause the backing array to be grown.
     *
     * @param ordered    If false, methods that remove elements may change the order of other elements in the array, which avoids a
     *                   memory copy.
     * @param array
     * @param startIndex
     * @param count
     */
    public ConcurrentByteArray(boolean ordered, byte[] array, int startIndex, int count) {
        super(ordered, array, startIndex, count);
    }

    @Override
    public void add(byte value) {
        lock.lockWrite();
        super.add(value);
        lock.unlockWrite();
    }

    @Override
    public void add(byte value1, byte value2) {
        lock.lockWrite();
        super.add(value1, value2);
        lock.unlockWrite();
    }

    @Override
    public void add(byte value1, byte value2, byte value3) {
        lock.lockWrite();
        super.add(value1, value2, value3);
        lock.unlockWrite();
    }

    @Override
    public void add(byte value1, byte value2, byte value3, byte value4) {
        lock.lockWrite();
        super.add(value1, value2, value3, value4);
        lock.unlockWrite();
    }

    @Override
    public void addAll(byte[] array, int offset, int length) {
        lock.lockWrite();
        super.addAll(array, offset, length);
        lock.unlockWrite();
    }

    @Override
    public byte get(int index) {
        lock.lockRead();
        byte b = super.get(index);
        lock.unlockRead();
        return b;
    }

    @Override
    public void set(int index, byte value) {
        lock.lockWrite();
        super.set(index, value);
        lock.unlockWrite();
    }

    @Override
    public void insert(int index, byte value) {
        lock.lockWrite();
        super.insert(index, value);
        lock.unlockWrite();
    }

    @Override
    public void swap(int first, int second) {
        lock.lockWrite();
        super.swap(first, second);
        lock.unlockWrite();
    }

    @Override
    public void addAll(ByteArray array, int offset, int length) {
        boolean otherIsConcurrent = array instanceof ConcurrentCollection;
        if (otherIsConcurrent){
            ((ConcurrentCollection) array).getLock().lockRead();
        }
        lock.lockWrite();
        super.addAll(array, offset, length);
        lock.unlockWrite();
        if (otherIsConcurrent){
            ((ConcurrentCollection) array).getLock().unlockRead();
        }
    }

    /**
     * Removes and returns the item at the specified index.
     *
     * @param index
     */
    @Override
    public int removeIndex(int index) {
        lock.lockWrite();
        int i = super.removeIndex(index);
        lock.unlockWrite();
        return i;
    }

    /**
     * Removes the items between the specified indices, inclusive.
     *
     * @param start
     * @param end
     */
    @Override
    public void removeRange(int start, int end) {
        lock.lockWrite();
        super.removeRange(start, end);
        lock.unlockWrite();
    }

    /**
     * Removes and returns the last item.
     */
    @Override
    public byte pop() {
        lock.lockWrite();
        byte b = super.pop();
        lock.unlockWrite();
        return b;
    }

    /**
     * Returns the last item.
     */
    @Override
    public byte peek() {
        lock.lockRead();
        byte b = super.peek();
        lock.unlockRead();
        return b;
    }

    /**
     * Returns the first item.
     */
    @Override
    public byte first() {
        lock.lockRead();
        byte b = super.first();
        lock.unlockRead();
        return b;
    }

    /**
     * Returns true if the array has one or more items.
     */
    @Override
    public boolean notEmpty() {
        lock.lockRead();
        boolean b = super.notEmpty();
        lock.unlockRead();
        return b;
    }

    /**
     * Returns true if the array is empty.
     */
    @Override
    public boolean isEmpty() {
        lock.lockRead();
        boolean b = super.isEmpty();
        lock.unlockRead();
        return b;
    }

    @Override
    public void clear() {
        lock.lockWrite();
        super.clear();
        lock.unlockWrite();
    }

    /**
     * Reduces the size of the backing array to the size of the actual items. This is useful to release memory when many items
     * have been removed, or if it is known that more items will not be added.
     *
     * @return {@link #items}
     */
    @Override
    public byte[] shrink() {
        lock.lockWrite();
        byte[] b = super.shrink();
        lock.unlockWrite();
        return b;
    }

    @Override
    public void incr(int index, byte value) {
        lock.lockWrite();
        super.incr(index, value);
        lock.unlockWrite();
    }

    @Override
    public void mul(int index, byte value) {
        lock.lockWrite();
        super.mul(index, value);
        lock.unlockWrite();
    }

    @Override
    public boolean contains(byte value) {
        lock.lockRead();
        boolean b = super.contains(value);
        lock.unlockRead();
        return b;
    }

    @Override
    public int indexOf(byte value) {
        lock.lockRead();
        int i = super.indexOf(value);
        lock.unlockRead();
        return i;
    }

    @Override
    public int lastIndexOf(byte value) {
        lock.lockRead();
        int i = super.lastIndexOf(value);
        lock.unlockRead();
        return i;
    }

    @Override
    public boolean removeValue(byte value) {
        lock.lockWrite();
        boolean b = super.removeValue(value);
        lock.unlockWrite();
        return b;
    }

    /**
     * Removes from this array all of elements contained in the specified array.
     *
     * @param array
     * @return true if this array was modified.
     */
    @Override
    public boolean removeAll(ByteArray array) {
        boolean otherIsConcurrent = array instanceof ConcurrentCollection;
        if (otherIsConcurrent){
            ((ConcurrentCollection) array).getLock().lockRead();
        }
        lock.lockWrite();
        boolean b = super.removeAll(array);
        lock.unlockWrite();
        if (otherIsConcurrent){
            ((ConcurrentCollection) array).getLock().unlockRead();
        }
        return b;
    }

    @Override
    public void sort() {
        lock.lockWrite();
        super.sort();
        lock.unlockWrite();
    }

    /**
     * Increases the size of the backing array to accommodate the specified number of additional items. Useful before adding many
     * items to avoid multiple backing array resizes.
     *
     * @param additionalCapacity
     * @return {@link #items}
     */
    @Override
    public byte[] ensureCapacity(int additionalCapacity) {
        lock.lockWrite();
        byte[] b = super.ensureCapacity(additionalCapacity);
        lock.unlockWrite();
        return b;
    }

    /**
     * Sets the array size, leaving any values beyond the current size undefined.
     *
     * @param newSize
     * @return {@link #items}
     */
    @Override
    public byte[] setSize(int newSize) {
        lock.lockWrite();
        byte[] b = super.setSize(newSize);
        lock.unlockWrite();
        return b;
    }

    @Override
    public void reverse() {
        lock.lockWrite();
        super.reverse();
        lock.unlockWrite();
    }

    @Override
    public void shuffle() {
        lock.lockWrite();
        super.shuffle();
        lock.unlockWrite();
    }

    /**
     * Reduces the size of the array to the specified size. If the array is already smaller than the specified size, no action is
     * taken.
     *
     * @param newSize
     */
    @Override
    public void truncate(int newSize) {
        lock.lockWrite();
        super.truncate(newSize);
        lock.unlockWrite();
    }

    /**
     * Returns a random item from the array, or false if the array is empty.
     */
    @Override
    public byte random() {
        lock.lockRead();
        byte b = super.random();
        lock.unlockRead();
        return b;
    }

    @Override
    public byte[] toArray() {
        lock.lockRead();
        byte[] b = super.toArray();
        lock.unlockRead();
        return b;
    }

    @Override
    public int hashCode() {
        lock.lockRead();
        int i = super.hashCode();
        lock.unlockRead();
        return i;
    }

    /**
     * Returns false if either array is unordered.
     *
     * @param object
     */
    @Override
    public boolean equals(Object object) {
        boolean isOtherConcurrent = object instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection)object).getLock().lockRead();
        }
        lock.lockRead();
        boolean b = super.equals(object);
        lock.unlockRead();
        if (isOtherConcurrent){
            ((ConcurrentCollection)object).getLock().unlockRead();
        }
        return b;
    }

    @Override
    public String toString() {
        lock.lockRead();
        String s =  super.toString();
        lock.unlockRead();
        return s;
    }

    @Override
    public String toString(String separator) {
        lock.lockRead();
        String s = super.toString(separator);
        lock.unlockRead();
        return s;
    }

    @Override
    public ReadWriteLock getLock() {
        return lock;
    }
}
