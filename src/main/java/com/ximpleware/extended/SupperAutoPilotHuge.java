/**
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ximpleware.extended;

/**
 * AutoPilotHuge 不支持 {@link #superIterateAttr}、 {@link #superSelectAttr}， 这里重新暴露出来
 * @author zl.shi
 *
 */
public class SupperAutoPilotHuge extends AutoPilotHuge {
	/**
	 * 
	 */
	public SupperAutoPilotHuge() {
		super();
	}

	/**
	 * @param v
	 */
	public SupperAutoPilotHuge(VTDNavHuge v) {
		super(v);
	}
	
	
	/**
	 * This method implements the attribute axis for XPath
	 * @return the integer of the selected VTD index for attribute name
	 * @throws com.ximpleware.extended.PilotException
	 */
	public int superIterateAttr() throws PilotExceptionHuge, NavExceptionHuge {
		return this.iterateAttr();
	}
	
	
	/**
	 * Select an attribute name for iteration, * choose all attributes of an element
	 * @param en
	 */
	public void superSelectAttr(String en) {
		this.selectAttr(en);
	}
}
