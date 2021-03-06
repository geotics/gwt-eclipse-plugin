/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.google.gwt.eclipse.core.refactoring;

import com.google.gwt.eclipse.core.platformproxy.refactoring.IJsniReferenceChange;
import com.google.gwt.eclipse.core.platformproxy.refactoring.JsniReferenceChangeFactory;
import com.google.gwt.eclipse.core.util.Util;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ltk.core.refactoring.TextFileChange;

/**
 * Tests the {@link JsniReferenceChangeFactory} class.
 */
public class JsniReferenceChangeFactoryTest extends TestCase {

  public void testCreateChange() {
    JsniReferenceChangeFactory factory = new JsniReferenceChangeFactory(null);

    // This file doesn't need to actually exist in the workspace
    IFile file = Util.getWorkspaceRoot().getFile(
        new Path("/Project/src/com/google/gwt/GWT.java"));

    // Create a change and verify its properties
    TextFileChange change = factory.createChange(file);
    assertEquals(file, change.getFile());

    assertTrue(change instanceof IJsniReferenceChange);
    IJsniReferenceChange jsniRefChange = (IJsniReferenceChange) change;
    ICompilationUnit cu = (ICompilationUnit) JavaCore.create(file);
    assertEquals(jsniRefChange.getCompilationUnit(), cu);
  }

}
