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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

final class CiotolaFutureImpl<R> implements CiotolaFuture<R>, Runnable {

  private boolean isError = false;
  private BlockingQueue<R> resultList = new LinkedBlockingQueue<>();
  private ActorException exception = null;
  private ThenFunctionFuture<R> thenPath = null;
  private ErrorFunctionFuture errorPath = null;
  private R value = null;

  public void setResult(R result) {
    resultList.add(result);
  }

  @Override
  public R get() {
    if (exception != null) {
      throw exception;
    }
    try {
      if (value == null) {
        value = resultList.take();
      }
      return value;
    } catch (InterruptedException e) {
      throw new ActorException(CiotolaDirector.ACTOR_INTERRUPTED_RESULT);
    }
  }

  @Override
  public boolean isError() {
    return isError;
  }

  public void setError(boolean isError, ActorException exception) {
    this.isError = isError;
    this.exception = exception;
  }

  @Override
  public boolean isComplete() {
    return !resultList.isEmpty();
  }

  @Override
  public ActorException getException() {
    return exception;
  }

  @Override
  public CiotolaFuture<R> then(ThenFunctionFuture<R> value) {
    this.thenPath = value;
    return this;
  }

  @Override
  public CiotolaFuture<R> error(ErrorFunctionFuture value) {
    this.errorPath = value;
    return this;
  }

  public boolean runnableCallBacks() {
    return (thenPath!=null || errorPath!=null);
  }

  @Override
  public void run() {
    if (exception!=null) {
      if (errorPath!=null) {
        errorPath.call(exception);
      }
    } else {
      if(thenPath!=null) {
        if(this.resultList.size()!=0) {
          thenPath.call(this.get());
        }
        else {
          thenPath.call(null);
        }
      }
    }
  }
}
