/**
 * 
 */
package net.frontlinesms.data.domain;

import net.frontlinesms.junit.BaseTestCase;

/**
 * @author aga
 *
 */
public class GroupTest extends BaseTestCase {
	public void testRootGroup() {
		Group root = getRootGroup();
		assertEquals("", root.getPath());
		assertTrue("isRoot() fails on root group", root.isRoot());
		
		assertEquals("All root groups should be equal.", new Group(null, null), root);
	}
	
	public void testCreateGroupWithEmptyName() {
		try {
			new Group(getRootGroup(), "");
			fail("Creating a group with an empty name should throw an exception.");
		} catch(IllegalArgumentException ex) { /* expected */ }
	}

	public void testGetPath() {
		Group root = getRootGroup();
		assertEquals("", root.getPath());

		Group random = new Group(root, "random");
		assertEquals("/random", random.getPath());

		Group randomChild = new Group(random, "child");
		assertEquals("/random/child", randomChild.getPath());
	}
	
	public void testGetName() {
		Group root = getRootGroup();
		assertEquals("", root.getName());
		
		Group child = new Group(root, "child");
		assertEquals("child", child.getName());
		
		Group grandchild = new Group(child, "grandchild");
		assertEquals("grandchild", grandchild.getName());
	}
	
	public void testGetParent() {
		Group root = getRootGroup();
		
		Group randomGroup = new Group(root, "random");
		assertEquals(root, randomGroup.getParent());
		
		Group child = new Group(randomGroup, "child");
		assertEquals(randomGroup, child.getParent());
	}

	/** Creates a root group */
	private static Group getRootGroup() {
		return new Group(null, null);
	}
}
