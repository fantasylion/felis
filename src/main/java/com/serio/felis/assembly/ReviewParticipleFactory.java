package com.serio.felis.assembly;

import java.util.Arrays;

public class ReviewParticipleFactory extends DefaultParticipleFactory {
	
	public ReviewParticipleFactory() {
		String[] baseArray			= { "content", "pid", "status", "submitTime", "submitterId", "tags", "rating", "channel", "ID" };
		baseList			=	Arrays.asList(baseArray);
	}
	
}
