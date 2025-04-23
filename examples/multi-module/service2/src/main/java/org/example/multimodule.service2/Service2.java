package org.example.multimodule.service2;

import org.example.multimodule.service1.Service1;

public class Service2 {

  public void printMessage() {
    Service1 service1 = new Service1();
    System.out.println("Service2 received: " + service1.getHelloMessage());
  }

  public static void main(String[] args) {
    new Service2().printMessage();
  }
}
