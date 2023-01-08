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

final class SourceAgentRunner<T> implements Runnable,RunnableScript {

  private NotifySourceActor<T> producer;
  private boolean processing = false;
  private AgentPort<T> agentPort;

  private long defaultKey;
  private boolean forkJoinTask = false;
  private CiotolaDirector director;

  public SourceAgentRunner(NotifySourceActor<T> producer, boolean forkJoin, CiotolaDirector director) {
    this.producer = producer;
    this.setForkJoinTask(forkJoin);
    this.director = director;
  }


  public void register(AgentPort<T> targetPort) {
    agentPort = targetPort;
  }

  private void forkJoin() {
    synchronized (this) {
      if(processing == false) {
        director.submitJob(this);
        processing = true;
      }
    }
  }

  private void executeTask() {
    if(producer.isReady()) {
      producer.execute(agentPort);
    }
  }


  private void execReadyTask() {
    if (this.isForkJoinTask()) {
      forkJoin();
    } else {
      executeTask();
    }
  }

  @Override
  public Object process(Object message) {
    execReadyTask();
    return null;
  }

  @Override
  public void run() {
    executeTask();
    synchronized (this) {
      processing = false;
    }
  }

  public long getDefaultKey() {
    return defaultKey;
  }

  public void setDefaultKey(long defaultKey) {
    this.defaultKey = defaultKey;
  }

  public boolean isForkJoinTask() {
    return forkJoinTask;
  }

  public void setForkJoinTask(boolean forkJoinTask) {
    this.forkJoinTask = forkJoinTask;
  }

  @Override
  public boolean hasReturn() {
    return false;
  }

  @Override
  public boolean hasValues() {
    return false;
  }

}
