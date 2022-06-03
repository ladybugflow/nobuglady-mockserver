/*
 * Copyright (c) 2021-present, NoBugLady-mockserver Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package io.github.nobuglady.mockserver.logger;

import java.util.ArrayList;
import java.util.List;

/**
 * a circular linked list with max size
 * 
 * @author NoBugLady
 *
 */
public class CircularLinkedList {

	private int maxSize = 64;
	private int currentSize = 0;
	private Node headNode = null;
	private Node tailNode = null;
	
	/**
	 * constructor
	 * 
	 * @param size max size
	 */
	public CircularLinkedList(int size) {
		this.maxSize = size;
	}
	
	/**
	 * append value
	 * 
	 * @param value value
	 */
	public void append(String value) {
		if(currentSize == 0) {
			Node node = new Node(value);
			this.headNode = node;
			this.tailNode = node;
			currentSize ++;
		}else if(currentSize < maxSize){
			Node node = new Node(value);
			this.tailNode.next = node;
			this.tailNode = node;
			currentSize ++;
			
			if(currentSize == maxSize) {
				this.tailNode.next = this.headNode;
			}
		}else {
			this.headNode = this.headNode.next;
			this.tailNode = this.tailNode.next;
			this.tailNode.value = value;
		}
	}
	
	/**
	 * read all node
	 * 
	 * @return all values
	 */
	public List<String> readAll(){
		
		List<String> resultList = new ArrayList<>();
		
		if(currentSize == 0) {
			return resultList;
		}else {
			
			Node temp = this.headNode;
			
			while(temp != null) {
				resultList.add(temp.value);
				temp = temp.next;
				if(temp == this.headNode) {
					break;
				}
			}
			
		}
		
		return resultList;
	}
	
	/**
	 * clear
	 */
	public void clear() {
		currentSize = 0;
		headNode = null;
		tailNode = null;
	}
}
