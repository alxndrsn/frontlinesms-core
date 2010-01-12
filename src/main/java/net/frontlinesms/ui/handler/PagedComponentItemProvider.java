/**
 * 
 */
package net.frontlinesms.ui.handler;

/**
 * Provider of Thinlet UI components for display in a list controlled by {@link ComponentPagingHandler}.
 * @author Alex
 */
public interface PagedComponentItemProvider {
	/**
	 * Get items to be displayed on a particular page of a list. 
	 * @param list The list or table whose items we are fetching 
	 * @param start The index of the first item in the list
	 * @param limit The maximum number of items to display in the list
	 * @return a list of Thinlet UI components to be displayed in the list controlled by {@link ComponentPagingHandler}.
	 */
	Object[] getListItems(Object list, int start, int limit);
	/**
	 * @param list The list or table whose items we are fetching
	 * @return the total count of all items to be displayed in this list, over all pages
	 */
	int getTotalListItemCount(Object list);
}
