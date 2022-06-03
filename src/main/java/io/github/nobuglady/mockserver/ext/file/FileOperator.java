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
package io.github.nobuglady.mockserver.ext.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * FileOperator
 * 
 * @author NoBugLady
 *
 */
public class FileOperator {

	/**
	 * read
	 * 
	 * @param fullPath full path
	 * @return file content
	 */
	public static String read(String fullPath) {
		return read(fullPath, "UTF-8");
	}

	/**
	 * read
	 * 
	 * @param fullPath full path
	 * @param charSet  charSet
	 * @return file content
	 */
	public static String read(String fullPath, String charSet) {
		try {
			File file = new File(fullPath);
			byte[] byteContent = Files.readAllBytes(file.toPath());
			return new String(byteContent, charSet);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * write
	 * 
	 * @param fullPath full path
	 * @param content  content
	 * @param charSet  charSet
	 */
	public static void write(String fullPath, String content) {
		write(fullPath, content, "UTF-8");
	}

	/**
	 * write
	 * 
	 * @param fullPath full path
	 * @param content  content
	 * @param charSet  charSet
	 */
	public static void write(String fullPath, String content, String charSet) {
		try {
			File file = new File(fullPath);
			if (!file.exists()) {
				file.createNewFile();
			}

			Files.write(file.toPath(), content.getBytes(), StandardOpenOption.APPEND);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
