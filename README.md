# Ciotola
Ciotola is an experimental actor-style execution framework written in Java to explore deterministic concurrency without relying on shared executor task scheduling.
The framework is based on a simple invariant:
Each Role is permanently owned by a single Actor thread.
All work targeting a Role is executed only by its owning Actor, ensuring that Role state is mutated by a single thread. This allows concurrent systems to avoid explicit locking while preserving ordered execution.

## Why Ciotola Exists

Modern systems often blur responsibility between components,
leading to implicit coupling and unsafe execution paths.

Ciotola enforces a strict architectural invariant:

- Actions are executed only by Actors
- Roles define capability, not execution
- Each Role belongs to exactly one Actor

This prevents:
- capability leakage
- ambiguous ownership
- invalid execution flows


## Roles, Scripts and Futures

### Execution Model
The runtime consists of three primary components:
#### Actors
Actors are long-lived worker threads.
Each Actor owns a collection of Roles and is responsible for executing all actions associated with those Roles.
An Actor may manage many Roles, but a Role is never executed by more than one Actor.
This establishes thread affinity and serialized execution.

#### Roles
A Role represents a stateful execution context.
Messages sent to a Role are placed into a mailbox (blocking queue).
The owning Actor consumes messages sequentially and executes the associated actions.
Because only one Actor processes a Role’s mailbox:
Role state does not require synchronization execution order is preserved
concurrent access is avoided by design

#### Director
The CiotolaDirector manages Actor creation and Role placement.
Roles are deterministically assigned to Actors using hash-based routing:
```roleId % actorPoolSize ```
This ensures stable ownership and predictable execution locality.

#### Message Flow
1. A caller sends a message to a Role.
2. The message is wrapped as an executable action.
3. The action is placed into the Role mailbox.
4. The owning Actor dequeues and executes the action.
5. Results are returned via futures or callbacks.

#### Known Tradeoffs
This design introduces several constraints:
* Hotspot Roles can saturate a single Actor thread.
* Role migration between Actors is not currently implemented.
* Executor pools may provide better utilization for stateless workloads.
Ciotola is intended as an exploration of execution models rather than a production-ready framework.

## Examples

A developer starts by writing a script, that is an object that implements
the Script interface. This object is then used to create a Role, 
via the director and the Role object is the main way one interacts
by sending messages, which return a future.

Example:

```aidl
    Role<String, String> example = Ciotola.defaultDirector().createRole(new Script<String, String>() {
      @Override
      public String process(String message) {
        System.out.println((String) message);
        return "hello there: " + message;
      }
    });
    
    CiotolaFuture<String> result = example.send("Test ");
```

To retrieve a result one can use the futures get function which will block, or use the call
back mechanism

Example:

```aidl
    String returnValue = result.get(); // Blocking get
```

Another way is to use the isComplete() method on the future to poll, while your object is doing
something else.

Example:

```aidl
    while(!result.isComplete()) {
        doSomething();
    }
    String returnValue = result.get(); // Blocking get
```

Using the call back mechanism the Future will execute when the value or error is set.

Example:

```aidl
      CiotolaFuture<String> result = example.send("Test");
      result.then(value -> System.out.println(value+" Callback"));
```

## Producer Consumer Pattern

Ciotola also supports a producer/consumer pattern, this is done via Ports, Sinks and Sources.
A port - refers to a link between a producer and one or more consumers.
Sinks are generally consumers of messages, and Sources being the producers.


### Creating a Port
This is rather simple and can be done via the Director
Example:
```aidl
AgentPort<String> port = director.getBus().createPort("test",true);
```
The two parameters are a name for the port and the boolean specifies if this is a broadcast port,
for the most part this will be set to true, unless you want to load balance consumers.

### Creating a Source(Producer) Actor

This is done by having an object inherit from one of the SinkActor interfaces, and implementign its
methods.


```aidl
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
```
Please note that once a createSource is invoked the producer will be called right-away.
To explain a bit more here, the SourceActor is created and the method execute is called with a 
AgentPort (target) passed in as a parameter where the Source needs to write to.

### Creating a Consumer

To create a consumer an object which implements the SinkAgent interface is passed to the ports
createSink method. 

Example:

```aidl
    port.register(new SinkActor<String>() {
      @Override
      public void onRecord(SourceRecord<String> record) {
        System.out.println(record.getValue()+" One");
        System.out.println(record.getPort());
      }
    });
```
When the onRecord methods is called the SourceRecord is passed containing the data that the 
Source produce - which is obtained by calling the getValue method.


## Actor Call

You have existing code, you want to make parallel but dont want to deal with race conditions, 
or perhaps you just need to behave asynchronously. ActorCall is here to help.

Example:

```aidl
  class MethodTester {
    public String testMethod(String test) {
      System.out.println(test);
      return test+"---NEW";
    }

    public String testTwo(int inNumber) {
      return "Hello: " + inNumber;
    }
  }
```

So lets say we are going to call these two methods asynchronously

```aidl
    MethodTester testing = new MethodTester();
    ActorCall<String> asyncCall = Ciotola.defaultDirector().createCall(testing,"testMethod");
    ActorCall<String> testMethodTwo = Ciotola.defaultDirector().createCall(testing,"testTwo");

```

The createCall method of the director is invoked, two parameters are passed, the first is the object
to use and the second is the name of the method to invoke. Now this is a string, and it will
use the first matching name.

It will return an ActorCall object, which will execute the specified method asynchronously
when the call method is invoked.

```aidl
    CiotolaFuture<String> ret = asyncCall.call("test-bob");
    System.out.println(ret.get());
```

The Call method is passed a var-arg of objects and returns a Future to which then is used
to obtain the value.
