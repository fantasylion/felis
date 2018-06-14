package com.serio.felis.assembly;

import java.util.Arrays;

public class ReplyParticipleFactory extends DefaultParticipleFactory {
	
	public ReplyParticipleFactory() {
		String[] baseArray			= { "content", "submitTime","sid" };
		this.baseList				=	Arrays.asList(baseArray);
	}
}
