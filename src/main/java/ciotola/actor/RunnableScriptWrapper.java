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

import java.lang.reflect.Method;

class RunnableScriptWrapper implements RunnableScript {

  private Script target;
  private boolean hasParams = false;
  private boolean hasRet = false;

  private void processMethod(Method targetMethod) {
    if (targetMethod.getParameterCount() != 0) {
      if(targetMethod.getParameterCount() == 1 &&
          targetMethod.getParameterTypes()[0].getCanonicalName().equals(Void.class.getCanonicalName())) {
        hasParams = false;
      } else {
        hasParams = true;
      }
    }
    if (targetMethod.getReturnType().getName().equals("void") ||
        targetMethod.getReturnType().getCanonicalName().equals(Void.class.getCanonicalName())) {
      hasRet = false;
    }  else {
      hasRet = true;
    }
  }

  public RunnableScriptWrapper(Script target) {
    for (Method method : target.getClass().getMethods()) {
      if(method.getName().equals(Script.class.getDeclaredMethods()[0].getName()))
      processMethod(method);
      this.target = target;
      return;
    }
    this.target = target;
  }

  @Override
  public boolean hasReturn() {
    return hasRet;
  }

  @Override
  public boolean hasValues() {
    return hasParams;
  }

  @Override
  public Object process(Object message) {
    return target.process(message);
  }
}
