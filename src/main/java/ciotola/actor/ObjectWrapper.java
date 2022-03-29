/*
 * Copyright (c) 2021.  Enzo Reyes Licensed under the Apache License, Version 2.0 (the "License");   you may
 * not use this file except in compliance with the License.   You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package ciotola.actor;

class ObjectWrapper {

  private RunnableScript script;
  private long runTime;
  private RoleImpl role;

  public RunnableScript getScript() {
    return script;
  }

  public void setScript(RunnableScript script) {
    this.script = script;
  }

  public long getRunTime() {
    return runTime;
  }

  public void setRunTime(long runTime) {
    this.runTime = runTime;
  }

  public RoleImpl getRole() {
    return role;
  }

  public void setRole(RoleImpl role) {
    this.role = role;
  }

  public int roleSize() {
    return role.requests();
  }

  public void execute() {

  }

}
