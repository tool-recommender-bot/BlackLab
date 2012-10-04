/*******************************************************************************
 * Copyright (c) 2010, 2012 Institute for Dutch Lexicology.
 * All rights reserved.
 *******************************************************************************/
package nl.inl.blacklab.search.sequences;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;
import nl.inl.blacklab.search.Hit;
import nl.inl.blacklab.search.lucene.SpansStub;

import org.apache.lucene.search.spans.Spans;
import org.junit.Before;
import org.junit.Test;

public class TestSpansInBucketsConsecutive {
	private SpansInBuckets hpd;

	@Before
	public void setUp() {
		int[] doc = { 1, 1, 2, 2, 2, 2 };
		int[] start = { 1, 2, 3, 4, 6, 7 };
		int[] end = { 2, 3, 4, 5, 7, 8 };
		Spans spans = new SpansStub(doc, start, end);
		hpd = new SpansInBucketsConsecutive(spans);
	}

	@Test
	public void testListInterface() throws IOException {
		Assert.assertTrue(hpd.next());
		List<Hit> l = hpd.getHits();
		Assert.assertEquals(1, hpd.doc());
		Assert.assertEquals(2, l.size());
		Assert.assertEquals(1, l.get(0).start);
		Assert.assertEquals(2, l.get(0).end);
		Assert.assertEquals(2, l.get(1).start);
		Assert.assertEquals(3, l.get(1).end);

		Assert.assertTrue(hpd.next());
		l = hpd.getHits();
		Assert.assertEquals(2, hpd.doc());
		Assert.assertEquals(2, l.size());
		Assert.assertEquals(3, l.get(0).start);
		Assert.assertEquals(4, l.get(0).end);

		Assert.assertTrue(hpd.next());
		l = hpd.getHits();
		Assert.assertEquals(2, l.size());
		Assert.assertEquals(6, l.get(0).start);
		Assert.assertEquals(7, l.get(0).end);

		Assert.assertFalse(hpd.next());
	}

}