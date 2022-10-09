### What is Spring IoC container ?
Inversion of Control (IoC) is a process in which an object defines its dependencies without creating them. 
This object delegates the job of constructing such dependencies to an IoC container.

Example:

Consider the class Customer.

```java
public class Customer{

    //Address is a collaborator class
    private Address address;

    public Customer(Address address) {
        this.address = address;
    }
}

public class Address {
    private String street;
    private int phoneNumber;

    public Address(String street, int number) {
        this.street = street;
        this.phoneNumber = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

//We create class objects with the constructors of the classes
Address address = new Address("10 Banana Road", 123456);
Customer customer = new Customer(address);
```

Assuming we have a clutch of such dependencies amongst hundreds of classes in an application. Creating such dependencies and objects with the "new"
keyword will be a tough job.Here Inversion of Control comes into play to solve the problem.
Instead of constructing dependencies by itself, an object can retrieve its dependencies from an IoC container. 
We need to provide the container with appropriate configuration metadata to attain this objective.
The configuration of beans is answered in the next question.


### What is a Spring bean ? ###

The objects that form the backbone of the application and that are managed by the Spring IoC container are called beans. 
A bean is an object that is instantiated, assembled, and otherwise managed by a Spring IoC container.

```java

@Component
public class Customer {
    //Address is a collaborator class
    private Address address;

    public Customer(Address address) {
        this.address = address;
    }
}

@Configuration
@ComponentScan(basePackageClasses = Customer.class)
public class Config {
    @Bean
    public Address getAddress() {
        return new Address("10 Banana Road", 123456);
    }
}
```

The configuration class produces a bean of type Address. 
It also carries the @ComponentScan annotation, which instructs the container to look for beans in the package containing the Customer class.
When a Spring IoC container constructs objects of these types, all the objects are called Spring beans, as they are managed by the IoC container.

Since we defined beans in a configuration class, we need an instance of the AnnotationConfigApplicationContext class to build up a container.

```java
ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
Customer customer = context.getBean("customer", Customer.class);
```

### Explain dependency injection ###

Dependency injection involves four roles: services, clients, interfaces and injectors.

Services and clients
--------------------

A service is any class which contains useful functionality. A client is any class which uses services.

Any object can be a service or a client; the names relate only to the role the objects play in an injection. 
The same object may even be both a client (it uses injected services) and a service (it is injected into other objects). 
Upon injection, the service is made part of the client's state, available for use.

Interfaces
-----------

Clients should not know how their dependencies are implemented, only their names and API. 
A service which retrieves emails, for instance, may use the IMAP or POP3 protocols behind the scenes, 
but this detail is likely irrelevant to calling code that merely wants an email retrieved. 
By ignoring implementation details, clients do not need to change when their dependencies do.

Injectors
---------
The injector, sometimes also called an assembler, container, provider or factory, introduces services to the client.
The role of injectors is to construct and connect complex object graphs, where objects may be both clients and services. 
The injector itself may be many objects working together, but must not be the client, as this would create a circular dependency.

Because dependency injection separates how objects are constructed from how they are used, it often diminishes 
the importance of the new keyword found in most object-oriented languages. 
Because the framework handles creating services, the programmer tends to only directly construct value objects which 
represents entities in the program's domain (such as an Employee object in a business app or an Order object in a shopping app).

### Types of Dependency Injection
