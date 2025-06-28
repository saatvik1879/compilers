# 🌿 Jlox – A Simple Tree-Walk Interpreter in Java

**Jlox** is an interpreter for a small, dynamically typed, object-oriented programming language. It is written entirely in Java and is built from scratch, covering all core language features like lexical analysis, parsing, syntax trees, environments, classes, inheritance, closures, and error handling.

The project is designed to provide insight into how high-level interpreters work internally by constructing each stage of the execution pipeline manually.

---

## 🧠 Language Overview

Jlox supports the following features:

### ✅ Language Features

- **Dynamic Typing**: No variable declarations or types.
- **First-Class Functions**: Functions are first-class objects and support closures.
- **Lexical Scoping**: Scopes are determined by where code is written.
- **Control Flow**: `if`, `else`, `while`, and `for` statements.
- **Classes and Inheritance**:
  - Single inheritance
  - `super` for calling superclass methods
  - `this` for instance references
- **Built-in Types**: `number`, `string`, `boolean`, `nil`
- **Garbage Collected**: Memory management is automatic (via Java's GC).
- **Error Handling**: Syntax and runtime error detection with detailed feedback.

---

## ✨ Example Code

```lox
fun makeCounter() {
  var count = 0;
  fun counter() {
    count = count + 1;
    print count;
  }
  return counter;
}

var c = makeCounter();
c(); // 1
c(); // 2

class Animal {
  speak() {
    print "generic sound";
  }
}

class Dog < Animal {
  speak() {
    print "woof";
  }
}

Dog().speak(); // woof
