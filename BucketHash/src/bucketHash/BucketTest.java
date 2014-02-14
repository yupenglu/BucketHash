package bucketHash;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import org.junit.Before;
import org.junit.Test;

public class BucketTest {
	
	BucketHash<Integer, String> hashtable;
	BucketHash<Integer, String>.Bucket bucket;
	
	@Before
	public void setUp() throws Exception {
		hashtable = new BucketHash<Integer, String>();
		hashtable.put(6, "6");
		hashtable.put(3, "3");
		hashtable.put(9, "9");
		bucket = hashtable.getBucket(9);
	}

	@Test
	public void testBucket() {
		// when calling the constructor of BucketHash
		bucket = hashtable.new Bucket();
		assertEquals(null, bucket.header);
	}
	
	@Test
	public void testSize() {
		assertEquals(1, bucket.size());
	}
	
	@Test
	public void testGet() {
		assertEquals("9", bucket.get(9));
		assertEquals(null, bucket.get(3));
	}
	
	@Test
	public void testPut() {
		// if the key is already in the list
		assertEquals("9", bucket.put(9, "19"));
		assertEquals("19", bucket.get(9));
		
		// if the key is not present in the list
		assertEquals(null, bucket.put(2, "2"));
		assertEquals("2", bucket.get(2));
	}

	@Test (expected = IllegalArgumentException.class)
	public void shouldNotPutNullAsKey() {
		bucket.put(null, "118");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void shouldNotPutNullAsValue() {
		bucket.put(118, null);
	}
	
	@Test
	public void testRemove() {
		// if the key is in the list
		assertEquals("9", bucket.remove(9));
		
		// if the key is not present in the list
		assertEquals(null, bucket.remove(9));
		
		// remove null key should return null
		assertEquals(null, bucket.remove(null));
	}
	
	@Test
	public void testKeySet() {
		HashSet<Integer> testSet = new HashSet<Integer>();
		testSet.add(9);
		assertEquals(testSet, bucket.keySet());
		
		// test keySet for empty Bucket
		bucket.remove(9);
		assertEquals(new HashSet<Integer>(), bucket.keySet());
	}
	
	@Test
	public void testValues() {
		ArrayList<String> testArrayList = new ArrayList<String>();
		testArrayList.add("9");
		assertEquals(testArrayList, bucket.values());
		
		// test values for empty Bucket
		bucket.remove(9);
		assertEquals(new ArrayList<String>(), bucket.values());
	}
	
	@Test
	public void testToString() {
		bucket.put(20, "20");
		System.out.println(bucket);
		assertEquals("20\t20\n9\t9\n", bucket.toString());
	}
	
}







