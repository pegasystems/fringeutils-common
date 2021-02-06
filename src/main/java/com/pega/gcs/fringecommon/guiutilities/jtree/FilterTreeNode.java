/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.jtree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;

public class FilterTreeNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 8798582356195234831L;

    private Map<String, Filter> filterMap;

    private boolean passed;

    private List<FilterTreeNode> filteredChildren;

    public FilterTreeNode(Object userObject) {

        super(userObject);

        this.passed = false;
        this.filterMap = new HashMap<String, Filter>();
        this.filteredChildren = new ArrayList<FilterTreeNode>();
    }

    protected void addFilter(Filter newfilter) {

        if (newfilter != null) {

            // only one instance of each key of filter should be maintained
            String key = newfilter.getKey();

            filterMap.put(key, newfilter);

            applyFilter();

            // sort();

        }

    }

    private void setFilterMap(Map<String, Filter> filterMap) {
        this.filterMap = filterMap;
    }

    private void applyFilter() {

        passed = false;
        filteredChildren.clear();

        if (filterMap.size() > 0) {

            for (Filter filter : filterMap.values()) {

                if (filter.pass(this)) {
                    passed = true;
                } else {
                    passed = false;
                    break;
                }
            }

            if (passed) {
                passed = true;
                passFilterDown(filterMap);
            } else {
                passFilterDown(filterMap);
                passed = filteredChildren.size() != 0;
            }

        } else {
            passed = true;
            passFilterDown(null);
        }
    }

    private void passFilterDown(Map<String, Filter> filterMap) {

        int realChildCount = super.getChildCount();

        for (int i = 0; i < realChildCount; i++) {

            FilterTreeNode realChild = (FilterTreeNode) super.getChildAt(i);

            realChild.setFilterMap(filterMap);

            realChild.applyFilter();

            if (realChild.isPassed()) {
                filteredChildren.add(realChild);
            }
        }

    }

    public void addNode(FilterTreeNode node) {

        super.add(node);

        node.setFilterMap(filterMap);

        // // TODO work up
        // if (node.isPassed()) {
        // filteredChildren.add(node);
        // }

        // sort();
    }

    @Override
    public void remove(int childIndex) {

        if (filterMap.size() > 0) {
            throw new IllegalStateException("Can't remove if the filter is active");
        }

        super.remove(childIndex);
    }

    @Override
    public int getChildCount() {

        int childCount = 0;

        if (filterMap.size() == 0) {
            childCount = super.getChildCount();
        } else {
            childCount = filteredChildren.size();
        }

        return childCount;
    }

    @Override
    public FilterTreeNode getChildAt(int index) {

        FilterTreeNode child = null;

        if (filterMap.size() == 0) {
            child = (FilterTreeNode) super.getChildAt(index);
        } else {
            child = filteredChildren.get(index);
        }

        return child;
    }

    public boolean isPassed() {
        return passed;
    }

    public Set<FilterTreeNode> getLeaves() {
        Set<FilterTreeNode> result = new HashSet<FilterTreeNode>();
        if (super.getChildCount() == 0) {
            result.add(this);
        } else {
            for (int i = 0; i < super.getChildCount(); i++) {
                FilterTreeNode child = (FilterTreeNode) super.getChildAt(i);
                result.addAll(child.getLeaves());
            }
        }
        return result;
    }

    public FilterTreeNode getChildForObject(Object userObject) {
        FilterTreeNode result = null;

        for (int i = 0; (i < super.getChildCount()) && (result == null); i++) {

            FilterTreeNode child = (FilterTreeNode) super.getChildAt(i);
            Object nodeObject = child.getUserObject();

            if (nodeObject.toString().equals(userObject.toString())) {
                result = child;
            }
        }

        return result;
    }

    // private void sort() {
    //
    // Collections.sort(filteredChildren, new Comparator<FilterTreeNode>() {
    // @Override
    // public int compare(FilterTreeNode o1, FilterTreeNode o2) {
    //
    // Object o1UserObject = o1.getUserObject();
    // Object o2UserObject = o2.getUserObject();
    //
    // if ((!(o1.isRootNode())) && (!(o2.isRootNode()))
    // && (o1UserObject instanceof Movie)
    // && (o2UserObject instanceof Movie)) {
    //
    // Movie o1Movie = (Movie) o1UserObject;
    // Movie o2Movie = (Movie) o2UserObject;
    //
    // Date o1DateAdded = new Date(o1Movie.getDateAdded());
    // Date o2DateAdded = new Date(o2Movie.getDateAdded());
    //
    // String o1DateAddedStr = DateFormatUtils.format(o1DateAdded,
    // "yyyy/MM/dd");
    // String o2DateAddedStr = DateFormatUtils.format(o2DateAdded,
    // "yyyy/MM/dd");
    //
    // // Descending Order
    // // return (o1DateAdded < o2DateAdded ? 1
    // // : (o1DateAdded == o2DateAdded ? 0 : -1));
    //
    // int compared = o1DateAddedStr.compareTo(o2DateAddedStr);
    //
    // // Descending Order
    // compared = compared * (-1);
    //
    // if (compared == 0) {
    // compared = o1Movie.compareTo(o2Movie);
    // }
    //
    // return compared;
    // }
    //
    // return 0;
    // }
    // });
    // }
}
