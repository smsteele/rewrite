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
package org.openrewrite.java;

import org.openrewrite.Tree;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.Statement;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.openrewrite.Formatting.formatLastSuffix;
import static org.openrewrite.Formatting.lastSuffix;

public class ShiftFormatRightVisitor extends JavaIsoRefactorVisitor {
    private final Tree scope;
    private final String shift;

    public ShiftFormatRightVisitor(Tree scope, int shift, boolean isIndentedWithSpaces) {
        this.scope = scope;
        this.shift = range(0, shift)
                .mapToObj(n -> isIndentedWithSpaces ? " " : "\t")
                .collect(Collectors.joining(""));
        setCursoringOn();
    }

    @Override
    public J.MethodDecl visitMethod(J.MethodDecl method) {
        J.MethodDecl m = super.visitMethod(method);
        if (getCursor().isScopeInPath(scope)) {
            m = m.withPrefix(m.getPrefix() + shift);
        }
        return m;
    }

    @Override
    public J.If.Else visitElse(J.If.Else elze) {
        J.If.Else e = super.visitElse(elze);
        if (getCursor().isScopeInPath(scope) && isOnOwnLine(elze)) {
            e = e.withPrefix(e.getPrefix() + shift);
        }
        return e;
    }

    @Override
    public Statement visitStatement(Statement statement) {
        Statement s = super.visitStatement(statement);
        if (getCursor().isScopeInPath(scope) && isOnOwnLine(statement)) {
            s = s.withPrefix(s.getPrefix() + shift);
        }
        return s;
    }

    @Override
    public J.Block<J> visitBlock(J.Block<J> block) {
        J.Block<J> b = super.visitBlock(block);
        if (getCursor().isScopeInPath(scope)) {
            J.Block.End end = b.getEnd();
            b = b.withEnd(end.withFormatting(end.getFormatting().withPrefix(
                    end.getPrefix() + shift)));
        }
        return b;
    }

    @Override
    public J.NewArray visitNewArray(J.NewArray newArray) {
        J.NewArray n = super.visitNewArray(newArray);
        if (n.getInitializer() != null) {
            String suffix = lastSuffix(n.getInitializer().getElements());
            if (suffix.contains("\n")) {
                List<Expression> elements = formatLastSuffix(n.getInitializer().getElements(), suffix + shift);
                n = n.withInitializer(n.getInitializer().withElements(elements.stream()
                        .map(e -> (Expression) e.withPrefix(e.getPrefix() + shift))
                        .collect(toList())));
            }
        }
        return n;
    }

    private boolean isOnOwnLine(Tree tree) {
        AtomicBoolean takeWhile = new AtomicBoolean(true);
        return tree.getPrefix().chars()
                .filter(c -> {
                    takeWhile.set(takeWhile.get() && (c == '\n' || c == '\r'));
                    return takeWhile.get();
                })
                .count() > 0;
    }
}
