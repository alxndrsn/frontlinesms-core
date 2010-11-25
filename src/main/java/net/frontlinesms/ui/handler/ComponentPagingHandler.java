package net.frontlinesms.ui.handler;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import net.frontlinesms.ui.i18n.TextResourceKeyOwner;

/**
 * Thinlet UI event handler for paging lists and tables.
 * @author Alex Anderson <alex@frontlinesms.com>
 */
@TextResourceKeyOwner
public class ComponentPagingHandler implements ThinletUiEventHandler {
	

	//> CONSTANTS
	/** Thinlet UI layout file for paging controls */
	private static final String UI_FILE_PAGE_PANEL = "/ui/core/util/pnPageControls.xml";
	
	/** I18N Text key: "Page X of Y" */
	private static final String I18N_KEY_PAGES_X_OF_Y = "common.page.number";

	/** UI Component name for  */
	private static final String COMPONENT_BT_NEXT_PAGE = "btNextPage";
	/** UI Component name for  */
	private static final String COMPONENT_BT_PREVIOUS_PAGE = "btPreviousPage";

//> INSTANCE VARIABLES
	/** {@link UiGeneratorController} that the paging and list components are attached to */
	private final UiGeneratorController ui;
	
	/** The panel containing the paging controls. */
	private Object panel;
	/** The list or table that we are paging */
	private final Object list;
	/** The provider of items to display in this list. */
	private final PagedComponentItemProvider itemProvider;
	
	/** The current page number.  This is zero-indexed. */
	private int currentPage;
	/** The total number of pages. */
	private int totalPages;
	/** The total number of items this list contains, across all pages. */
	private int totalListItems;
	/** The maximum items to display per page. */
	private final int maxItemsPerPage;
	
//> CONSTRUCTORS
	/**
	 * Creates a new {@link ComponentPagingHandler}.
	 * @param ui The {@link UiGeneratorController} instance that this is tied to.
	 * @param itemProvider The class which provides items to place in the list
	 * @param list The list that this will handle paging for.
	 */
	public ComponentPagingHandler(UiGeneratorController ui, PagedComponentItemProvider itemProvider, Object list) {
		assert(ui!=null) : "Must supply non-null UI";
		assert(itemProvider!=null) : "Must supply non-null itemProvider";
		assert(list!=null) : "Must supply non-null list";
		this.ui = ui;
		this.itemProvider = itemProvider;
		this.list = list;
		this.maxItemsPerPage = ui.getProperties().getItemsPerPage();
	}

//> ACCESSORS
	/**
	 * <p>Sets the value of {@link #currentPage}.</p>
	 * <p>N.B. this method does NOT refresh the view, it merely sets the value of the current page.</p>
	 * @param currentPage the page number to set
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	/** @return the maximum number of items that this will display per page. */
	public int getMaxItemsPerPage() {
		return maxItemsPerPage;
	}
	
	/**
	 * Loads the panel and returns it for adding to the UI.
	 * @return A new instance of the thinlet panel for adding to the UI.
	 */
	public Object getPanel() {
		this.panel = ui.loadComponentFromFile(UI_FILE_PAGE_PANEL, this);
		return panel;
	}
	
	/** @return {@link #list} */
	public Object getList() {
		return list;
	}
	
	/** Refresh the paging panel and its associated list. */
	public void refresh() {
		this.refreshList();
	}
	
//> UI EVENT METHODS
	/** Turn to the previous page. */
	public void previousPage() {
		if(this.currentPage > 0) {
			--this.currentPage;
			refreshList();
		}
	}
	
	/** Turn to the previous page. */
	public void nextPage() {
		if(currentPage < this.totalPages-1) {
			++this.currentPage;
			refreshList();
		}
	}
	
//> UI HELPER METHODS
	/** Update the page count internally, and that displayed. */
	private void updatePageCount(int totalItemCount) {
		// Update the page count etc.
		this.totalListItems = totalItemCount;
		this.totalPages = (int) Math.ceil(((double)this.totalListItems) / this.maxItemsPerPage);
		// Make sure that the page has not got out of range
		if(totalPages == 0) {
			currentPage = 0;
		} else if(this.currentPage >= this.totalPages) {
			this.currentPage = this.totalPages - 1;
		}
		
		// Update the paging text, and enable previous and next page buttons as appropriate
		ui.setText(ui.find(this.panel, "lbPageNumber"),
				InternationalisationUtils.getI18nString(I18N_KEY_PAGES_X_OF_Y,
						Integer.toString(this.currentPage + 1),
						Integer.toString(Math.max(1, this.totalPages))));
		ui.setEnabled(ui.find(panel, COMPONENT_BT_PREVIOUS_PAGE), this.currentPage > 0);
		ui.setEnabled(ui.find(panel, COMPONENT_BT_NEXT_PAGE), this.currentPage < this.totalPages - 1);
	}
	
	/** Load the list items for the {@link #currentPage} and display them. */
	private synchronized void refreshList() {
		int startIndex = this.currentPage*this.maxItemsPerPage;
		PagedListDetails details = this.itemProvider.getListDetails(this.list, startIndex, this.maxItemsPerPage);
		
		updatePageCount(details.getTotalItemCount());
		
		// Refresh the contents of the list
		ui.removeAll(this.list);
		for(Object newChild : details.getListItems()) {
			ui.add(this.list, newChild);
			if(details.isSelected(newChild)) {
				ui.setSelected(newChild, true);
			}
		}
	}
}
