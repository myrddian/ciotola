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

package ciotola;

import ciotola.actor.*;
import ciotola.network.parser.TLVParserFactory;
import java.io.IOException;

import org.junit.jupiter.api.Test;

public class SimpleTest {


  class MethodTester {
    public String testMethod(String test) {
      System.out.println(test);
      return test+"---NEW";
    }

    public String testTwo(int inNumber) {
      return "Hello: " + inNumber;
    }
  }


  @Test
  public void simpleTest() throws IOException {
    /*Ciotola ciotola = Ciotola.getInstance();
    CiotolaServerConnection serverConnection = new CiotolaServerConnection(600,ciotola.threadCapacity(),null,ciotola);
    ciotola.addService(serverConnection);
    ciotola.startContainer();

    while(true);*/

    TLVParserFactory<TestTLVMesg> testFactory = new TLVParserFactory<>();
    testFactory.handle(TestTLVMesg.class);
    CiotolaDirector director = Ciotola.defaultDirector();

    MethodTester testing = new MethodTester();
    ActorCall<String> asyncCall = director.createCall(testing,"testMethod");
    ActorCall<String> testMethodTwo = director.createCall(testing,"testTwo");
    CiotolaFuture<String> ret = asyncCall.call("test-bob");
    CiotolaFuture<String> retValues = testMethodTwo.call(1);
    System.out.println(ret.get());
    System.out.println(retValues.get());

    director.createBackgroundActor(new BackgroundDelayActor() {
      @Override
      public void process() {
        System.out.println("Ready to make it to the top");
      }

      @Override
      public long getDelay() {
        return 2000L;
      }
    }
  );

    Role<String, String> newRole2 = director.createRole(new Script<String, String>() {
      @Override
      public String process(String message) {
        System.out.println((String) message);
        return "hello there: " + message;
      }
    });

    Role<Integer, String> testLock = director.createRole(new Script<Integer,String>() {
      @Override
      public String process(Integer message) {
        System.out.println("Calling Method Two");
        return testing.testTwo(message);
      }
    }, testing);

    Role<String, String> testLockTwo = director.createRole(new Script<String,String>() {
      @Override
      public String process(String message) {
        System.out.println("Calling Method One");
        return testing.testMethod(message);
      }
    }, testing);

    testLock.send(1);
    testLockTwo.send("bob");
    testLock.send(12);

    AgentPort<String> port = director.getBus().createPort("test",true);
    port.createSource(new SourceActor<String>() {
      @Override
      public void execute(AgentPort<String> target) {
        try {
          Thread.sleep(500);
          target.write("HELLO THERE");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    port.register(new SinkActor<String>() {
      @Override
      public void onRecord(SourceRecord<String> record) {
        System.out.println(record.getValue()+" One");
        System.out.println(record.getPort());
      }
    });

    port.register(new SinkActor<String>() {
      @Override
      public void onRecord(SourceRecord<String> record) {
        System.out.println(record.getValue()+" Two");
        System.out.println(record.getPort());
      }
    });

   port.createSource(new SourceActor<String>() {
      @Override
      public void execute(AgentPort<String> target) {
        try {
          Thread.sleep(500);
          target.write("WELCOME");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    int counter = 0;
    while (true) {
      CiotolaFuture<String> result = newRole2.send("Test: "+ counter);
      ++counter;
        System.out.println(result.get());
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

  }
}
