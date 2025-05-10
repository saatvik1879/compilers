# (optional but clean) delete old .class files
find com -name "*.class" -delete

# recompile all Java source files
find com -name "*.java" > sources.txt

javac @sources.txt

java com.compilers.lox.lox input.txt
