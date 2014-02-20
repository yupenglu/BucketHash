package bucketHash;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Yupeng Lu
 * Implementation of a hash map.
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class BucketHash<K, V> implements Map<K, V> {
	
	/**
	 * Number of buckets that should be predefined.
	 */
	private int numBuckets = 101;
	
	/**
	 * Field that keeps an array of linkedlist.
	 */
	private Object[] buckets = new Object[numBuckets];

	// The two standard constructors for any Map implementation
	// All the methods required to implement the Map interface

	/**
	 * Constructs an empty array of linked list
	 */
	BucketHash() {
		for (int i = 0; i < numBuckets; i++) {
			buckets[i] = new Bucket();
		}
	}
	
	/**
	 * Constructs an new bucket hash by copying all the entries in map.
	 * @param map
	 */
	BucketHash(Map<K, V> map) {
		for (int i = 0; i < numBuckets; i++) {
			buckets[i] = new Bucket();
		}
		for (Map.Entry<K, V> i: map.entrySet()) {
			this.getBucket(i.getKey()).put(i.getKey(), i.getValue());
		}
	}
	
	// Plus all the following...
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		Set<K> key = (Set<K>) keySet();
		if (o.hashCode() != hashCode()) return false;
		if (! key.equals(((BucketHash<?, ?>) o).keySet())) return false;
		for (K k: key) {
			if (get(k) != ((Map<K, V>) o).get(k))
				return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int hashCode = 0;
		if (keySet() == null) return 0;
		for (K k: keySet()) {
			hashCode += calculateHashCode(k, get(k));
		}
		return hashCode;
	}
	
	/**
	 * Returns a object of Bucket that containing the given key
	 * @param key key that is used to get the corresponding bucket
	 * @return the bucket associated with the given key
	 */
	Bucket getBucket(K key) {
		if (key == null) throw new IllegalArgumentException("null key");
		int hashCode = key.hashCode();
		return (Bucket) buckets[hashCode % numBuckets];
	}
	
	/**
	 * Given a key, calculate the index of the bucket that the entry should be put in.
	 * @param o object that is going to be put in the hash table
	 * @return
	 */
	private int getIndex(Object o) { // optional
		return o.hashCode() % numBuckets;
	}

	/**
	 * @author Yupeng Lu
	 * Inner class that implement a linked list
	 */
	class Bucket {
		
		/**
		 * Head of the linked list
		 */
		ListNode header = null;
		
		/**
		 * @author Yupeng Lu
		 * Inner class that constructs a form of node in linked list
		 */
		private class ListNode  {
			
			/**
			 * Key of the hash map entry.
			 */
			K key;
			
			/**
			 * Value of the hash map entry.
			 */
			V value;
			
			/**
			 * Reference of the next node in the linked list.
			 */
			ListNode next;
			
			/**
			 * Constructs a linked list node with the given entry
			 * @param key
			 * @param value
			 * @param next
			 */
			ListNode(K key, V value, ListNode next) { // Constructor
				this.key = key;
				this.value = value;
				this.next = next;
			}
			
			/* (non-Javadoc)
			 * @see java.lang.Object#hashCode()
			 */
			public int hashCode() {
				return calculateHashCode(key, value);
			}
		}
		
		/**
		 * Constructs an empty linked list.
		 */
		public Bucket() { // Constructor
			header = null;
		}
		
		/**
		 * Returns the size of the linked list. Returns 0 if header is null.
		 * @return size of the linked list
		 */
		public int size() {
			ListNode tempNode = header;
			int size = 0;
			while (tempNode != null) {
				tempNode = tempNode.next;
				size ++;
			}
			return size;
		}
		
		/**
		 * Given a key, look for the corresponding value in the linked list. 
		 * @param key key of the entry
		 * @return value of the entry
		 */
		public V get(K key) {
			if (find(key) == null) return null;
			else return find(key).value;
		}
		
		/**
		 * Tests whether the linked list is empty.
		 * @return true if the linked list contains no node
		 */
		private boolean isEmpty() {
			return header == null;
		}
		
		/**
		 * Returns the ListNode containing the key, or null. 
		 * Useful as a helper method for some of the other methods.
		 * @param key key that used to look for the node entry
		 * @return the node of the linked list
		 */
		private ListNode find(K key) { // optional
			ListNode tempNode = header;
			while (tempNode != null) {
				if (tempNode.key.equals(key))
					return tempNode;
				tempNode = tempNode.next;
			}
			return null;
		}
		
		/**
		 * Generates a node in the linked list with the given key and value
		 * @param key with which the specified value is to be associated
		 * @param value value to be associated with the specified key
		 * @return the previous value associated with key,
		 * or null if there was no node containing the key.
		 * (A null return can also indicate that the linked list previously associated null with key.)
		 */
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
		
		/**
		 * Removes the list node containing the given key.
		 * @param key key whose node is to be removed from the linked list
		 * @return the previous value associated with key, 
		 * or null if there was no mapping for key. 
		 * (A null return can also indicate that the map previously associated null with key.)
		 */
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
		
		/**
		 * Returns a Set of the keys contained in this linked list.
		 * @return a set of keys contained in the linked list
		 */
		public Set<K> keySet() {
			Set<K> keySet = new HashSet<K>();
			ListNode tempNode = header;
			while (tempNode != null) {
				keySet.add(tempNode.key);
				tempNode = tempNode.next;
			}
			return keySet;
		}
		
		/**
		 * Returns a Collection of the values contained in this linked list.
		 * @return a collection of values contained in the linked list
		 */
		public Collection<V> values() {
			Collection<V> values = new ArrayList<V>();
			ListNode tempNode = header;
			while (tempNode != null) {
				values.add(tempNode.value);
				tempNode = tempNode.next;
			}
			return values;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
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
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
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

	/* (non-Javadoc)
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		int size = 0;
		for (int i = 0; i < numBuckets; i++) {
			size += ((Bucket) buckets[i]).size();
		}
		return size;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		for (int i = 0; i < numBuckets; i++) {
			if (! ((Bucket) buckets[i]).isEmpty())
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		return get(key) != null;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(Object value) {
		Collection<V> values = (ArrayList<V>) values();
		for (V v: values) {
			if (v.equals(value))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public V get(Object key) {
		return getBucket((K) key).get((K) key);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V put(K key, V value) {
		return (getBucket(key).put(key, value));
	}

	/* (non-Javadoc)
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public V remove(Object key) {
		if (key == null) return null;
		return ((Bucket) buckets[getIndex(key)]).remove((K) key);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for(K key: m.keySet()) {
			put(key, m.get(key));
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		buckets = new Object[numBuckets];
		for (int i = 0; i < numBuckets; i++) {
			buckets[i] = new Bucket();
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<K> keySet() {
		Set<K> keySet = new HashSet<K>();
		for (int i = 0; i < numBuckets; i++) {
			keySet.addAll(((Bucket) buckets[i]).keySet());
		}
		return keySet;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<V> values() {
		Collection<V> values = new ArrayList<V>();
		for (int i = 0; i < numBuckets; i++) {
			values.addAll(((Bucket) buckets[i]).values());
		}
		return values;
	}

	/**
	 * entrySet() method is not supported in BucketHash class
	 */
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Helper method that calculate the hashcode for a map entry.
	 * @param key the key of the entry
	 * @param value the value of the entry
	 * @return the value of hashcode
	 */
	private int calculateHashCode(K key, V value) {
		return (key == null ? 0 : key.hashCode()) ^
	     (value == null ? 0 : value.hashCode());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
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
