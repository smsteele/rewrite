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
package org.openrewrite.git;

import org.openrewrite.Incubating;
import org.openrewrite.Metadata;

@Incubating(since = "2.0.0")
public class GitMetadata implements Metadata {
    private String headCommitId;
    private String headTreeId;
    private String branch;
    private String remote;

    public String getHeadCommitId() {
        return headCommitId;
    }

    public void setHeadCommitId(String headCommitId) {
        this.headCommitId = headCommitId;
    }

    public String getHeadTreeId() {
        return headTreeId;
    }

    public void setHeadTreeId(String headTreeId) {
        this.headTreeId = headTreeId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }
}
