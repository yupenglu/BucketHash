package bucketHash;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BucketHash<K, V> implements Map<K, V> {
	private int numBuckets = 101;
	private Object[] buckets = new Object[numBuckets];

	// The two standard constructors for any Map implementation
	// All the methods required to implement the Map interface

	BucketHash() {
		for (int i = 0; i < numBuckets; i++) {
			buckets[i] = new Bucket();
		}
	}
	
	BucketHash(Map<K, V> map) {
		
	}
	
	// Plus all the following...
	public boolean equals(Object o) {
		return true;
	}
	
	public int hashCode() {
		int hashCode = 0;
		if (keySet() == null) return 0;
		for (K k: keySet()) {
			hashCode += calculateHashCode(k, get(k));
		}
		return hashCode;
	}
	
	Bucket getBucket(K key) {
		if (key == null) throw new IllegalArgumentException("null key");
		int hashCode = key.hashCode();
		return (Bucket) buckets[hashCode % numBuckets];
	}
	
	private int getIndex(Object o) { // optional
		return o.hashCode() % numBuckets;
	}

	class Bucket {
		ListNode header = null;
		
		private class ListNode  {
			K key;
			V value;
			ListNode next;
			ListNode(K key, V value, ListNode next) { // Constructor
				this.key = key;
				this.value = value;
				this.next = next;
			}
			
			public int hashCode() {
				return calculateHashCode(key, value);
			}
		}
		
		public Bucket() { // Constructor
			header = null;
		}
		
		public int size() {
			ListNode tempNode = header;
			int size = 0;
			while (tempNode != null) {
				tempNode = tempNode.next;
				size ++;
			}
			return size;
		}
		
		public V get(K key) {
			if (find(key) == null) return null;
			else return find(key).value;
		}
		
		private boolean isEmpty() {
			return header == null;
		}
		
		private ListNode find(K key) { // optional
			ListNode tempNode = header;
			while (tempNode != null) {
				if (tempNode.key == key)
					return tempNode;
				tempNode = tempNode.next;
			}
			return null;
		}
		
		public V put(K key, V value) {
			if (key == null || value == null) {
				throw new IllegalArgumentException();
			} else if (find(key) != null) {
				V tempValue = find(key).value;
				find(key).value = value;
				return tempValue;
			} else {
				ListNode node = new ListNode(key, value, null);
				if (header == null) header = node;
				else {
					ListNode temp = header;
					while (temp != null) {
						temp = temp.next;
					}
					temp = node;
				}
			    node.value = value;
				return null;
			}
		}
		
		public V remove(K key) {
			if (header == null) return null;
			if (header.key.equals(key)) {
				V tempValue = header.value;
				header = header.next;
				return tempValue;
			} else {
				ListNode tempNode = header;
				ListNode tempNodeTwo = header;
				while (tempNode != null) {
					tempNode = tempNode.next;
					if (tempNode.key.equals(key)) {
						tempNodeTwo.next = tempNode.next;
						return tempNode.value;
					}
					tempNodeTwo = tempNode;
				}
				System.out.println("remove");
			}
			return null;
		}
		
		public Set<K> keySet() {
			Set<K> keySet = new HashSet<K>();
			ListNode tempNode = header;
			while (tempNode != null) {
				keySet.add(tempNode.key);
				tempNode = tempNode.next;
			}
			return keySet;
		}
		
		public Collection<V> values() {
			Collection<V> values = new HashSet<V>();
			ListNode tempNode = header;
			while (tempNode != null) {
				values.add(tempNode.value);
				tempNode = tempNode.next;
			}
			return values;
		}
		
		public String toString() { // optional
			return null;
		}
		
		@Override
		public int hashCode() {
			int hashCode = 0;
			ListNode tempNode = header;
			while (tempNode != null) {
				hashCode += tempNode.hashCode();
				tempNode = tempNode.next;
			}
			return hashCode;
		}
	}

	@Override
	public int size() {
		int size = 0;
		for (int i = 0; i < numBuckets; i++) {
			size += ((Bucket) buckets[i]).size();
		}
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < numBuckets; i++) {
			if (! ((Bucket) buckets[i]).isEmpty())
				return false;
		}
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean containsKey(Object key) {
		
		// TODO Auto-generated method stub
		return get(key) != null;
	}

	@Override
	public boolean containsValue(Object value) {
		Set<V> values = (Set<V>) values();
		for (V v: values) {
			if (v.equals(value))
				return true;
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public V get(Object key) {
		return getBucket((K) key).get((K) key);
		// TODO Auto-generated method stub
	}

	@Override
	public V put(K key, V value) {
		return (getBucket(key).put(key, value));
	}

	@Override
	public V remove(Object key) {
		return ((Bucket) buckets[getIndex(key)]).remove((K) key);
		// TODO Auto-generated method stub
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for(K key: m.keySet()) {
			put(key, m.get(key));
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		buckets = new Object[numBuckets];
		for (int i = 0; i < numBuckets; i++) {
			buckets[i] = new Bucket();
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<K> keySet() {
		Set<K> keySet = new HashSet<K>();
		for (int i = 0; i < numBuckets; i++) {
			keySet.addAll(((Bucket) buckets[i]).keySet());
		}
		return keySet;
	}

	@Override
	public Collection<V> values() {
		Collection<V> values = new HashSet<V>();
		for (int i = 0; i < numBuckets; i++) {
			values.addAll(((Bucket) buckets[i]).values());
		}
		// TODO Auto-generated method stub
		return values;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}
	
	private int calculateHashCode(K key, V value) {
		return (key == null ? 0 : key.hashCode()) ^
	     (value == null ? 0 : value.hashCode());
	}
}
