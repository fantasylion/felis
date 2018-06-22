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
package com.serio.felis.xml.parser.translater;

import java.util.List;
import java.util.Map;

/**
 * The default dictionary
 * @author serio.shi
 *
 */
public class DefaultDictionary extends Dictionary {
	
	public DefaultDictionary() {}
	
	public DefaultDictionary( Map<String, String> dictionary ) {
		this.dictionary = dictionary;
	}
	
	public DefaultDictionary( Map<String, String> dictionary, List<String> endecryptFields ) {
		this.dictionary = dictionary;
		this.endecryptFields = endecryptFields;
	}
	
	public void setEndecryptFields( List<String> endecryptFields ) {
		this.endecryptFields = endecryptFields;
	}
	
	public void setDictionary( Map<String, String> dictionary ) {
		this.dictionary = dictionary;
	}
	
}
