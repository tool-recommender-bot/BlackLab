/*******************************************************************************
 * Copyright (c) 2010, 2012 Institute for Dutch Lexicology.
 * All rights reserved.
 *******************************************************************************/
package nl.inl.blacklab.search.grouping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.inl.blacklab.search.Hit;
import nl.inl.blacklab.search.Hits;
import nl.inl.blacklab.search.Hits.ConcType;
import nl.inl.blacklab.search.Searcher;

import org.apache.lucene.search.spans.Spans;

/**
 * Groups results on the basis of a list of criteria, and provide random access to the resulting
 * groups.
 *
 * This implementation doesn't care in what order the spans appear, it will just retrieve all of
 * them and put each of them in a group. This takes more memory and time than if the spans to be
 * grouped are sequential (in which case you should use ResultsGrouperSequential).
 */
public class ResultsGrouper extends RandomAccessGroups {
	/**
	 * The groups.
	 */
	Map<String, RandomAccessGroup> groups = new HashMap<String, RandomAccessGroup>();

	/**
	 * The groups, in sorted order.
	 */
	List<RandomAccessGroup> groupsOrdered = new ArrayList<RandomAccessGroup>();

	/**
	 * Default field to make concordances from.
	 */
	private String defaultConcField;

	/**
	 * Field our current concordances came from.
	 */
	private String concField;

	/**
	 * Current concordance type (might be from the original content, or reconstructed from the term
	 * vector / forward index.
	 */
	private ConcType concType;

	/**
	 * Total number of hits.
	 */
	private int totalHits = 0;

	/**
	 * Size of the largest group.
	 */
	private int largestGroupSize = 0;

	/**
	 * Construct a ResultsGrouper object, by grouping the supplied spans.
	 *
	 * @param searcher
	 *            our Searcher object
	 * @param source
	 *            the Spans to group
	 * @param criteria
	 *            the criteria to group on
	 * @param defaultConcField
	 *            the default concordance field
	 */
	public ResultsGrouper(Searcher searcher, Spans source, HitProperty criteria,
			String defaultConcField) {
		this(new Hits(searcher, source, defaultConcField), criteria);
	}

	/**
	 * Construct a ResultsGrouper object, by grouping the supplied hits.
	 *
	 * @param hits
	 *            the hits to group
	 * @param criteria
	 *            the criteria to group on
	 */
	public ResultsGrouper(Hits hits, HitProperty criteria) {
		super(hits.getSearcher(), criteria);
		defaultConcField = hits.getDefaultConcordanceField();
		if (criteria.needsConcordances())
			hits.findConcordances(true);
		concField = hits.getConcordanceField();
		concType = hits.getConcordanceType();
		for (Hit hit : hits) {
			addHit(hit);
		}
	}

	/**
	 * Add a hit to the appropriate group.
	 *
	 * @param hit
	 *            the hit to add
	 */
	public void addHit(Hit hit) {
		String idStr = getGroupIdentity(hit);
		RandomAccessGroup group = groups.get(idStr);
		if (group == null) {
			group = new RandomAccessGroup(searcher, getGroupIdentity(hit),
					getHumanReadableGroupIdentity(hit), defaultConcField);
			group.setConcordanceStatus(concField, concType);
			groups.put(idStr, group);
			groupsOrdered.add(group);
		}
		group.add(hit);
		if (group.size() > largestGroupSize)
			largestGroupSize = group.size();
		totalHits++;
	}

	/**
	 * Get the total number of hits
	 *
	 * @return the number of hits
	 */
	@Override
	public int getTotalResults() {
		return totalHits;
	}

	/**
	 * Get all groups as a map
	 *
	 * @return a map of groups indexed by group property
	 */
	@Override
	public Map<String, RandomAccessGroup> getGroupMap() {
		return Collections.unmodifiableMap(groups);
	}

	/**
	 * Get all groups as a list
	 *
	 * @return the list of groups
	 */
	@Override
	public List<RandomAccessGroup> getGroups() {
		return Collections.unmodifiableList(groupsOrdered);
	}

	/**
	 * Sort groups
	 *
	 * @param prop
	 *            the property to sort on
	 * @param sortReverse
	 *            whether to sort in descending order
	 */
	@Override
	public void sortGroups(GroupProperty prop, boolean sortReverse) {
		Comparator<Group> comparator = new ComparatorGroupProperty(prop, sortReverse,
				searcher.getCollator());

		Collections.sort(groupsOrdered, comparator);
	}

	/**
	 * Return the size of the largest group
	 *
	 * @return size of the largest group
	 */
	@Override
	public int getLargestGroupSize() {
		return largestGroupSize;
	}

	/**
	 * Return the number of groups
	 *
	 * @return number of groups
	 */
	@Override
	public int numberOfGroups() {
		return groups.size();
	}
}