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
package com.serio.felis.sql.maker.sequence;

/**
 * @author zl.shi
 *
 */
public class IndexCounter {
	

	private int index = 1;
	
	public IndexCounter(){}
	
	public IndexCounter( int index ) {
		this.index = index;
	}
	
	public void autoIncrease() {
		index++;
	}
	
	
	/**
	 * 返回当前的Index后将Index自增1个数值
	 * @return
	 */
	public int getIndexAndautoIncrement() {
		int indexCopy = index;
		autoIncrease();
		return indexCopy;
	}
	
	
	/**
	 * 返回当前的Index
	 * @return
	 */
	public int getCurrentIndex() {
		return index;
	}
	
	/**
	 * 返回当前的Index 类型为String
	 * @return
	 */
	public String getCurrentIndexStr() {
		return index+"";
	}
}
