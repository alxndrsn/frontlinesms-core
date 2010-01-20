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
	 * Get items and details to be displayed on a particular page of a list. 
	 * @param list The list or table whose items we are fetching 
	 * @param startIndex The index of the first item in the list
	 * @param limit The maximum number of items to display in the
	 * @return
	 */
	PagedListDetails getListDetails(Object list, int startIndex, int limit);
}
