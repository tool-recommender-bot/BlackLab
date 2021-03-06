package nl.inl.blacklab.search;

public interface ResultsWindow {
	/**
	 * Are there more results in the original Hits object beyond our window?
	 *
	 * @return true if there are, false if not.
	 */
	boolean hasNext();

	/**
	 * Are there more results in the original Hits object "to the left" of our window?
	 *
	 * @return true if there are, false if not.
	 */
	boolean hasPrevious();

	/**
	 * Where would the next window start?
	 *
	 * @return index of the first result beyond our window
	 */
	int nextFrom();

	/**
	 * Where would the previous window start?
	 *
	 * @return index of the start result for the previous page
	 */
	int prevFrom();

	/**
	 * What's the first in the window?
	 *
	 * @return index of the first result
	 */
	int first();

	/**
	 * What's the last in the window?
	 *
	 * @return index of the last result
	 */
	int last();

	/**
	 * How many results are in this window?
	 *
	 * Note that this may be different from the specified "window size",
	 * as the window may not be full.
	 *
	 * @return number of results
	 */
	int size();

	/**
	 * How many results are available in the original source Hits object?
	 *
	 * @return total number of results
	 */
	int sourceSize();

	/**
	 * How many total results are in the original source Hits object?
	 *
	 * NOTE: this includes results that were counted but not retrieved.
	 *
	 * @return total number of results
	 */
	int sourceTotalSize();

	/**
	 * How many results per page did we request?
	 *
	 * @return number of results per page requested
	 */
	int requestedWindowSize();

}
