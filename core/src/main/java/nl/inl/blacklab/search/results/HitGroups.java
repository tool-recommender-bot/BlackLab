/*******************************************************************************
 * Copyright (c) 2010, 2012 Institute for Dutch Lexicology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package nl.inl.blacklab.search.results;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.inl.blacklab.resultproperty.GroupProperty;
import nl.inl.blacklab.resultproperty.HitProperty;
import nl.inl.blacklab.resultproperty.PropertyValue;

/**
 * Groups results on the basis of a list of criteria.
 *
 * This class allows random access to the groups, and each group provides random
 * access to the hits. Note that this means that all hits found must be
 * retrieved, which may be unfeasible for large results sets.
 */
public abstract class HitGroups implements ResultGroups<HitGroup> {
    QueryInfo queryInfo;

    protected HitProperty criteria;

    public HitGroups(QueryInfo queryInfo, HitProperty groupCriteria) {
        this.queryInfo = queryInfo;
        this.criteria = groupCriteria;
    }
    
    @Override
    public QueryInfo queryInfo() {
        return queryInfo;
    }

    public abstract Map<PropertyValue, HitGroup> getGroupMap();

    public abstract List<HitGroup> getGroups();

    public HitProperty getGroupCriteria() {
        return criteria;
    }
    
    /**
     * Sort groups by some property.
     * 
     * @param prop the property to sort on
     */
    public void sortGroups(GroupProperty prop) {
        sortGroups(prop, false);
    }

    /**
     * Sort groups by some property, ascending or descending.
     * 
     * @param prop the property to sort on
     * @param sortReverse if true, reverse the natural sort of the specified
     *            property.
     */
    public abstract void sortGroups(GroupProperty prop, boolean sortReverse);

    public HitGroup getGroup(PropertyValue identity) {
        return getGroupMap().get(identity);
    }

    @Override
    public Iterator<HitGroup> iterator() {
        final Iterator<HitGroup> currentIt = getGroups().iterator();

        return new Iterator<HitGroup>() {

            @Override
            public boolean hasNext() {
                return currentIt.hasNext();
            }

            @Override
            public HitGroup next() {
                return currentIt.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    /**
     * Get the total number of hits
     *
     * @return the number of hits
     */
    @Override
    public abstract int getTotalResults();

    /**
     * Return the size of the largest group
     *
     * @return size of the largest group
     */
    @Override
    public abstract int getLargestGroupSize();

    /**
     * Return the number of groups
     *
     * @return number of groups
     */
    @Override
    public abstract int numberOfGroups();
}
