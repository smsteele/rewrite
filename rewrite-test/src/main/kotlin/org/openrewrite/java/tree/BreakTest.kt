/*
 * Copyright 2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.java.tree

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrewrite.java.JavaParser
import org.openrewrite.java.firstMethodStatement

interface BreakTest {

    @Test
    fun breakFromWhileLoop(jp: JavaParser) {
        val a = jp.parse("""
            public class A {
                public void test() {
                    while(true) break;
                }
            }
        """)[0]

        val whileLoop = a.firstMethodStatement() as J.WhileLoop
        assertTrue(whileLoop.body is J.Break)
        assertNull((whileLoop.body as J.Break).label)
    }

    @Test
    fun breakFromLabeledWhileLoop(jp: JavaParser) {
        val a = jp.parse("""
            public class A {
                public void test() {
                    labeled: while(true)
                        break labeled;
                }
            }
        """)[0]

        val whileLoop = (a.firstMethodStatement() as J.Label).statement as J.WhileLoop
        assertTrue(whileLoop.body is J.Break)
        assertEquals("labeled", (whileLoop.body as J.Break).label?.simpleName)
    }

    @Test
    fun formatLabeledBreak(jp: JavaParser) {
        val a = jp.parse("""
            public class A {
                public void test() {
                    labeled : while(true)
                        break labeled;
                }
            }
        """)[0]

        val whileLoop = (a.firstMethodStatement() as J.Label).statement as J.WhileLoop
        assertEquals("break labeled", whileLoop.body.printTrimmed())
    }
}
