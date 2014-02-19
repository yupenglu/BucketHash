package bucketHash;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
		for (int i = 0; i < numBuckets; i++) {
			buckets[i] = new Bucket();
		}
		for (Map.Entry<K, V> i: map.entrySet()) {
			this.getBucket(i.getKey()).put(i.getKey(), i.getValue());
		}
	}
	
	// Plus all the following...
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		Set<K> key = (Set<K>) keySet();
//		if (!(o instanceof BucketHash<?, ?>)) return false;
		if (o.hashCode() != hashCode()) return false;
		if (! key.equals(((BucketHash<?, ?>) o).keySet())) return false;
		for (K k: key) {
			if (get(k) != ((Map<K, V>) o).get(k))
				return false;
		}
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
	
	@SuppressWarnings("unchecked")
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
					while (temp.next != null) {
						temp = temp.next;
					}
					temp.next = node;
				}
			    node.value = value;
				return null;
			}
		}
		
		public V remove(K key) {
			
			if (key == null || header == null) return null;
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
			Collection<V> values = new ArrayList<V>();
			ListNode tempNode = header;
			while (tempNode != null) {
				values.add(tempNode.value);
				tempNode = tempNode.next;
			}
			return values;
		}
		
		public String toString() { // optional
			String output = "";
//			output += "Key\tValue\n";
			V value;
			for (K key: keySet()) {
				value = get(key);
				output += key + "\t" + value + "\n";
			}
			return output;
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

	@SuppressWarnings("unchecked")
	@Override
	public int size() {
		int size = 0;
		for (int i = 0; i < numBuckets; i++) {
			size += ((Bucket) buckets[i]).size();
		}
		return size;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isEmpty() {
		for (int i = 0; i < numBuckets; i++) {
			if (! ((Bucket) buckets[i]).isEmpty())
				return false;
		}
		return true;
	}

	@Override
	public boolean containsKey(Object key) {
		return get(key) != null;
	}

	@Override
	public boolean containsValue(Object value) {
		Collection<V> values = (ArrayList<V>) values();
		for (V v: values) {
			if (v.equals(value))
				return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key) {
		return getBucket((K) key).get((K) key);
	}

	@Override
	public V put(K key, V value) {
		return (getBucket(key).put(key, value));
	}

	@SuppressWarnings("unchecked")
	@Override
	public V remove(Object key) {
		if (key == null) return null;
		return ((Bucket) buckets[getIndex(key)]).remove((K) key);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for(K key: m.keySet()) {
			put(key, m.get(key));
		}
	}

	@Override
	public void clear() {
		buckets = new Object[numBuckets];
		for (int i = 0; i < numBuckets; i++) {
			buckets[i] = new Bucket();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<K> keySet() {
		Set<K> keySet = new HashSet<K>();
		for (int i = 0; i < numBuckets; i++) {
			keySet.addAll(((Bucket) buckets[i]).keySet());
		}
		return keySet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<V> values() {
		Collection<V> values = new ArrayList<V>();
		for (int i = 0; i < numBuckets; i++) {
			values.addAll(((Bucket) buckets[i]).values());
		}
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
	
	@Override
	public String toString() {
		String output = "";
		output += "Key\tValue\n";
		for (int i = 0; i < numBuckets; i++) {
			output += buckets[i].toString();
		}
		
		return output;
		
	}

}
