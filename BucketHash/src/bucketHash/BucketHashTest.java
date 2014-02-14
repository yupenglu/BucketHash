package bucketHash;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class BucketHashTest {

	BucketHash<Integer, String> hashtable;
	
	@Before
	public void setUp() throws Exception {
		hashtable = new BucketHash<Integer, String>();
		hashtable.put(6, "6");
		hashtable.put(3, "3");
		hashtable.put(9, "9");
		hashtable.put(19, "9");
	}

	@Test
	public void testBucketHash() {
		hashtable = new BucketHash<Integer, String>();
		assertTrue(hashtable.isEmpty());
	}
	
	@Test
	public void testBucketHashMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(3, "3");
		map.put(6, "6");
		map.put(9, "9");
		map.put(19, "9");
		BucketHash<Integer, String> hashtableTwo = new BucketHash<Integer, String>(map);
		assertEquals(hashtable, hashtableTwo);
	}
	
	@Test
	public void testHashCode() {
		int hashCode = 0;
		String v;
		for (Integer i: hashtable.keySet()) {
			v = hashtable.get(i);
			hashCode += (i == null ? 0 : i.hashCode()) ^
				     (v == null ? 0 : v.hashCode());
		}
		assertEquals(hashCode, hashtable.hashCode());
	}

	@Test
	public void testEqualsObject() {
		BucketHash<Integer, String> hashtableTwo = new BucketHash<Integer, String>();
		hashtableTwo.put(6, "6");
		hashtableTwo.put(3, "3");
		hashtableTwo.put(9, "9");
		hashtableTwo.put(19, "9");
		assertTrue(hashtable.equals(hashtableTwo));
		
		hashtableTwo.remove(19);
		assertFalse(hashtable.equals(hashtableTwo));
	}

	@Test
	public void testGetBucket() {
		assertEquals("9", hashtable.getBucket(9).get(9));
	}

	@Test
	public void testSize() {
		assertEquals(4, hashtable.size());
	}

	@Test
	public void testIsEmpty() {
		assertFalse(hashtable.isEmpty());
	}

	@Test
	public void testContainsKey() {
		assertTrue(hashtable.containsKey(9));
		assertFalse(hashtable.containsKey(39));
	}

	@Test
	public void testContainsValue() {
		assertTrue(hashtable.containsValue("9"));
		assertFalse(hashtable.containsValue("39"));
	}

	@Test
	public void testGet() {
		assertEquals("9", hashtable.get(9));
		assertEquals(null, hashtable.get(118));
	}

	@Test
	public void testPut() {
		// put when key is in the map
		assertEquals("9", hashtable.get(9));
		assertEquals("9", hashtable.put(9, "19"));
		assertEquals("19", hashtable.get(9));
		
		// put when key is not in the map
		assertEquals(null, hashtable.get(118));
		assertEquals(null, hashtable.put(118, "118"));
		assertEquals("118", hashtable.get(118));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void shouldNotPutNullAsKey() {
		hashtable.put(null, "118");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void shouldNotPutNullAsValue() {
		hashtable.put(118, null);
	}

	@Test
	public void testRemove() {
		// remove when key is in the map
		assertEquals("9", hashtable.get(9));
		assertEquals("9", hashtable.remove(9));
		assertEquals(null, hashtable.get(9));
		
		// remove when key is not present
		assertEquals(null, hashtable.get(118));
		assertEquals(null, hashtable.remove(118));
		assertEquals(null, hashtable.get(118));
		
		// remove null key
		assertEquals(null, hashtable.remove(null));
	}

	@Test
	public void testPutAll() {
		BucketHash<Integer, String> hashtableTwo = new BucketHash<Integer, String>();
		hashtableTwo.put(118, "118");
//		hashtableTwo.put(239, "239");
		hashtable.putAll(hashtableTwo);
		assertEquals("118", hashtable.get(118));
//		assertEquals("239", hashtable.get(239));
	}

	@Test
	public void testClear() {
		hashtable.clear();
		assertTrue(hashtable.isEmpty());
	}

	@Test
	public void testKeySet() {
		Set<Integer> testKeySet = new HashSet<Integer>();
		testKeySet.add(6);
		testKeySet.add(3);
		testKeySet.add(9);
		testKeySet.add(19);
		Set<Integer> keySet = hashtable.keySet();
		assertEquals(testKeySet, keySet);
		
//		// the generated keySet should always be updated
//		hashtable.put(118, "118");
//		testKeySet.add(118);
//		assertEquals(testKeySet, keySet);
//		
//		// the set should support element removal
//		keySet.remove(118);
//		assertEquals(null, hashtable.get(118));
	}

	@Test
	public void testValues() {
		Collection<String> testValues = new ArrayList<String>();
		testValues.add("3");
		testValues.add("6");
		testValues.add("9");
		testValues.add("9");
		ArrayList<String> values = (ArrayList<String>) hashtable.values();
		assertEquals(testValues, values);
		
//		// the generated collection of values should always be updated
//		hashtable.put(118, "118");
//		testValues.add("118");
//		assertEquals(testValues, values);
//		
//		// the set should support element removal
//		hashtable.put(128, "118");
//		values.remove(118);
//		assertEquals(null, hashtable.get(118));
//		assertEquals(null, hashtable.get(128));
	}

	@Test (expected = UnsupportedOperationException.class)
	public void testEntrySet() {
		hashtable.entrySet();
		System.out.println(hashtable);
	}
}
