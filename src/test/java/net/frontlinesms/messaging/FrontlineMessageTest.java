package net.frontlinesms.messaging;

import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.junit.BaseTestCase;

public class FrontlineMessageTest extends BaseTestCase {
	private static final String ONE_PART_MESSAGE = "This is a one-part message";
	private static final String ONE_PART_MESSAGE_LIMIT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla molestie pretium lacinia. Donec feugiat, enim nec semper dignissim, mi elit pulvinar enim nullam.";
	private static final String TWO_PART_MESSAGE_LIMIT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean accumsan commodo tempor. Sed felis dolor, suscipit non consequat vitae, varius non libero. Maecenas fermentum, libero sed lobortis tincidunt, odio lectus sollicitudin tellus, et consectetur massa dolor quis";
	private static final String THREE_PART_MESSAGE_LIMIT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam ut enim arcu, vel tempus mi. Nulla quis dui diam, vitae dapibus lorem. Aenean enim diam, ornare ut ultricies quis, tempor vel augue. Nunc venenatis rhoncus consectetur. Fusce quis metus id tortor iaculis ornare. Aenean posuere ligula quis dolor aliquam congue. In semper porttitor magna. Fusce ac odio urna. Donec condimentum pretium arcu, e";
	
	private static final String ONE_PART_MESSAGE_UCS2 = "This is a \u00f4ne-part message";
	private static final String ONE_PART_MESSAGE_LIMIT_UCS2 = "Lorem ipsum d\u00f4lor sit amet, consectetur adipiscing elit viverra fusce.";
	private static final String TWO_PART_MESSAGE_LIMIT_UCS2 = "Lorem ipsum d\u00f4lor sit amet, consectetur adipiscing elit. Phasellus vitae ligula a lorem suscipit condimentum. Quisque t.";
	private static final String THREE_PART_MESSAGE_LIMIT_UCS2 = "Lorem ipsum d\u00f4lor sit amet, consectetur adipiscing elit. Fusce volutpat feugiat consectetur. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himen.";
	
	public static void main(String[] args) {
		System.out.println(ONE_PART_MESSAGE_LIMIT_UCS2.length());
	}
	
	public void testGetExpectedNumberOfSmsParts () {
		assertEquals(0, FrontlineMessage.getExpectedNumberOfSmsParts(""));
		assertEquals(1, FrontlineMessage.getExpectedNumberOfSmsParts(ONE_PART_MESSAGE));
		assertEquals(1, FrontlineMessage.getExpectedNumberOfSmsParts(ONE_PART_MESSAGE_LIMIT));
		assertEquals(2, FrontlineMessage.getExpectedNumberOfSmsParts(ONE_PART_MESSAGE_LIMIT + "."));
		assertEquals(2, FrontlineMessage.getExpectedNumberOfSmsParts(TWO_PART_MESSAGE_LIMIT));
		assertEquals(3, FrontlineMessage.getExpectedNumberOfSmsParts(TWO_PART_MESSAGE_LIMIT + "."));
		assertEquals(3, FrontlineMessage.getExpectedNumberOfSmsParts(THREE_PART_MESSAGE_LIMIT));
		assertEquals(4, FrontlineMessage.getExpectedNumberOfSmsParts(THREE_PART_MESSAGE_LIMIT + "."));
		
		// UCS2
		assertEquals(1, FrontlineMessage.getExpectedNumberOfSmsParts(ONE_PART_MESSAGE_UCS2));
		assertEquals(1, FrontlineMessage.getExpectedNumberOfSmsParts(ONE_PART_MESSAGE_LIMIT_UCS2));
		assertEquals(2, FrontlineMessage.getExpectedNumberOfSmsParts(ONE_PART_MESSAGE_LIMIT_UCS2 + "."));
		assertEquals(2, FrontlineMessage.getExpectedNumberOfSmsParts(TWO_PART_MESSAGE_LIMIT_UCS2));
		assertEquals(3, FrontlineMessage.getExpectedNumberOfSmsParts(TWO_PART_MESSAGE_LIMIT_UCS2 + "."));
		assertEquals(3, FrontlineMessage.getExpectedNumberOfSmsParts(THREE_PART_MESSAGE_LIMIT_UCS2));
		assertEquals(4, FrontlineMessage.getExpectedNumberOfSmsParts(THREE_PART_MESSAGE_LIMIT_UCS2 + "."));
	}
}
